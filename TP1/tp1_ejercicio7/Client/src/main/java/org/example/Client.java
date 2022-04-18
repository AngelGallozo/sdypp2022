
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
            System.out.println("Ingrese la IP donde corre el servidor de vectores");

            boolean flag = false;
            while(!flag){
                this.ip_Destino = scanner.nextLine();
                if(!validar_ip(this.ip_Destino)){
                    System.out.println("ip no valida, ingrese otra");
                }else{
                    flag = true;
                }
            }
            System.out.println("Ingrese el puerto donde corre el servidor de vectores");
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

            SumVecInt ri = (SumVecInt) clientRMI.lookup("Suma-vectores");
            int[] v1 = {1,2,3,4,5,6,7,8,9,10};
            int[] v2 = {1,2,3,4,5,6,7,8,9,10};

            flag= false;
            while ( !flag ) {
                System.out.println(opcionesVectores());
                String stropcion = scanner.nextLine();
                try{
                    Integer.parseInt(stropcion);
                     if (stropcion.equals("0")){
                        flag = true ;
                    }else{
                        if(stropcion.equals("1")){     //Inicio de Sesion.
                            operarVectores(ri,v1,v2,true);
                        }else if(stropcion.equals("2")){//Registro.
                            operarVectores(ri,v1,v2,false);
                        }else{
                            // Respuesta invalida
                            System.out.println("Opcion invalida!! Elija una de las opciones anteriores! \n");
                        }
                    }   
                }
                catch(Exception e){
                    System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
                    System.out.println("Opcion invalida!! Elija una de las opciones anteriores!  ");
                }
                
            }

            System.out.println( "Servidor "+ this.puerto_Destino + " desconectado.");
            System.out.println("=====================================");
        } catch (Exception e) {
            System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
            System.out.println("Ha surgido un error");
            System.out.println(e);
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

    public String mostrarv (int[] v){
        String mensaje = "";
        for (int k=0; k<v.length;k++){
            mensaje+= v[k]+"-";

        }
        return mensaje;
    }

    //Opciones de Operaciones.
    public String opcionesVectores(){
        return "==========================\n    "
        + "Operaciones con Vectores\n--------------------------"
        + "\n<1> - Sumar\n<2> - Restar\n<0> - Salir\n"
        + "Ingrese una Opcion: \n--------------------------";
    }


    public void operarVectores(SumVecInt ri,int[] v1 ,int[] v2, boolean suma){
        try{
            String operacion;
            if(suma){
                operacion ="suma";            
            }else{
                operacion ="resta";
            }

            //Prueba con vectores pasados por valor -> simula el error de que no se modifican los vectoires como se quiere.
            System.out.println("************* Antes de la "+operacion+" *************");
            System.out.println("V1:"+mostrarv(v1));
            System.out.println("V2:"+mostrarv(v2));

            LocalTime tiempoAhora = java.time.LocalTime.now();
            System.out.println("Tiempo mandado --> " + tiempoAhora); 

            int[] v3;
            if(operacion.equals("suma")){
                v3 = ri.getSumaVectores(v1, v2);
            }else{
                v3 = ri.getRestaVectores(v1, v2);
            }
            

            System.out.println("*************LUEGO DE LA +"+operacion+" *************");
            System.out.println("V1:"+mostrarv(v1));
            System.out.println("V2:"+mostrarv(v2));
            System.out.println("V3:"+mostrarv(v3));

            LocalTime tiempoDespues = java.time.LocalTime.now();  
                
            System.out.println("Tiempo recibido --> " + tiempoDespues); 

            System.out.println("Tiempo transcurrido (milisegundos) --> " + tiempoAhora.until(tiempoDespues, MILLIS));                
            System.out.println("------------------------------------");

            //Prueba con vectores pasados dentro de un objeto y el objeto es pasado en formato gson a la funcion.
            Gson gson = new Gson();
            v3 = new int[10];
            ObjVector v = new ObjVector(v1,v2,v3);
            String jsonMsg = gson.toJson(v,ObjVector.class);
            System.out.println("Obj COnvertido a Formato JSON");
            System.out.println(jsonMsg);        

            System.out.println("*************Antes de la "+ operacion+" *************");
            System.out.println("V1:"+mostrarv(v1));
            System.out.println("V2:"+mostrarv(v2));

            tiempoAhora = java.time.LocalTime.now();
            System.out.println("Tiempo mandado --> " + tiempoAhora); 
            
            if(operacion.equals("suma")){
                v = gson.fromJson(ri.getSumaVecObjetos(jsonMsg),ObjVector.class);
            }else{
                v = gson.fromJson(ri.getRestaVecObjetos(jsonMsg),ObjVector.class);
            }

            System.out.println("*************LUEGO DE LA "+operacion+" *************");
            System.out.println("V1:"+mostrarv(v.getV1()));
            System.out.println("V2:"+mostrarv(v.getV2()));
            System.out.println("V3:"+mostrarv(v.getV3()));

            tiempoDespues = java.time.LocalTime.now();  
                
            System.out.println("Tiempo recibido --> " + tiempoDespues); 

            System.out.println("Tiempo transcurrido (milisegundos) --> " + tiempoAhora.until(tiempoDespues, MILLIS));                
            System.out.println("------------------------------------");
        }
        catch(Exception e){
            System.out.println("\n#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
            System.out.println("Ha surgido un error al operar con los vectores.");
            System.out.println(e);
        }
    }

}
