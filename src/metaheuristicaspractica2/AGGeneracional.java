/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicaspractica2;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

/**
 *
 * @author ROBERTO
 */
public class AGGeneracional {
    
    static final int MAX_EVALUACIONES = 50000;
    static final int MAX_GENERACIONES = 50000;

    private Poblacion poblacion;
    private Random rand;
    private String operadorCruce;
    private Solucion[] padres;
    private Solucion[] hijos;
    private Poblacion nuevaGen;
    private int numEvaluaciones;
    private int numGeneracion;
    private String rutaLog;
    
    public AGGeneracional(Poblacion _poblacion, int sem, String _cruce, Problema prob, String rutaDatos) {
        poblacion = _poblacion;
        rand = new Random(sem);
        operadorCruce = _cruce;
        numEvaluaciones = 0;
        numGeneracion = 0;
        padres = new Solucion[2];
        hijos = new Solucion[2];
        for (int i = 0; i < 2; i++) {
            padres[i] = new Solucion(prob);
            hijos[i] = new Solucion(prob);
        }
        nuevaGen = new Poblacion(poblacion.getTam(), sem, prob);
        rutaLog = rutaDatos + "_Generacional_" + operadorCruce + "_" + sem + ".log";
    }
    
    public void Ejecutar() {
        while (numEvaluaciones < MAX_EVALUACIONES && numGeneracion < MAX_GENERACIONES) {
            for(int i=0; i<poblacion.getTam()/2; i++){
                Seleccion(i);
                if(rand.nextFloat() < 0.7){
                    Cruce();
                    Mutacion();
                    nuevaGen.reemplazarIndividuo(hijos[0], i*2);
                    nuevaGen.reemplazarIndividuo(hijos[1], (i*2)+1);
                }else{
                    nuevaGen.reemplazarIndividuo(padres[0], i*2);
                    nuevaGen.reemplazarIndividuo(padres[1], (i*2)+1);
                }
            }
            nuevaGen.ordenarPoblacion();
            Reemplazamiento();
            numGeneracion++;
            log(!(numGeneracion == 1)); //Si es la primera generacion, creara el fichero o lo sobreescribira si contiene datos de ejecuciones anteriores.           
        }
        System.out.printf("Generacion final: %d\n", numGeneracion);
    }
    
    private void Seleccion(int index) {
        padres[0] = poblacion.individuo(index*2);
        padres[1] = poblacion.individuo((index*2)+1);
    }

    private void Cruce() {
        int corte1 = 0, corte2 = 0, tmp;
        //Seleccion de los puntos de corte
        do {
            corte1 = rand.nextInt(padres[0].getTam());
            corte2 = rand.nextInt(padres[0].getTam());
            if (corte2 < corte1) {
                tmp = corte1;
                corte1 = corte2;
                corte2 = tmp;
            }
            System.out.printf("Corte 1: " + corte1 + ", Corte 2: " + corte2 + "\n");
        } while ((corte1 - corte2 >= poblacion.getTam() - 1) || (corte1 == corte2));
        switch (operadorCruce) {
            case "OX":
                operadorOX(corte1, corte2);
                break;
            case "PMX":
                operadorPMX(corte1, corte2);
                break;
        }
    }
    
    private void operadorOX(int corte1, int corte2){
        int iteradorPadre, iteradorGenes, tam = padres[0].getTam();
        boolean adjudicados[] = new boolean[tam];
        int genes[] = new int[tam];
        int cont;
        for(int j=0; j<2; j++){
            cont = 0;
            for(int i=0; i<tam; i++){
                adjudicados[i] = false;
            }
            for(int i=corte1; i<corte2; i++){
                genes[i] = padres[j].getValorPermutacion(i);
                adjudicados[genes[i]] = true;
                cont++;
            }
            iteradorPadre = corte2;
            iteradorGenes = corte2;
            while(cont < tam){
                if(!adjudicados[padres[(j+1)%2].getValorPermutacion(iteradorPadre)]){
                    genes[iteradorGenes] = padres[(j+1)%2].getValorPermutacion(iteradorPadre);
                    adjudicados[genes[iteradorGenes]] = true;
                    iteradorGenes = (iteradorGenes + 1) % tam;
                    cont++;
                }
                iteradorPadre = (iteradorPadre + 1) % tam;
            }
            for(int i=0; i<tam; i++){
                hijos[j].setValorPermutacion(i, genes[i]);
            }
            hijos[j].Coste();
            numEvaluaciones++;
        }
        for(int j=0; j<2; j++){
            System.out.print("Hijo "+j+": ");
            for(int i=0; i<tam; i++){
               System.out.print(hijos[j].getValorPermutacion(i)+" ");
            }
            System.out.println();
        }
    }
    
    private void operadorPMX(int corte1, int corte2) {
        int iteradorPadre, iteradorGenes, tam = padres[0].getTam();
        boolean adjudicados[] = new boolean[tam];
        int genes[] = new int[tam];
        int cont;
        for(int j=0; j<2; j++){
            cont = 0;
            for(int i=0; i<tam; i++){
                adjudicados[i] = false;
            }
            for(int i=corte1; i<corte2; i++){
                genes[i] = padres[j].getValorPermutacion(i);
                adjudicados[genes[i]] = true;
                cont++;
//                System.out.print(cont+"\n");
            }
            iteradorGenes = corte2;
            iteradorPadre = corte2;
            while(cont < tam){
                while(adjudicados[padres[(j+1)%2].getValorPermutacion(iteradorPadre)]){
                    iteradorPadre = padres[j].getIndex(padres[(j + 1) % 2].getValorPermutacion(iteradorPadre));
                    System.out.print(iteradorPadre+"\n");
                }
                genes[iteradorGenes] = padres[(j + 1) % 2].getValorPermutacion(iteradorPadre);
                adjudicados[genes[iteradorGenes]] = true;
                iteradorGenes = (iteradorGenes + 1) % tam;
                cont++;
                iteradorPadre = iteradorGenes;
//                System.out.print(cont+"\n");
            }
            for(int i=0; i<tam; i++){
                hijos[j].setValorPermutacion(i, genes[i]);
            }
            hijos[j].Coste();
            numEvaluaciones++;
        }
        for(int j=0; j<2; j++){
            System.out.print("Hijo "+j+": ");
            for(int i=0; i<tam; i++){
               System.out.print(hijos[j].getValorPermutacion(i)+" ");
            }
            System.out.println();
        }
    }
    
    private void Mutacion() {
        int valor;
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < hijos[j].getTam(); i++) {
                if (rand.nextFloat() < 0.001 * hijos[j].getTam()) {
                    do {
                        valor = rand.nextInt(hijos[j].getTam());
                    } while (valor == i);
                    hijos[j].Intercambio(i, valor);
                    hijos[j].CosteIntercambio(i, valor);
                    numEvaluaciones++;
                }
            }
        }

    }
    
    private void Reemplazamiento() {
        //Se consigue la elite de 1 individuo
        nuevaGen.reemplazarIndividuo(poblacion.individuo(0), nuevaGen.getTam()-1);
        nuevaGen.ordenarPoblacion();
        //Se reemplaza la nueva generacion
        poblacion = nuevaGen;
    }
    
    private void log(boolean PrimeraEscritura) {
        try {
            File archivo = new File(rutaLog);

            FileWriter escribir = new FileWriter(archivo, PrimeraEscritura);

            escribir.write("GENERACION: " + numGeneracion + "\nMejor Solucion: ");
            for (int i = 0; i < poblacion.individuo(0).getTam(); i++) {
                escribir.write(" " + poblacion.individuo(0).getValorPermutacion(i));
            }
            escribir.write("\n");
            escribir.write("Coste: "+poblacion.individuo(0).getCoste()+"\n\n");
            escribir.close();

        } catch (Exception e) {
            System.err.println("Error al escribir");
        }
    }

    public Solucion individuo(int index) {
        return poblacion.individuo(index);
    }
    
}
