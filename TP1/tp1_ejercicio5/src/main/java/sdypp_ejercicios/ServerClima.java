package sdypp_ejercicios;

import java.util.logging.FileHandler;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

// Imports de Logger
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerClima {
    final int port = 9090;

    // private final Logger log = LoggerFactory.getLogger(Servidor.class);
    private final Logger log = Logger.getLogger(ServerClima.class.getName());
    private FileHandler fh;  
    
    public ServerClima(){
        try {
             this.fh = new FileHandler("logFile.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            this.log.addHandler(this.fh);

            this.log.info("Server escuchando en el puerto: "+port);
            System.out.println("=====================================");

            Registry serverRMI = LocateRegistry.createRegistry(port);
            System.out.println ("RMI Registry se inicio en el puerto: "+port);

            ServiceWeather ss = new ServiceWeather(this.log);
            WeatherInt srvclima = (WeatherInt) UnicastRemoteObject.exportObject(ss, 6666);
            serverRMI.rebind("Info-clima", srvclima);
            System.out.println("¡¡Servidor levantado con exito!");
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


    public static void main(String[] args) {
        ServerClima ser= new ServerClima();
    }
}
