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
                atenderCliente(clientSocket);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void atenderCliente(Socket client)
    {
        try {
            boolean flag= false;
            DataOutputStream salida= new DataOutputStream(client.getOutputStream());    //Canal de salida de datos al cliente
            DataInputStream entrada= new DataInputStream(client.getInputStream());      //Canal de entrada de datos al cliente
            while ( !flag ) {
                String rec = entrada.readUTF();//se queda a la espera de algun mensaje del cliente
                if ( !rec.equals("exit") ) {
                    System.out.println("Mensaje del cliente: " + rec);
                    System.out.println("------------------------------------");
                    salida.writeUTF("El server responde: " + rec);//Le manda el mensaje al cliente mediante el canal de salida
                } else {
                    flag = true ;
                    salida.writeUTF("El cliente se ha desconectado.");//Le manda el mensaje al cliente mediante el canal de salida
                    System.out.println("=====================================");
                    salida.close(); //cierre canal salida
                    client.close();//cierre conexion con el cliente
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        Servidor server = new Servidor();
    }    
}
