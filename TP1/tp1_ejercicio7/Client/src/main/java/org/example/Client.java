
package org.example;
import com.google.gson.Gson;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MILLIS;

public class Client {
    private String ip_Destino;
    private String puerto_Destino;

    public Client(){
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("=====================================");
            System.out.println("¡Cliente inicializado!");
            System.out.println("Ingrese la IP donde corre el servidor de Tareas");

            boolean flag = false;
            while(!flag){
                this.ip_Destino = scanner.nextLine();
                if(!validar_ip(this.ip_Destino)){
                    System.out.println("ip no valida, ingrese otra");
                }else{
                    flag = true;
                }
            }
            System.out.println("Ingrese el puerto donde corre el servidor de Tareas");
            flag = false;
            while(!flag){
                this.puerto_Destino = scanner.nextLine();
                if(!validar_puerto(this.puerto_Destino)){
                    System.out.println("puerto no valido, ingrese otra");
                }else{
                    flag = true;
                }
            }
            System.out.println("=====================================");

            flag=false;
            Registry clientRMI = LocateRegistry.getRegistry(ip_Destino, Integer.parseInt(puerto_Destino));
            System.out.println("Conectado al IP: "+ip_Destino+" Puerto: "+puerto_Destino);
            System.out.println("------------------------------------"); 

            EjecutorTareas ri = (EjecutorTareas) clientRMI.lookup("ServidorTareas");
            
            NumeroAleatorio na = new NumeroAleatorio(988);
            Gson gson = new Gson();
            String jsonMsg = gson.toJson(na,NumeroAleatorio.class);
            String classname= na.getClass().toString().split("class ")[1];
            System.out.println("Numero Aleatorio: " + ri.ejecutarTareas(jsonMsg,classname));

            VerificarPrimo vp = new VerificarPrimo(11);
            jsonMsg = gson.toJson(vp,VerificarPrimo.class);
            classname= vp.getClass().toString().split("class ")[1];
            System.out.println("Verificar Primo: " + ri.ejecutarTareas(jsonMsg,classname));

            System.out.println( "Servidor "+ this.puerto_Destino + " desconectado.");
            System.out.println("=====================================");
        } catch (Exception e) {
            System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
            System.out.println("Ha surgido un error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client cliente = new Client();
    }

    public boolean validar_ip(String input){
        boolean validado = true;
        try{
            int index = 0;
            while((validado) && (index < 4)){
                String octeto = "";
                if(index != 3){
                    octeto = input.substring(0, input.indexOf("."));
                    input = input.substring(input.indexOf(".")+1);
                }else{
                    octeto = input;
                }
                if((!octeto.matches("[0-9]+")) || (!validar_octeto(Integer.parseInt(octeto))))
                    validado = false;
                index++;
            }

        }catch (Exception e){
            validado = false;
        }
        return validado;
    }

    public boolean validar_octeto(int octeto){
        boolean validado = false;
        if((0 <= octeto)&&(octeto <= 255))
            validado = true;
        return validado;
    }

    public boolean validar_puerto(String port){
        boolean validado = false;
        try{
            int puerto = Integer.parseInt(port);
            if((1024 <= puerto)&&(puerto <= 65535))
                validado = true;
        
        }catch(Exception e){
            validado = false;
        }
        return validado;
        
    }

}
