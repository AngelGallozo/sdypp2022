package sdypp_ejercicios;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Imports de Logger
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Servidor {

    final int port = 9090;

    // private final Logger log = LoggerFactory.getLogger(Servidor.class);
    private final Logger log = Logger.getLogger(Servidor.class.getName());
    private FileHandler fh;  

	public Servidor()
    {
        try{
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
                this.log.info("Atendiendo al cliente: " + port_cli);
                System.out.println("------------------------------------");
                atenderCliente(clientSocket);
            }

        }catch(IOException e){
            log.severe("No se pudo abrir el fichero");
            e.printStackTrace();
        }catch(SecurityException e){
            log.severe("Hubo un problema con la seguridad del fileSystem");
            e.printStackTrace();
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
                    log.info("Mensaje del cliente: " + rec);
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

