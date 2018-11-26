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

    private Poblacion poblacion;
    private Random rand;
    private String operadorCruce;
    private Solucion[] padres;
    private Solucion[] hijos;
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
        rutaLog = rutaDatos + "_Estacionario_" + operadorCruce + ".log";
    }

    public void Ejecutar() {
        while (numEvaluaciones < MAX_EVALUACIONES && numGeneracion < 50000) {
            Seleccion();
            Cruce();
            Mutacion();
            Reemplazamiento();
            numGeneracion++;
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
//        for (int i = 0; i < 2; i++) {
//            hijos[i].Coste();
//        }

    }

    private void operadorOX(int corte1, int corte2) {
        int iteradorHijo = 0, iteradorPadre = 0;
        int[] dlb = new int[padres[0].getTam()];
        for (int j = 0; j < 2; j++) {
            System.out.print("Padre" + j + ": ");
            for (int i = 0; i < padres[0].getTam(); i++) {
                System.out.print(" " + padres[j].getValorPermutacion(i));
            }
            System.out.println();
        }
        for (int j = 0; j < 2; j++) {
            iteradorHijo = corte2;
            iteradorPadre = corte2;
            for (int i = 0; i < dlb.length; i++) {
                dlb[i] = 0;
            }
            for (int i = corte1; i < corte2; i++) {
                hijos[j].setValorPermutacion(i, padres[j].getValorPermutacion(i));
                dlb[padres[j].getValorPermutacion(i)] = 1;
//                cont++;
            }
            System.out.printf("GENERACION: " + numGeneracion + "\nHijo " + j + ": ");
            for (int i = 0; i < poblacion.individuo(0).getTam(); i++) {
                System.out.printf(" " + hijos[j].getValorPermutacion(i));
            }
            while (iteradorHijo != corte1) {
                        System.out.print("Hijo: "+ iteradorHijo + "Padre: "+iteradorPadre+"DLB:");
                    for(int i=0; i<dlb.length; i++){
                       System.out.printf(" "+dlb[i]);
                    }
            System.out.println();
//            System.out.print(padres[(j + 1) % 2].getValorPermutacion(iteradorPadre));
                if (dlb[padres[(j + 1) % 2].getValorPermutacion(iteradorPadre)] == 0) {
                    hijos[j].setValorPermutacion(iteradorHijo, padres[(j + 1) % 2].getValorPermutacion(iteradorPadre));
                    dlb[padres[(j + 1) % 2].getValorPermutacion(iteradorPadre)] = 1;
                    iteradorHijo = (iteradorHijo + 1) % hijos[j].getTam();
//                    System.out.println("Hey Listen!");
                }

                iteradorPadre = (iteradorPadre + 1) % padres[j].getTam();
            }
            hijos[j].Coste();
            numEvaluaciones++;
            System.out.printf("GENERACION: " + numGeneracion + "\nHijo " + j + ": ");
            for (int i = 0; i < poblacion.individuo(0).getTam(); i++) {
                System.out.printf(" " + hijos[j].getValorPermutacion(i));
            }
            System.out.println();
        }
    }
    
//    private void operadorOX(int corte1, int corte2) {
//        int iteradorHijo = 0, iteradorPadre = 0;
//        int[] dlb = new int[padres[0].getTam()];
//            iteradorHijo = corte2;
//            iteradorPadre = corte2;
//            System.out.print("Padre" + 0 + ": ");
//            for (int i = 0; i < padres[0].getTam(); i++) {
//                System.out.print(" " + padres[0].getValorPermutacion(i));
//            }
//            System.out.println();
//    
//    
//            for (int i = 0; i < dlb.length; i++) {
//                dlb[i] = 0;
//            }
//            for (int i = corte1; i < corte2; i++) {
//                hijos[0].setValorPermutacion(i, padres[0].getValorPermutacion(i));
//                dlb[padres[0].getValorPermutacion(i)] = 1;
////                cont++;
//            }
//            while (iteradorHijo != corte1) {
//                System.out.print("Hijo: "+ iteradorHijo + "Padre: "+iteradorPadre+"DLB:");
//            for(int i=0; i<dlb.length; i++){
//               System.out.printf(" "+dlb[i]);
//            }
//            System.out.println();
//                if (dlb[padres[1].getValorPermutacion(iteradorPadre)] == 0) {
//                    hijos[0].setValorPermutacion(iteradorHijo, padres[1].getValorPermutacion(iteradorPadre));
//                    dlb[padres[1].getValorPermutacion(iteradorPadre)] = 1;
//                    iteradorHijo = (iteradorHijo + 1) % hijos[0].getTam();
////                    System.out.println("Hey Listen!");
//                }
//
//                iteradorPadre = (iteradorPadre + 1) % padres[0].getTam();
//            }
//            hijos[0].Coste();
//            numEvaluaciones++;
//            System.out.printf("GENERACION: " + numGeneracion + "\nHijo " + 0 + ": ");
//            for (int i = 0; i < poblacion.individuo(0).getTam(); i++) {
//                System.out.printf(" " + hijos[0].getValorPermutacion(i));
//            }
//            System.out.println();
//        
//        iteradorHijo = corte2;
//            iteradorPadre = corte2;
//            System.out.print("Padre" + 1 + ": ");
//            for (int i = 0; i < padres[0].getTam(); i++) {
//                System.out.print(" " + padres[1].getValorPermutacion(i));
//            }
//            System.out.println();
//        
//        
//            for (int i = 0; i < dlb.length; i++) {
//                dlb[i] = 0;
//            }
//            for (int i = corte1; i < corte2; i++) {
//                hijos[1].setValorPermutacion(i, padres[1].getValorPermutacion(i));
//                dlb[padres[1].getValorPermutacion(i)] = 1;
////                cont++;
//            }
//            while (iteradorHijo != corte1) {
//                System.out.print("Hijo: "+ iteradorHijo + "Padre: "+iteradorPadre+"DLB:");
//            for(int i=0; i<dlb.length; i++){
//               System.out.printf(" "+dlb[i]);
//            }
//            System.out.println();
//                if (dlb[padres[0].getValorPermutacion(iteradorPadre)] == 0) {
//                    hijos[1].setValorPermutacion(iteradorHijo, padres[0].getValorPermutacion(iteradorPadre));
//                    dlb[padres[0].getValorPermutacion(iteradorPadre)] = 1;
//                    iteradorHijo = (iteradorHijo + 1) % hijos[1].getTam();
////                    System.out.println("Hey Listen!");
//                }
//
//                iteradorPadre = (iteradorPadre + 1) % padres[1].getTam();
//            }
//            hijos[1].Coste();
//            numEvaluaciones++;
//            System.out.printf("GENERACION: " + numGeneracion + "\nHijo " + 1 + ": ");
//            for (int i = 0; i < poblacion.individuo(0).getTam(); i++) {
//                System.out.printf(" " + hijos[1].getValorPermutacion(i));
//            }
//            System.out.println();
//    }
    
//    private void operadorOX(int corte1, int corte2){
//        int[] dlb = new int[padres[0].getTam()];
//        for(int i=0; i<padres[0].getTam(); i++){
//            dlb[i] = 0;
//        }
//        for(int i=corte1; i<corte2; i++){
//            
//        }
//        hijos[0]
//    }

    private void operadorPMX(int corte1, int corte2) {
        System.out.printf("Corte 1: " + corte1 + ", Corte 2: " + corte2 + "\n");
        int iteradorHijo = 0, iteradorPadre = 0;
        int[] dlb = new int[padres[0].getTam()];
        for (int j = 0; j < 2; j++) {
            iteradorHijo = 0;
            if(corte1 == 0){
                iteradorHijo = corte2;
            }
            iteradorPadre = iteradorHijo;
            for (int i = 0; i < dlb.length; i++) {
                dlb[i] = 0;
            }
            
            for (int i = corte1; i < corte2; i++) {
                hijos[j].setValorPermutacion(i, padres[j].getValorPermutacion(i));
                dlb[padres[j].getValorPermutacion(i)] = 1;
            }
            
            while(iteradorHijo < hijos[j].getTam()){
                System.out.print(iteradorHijo+"\n");
                while(dlb[padres[(j + 1) % 2].getValorPermutacion(iteradorPadre)] != 0){
                    System.out.print("Hijo: "+ iteradorHijo + "Padre: "+iteradorPadre+"DLB:");
                    for(int i=0; i<dlb.length; i++){
                       System.out.printf(" "+dlb[i]);
                    }
                    System.out.println();
//                    System.out.print(iteradorPadre+"   ");
                    iteradorPadre = padres[j].getIndex(padres[(j + 1) % 2].getValorPermutacion(iteradorPadre));
//                    System.out.print(iteradorPadre+"\n");
                }
                hijos[j].setValorPermutacion(iteradorHijo, padres[(j + 1) % 2].getValorPermutacion(iteradorPadre));
                dlb[padres[(j + 1) % 2].getValorPermutacion(iteradorPadre)] = 1;
                iteradorHijo++;
                if(iteradorHijo == corte1){
                    iteradorHijo = corte2;
                }
                iteradorPadre = iteradorHijo;
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
        //Se comprueba el mejor hijo con la peor solucion y se intercambia en caso de que lo mejore.
        if (hijos[0].getCoste() < poblacion.individuo(poblacion.getTam() - 1).getCoste()) {
            poblacion.reemplazarIndividuo(hijos[0], poblacion.getTam() - 1);
            //A continuacion se hace lo propio con el segundo mejor hijo y la segunda mejor solucion.
            if (hijos[1].getCoste() < poblacion.individuo(poblacion.getTam() - 2).getCoste()) {
                poblacion.reemplazarIndividuo(hijos[1], poblacion.getTam() - 2);
            }
        }//No es necesario comprobar el segundo mejor hijo con la pero solucion si el primero no la supera.
        poblacion.ordenarPoblacion();
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
            escribir.close();

        } catch (Exception e) {
            System.err.println("Error al escribir");
        }
    }

    public Solucion individuo(int index) {
        return poblacion.individuo(index);
    }

}
