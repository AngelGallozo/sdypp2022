package sdypp_ejercicios;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerSumaVec {
    final int port = 9090;
    
    public ServerSumaVec(){
        try {
            System.out.println("Server escuchando en el puerto: "+port);
            System.out.println("=====================================");
            
            Registry serverRMI = LocateRegistry.createRegistry(port);
            System.out.println ("RMI Registry se inicio en el puerto: "+port);
            ServiceSum ss = new ServiceSum();
            SumVecInt srvsumavec = (SumVecInt) UnicastRemoteObject.exportObject(ss, 6666);
            serverRMI.rebind("Suma-vectores", srvsumavec);
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ServerSumaVec ser= new ServerSumaVec();
    }
}
