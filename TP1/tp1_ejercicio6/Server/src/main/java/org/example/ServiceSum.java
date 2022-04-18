package org.example;

import com.google.gson.Gson;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class ServiceSum  implements SumVecInt {

    private Gson gson= new Gson();
    Logger log;

    public ServiceSum(Logger log){
        this.log = log;
    }

    @Override
    public int[] getSumaVectores(int[] v1, int[] v2) throws RemoteException{
        int[] v3 = new int[10];
        try{
            log.info("Suma vectores has been called");
            for (int i =0;i<v1.length;i++){
                v3[i]= v1[i]+v2[i];
                v1[i] = 0;
                v2[i] = 0;
            }
            log.info("Server is returning: " + mostrarv(v3));
        }catch(Exception error){
            log.severe("Ha ocurrido un error!!!\n" + error.getMessage());
        }
        return v3;
    }

    @Override
    public int[] getRestaVectores(int[] v1, int[] v2) throws RemoteException{
        int[] v3 = new int[10];
        try{
            log.info("Resta vectores has been called");
            for (int i =0;i<v1.length;i++){
                v3[i]= v1[i]-v2[i];
                v1[i] = 0;
                v2[i] = 0;
            }
            log.info("Server is returning: " + mostrarv(v3));
        }catch(Exception error){
            log.severe("Ha ocurrido un error!!!\n" + error.getMessage());
        }
        return v3;
    }

    @Override
    public String getSumaVecObjetos(String sov) throws RemoteException {
        try{
            int[] v3 = new int[10];
            log.info(" Suma Objetos  has been called ");
            ObjVector ov = gson.fromJson(sov, ObjVector.class);
            ov.setV3(v3);
            for (int i =0;i<ov.getV1().length;i++){
                ov.getV3()[i]= ov.getV1()[i]+ov.getV2()[i];
                ov.getV1()[i] = 0;
                ov.getV2()[i] = 0;
            }
            String objetoJSONString = gson.toJson(ov);
            log.info("Server is returning  "+ objetoJSONString);
            return objetoJSONString;
        }catch(Exception error){
            log.severe("Ha ocurrido un error!!!\n" + error.getMessage());
            return sov;
        }
    }

    @Override
    public String getRestaVecObjetos(String sov) throws RemoteException {
        try{
            log.info("Resta Objetos has been called");
            ObjVector ov = gson.fromJson(sov, ObjVector.class);
            int[] v3 = new int[10];
            ov.setV3(v3);
            for (int i =0;i<ov.getV1().length;i++){
                ov.getV3()[i]= ov.getV1()[i]-ov.getV2()[i];
                ov.getV1()[i] = 0;
                ov.getV2()[i] = 0;
            }
            String objetoJSONString = gson.toJson(ov);
            log.info("Server is returning  "+ objetoJSONString);
            return objetoJSONString;
        }catch(Exception error){
            log.severe("Ha ocurrido un error!!!\n" + error.getMessage());
            return sov;
        }
    }

    public String mostrarv (int[] v){
        String mensaje = "";
        for (int k=0; k<v.length;k++){
            mensaje+= v[k]+"-";
        }
        return mensaje;
    }
}
