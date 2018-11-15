/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicaspractica2;

/**
 *
 * @author Miguel Gonzalez Garcia y Roberto Martinez Fernandez
 */
public class Poblacion {
    
    private Solucion[] individuos;
    private int tam;
    private int semilla;
    
    public Poblacion(int _tam, int _semilla, Problema prob){
        tam = _tam;
        individuos = new Solucion[tam];
        semilla = _semilla;
        for(int i=0; i<tam; i++){
            individuos[i] = new Solucion(prob);
            individuos[i].GenerarAleatoria(semilla);
        }
    }
    
    public Solucion individuo(int index){
        return individuos[index];
    }
    
    public int getTam(){
        return tam;
    }
    
}
