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

import org.json.JSONObject;


public class CadastrarActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener{

    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "Registration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        EditText nome = findViewById(R.id.textonome);
        EditText habilidade = findViewById(R.id.etHabilidades);
        Intent it = getIntent();
        Bundle params = it.getExtras();
        int id = params.getInt("mutanteId");
    }

    public void cadastrar(View view) {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText nome = findViewById(R.id.textonome);
        EditText habilidade = findViewById(R.id.etHabilidades);

        try {
            Intent it = getIntent();
            Bundle params = it.getExtras();
            int aux2 = params.getInt("aux");
            int mutanteNome = params.getInt("mutanteNome");
            String nomeUsuario = params.getString("nomeUsuario");

            if ( nome.getText().toString().equals("")|| !(habilidade.getText().toString().equals(""))) {
                String url = "http://192.168.100.16:3000/mutantes";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nome", String.valueOf(nome.getText().toString()));
                jsonObject.put("habilidade", String.valueOf(habilidade.getText().toString()));
                jsonObject.put("nomeUsuario", nomeUsuario);
                mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
                if (aux2 == 0) {
                    final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObject, this, this);
                    jsonRequest.setTag(REQUEST_TAG);
                    mQueue.add(jsonRequest);
                }
                if (aux2 == 1) {
                    final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.PUT, url, jsonObject, this, this);
                    jsonRequest.setTag(REQUEST_TAG);
                    mQueue.add(jsonRequest);
                }
            } else {
                String e = "Preencha todos os dados";
                throw new Exception(e);
            }
        } catch (Exception e) {
            builder.setTitle("Um erro ocorreu");
            builder.setMessage(e.getMessage());
            alerta = builder.create();
            alerta.show();
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
                builder.setMessage("Mutante cadastrado");
                alerta = builder.create();
                alerta.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                alerta.show();
            }
            if (((JSONObject) response).getInt("status") == 500){
                String e = "Nome desse mutante já foi cadastrado";
                throw new Exception(e);
            }
        } catch (Exception e){
            builder.setTitle("Um erro ocorreu");
            builder.setMessage(e.getMessage());
            alerta = builder.create();
            alerta.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}