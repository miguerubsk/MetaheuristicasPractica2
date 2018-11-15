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
    
    
    public static void mergesort(int A[],int izq, int der){
        if (izq<der){
                int m=(izq+der)/2;
                mergesort(A,izq, m);
                mergesort(A,m+1, der);
                merge(A,izq, m, der);
        }
    }
        
   public static void merge(int A[],int izq, int m, int der){
   int i, j, k;
   int [] B = new int[A.length]; //array auxiliar
   for (i=izq; i<=der; i++) //copia ambas mitades en el array auxiliar
             B[i]=A[i];

             i=izq; j=m+1; k=izq;
             while (i<=m && j<=der) //copia el siguiente elemento más grande
             if (B[i]<=B[j])
                     A[k++]=B[i++];
             else
                     A[k++]=B[j++];
             while (i<=m) //copia los elementos que quedan de la
                           A[k++]=B[i++]; //primera mitad (si los hay)
 }
        
        
    
}
