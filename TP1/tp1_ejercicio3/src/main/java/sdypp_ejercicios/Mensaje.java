package sdypp_ejercicios;

import java.io.Serializable;

public class Mensaje implements Serializable{
    private String usuario_origen;
    private String usuario_destino;
    private String asunto;
    private String mensaje;

    public Mensaje(String usuario_origen, String usuario_destino, String asunto, String mensaje ){
        this.usuario_origen = usuario_origen;
        this.usuario_destino = usuario_destino;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public String toString() {
        return "Origen:"+ this.usuario_origen+"\n"
                +"Destinatario:" + this.usuario_destino+"\n"
                +"Asunto"+ this.asunto+"\n"
                +"Mensaje:\n" + this.mensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getUsuario_origen() {
        return usuario_origen;
    }

    public String mensajeMin(){
        return "De: " + this.usuario_origen + " - Asunto: " + this.asunto;
    }

    public String getUsuario_destino() {
        return this.usuario_destino;
    }
}
