/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metaheuristicaspractica2;

import java.util.Arrays;
import java.util.Random;
import static metaheuristicaspractica2.Problema.DISTANCIAS;
import static metaheuristicaspractica2.Problema.FLUJOS;

/**
 *
 * @author Miguel Gonzalez Garcia y Roberto Martinez Fernandez
 */
public class Solucion {
    
    private int tam;
    private int[] permutacion = new int[tam];
    private int coste;
    private Problema problema;
    
    public Solucion(Problema prob){
        problema = prob;
        tam = prob.getTam();
        permutacion = new int[tam];
        coste = 0;
    }
     public Solucion(Solucion s){
        this.problema =s.problema;
        this.tam = s.tam;
        this.permutacion = Arrays.copyOf(s.permutacion, s.tam);
        this.coste = s.coste;
    }
    
    public void GenerarAleatoria(Random rand){
        boolean adjudicado[] = new boolean[tam];
        for(int i=0; i<tam; i++){
            adjudicado[i] = false;
        }
        for(int i=0; i<tam; i++){
            permutacion[i] = rand.nextInt(tam);
            while(adjudicado[permutacion[i]]){
                permutacion[i] = rand.nextInt(tam);
            }
            adjudicado[permutacion[i]] = true;
        }
        Coste();
    }
    
    public void Intercambio(int posi, int posj){
        int tmp = permutacion[posi];
        permutacion[posi] = permutacion[posj];
        permutacion[posj] = tmp;
    }
    
    public void Coste(){
        
        coste = 0;
        for (int i = 0; i<tam; i++){
            for (int j = 0; j<tam; j++){
                coste += problema.datos(i, j, FLUJOS) * problema.datos(permutacion[i],permutacion[j],DISTANCIAS);
            }
        }
       
    }
    
    public int CosteIntercambio(int posi, int posj){
        int costeAdd=0, costeSub=0;
        for(int i=0; i<tam; i++){
            if(i!=posi && i!=posj){
             costeSub += problema.datos(posi,i,FLUJOS) * problema.datos(permutacion[posj],permutacion[i],DISTANCIAS) + problema.datos(posj,i,FLUJOS) * problema.datos(permutacion[posi],permutacion[i],DISTANCIAS);
             costeAdd += problema.datos(posi,i,FLUJOS) * problema.datos(permutacion[posi],permutacion[i],DISTANCIAS) + problema.datos(posj,i,FLUJOS) * problema.datos(permutacion[posj],permutacion[i],DISTANCIAS);
            }
        }
        coste = coste + costeAdd - costeSub;
        return coste;
    }
    
    public int getCoste(){
        return coste;
    }
    
    public int getValorPermutacion(int index){
        return permutacion[index];
    }
    
    public void setValorPermutacion(int index, int valor){
        permutacion[index] = valor;
    }
    
    public int getIndex(int valor){
        for(int i=0; i<tam; i++){
            if(permutacion[i] == valor){
                return i;
            }
        }
        return -1;
    }
    
    public int getTam(){
        return tam;
    }
    
    public void MostrarSolucion(){
        System.out.printf("Solucion = ");
        for(int i=0; i<tam; i++){
            System.out.printf("%d ",permutacion[i]);
        }
        System.out.printf("\nCoste:  %d \n", coste);
        System.out.printf("\n");
    }
    
}
