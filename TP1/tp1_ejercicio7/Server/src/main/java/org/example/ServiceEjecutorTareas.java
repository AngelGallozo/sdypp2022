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
        try{
            log.info("Servidor recibio la Tarea " + classname);
            tarea = (Tarea) this.gson.fromJson(objectJson, Class.forName(classname));
            resultado = tarea.ejecutar();
            log.info("Resultado de la tarea: " + resultado);
        }catch(Exception e){
            log.severe("Hubo un error al ejecutar la tarea " + classname);
        }
        return resultado;
    }
   
}
