package sdypp_ejercicios;

import java.util.logging.Logger;
import java.rmi.RemoteException;
import java.util.Random;

public class ServiceWeather  implements WeatherInt {

    private Logger log;

    public ServiceWeather(Logger log){
        this.log = log;
    }

    @Override
    public String getWeather() throws RemoteException {
        try{
            log.info("GetWeather has been called!");
            Random r= new Random();
            int grados = r.nextInt(35);
            String clima= "Templado"; 
            if ( grados <= 15 ) {
                clima="Frío";
            }else if ( grados >= 30 ) {
                clima="Caluroso";
            }
            log.info("Clima devuelto: " + "°"+grados+" ---> "+clima);
            return "°"+grados+" ---> "+clima;
        }catch(Exception e){
            log.severe("Error al obtener la temperatura!\n" + e.getMessage());
            return "°00 ---> nulo";
        }
    }
    
}
