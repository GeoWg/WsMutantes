package com.example.geowg.wsmutantes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {

    public static final String REQUEST_TAG = "UserAuthentication";
    private RequestQueue mQueue;
    public String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {
        EditText user = findViewById(R.id.etUser);
        EditText password = findViewById(R.id.etPassword);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", String.valueOf(user.getText().toString()));
            jsonObject.put("password", String.valueOf(password.getText().toString()));

            mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
            String url = "http://192.168.100.16:3000/login";
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObject, this, this);
            jsonRequest.setTag(REQUEST_TAG);

            mQueue.add(jsonRequest);

        } catch (JSONException e){
            Toast.makeText(this, "Não conectou com o banco", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try {
            if (((JSONObject) response).getInt("status") == 200){
                nome = ((JSONObject) response).getJSONObject("response").getString("user");
                builder.setTitle("Login efetuado");
                builder.setMessage("Bem vindo, " + nome);
                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                alerta = builder.create();
                alerta.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(
                                LoginActivity.this, MainActivity.class
                        );
                        Bundle params = new Bundle();
                        params.putString("nomeUsuario", nome);
                        intent.putExtras(params);
                        startActivity(intent);
                        finish();
                    }
                });
                alerta.show();
            }
            if (((JSONObject) response).getInt("status") == 401){
                String e = "Usuário ou senha inválidos!";
                throw new Exception(e);
            }
        } catch (Exception e){
            builder.setTitle("Um erro ocorreu");
            builder.setMessage(e.getMessage());
            alerta = builder.create();
            alerta.show();
        }
    }
}
