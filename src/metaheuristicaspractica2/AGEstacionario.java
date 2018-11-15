/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicaspractica2;

import java.util.Random;

/**
 *
 * @author macosx
 */
public class AGEstacionario {
    
    private Poblacion poblacion;
    private int semilla;
    
    public AGEstacionario(Poblacion _poblacion, int sem){
        poblacion = _poblacion;
        semilla = sem;
    }
    
    public void Ejecutar(){
        Solucion[] padres = new Solucion[2];
        Seleccion(padres);
        Reemplazamiento(padres);
        Cruce();
        Mutacion();
    }
    
    private void Seleccion(Solucion[] padres){
        Random rand = new Random(semilla);
        int[] val = new int[2];
        int seleccionado = -1;
        for(int i=0; i<=1; i++){
            do {
                val[0] = rand.nextInt(poblacion.getTam());
            } while(val[0] == seleccionado);
            do {
                val[1] = rand.nextInt(poblacion.getTam());
            } while(val[0] == val[1] || val[1] == seleccionado);
            if(poblacion.individuo(val[0]).getCoste() < poblacion.individuo(val[1]).getCoste()){
                padres[i] = poblacion.individuo(val[0]);
                seleccionado = val[0];
            }else{
                padres[i] = poblacion.individuo(val[1]);
                seleccionado = val[1];
            }  
        }
    }
    
    private void Reemplazamiento(Solucion[] padres){
        
    }

    public void Cruce(){
    
    }

    private void Mutacion() {
        throw new UnsupportedOperationException("Not supported yet.");
    
}
}
