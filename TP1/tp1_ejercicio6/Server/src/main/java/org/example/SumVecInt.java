package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SumVecInt extends Remote {
    public int[] getSumaVectores(int[] v1, int[] v2)throws RemoteException;
    public int[] getRestaVectores(int[] v1, int[] v2)throws RemoteException;
    public String getSumaVecObjetos(String ov)throws RemoteException;
    public String getRestaVecObjetos(String sov)throws RemoteException;
}
