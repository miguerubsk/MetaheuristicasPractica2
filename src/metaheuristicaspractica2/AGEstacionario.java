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
    private Solucion[] padres;
    private Solucion[] hijos;
    private int numEvaluaciones;
    private int numGeneracion;
    
    public AGEstacionario(Poblacion _poblacion, int sem, String _cruce, Problema prob){
        poblacion = _poblacion;
        semilla = sem;
        operadorCruce = _cruce;
        numEvaluaciones = 0;
        numGeneracion = 0;
        padres = new Solucion[2];
        hijos = new Solucion[2];
        for(int i=0; i<2; i++){
           padres[i] = new Solucion(prob);
           hijos[i] = new Solucion(prob);
        }
        
    }
    
    public void Ejecutar(){
//        while(numEvaluaciones < 50 && numGeneracion < 50){
            Seleccion();
            Cruce();
            System.out.printf("Hey Listen\n");
            Mutacion();
            System.out.printf("Hey Listen\n");
            Reemplazamiento();
            System.out.printf("Hey Listen\n");
//        }
        
    }
    
    private void Seleccion(){
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
    
    private void Cruce(){
        Random rand = new Random(semilla);
        int corte1 = 0, corte2 = 0, tmp;
        //Seleccion de los puntos de corte
        while((corte1 - corte2 >= poblacion.getTam() - 1) || (corte1 - corte2 == 0)){
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
                operadorOX(corte1, corte2);
                break;
            case "PMX":
                operadorPMX(corte1, corte2);
                break;
        }
        for(int i =0; i<2; i++){
            hijos[i].Coste();
        }
        
    }
    
    private void operadorOX(int corte1, int corte2){
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
            while(iteradorHijo != corte1){
                if(dlb[padres[(j+1)%2].getValorPermutacion(iteradorPadre)] == 0){
                    hijos[j%2].setValorPermutacion(iteradorHijo, padres[(j+1)%2].getValorPermutacion(iteradorPadre));
                    dlb[padres[(j+1)%2].getValorPermutacion(iteradorPadre)] = 1;
                    iteradorHijo = (iteradorHijo+1)%hijos[j%2].getTam();
                }
                iteradorPadre = (iteradorPadre+1)%padres[j%2].getTam();
            }
            hijos[j%2].Coste();
        }
    }
    
    private void operadorPMX(int corte1, int corte2){
        
    }
    
    private void Mutacion(){
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
    
    private void Reemplazamiento(){
        Solucion tmp;
        //Se coloca el mejor de los hijos en el indice 0 del vector hijos.
        if(hijos[0].getCoste()>hijos[1].getCoste()){
            tmp = hijos[1];
            hijos[1] = hijos[0];
            hijos[0] = tmp;
        }
        //Se comprueba el mejor hijo con la peor solucion y se intercambia en caso de que lo mejore.
        if(hijos[0].getCoste()<poblacion.individuo(poblacion.getTam()-1).getCoste()){
            poblacion.reemplazarIndividuo(hijos[0], poblacion.getTam()-1);
            //A continuacion se hace lo propio con el segundo mejor hijo y la segunda mejor solucion.
            if(hijos[1].getCoste()<poblacion.individuo(poblacion.getTam()-2).getCoste()){
                poblacion.reemplazarIndividuo(hijos[1], poblacion.getTam()-2);
            }
        }//No es necesario comprobar el segundo mejor hijo con la pero solucion si el primero no la supera.
//    poblacion.ordenarPoblacion();
    }

    private void log(){
        
    }
    
    public Solucion individuo(int index){
        return poblacion.individuo(index);
    }
    
}
