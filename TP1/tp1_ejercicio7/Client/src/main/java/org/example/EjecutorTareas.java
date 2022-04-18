package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EjecutorTareas extends Remote {
    public String ejecutarTareas(String ObjectJson, String Classname)throws RemoteException;
}
