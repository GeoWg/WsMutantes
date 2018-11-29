package com.example.geowg.wsmutantes;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarActivity extends ListActivity implements Response.Listener, Response.ErrorListener  {

    private ListView list;
    private MutanteOperations mutanteOperations;
    ArrayList<Mutante> mutantes = new ArrayList<>();
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "GetAllUsers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://192.168.100.16:3000/mutantes";
        try {
            JSONObject jsonObject = new JSONObject();
            mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
            final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, jsonObject, this, this);
            jsonRequest.setTag(REQUEST_TAG);
            mQueue.add(jsonRequest);
        } catch (Exception e){
            Toast.makeText(this, "Não conectou com o banco", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MutanteOperations mutanteOperations = new MutanteOperations();
        //ListView list = (ListView) this.findViewById(R.id.list);

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//
//                    int mutanteId = (int) mutantes.get(arg2).getId();
//                    Intent it = new Intent(ListarActivity.this, DetalhesActivity.class);
//                    Bundle params = new Bundle();
//                    params.putInt("mutanteId", mutanteId);
//                    it.putExtras(params);
//                    startActivity(it);
//                };
//        });
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
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mutantes);
            setListAdapter(adapter);
            if (((JSONObject) response).getInt("status") == 401){
                String e = "Não possui mutantes";
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
