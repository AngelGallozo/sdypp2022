package sdypp_ejercicios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MILLIS;

public class Cliente 
{
    private String ip_Destino;
    private String puerto_Destino;
    
	public Cliente(){
        try{
            Scanner scanner = new Scanner(System.in);

            System.out.println("=====================================");
            System.out.println("¡Cliente inicializado!");
            System.out.println("Ingrese la IP donde corre el servidor");

            boolean flag = false;
            while(!flag){
                this.ip_Destino = scanner.nextLine();
                if(!validar_ip(this.ip_Destino)){
                    System.out.println("¡Direccion IP no valida!\nLa IP debe tener formato de IPv4.\nVuelva a ingresar:");
                }else{
                    flag = true;
                }
            }
            System.out.println("\n\nIngrese el puerto donde corre el servidor");
            flag = false;
            while(!flag){
                this.puerto_Destino = scanner.nextLine();
                if(!validar_puerto(this.puerto_Destino)){
                    System.out.println("¡Puerto no valido! El numero de puerto debe estar entre 1024 y 65535.\nVuelva a ingresar:");
                }else{
                    flag = true;
                }
            }
            System.out.println("=====================================\n");

            Socket socket = new Socket(this.ip_Destino, Integer.parseInt(this.puerto_Destino));
            System.out.println("¡Conectado con el Servidor: " + this.ip_Destino + "!\n");

            DataOutputStream salida= new DataOutputStream(socket.getOutputStream());//Canal de salida de datos al servidor
            DataInputStream entrada= new DataInputStream(socket.getInputStream());//Canal de entrada de datos al servidor
            
            flag = false;
            while(!flag) {
                String rec= entrada.readUTF();
                if(rec.equals("-1"))
                    flag = true;
                else{
                    System.out.println(rec);
                    String input = scanner.nextLine();
                    salida.writeUTF(input);
                }
            }

            socket.close();
            scanner.close();
            System.out.println( "Servidor "+ this.puerto_Destino + " desconectado.");
            System.out.println("=====================================");
            
        }catch (Exception e){
            System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
            System.out.println("Ha surgido un error!! Desconectando del servidor");
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
