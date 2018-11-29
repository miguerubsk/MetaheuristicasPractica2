/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicaspractica2;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Miguel Gonzalez Garcia y Roberto Martinez Fernandez
 */
public class Problema {
    
    public static final int FLUJOS = 0;
    public static final int DISTANCIAS = 1;
    
    private int tam;
    private int[][][] matrizDatos;
    
    public Problema(String rutaArchivo){
        try{
            String s;
            
            //Lectura de los datos.
            DataInputStream datos = new DataInputStream(new BufferedInputStream(new FileInputStream(rutaArchivo)));
            
            //Obtencion del tamaño de la matriz.
            s = datos.readLine();
            String[] campos = s.split(" ");
            tam = Integer.parseInt(campos[0]);
            
            matrizDatos = new int[tam][tam][2];
            
            //Linea en blanco en el fichero.
            s=datos.readLine();
            
            //Inicialización y relleno de la matriz de flujos.
            
            for(int i=0; i<tam; i++){
                s=datos.readLine();
                Pattern patron = Pattern.compile(" * ");
                Matcher encaja = patron.matcher(s);
                String resultado = encaja.replaceAll(" ");
                campos = resultado.split(" ");
                for(int j=0; j<tam; j++){
                    matrizDatos[i][j][FLUJOS] = Integer.parseInt(campos[j+1]);
                }
            }
            
            //Linea en blanco entre matrices en el fichero.
            s=datos.readLine();
//            System.out.printf("\n");
            
            //Inicialización y relleno de la matriz de distancias.
            for(int i=0; i<tam; i++){
                s=datos.readLine();
                Pattern patron = Pattern.compile(" * ");
                Matcher encaja = patron.matcher(s);
                String resultado = encaja.replaceAll(" ");
                campos = resultado.split(" ");
                for(int j=0; j<tam; j++){
                    matrizDatos[i][j][DISTANCIAS] = Integer.parseInt(campos[j+1]);
//                    System.out.printf("%d ", matrizDatos[i][j][DISTANCIAS]);
                }
//                System.out.printf("\n");
            }
            
            //Cierre del fichero.
            datos.close();
            //Comprobación de posibles excepciones.
        }catch(FileNotFoundException e){
            System.out.println("No se ha encontrado el fichero");
        }catch(IOException ioe){
            System.out.println("Error en la E/S");
        }  
    }
    
    public int datos(int posi, int posj, int matriz){
        return matrizDatos[posi][posj][matriz];
    }
    
    public int getTam(){
        return tam;
    }
    
//    public void MostrarDatos(){
//        System.out.printf("Matriz de Flujos: \n\n");
//        for(int i=0; i<tam; i++){
//           for(int j=0; j<tam; j++){
//                    System.out.printf("%d ", matrizDatos[i][j][FLUJOS]);
//                }
//                System.out.printf("\n");
//        }
//        System.out.printf("=========================================================\n");
//        System.out.printf("Matriz de Distancias: \n\n");
//        for(int i=0; i<tam; i++){
//           for(int j=0; j<tam; j++){
//                    System.out.printf("%d ", matrizDatos[i][j][DISTANCIAS]);
//                }
//                System.out.printf("\n");
//        }
//    }
        
}
