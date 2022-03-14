package sdypp_ejercicios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;  
import static java.time.temporal.ChronoUnit.MILLIS;

public class Cliente 
{
    private String ip_Destino;
    private String puerto_Destino;

	public Cliente(){
        try{
            Scanner scanner = new Scanner(System.in);

            System.out.println("=====================================");
            System.out.println("¡Cliene inicializado!");
            System.out.println("Ingrese la IP donde corre el servidor");

            boolean flag = false;
            while(!flag){
                this.ip_Destino = scanner.nextLine();
                if(!validar_ip(this.ip_Destino)){
                    System.out.println("ip no valido, ingrese otra");
                }else{
                    flag = true;
                }
            }
            System.out.println("Ingrese el puerto donde corre el servidor");
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

            Socket socket = new Socket(this.ip_Destino, Integer.parseInt(this.puerto_Destino));
            System.out.println("Conectado con el Servidor: " + this.ip_Destino + "\n");

            DataOutputStream salida= new DataOutputStream(socket.getOutputStream());//Canal de salida de datos al servidor
            DataInputStream entrada= new DataInputStream(socket.getInputStream());//Canal de entrada de datos al servidor
            while(!flag) 
            {
                String input = scanner.nextLine();

                if(input.equals("exit")) {
                	flag=true;
                }

                System.out.println("------------------------------------"); 

                LocalTime tiempoAhora = java.time.LocalTime.now();
                System.out.println("Tiempo mandado --> " + tiempoAhora); 

                salida.writeUTF(input);//Le manda el mensaje al servidor mediante el canal de salida
                String rec= entrada.readUTF();//se queda a la espera de algun mensaje del servidor
                System.out.println("Respuesta del Servidor");
                System.out.println(rec);
                LocalTime tiempoDespues = java.time.LocalTime.now();  
                
                System.out.println("Tiempo recibido --> " + tiempoDespues); 

                System.out.println("Tiempo transcurrido (milisegundos) --> " + tiempoAhora.until(tiempoDespues, MILLIS));                
                System.out.println("------------------------------------");
            }

            socket.close();
            scanner.close();
            System.out.println( "Servidor "+ this.puerto_Destino + " desconectado.");
            System.out.println("=====================================");
            
        }catch (Exception e){
            System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
            System.out.println("Ha surgido un error");
            System.out.println(e);
        }
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

    public static void main( String[] args )
    {
        Cliente cliente = new Cliente();
    }
}
