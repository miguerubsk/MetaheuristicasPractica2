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
            Poblacion poblacion = new Poblacion(TAM_POBLACION, sem, problema);
            
            poblacion.ordenarPoblacion();
            
            //problema.MostrarDatos();
            for(int i=0; i<TAM_POBLACION; i++){
                poblacion.individuo(i).MostrarSolucion();
            }
            
            s = datos.readLine();
            linea = s.split(" "); //Se obtiene la Semilla
            String opCruce = linea[1];
//            AGEstacionario age = new AGEstacionario(poblacion, sem, opCruce, problema, rutaDatos);
//            age.Ejecutar();

            AGGeneracional agg = new AGGeneracional(poblacion, sem, opCruce, problema, rutaDatos);
            agg.Ejecutar();
            
            for(int i=0; i<TAM_POBLACION; i++){
//                age.individuo(i).MostrarSolucion();
                agg.individuo(i).MostrarSolucion();
            }
        }catch(Exception e){
            System.err.printf(e.getMessage()+"\n");
        }
        
        
    }
    
}
