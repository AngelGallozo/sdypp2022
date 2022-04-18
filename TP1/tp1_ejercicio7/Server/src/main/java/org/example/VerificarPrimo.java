package org.example;
 
public class VerificarPrimo implements Tarea{
    private int nro;
    public VerificarPrimo(int nro){
        this.nro = nro;
    }

    @Override  
    public String ejecutar(){
        int i=2;
        boolean no_primo=false;
        String resultado = " Es primo.";
        while(!no_primo && i < this.nro){
            if((this.nro % i) == 0){
                no_primo=true;
                resultado=" NO es primo.";
            }
            i++;
        }  
        return "El numero "+this.nro+resultado;
    }
}
