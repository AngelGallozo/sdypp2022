package sdypp_ejercicios;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WeatherInt extends Remote {
    public String getWeather()throws RemoteException;
}
