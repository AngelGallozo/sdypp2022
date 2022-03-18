package sdypp_ejercicios;

import java.util.ArrayList;

public class Usuario {
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
    
    public ArrayList<Mensaje> getList_mensajes() {
        return this.list_mensajes;
    }


}
