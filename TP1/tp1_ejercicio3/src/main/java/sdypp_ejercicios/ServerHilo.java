package sdypp_ejercicios;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerHilo implements Runnable {
	BufferedReader canalEntrada;
    PrintWriter canalSalida;
    Socket client;
    Logger log;

    public ServerHilo(Socket client, Logger log) {
        this.log = log;
        this.client = client;
    }
    
    public void run() {

		try {
            boolean flag= false;
            DataOutputStream salida= new DataOutputStream(client.getOutputStream());    //Canal de salida de datos al cliente
            DataInputStream entrada= new DataInputStream(client.getInputStream());      //Canal de entrada de datos al cliente
            while ( !flag ) {
                String rec = entrada.readUTF();//se queda a la espera de algun mensaje del cliente
                if ( !rec.equals("exit") ) {
                    this.log.info("Cliente <" + this.client.getPort() + "> -- Mensaje: " + rec);
                    System.out.println("------------------------------------");
                    salida.writeUTF("El server responde: " + rec);//Le manda el mensaje al cliente mediante el canal de salida
                } else {
                    flag = true ;
                    salida.writeUTF("El cliente se ha desconectado.");//Le manda el mensaje al cliente mediante el canal de salida
                    this.log.info("El cliente <"+ this.client.getPort() +"> se ha desconectado.");//Le manda el mensaje al cliente mediante el canal de salida
                    System.out.println("=====================================");
                    salida.close(); //cierre canal salida
                    client.close();//cierre conexion con el cliente
                }
            }
        } catch (Exception e) {
            System.out.println("################################");
            this.log.severe("Ha ocurrido un error!");
            e.printStackTrace();
        }

    }
}