package sdypp_ejercicios;

import java.rmi.RemoteException;
import java.util.Random;

public class ServiceWeather  implements WeatherInt {

    @Override
    public String getWeather() throws RemoteException {
        Random r= new Random();
        int grados = r.nextInt(35);
        String clima= "Templado"; 
        if ( grados <= 15 ) {
            clima="Frío";
        }else if ( grados >= 30 ) {
            clima="Caluroso";
        }
        return "°"+grados+"--->"+clima;
    }
    
}
