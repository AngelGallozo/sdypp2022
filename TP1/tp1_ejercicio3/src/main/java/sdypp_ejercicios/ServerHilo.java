package sdypp_ejercicios;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ServerHilo implements Runnable {
	BufferedReader canalEntrada;
    PrintWriter canalSalida;
    Socket client;
    Logger log;
    String response;
    Usuario usuario_actual; //TODO inicializarlo
    private ArrayList<Usuario> usuarios;

    public ServerHilo(Socket client, Logger log, ArrayList<Usuario> usuarios) {
        this.log = log;
        this.client = client;
        this.usuarios = usuarios;
    }
    
    public void run() {

		try {
            this.response = "";
            boolean flag= false;
            DataOutputStream salida= new DataOutputStream(client.getOutputStream());    //Canal de salida de datos al cliente
            DataInputStream entrada= new DataInputStream(client.getInputStream());      //Canal de entrada de datos al cliente
            while ( !flag ) {
                this.response += opcionesMenuLogin();
                System.out.println("------------------------------------");
                salida.writeUTF(this.response);//enviamos Menu Login.
                String opcion = entrada.readUTF();
                if (opcion.equals("3")){
                    flag = true ;
                    salida.writeUTF("-1");//Señal de desconexion al cliente.
                }else{
                    if(opcion.equals("1")){//Inicio de Sesion.
                        if (iniciarSesion(entrada, salida)){
                            menuMensajes(entrada, salida);
                        }
                    }

                    if(opcion.equals("2")){//Registro.
                        if(registrarse(entrada, salida)){
                            menuMensajes(entrada, salida);
                        }
                    }
                }
            }
            salida.writeUTF("El cliente se ha desconectado.");//Le manda el mensaje al cliente mediante el canal de salida
            this.log.info("El cliente <"+ this.client.getPort() +"> se ha desconectado.");//Le manda el mensaje al cliente mediante el canal de salida
            System.out.println("=====================================");
            salida.close(); //cierre canal salida
            client.close();//cierre conexion con el cliente
        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error!");
            e.printStackTrace();
        }
    }



    public boolean iniciarSesion(DataInputStream entrada, DataOutputStream salida){
        boolean respuesta=true;
        try {
            salida.writeUTF("Ingrese username");    
            String username= entrada.readUTF();
            salida.writeUTF("Ingrese contrasenia");
            String password= entrada.readUTF();
            Usuario usuario_log = existeUsuario(username,password);
            if (usuario_log != null){
                if(usuario_log.getPassword().equals(password)){
                    this.usuario_actual = usuario_log;
                    respuesta=true;
                }else{
                    respuesta=false;
                    this.response = "\n "+"Error: Usuario o Contrasenia erroneos.";
                }
            }else{
                respuesta=false;
                this.response = "\n "+"Error: Usuario no existe";
            }

        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error!");
            this.response = "\n "+"Error: Usuario no existe";
            e.printStackTrace();
            respuesta=false;
        }
        return respuesta;
    }


    public boolean registrarse(DataInputStream entrada, DataOutputStream salida){
        boolean respuesta = true;
        try {
            salida.writeUTF("Ingrese username");    
            String username= entrada.readUTF();
            salida.writeUTF("Ingrese contrasenia");
            String password= entrada.readUTF();
            Usuario usuario_log = existeUsuario(username,password);
            if(usuario_log == null){
                this.usuario_actual = new Usuario(username, password);
                this.usuarios.add(this.usuario_actual);
            }else{
                this.response = "\n "+"Error: Usuario ya existe.";
                respuesta=false;
            }

        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error en el Registro del Usuario!");
            this.response = "\n "+"Error: Ocurrio un error al Registrar usuario.";
            e.printStackTrace();
            respuesta=false;
        }
        return respuesta;
    }

    public Usuario existeUsuario(String username, String password){
        Usuario usu_existe = null;
        int i = 0;
        while ( usu_existe ==null && i < this.usuarios.size()) {
            if (this.usuarios.get(i).getNombre().equals(username)){
                usu_existe =this.usuarios.get(i);
            }
            i++;
        }
        return usu_existe;
    }

    //_________________________________________________________________________Opcion Escribir Mensaje____________________________________________________________________
    public void menuMensajes(DataInputStream entrada, DataOutputStream salida){
        try {
            boolean flag = false;
            while(!flag){
                this.response += opcionesMenuMensaje();
                salida.writeUTF(this.response);
                String opcion = entrada.readUTF();
                if (opcion.equals("3")){
                    flag = true;
                }else{
                    if(opcion.equals("1")){//Inicio de Sesion.
                        escribirMensaje(entrada,salida);
                        this.response = "El mensaje fue enviado con exito.!!";
                    }

                    if(opcion.equals("2")){//Registro.
                        bajarMensajes(entrada,salida);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error!");
            this.response = "\n "+"Ha ocurrido un error con el Menu Mensajes!";
            e.printStackTrace();
        }
    }

    public void escribirMensaje(DataInputStream entrada, DataOutputStream salida){
        try {
            salida.writeUTF("Ingrese usuario destino");    
            String user_destino= entrada.readUTF();
            salida.writeUTF("Ingrese asunto");
            String asunto= entrada.readUTF();
            salida.writeUTF("Ingrese mensaje");    
            String msj= entrada.readUTF();
            Mensaje mensaje = new Mensaje(this.usuario_actual.getNombre(), user_destino, asunto, msj);
            enviarMensaje(mensaje);

        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error!");
            this.response = "\n "+"Ha ocurrido un error al Escribir un Mensaje.!";
            e.printStackTrace();
        }
        
    }


    public void enviarMensaje( Mensaje mensaje){
        int i = 0;
        boolean flag = false;
        while (!flag && i < this.usuarios.size()) {
            if(usuarios.get(i).getNombre().equals(mensaje.getUsuario_destino())){
                usuarios.get(i).guardarMensaje(mensaje);
                flag = true;
            }   
            i++;
        }
    }
    //_________________________________________________________________________FIN Opcion Escribir Mensaje____________________________________________________________________

    //_________________________________________________________________________Opcion Bajar Mensajes____________________________________________________________________
    public void bajarMensajes(DataInputStream entrada, DataOutputStream salida){

    }

    //_________________________________________________________________________FIN Opcion Bajar Mensajes_________________________________________________________________


    //Opciones de los Menus.
    public String opcionesMenuLogin(){
        return "==========================\n    "
        + "Menu Login\n--------------------------"
        + "\n<1> - Iniciar Sesion\n<2> - Registrarse\n<3> - Desconectarse\n"
        + "Ingrese una Opcion: \n--------------------------\n";
    }

    public String opcionesMenuMensaje(){
        return "==========================\n    "
        + "Menu Mensajes\n--------------------------"
        + "\n<1> - Escribir Mensaje\n<2> - Listar Mensajes\n<3> - Salir\n"
        + "Ingrese una Opcion: \n--------------------------\n";
    }
}