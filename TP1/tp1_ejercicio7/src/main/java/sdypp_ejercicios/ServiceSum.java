package sdypp_ejercicios;

import com.google.gson.Gson;
import java.rmi.RemoteException;

public class ServiceSum  implements SumVecInt {
    private Gson gson= new Gson();
    @Override
    public int[] getSumaVectores(int[] v1, int[] v2) throws RemoteException{
        int[] v3 = new int[10];
        for (int i =0;i<v1.length;i++){
            v3[i]= v1[i]+v2[i];
            v1[i] = 0;
            v2[i] = 0;
        }

        return v3;
    }
    @Override
    public int[] getRestaVectores(int[] v1, int[] v2) throws RemoteException{
        int[] v3 = new int[10];
        for (int i =0;i<v1.length;i++){
            v3[i]= v1[i]-v2[i];
            v1[i] = 0;
            v2[i] = 0;
        }

        return v3;
    }

    @Override
    public String getSumaVecObjetos(String sov) throws RemoteException {
        ObjVector ov = gson.fromJson(sov, ObjVector.class);
        int[] v3 = new int[10];
        ov.setV3(v3);
        for (int i =0;i<ov.getV1().length;i++){
            ov.getV3()[i]= ov.getV1()[i]+ov.getV2()[i];
            ov.getV1()[i] = 0;
            ov.getV2()[i] = 0;
        }

        String objetoJSONString = gson.toJson(ov);
        return objetoJSONString;
    }

    @Override
    public String getRestaVecObjetos(String sov) throws RemoteException {
        ObjVector ov = gson.fromJson(sov, ObjVector.class);
        int[] v3 = new int[10];
        ov.setV3(v3);
        for (int i =0;i<ov.getV1().length;i++){
            ov.getV3()[i]= ov.getV1()[i]-ov.getV2()[i];
            ov.getV1()[i] = 0;
            ov.getV2()[i] = 0;
        }

        String objetoJSONString = gson.toJson(ov);
        return objetoJSONString;
    }
}
