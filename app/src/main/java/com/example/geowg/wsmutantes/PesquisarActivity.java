package com.example.geowg.wsmutantes;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PesquisarActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    public static final String REQUEST_TAG = "UserAuthentication";
    private RequestQueue mQueue;
    public String nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
    }

    public void onClick(View view) {
        EditText nome = findViewById(R.id.etNome);
        EditText habilidade = findViewById(R.id.etHabilidade);
        if (nome.length() > 0 && habilidade.length() > 0){
            Toast.makeText(this, "Preencher apenas um campo", Toast.LENGTH_SHORT).show();
        } else if(nome.length()>0){
            try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nome", String.valueOf(nome.getText().toString()));
            mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
            String url = "http://192.168.100.16:3000/mutantes/nome/" + nome.getText().toString();
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, jsonObject, this, this);
            jsonRequest.setTag(REQUEST_TAG);

            mQueue.add(jsonRequest);
            } catch (JSONException e){
                Toast.makeText(this, "Não conectou com o banco", Toast.LENGTH_SHORT).show();
            }
        } else if (habilidade.length()>0) {
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("habilidade", String.valueOf(habilidade.getText().toString()));
                mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
                String url = "http://192.168.100.16:3000/mutantes/habilidade/" + habilidade.getText().toString();
                final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, jsonObject, this, this);
                jsonRequest.setTag(REQUEST_TAG);

                mQueue.add(jsonRequest);
            } catch (JSONException e){
                Toast.makeText(this, "Não conectou com o banco", Toast.LENGTH_SHORT).show();
            }
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
        ArrayList<String> mutantes = new ArrayList<>();
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try {
            if (((JSONObject) response).getInt("status") == 200){
                JSONArray jmutantes = ((JSONObject) response).getJSONArray("response");
                int length = jmutantes.length();
                for(int i=0; i<length; i++) {
                    JSONObject obj = jmutantes.getJSONObject(i);
                    System.out.println(obj);
                    mutantes.add(obj.toString());
                }
            }
            Intent it = new Intent(this, PesquisarListaActivity.class);
            Bundle params = new Bundle();
            params.putStringArrayList("mutantes", mutantes);
            it.putExtras(params);
            startActivity(it);
            if (((JSONObject) response).getInt("status") == 401){
                String e = "Não tem esse mutante ou habilidade";
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
