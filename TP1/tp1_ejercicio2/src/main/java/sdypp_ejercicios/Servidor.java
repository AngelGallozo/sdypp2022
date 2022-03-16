package sdypp_ejercicios;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Servidor {

    final int port = 9090;

	public Servidor()
    {
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server escuchando en el puerto: "+port);
            System.out.println("=====================================");

            while (true){
                Socket clientSocket = serverSocket.accept(); //Queda a la espera conexion del cliente
                int port_cli= clientSocket.getPort();
                System.out.println( "Atendiendo al cliente: " + port_cli);
                System.out.println("------------------------------------");
                ServerHilo sh = new ServerHilo(clientSocket);
                Thread serverThread = new Thread(sh);
                serverThread.start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main( String[] args )
    {
        Servidor server = new Servidor();
    }    
}
