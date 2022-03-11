package sdypp_ejercicios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente 
{
    private String ip_Destino;
    private int puerto_Destino;

	public Cliente(){
        try{
            Scanner scanner = new Scanner(System.in);

            System.out.println("=====================================");
            System.out.println("¡Cliene inicializado!");
            System.out.println("Ingrese la IP donde corre el servidor");
            this.ip_Destino = scanner.nextLine();
            System.out.println("Ingrese el puerto donde corre el servidor");
            this.puerto_Destino = scanner.nextInt();
            System.out.println("=====================================");

            boolean flag=false;

            Socket socket = new Socket(this.ip_Destino, this.puerto_Destino);
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

                salida.writeUTF(input);//Le manda el mensaje al servidor mediante el canal de salida
                String rec= entrada.readUTF();//se queda a la espera de algun mensaje del servidor
                System.out.println("Respuesta del Servidor");
                System.out.println("------------------------------------");
                System.out.println(rec);
            }

            socket.close();
            scanner.close();
            System.out.println( "Servidor "+ this.puerto_Destino + " desconectado.");
            System.out.println("=====================================");
            
        }catch (Exception e){
            System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
            System.out.println("Ha surgido un error");
            e.printStackTrace();
        }


    }
    public static void main( String[] args )
    {
        Cliente cliente = new Cliente();
    }
}
