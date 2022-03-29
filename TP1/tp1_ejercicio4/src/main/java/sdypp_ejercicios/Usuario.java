package sdypp_ejercicios;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable{
    private String nombre;
    private String password;
    private ArrayList<Mensaje> list_mensajes = new ArrayList<Mensaje>(); 

    public Usuario(String nombre, String password){
        this.nombre = nombre;
        this.password = password;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getPassword() {
        return password;
    }

    public void guardarMensaje(Mensaje msj){
        this.list_mensajes.add(msj);
    }
    
    public String getList_mensajes() {
        String lista = "";
        for (int i = 0; i < list_mensajes.size(); i++) {
            lista += "<" + i + "> - " + this.list_mensajes.get(i).mensajeMin() + "\n";
        }
        return lista;
    }

    public String getMensaje(int index){
        return this.list_mensajes.get(index).toString();
    }

    public boolean hayMensajes(){
        boolean hay_msjs = false;
        if (this.list_mensajes.size() > 0 )
            hay_msjs=true;
        return hay_msjs;
    }

    public void deleteMensajes(){
        this.list_mensajes.clear();
    }

}
