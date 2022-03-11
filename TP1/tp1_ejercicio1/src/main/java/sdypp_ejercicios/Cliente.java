package sdypp_ejercicios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Cliente 
{
    final int port = 9090;
	final String ip = "localhost";

	public Cliente(){
        try{
            Socket sc = new Socket(ip,port);
            System.out.println("Conectado con el Servidor: "+port);
            boolean flag=false;
            Scanner scanner = new Scanner(System.in);

            DataOutputStream salida= new DataOutputStream(sc.getOutputStream());//Canal de salida de datos al servidor
            DataInputStream entrada= new DataInputStream(sc.getInputStream());//Canal de entrada de datos al servidor
            while(!flag) 
            {
                String input = scanner.nextLine();

                if(input.equals("exit")) {
                	flag=true;
                }

                salida.writeUTF(input);//Le manda el mensaje al servidor mediante el canal de salida
                String rec= entrada.readUTF();//se queda a la espera de algun mensaje del servidor
                System.out.println("Respuesta del Servidor");
                System.out.println(rec);
            }

            sc.close();
            scanner.close();
            System.out.println( "Servidor "+ port + " desconectado.");

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void main( String[] args )
    {
        Cliente cliente = new Cliente();
    }
}
