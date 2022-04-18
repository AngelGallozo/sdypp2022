package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;
import com.google.gson.Gson;

public class ServerSumaVec {
    final int port = 9090;
    private final Logger log = Logger.getLogger(ServerSumaVec.class.getName());
    private FileHandler fh;  
    
    public ServerSumaVec(){
        try {

            this.fh = new FileHandler("logFile.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            this.log.addHandler(this.fh);

            log.info("Server escuchando en el puerto: "+port);
            System.out.println("=====================================");
            
            Registry serverRMI = LocateRegistry.createRegistry(port);
            log.info("RMI Registry se inicio en el puerto: "+port);
            ServiceSum ss = new ServiceSum(this.log);
            SumVecInt srvsumavec = (SumVecInt) UnicastRemoteObject.exportObject(ss, 6666);
            serverRMI.rebind("Suma-vectores", srvsumavec);
            log.info("¡¡Servidor levantado con exito!");
            
        }catch (Exception e){
            log.severe("Hubo un error al iniciar el servidor!\n" + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ServerSumaVec ser= new ServerSumaVec();
    }
}
