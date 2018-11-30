/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicaspractica2;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 *
 * @author Miguel Gonzalez Garcia y Roberto Martinez Fernandez
 */
public class MetaheuristicasPractica2 {

    static final String FICHERO_PARAM = "Parametros.txt"; //Ruta del fichero con los parametros para el programa.
    static final int TAM_POBLACION = 50;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try{
            DataInputStream datos = new DataInputStream(new BufferedInputStream(new FileInputStream(FICHERO_PARAM)));
            String s;
            
            s = datos.readLine();
            String[] linea = s.split(" "); //Se obtiene la ruta al Fichero de Datos
            String rutaDatos = linea[1];
            Problema problema = new Problema(rutaDatos);
            
            s = datos.readLine();
            linea = s.split(" "); //Se obtiene la Semilla
            int sem = Integer.parseInt(linea[1]);
            
            //Se obtiene una poblacion inicial aleatoria para el Algoritmo Genetico Estacionario.
            Poblacion poblacionAGE = new Poblacion(TAM_POBLACION, sem, problema);
            poblacionAGE.ordenarPoblacion();
            
            //Se copia esa poblacion para el Algoritmo Genetico Generacional
            Solucion[] aux = new Solucion[poblacionAGE.getTam()];
                for(int i=0; i<poblacionAGE.getTam(); i++){
                    aux[i] = new Solucion(poblacionAGE.individuo(i));
                }
            Poblacion poblacionAGG = new Poblacion(poblacionAGE, aux);
            problema.MostrarDatos();
            
            s = datos.readLine();
            linea = s.split(" "); //Se obtiene la Semilla
            String opCruce = linea[1];
            
            long startTime1 = System.currentTimeMillis();
            AGEstacionario age = new AGEstacionario(poblacionAGE, sem, opCruce, problema, rutaDatos);
            age.Ejecutar();
            long endTime1 = System.currentTimeMillis() - startTime1;

            System.out.print("\n\nALGORIMO GENETICO ESTACIONARIO:\n");
            age.mejorSolucion().MostrarSolucion();
            System.out.println("\nTiempo de ejecucion Algoritmo Genetico Esatacionario: " + endTime1 + " ms.");
            
            System.out.print("___________________________________________\n");
            
            long startTime2 = System.currentTimeMillis();
            AGGeneracional agg = new AGGeneracional(poblacionAGG, sem, opCruce, problema, rutaDatos);
            agg.Ejecutar();
            long endTime2 = System.currentTimeMillis() - startTime2;
            
            System.out.print("\n\nALGORIMO GENETICO GENERACIONAL:\n");
            agg.mejorSolucion().MostrarSolucion();
            System.out.println("\nTiempo de ejecucion Algoritmo Genetico Generacional: " + endTime2 + " ms.");
            
        }catch(Exception e){
            System.err.printf(e.getMessage()+"\n");
        }
        
        
    }
    
}
