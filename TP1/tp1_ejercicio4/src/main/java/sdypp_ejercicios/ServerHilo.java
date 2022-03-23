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
                salida.writeUTF(this.response);     //enviamos Menu Login.
                String opcion = entrada.readUTF();
                if (opcion.equals("3")){
                    flag = true ;
                    salida.writeUTF("-1");  //Señal de desconexion al cliente.
                }else{
                    if(opcion.equals("1")){     //Inicio de Sesion.
                        if (iniciarSesion(entrada, salida)){
                            menuMensajes(entrada, salida);
                        }
                    }
                    if(opcion.equals("2")){//Registro.
                        if(registrarse(entrada, salida)){
                            menuMensajes(entrada, salida);
                        }
                    }else{  // Respuesta invalida
                        this.response = "Opcion invalida!! Elija una de las opciones anteriores! \n";
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
            Usuario usuario_log = existeUsuario(username);
            if (usuario_log != null){
                if(usuario_log.getPassword().equals(password)){
                    this.usuario_actual = usuario_log;
                    this.log.info("Usuario <" + this.client.getPort() +"> Ha iniciado sesión como <" + username +">");
                    respuesta=true;
                }else{
                    respuesta=false;
                    this.log.warning( "Usuario <" + this.client.getPort() + "> Error: Usuario o Contrasenia erroneos");
                    this.response = "\n "+"Error: Usuario o Contrasenia erroneos.";
                }
            }else{
                respuesta=false;
                this.log.warning( "Usuario <" + this.client.getPort() + "> Error: Usuario no existe");
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
            Usuario usuario_log = existeUsuario(username);
            if(usuario_log == null){
                this.usuario_actual = new Usuario(username, password);
                this.usuarios.add(this.usuario_actual);
                this.log.info( "Usuario <" + this.client.getPort() + "> Usuario registrado!");
            }else{
                this.response = "\n "+"Error: Usuario ya existe.";
                this.log.warning( "Usuario <" + this.client.getPort() + "> Usuario ya existe");
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

    public Usuario existeUsuario(String username){
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
        this.response = "\nBienvenido " + this.usuario_actual.getNombre() + "!\n";
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
        this.response = "";
    }

    public void escribirMensaje(DataInputStream entrada, DataOutputStream salida){
        try {
            salida.writeUTF("Ingrese usuario destino");    
            String user_destino= entrada.readUTF();
            if(this.validarDestino(user_destino)){
                salida.writeUTF("Ingrese asunto");
                String asunto= entrada.readUTF();
                salida.writeUTF("Ingrese mensaje");    
                String msj= entrada.readUTF();
                Mensaje mensaje = new Mensaje(this.usuario_actual.getNombre(), user_destino, asunto, msj);
                enviarMensaje(mensaje);
                this.response = "El mensaje fue enviado con exito.!!\n";
                this.log.info( "Usuario <" + this.client.getPort() + "> Mensaje enviado con exito, de <" 
                    + this.usuario_actual.getNombre() + "> a <" +user_destino + ">");
            }else{
                this.response = "El usuario destino no existe!!\n";
                this.log.warning( "Usuario <" + this.client.getPort() + "> El usuario destino no existe");
            }

        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error!");
            this.response = "\n "+"Ha ocurrido un error al Escribir un Mensaje.!";
            e.printStackTrace();
        }
        
    }

    public boolean validarDestino(String destino){
        boolean response = false;
        if(this.existeUsuario(destino) != null)
            response = true;
        return response;
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
        // Lista de mensjaes del usuario
        boolean flag = false;
        this.response = "";
        while(!flag){
            try {
                this.log.info( "Usuario <" + this.client.getPort() + "> Bajo su lista de mensajes");
                this.response += " -- Lista de mensajes --\n";
                this.response += this.usuario_actual.getList_mensajes();
                this.response += "\nSeleccione el mensaje que quiera ver\nMande 'x' para salir";
                salida.writeUTF(this.response); 
                String opcion = entrada.readUTF();
                if(opcion.equals("x")){
                    flag = true;
                }
                else{
                    if(!getMensajeIndividual(opcion, entrada, salida))
                        this.response = " ## Opcion invalida - Vuelva a ingresar ##\n";
                    else
                        this.log.info( "Usuario <" + this.client.getPort() + "> Bajo mensaje " + opcion);
                }
            } catch (Exception e) {
                System.out.println("################################");
                this.log.severe("Ha ocurrido un error!");
                this.response = "\nHa ocurrido un error al bajar los Mensajes!";
                e.printStackTrace();
            }
        }
        this.response = "";
    }

    // bajar Mensaje individual(indice en el array)
    public boolean getMensajeIndividual(String opcion, DataInputStream entrada, DataOutputStream salida){
        boolean valido = true;
        try {
            int index = Integer.parseInt(opcion);
            if(index < 0)
                valido = false;
            else{
                String mensaje = this.usuario_actual.getMensaje(index);
                salida.writeUTF(mensaje + "\n\nPulse cualquier tecla para salir");
                String tecla = entrada.readUTF();
                valido = true;
                this.response = "";
            }
        } catch (Exception e) {
            valido = false;
        }
        return valido;
    }

    public boolean eliminarMensaje(int index){
        boolean response = false;
        // borrar mensaje en la lista del usuario actual
        try {
            this.usuario_actual.removerMensaje(index);
            response = true;
        } catch (IndexOutOfBoundsException e) {
            response = false;
            this.log.severe("Usuario <" + this.client.getPort() + "> - IndexOutOfBoundsException - Indice de mensaje en lista invalido");
            this.response = "¡Indice de mensaje invalido!\n";
        }
        return response;
    }

    //_________________________________________________________________________FIN Opcion Bajar Mensajes_________________________________________________________________


    //Opciones de los Menus.
    public String opcionesMenuLogin(){
        return "==========================\n    "
        + "Menu Login\n--------------------------"
        + "\n<1> - Iniciar Sesion\n<2> - Registrarse\n<3> - Desconectarse\n"
        + "Ingrese una Opcion: \n--------------------------";
    }

    public String opcionesMenuMensaje(){
        return "==========================\n    "
        + "Menu Mensajes\n--------------------------"
        + "\n<1> - Escribir Mensaje\n<2> - Listar Mensajes\n<3> - Salir\n"
        + "Ingrese una Opcion: \n--------------------------";
    }
}