package sdypp_ejercicios;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;

// Imports de Logger
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Servidor {

    final int port = 9090;
    private final Logger log = Logger.getLogger(Servidor.class.getName());
    private FileHandler fh;  
    private ArrayList<Usuario> usuarios;

	public Servidor()
    {
        try{
            usuarios = new ArrayList<Usuario>();

            this.fh = new FileHandler("logFile.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            this.log.addHandler(this.fh);

            ServerSocket serverSocket = new ServerSocket(port);
            this.log.info("Server escuchando en el puerto: "+port);
            System.out.println("=====================================");

            while (true){
                Socket clientSocket = serverSocket.accept(); //Queda a la espera conexion del cliente
                int port_cli= clientSocket.getPort();
                System.out.println("------------------------------------");
                this.log.info( "Atendiendo al cliente: " + port_cli);
                System.out.println("------------------------------------");
                ServerHilo sh = new ServerHilo(clientSocket, this.log, usuarios);
                Thread serverThread = new Thread(sh);
                serverThread.start();
            }

        }catch (Exception e){
            
            this.log.severe("Ha surgido un problema");
            e.printStackTrace();
        }
    }


    public static void main( String[] args )
    {
        Servidor server = new Servidor();
    }    
}
