package org.example;

import java.util.Random;

public class NumeroAleatorio implements Tarea{
    private int nro_max;

    public NumeroAleatorio(int maximo){
        this.nro_max=maximo;
    }

    @Override 
    public String ejecutar(){
        Random r= new Random();
        int nro = r.nextInt(this.nro_max);
        return Integer.toString(nro);
    }
}