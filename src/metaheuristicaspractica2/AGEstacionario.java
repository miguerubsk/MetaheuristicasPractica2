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
    private String operadorCruce;
    
    public AGEstacionario(Poblacion _poblacion, int sem){
        poblacion = _poblacion;
        semilla = sem;
    }
    
    public void Ejecutar(){
        Solucion[] padres = new Solucion[2];
        Solucion[] hijos = new Solucion[2];
//        while(condicion de parada por evaluaciones o generaciones)
        Seleccion(padres);
        Cruce(padres, hijos);
        Mutacion(hijos);
        Reemplazamiento(padres);
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
    
    private void Cruce(Solucion[] padres, Solucion[] hijos){
        Random rand = new Random(semilla);
        int corte1 = 0, corte2 = 0, tmp;
        //Seleccion de los puntos de corte
        while((corte1 - corte2 < poblacion.getTam() - 1) || (corte1 - corte2 == 0)){
            corte1 = rand.nextInt(poblacion.getTam());
            corte2 = rand.nextInt(poblacion.getTam());
            if(corte2 < corte1){
                tmp = corte1;
                corte1 = corte2;
                corte2 = tmp;
            }
        }
        switch (operadorCruce){
            case "OX":
                operadorOX(padres, hijos, corte1, corte2);
                break;
            case "PMX":
                
                break;
        }
        for(int i =0; i<2; i++){
            hijos[i].Coste();
        }
        
    }
    
    private void operadorOX(Solucion[] padres, Solucion[] hijos, int corte1, int corte2){
        for(int j=0; j<2; j++){
            int iteradorHijo = corte2, iteradorPadre = corte2;
            int[] dlb = new int[padres[j%2].getTam()];
            for(int i=0; i<dlb.length; i++){
                dlb[i] = 0;
            }
            for(int i=corte1; i<corte2; i++){
                hijos[j%2].setValorPermutacion(i, padres[j%2].getValorPermutacion(i));
                dlb[padres[j%2].getValorPermutacion(i)] = 1;
            }
            while(iteradorHijo == corte1){
                if(dlb[padres[(j+1)%2].getValorPermutacion(iteradorPadre)] == 0){
                    hijos[j%2].setValorPermutacion(iteradorHijo, padres[(j+1)%2].getValorPermutacion(iteradorPadre));
                    dlb[padres[(j+1)%2].getValorPermutacion(iteradorPadre)] = 1;
                    iteradorHijo = (iteradorHijo+1)%hijos[j%2].getTam();
                }
                iteradorPadre = (iteradorPadre+1)%padres[j%2].getTam();
            }
        }
    }
    
    private void Mutacion(Solucion[] hijos){
        Random rand = new Random(semilla);
        int valor;
        for(int j=0; j<2; j++){
            for(int i=0; i<hijos[j].getTam(); i++){
                if(rand.nextFloat() < 0.001*hijos[j].getTam()){
                    do{
                        valor = rand.nextInt(hijos[j].getTam());
                    }while(valor == i);
                    hijos[j].Intercambio(i, valor);
                    hijos[j].CosteIntercambio(i, valor);
                }
            }
        }
        
    }
    
    private void Reemplazamiento(Solucion[] padres){
        
    }

}
