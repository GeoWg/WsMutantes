package com.example.geowg.wsmutantes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Inicia activity de cadastro de mutante
    public void cadastrarMutante(View view){
        Intent it = new Intent(this, CadastrarActivity.class);
        startActivity(it);
    }

    //Inicia activity com a lista dos mutantes cadastrados
    public void listarMutante(View view){
        Intent it = new Intent(this, ListarActivity.class);
        startActivity(it);
    }

    //Inicia activity para pesquisa de mutantes
    public void pesquisarMutante(View view){
        Intent it = new Intent(this, PesquisarActivity.class);
        startActivity(it);
    }

    //Fecha o aplicativo
    public void fecharApp(View view){
        System.exit(0);
    }
}
