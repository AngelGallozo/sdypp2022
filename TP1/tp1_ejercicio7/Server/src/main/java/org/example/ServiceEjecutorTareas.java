package org.example;

import com.google.gson.Gson;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class ServiceEjecutorTareas  implements EjecutorTareas {

    private Gson gson= new Gson();
    Logger log;

    public ServiceEjecutorTareas(Logger log){
        this.log = log;
    }

    @Override
    public String ejecutarTareas(String objectJson, String classname) throws RemoteException{
        String resultado = "null";
        Tarea tarea=null;
        System.out.println("Servidor recibio Tarea.");
        try{
            tarea = (Tarea) this.gson.fromJson(objectJson, Class.forName(classname));
            resultado = tarea.ejecutar();
            System.out.println("Resultado de la tarea: " + resultado);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
   
}
