package com.example.geowg.wsmutantes;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class MutanteOperations {

    public void addMutante(String nome, String habilidade) {
    }

    public void deleteMutante(int id) {

    }

    public void updateMutante(String nome, String habilidade, int id) {

    }

    public Mutante getMutanteById(int id) {
        Mutante mutante = new Mutante();
        return mutante;
    }

    public List getAllMutante() {
        List mutantes = new ArrayList();
        return mutantes;
    }

    public List getMutanteByName(String name){
        List mutantes = new ArrayList();
        return mutantes;
    }

    public List getMutanteByHabilidade(String habilidade){
        List mutantes = new ArrayList();
        return mutantes;
    }

    private Mutante parseMutante(Cursor cursor) {
        Mutante mutante = new Mutante();
        return mutante;
    }

}
