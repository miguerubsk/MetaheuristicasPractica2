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
 * @author macosx
 */
public class AGEstacionario {

    static final int MAX_EVALUACIONES = 50000;
    static final int MAX_GENERACIONES = 50000;

    private Poblacion poblacion;
    private Random rand;
    private String operadorCruce;
    private Solucion[] padres;
    private Solucion[] hijos;
    private Solucion mejorSol;
    private int numEvaluaciones;
    private int numGeneracion;
    private String rutaLog;

    public AGEstacionario(Poblacion _poblacion, int sem, String _cruce, Problema prob, String rutaDatos) {
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
        mejorSol = new Solucion(poblacion.individuo(0));
        rutaLog = rutaDatos + "_Estacionario_" + operadorCruce + "_" + sem + ".log";
    }

    public void Ejecutar() {
        while (numEvaluaciones < MAX_EVALUACIONES && numGeneracion < MAX_GENERACIONES) {
            Seleccion();
            Cruce();
            Mutacion();
            Reemplazamiento();
            numGeneracion++;
            if(mejorSol.getCoste()>poblacion.individuo(0).getCoste()){
                mejorSol = new Solucion(poblacion.individuo(0));            
            }
            log(!(numGeneracion == 1)); //Si es la primera generacion, creara el fichero o lo sobreescribira si contiene datos de ejecuciones anteriores.           
            
        }
        System.out.printf("Generacion final: %d\n", numGeneracion);
    }

    private void Seleccion() {
        int[] val = new int[2];
        int seleccionado = -1;
        for (int i = 0; i <= 1; i++) {
            do {
                val[0] = rand.nextInt(poblacion.getTam());
            } while (val[0] == seleccionado);
            do {
                val[1] = rand.nextInt(poblacion.getTam());
            } while (val[0] == val[1] || val[1] == seleccionado);
            if (poblacion.individuo(val[0]).getCoste() < poblacion.individuo(val[1]).getCoste()) {
                padres[i] = poblacion.individuo(val[0]);
                seleccionado = val[0];
            } else {
                padres[i] = poblacion.individuo(val[1]);
                seleccionado = val[1];
            }
        }
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
            }
            iteradorGenes = corte2;
            iteradorPadre = corte2;
            while(cont < tam){
                while(adjudicados[padres[(j+1)%2].getValorPermutacion(iteradorPadre)]){
                    iteradorPadre = padres[j].getIndex(padres[(j + 1) % 2].getValorPermutacion(iteradorPadre));
                }
                genes[iteradorGenes] = padres[(j + 1) % 2].getValorPermutacion(iteradorPadre);
                adjudicados[genes[iteradorGenes]] = true;
                iteradorGenes = (iteradorGenes + 1) % tam;
                cont++;
                iteradorPadre = iteradorGenes;
            }
            for(int i=0; i<tam; i++){
                hijos[j].setValorPermutacion(i, genes[i]);
            }
            hijos[j].Coste();
            numEvaluaciones++;
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
        Solucion tmp;
        //Se coloca el mejor de los hijos en el indice 0 del vector hijos.
        if (hijos[0].getCoste() > hijos[1].getCoste()) {
            tmp = hijos[1];
            hijos[1] = hijos[0];
            hijos[0] = tmp;
        }
       
        Solucion[] aux = new Solucion[poblacion.getTam()];
        for(int i=0; i<poblacion.getTam(); i++){
            aux[i] = new Solucion(poblacion.individuo(i));
        }
        if(hijos[0].getCoste() < aux[poblacion.getTam()-1].getCoste()){
            aux[poblacion.getTam()-1] = new Solucion(hijos[0]);
            if(hijos[1].getCoste() < aux[poblacion.getTam()-2].getCoste()){
                aux[poblacion.getTam()-2] = new Solucion(hijos[1]);
            }
        }
        
        poblacion = new Poblacion(poblacion, aux);
        poblacion.ordenarPoblacion();
    }

    private void log(boolean PrimeraEscritura) {
        try {
            File archivo = new File(rutaLog);

            FileWriter escribir = new FileWriter(archivo, PrimeraEscritura);

            escribir.write("GENERACION: " + numGeneracion + "\nMejor Solucion: ");
            for (int i = 0; i < mejorSol.getTam(); i++) {
                escribir.write(" " + mejorSol.getValorPermutacion(i));
            }
            escribir.write("\n");
            escribir.write("Coste: "+mejorSol.getCoste()+"\n\n");
            escribir.close();

        } catch (Exception e) {
            System.err.println("Error al escribir");
        }
    }

    public Solucion individuo(int index) {
        return poblacion.individuo(index);
    }

    public Solucion mejorSolucion() {
        return mejorSol;
    }
}
