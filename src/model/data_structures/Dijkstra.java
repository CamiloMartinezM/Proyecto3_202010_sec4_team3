package model.data_structures;

import java.util.Iterator;

import model.data_structures.Edge;
import model.data_structures.IGraph;
import model.data_structures.MinHeapPQ;
import model.data_structures.Vertex;

public class Dijkstra<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>, E extends Comparable<E>> {
	private final int MAX = Integer.MAX_VALUE;  //maximo numero de vértices
    private final int INF = Integer.MAX_VALUE;  //definimos un valor grande que represente la distancia infinita inicial, basta conque sea superior al maximo valor del peso en alguna de las aristas
    
     //lista de adyacencia
    private double distancia[ ] = new double[ MAX ];          //distancia[ u ] distancia de vértice inicial a vértice con ID = u
    private boolean visitado[ ] = new boolean[ MAX ];   //para vértices visitados
	private MinHeapPQ<Vertex<K, V, E>> pq;                       //priority queue.
    private int V;                                      //numero de vertices
    private int previo[] = new int[ MAX ];              //para la impresion de caminos
    private boolean dijkstraEjecutado;
    
  //inicializamos todas las distancias con valor infinito
  //inicializamos todos los vértices como no visitados 
  //inicializamos el previo del vertice i con -1
    private void init(){
        for( int i = 0 ; i <= V ; ++i ){
            distancia[ i ] = INF;  
            visitado[ i ] = false; 
            previo[ i ] = -1;      
        }
    }    

    @SuppressWarnings("unchecked")
	private void relajacion( IGraph G, int actual , int adyacente , double peso ){
    	
        //Si la distancia del origen al vertice actual + peso de su arista es menor a la distancia del origen al vertice adyacente
        if( distancia[ actual ] + peso < distancia[ adyacente ] ){
            distancia[ adyacente ] =  (distancia[ actual ] + peso);  //relajamos el vertice actualizando la distancia
            previo[ adyacente ] = actual;                         //a su vez actualizamos el vertice previo
            pq.insert( G.getVertex(adyacente));                  //agregamos adyacente a la cola de prioridad
        }  
    }
   public Dijkstra( IGraph G ,int inicial, int destino){
        init(); //inicializamos nuestros arreglos
        pq.insert( G.getVertex(inicial) ); //Insertamos el vértice inicial en la Cola de Prioridad
        distancia[ inicial ] = 0;      //Este paso es importante, inicializamos la distancia del inicial como 0
        Vertex<K, V, E> actual , adyacente;
        double peso;
        while( !pq.isEmpty() ){                        //Mientras cola no este vacia
              actual = pq.poll();                      //Obtengo de la cola el vertice con menor peso, en un comienzo será el inicial.Sacamos el elemento de la cola                                              
            if( visitado[ actual.getId() ] ) continue; //Si el vértice actual ya fue visitado entonces sigo sacando elementos de la cola
                visitado[ actual.getId() ] = true;         //Marco como visitado el vértice actual
            Iterator<Integer> iterador= G.vertexAdjacentTo(actual.getId());
            
            while( iterador.hasNext())
            {
               int id = iterador.next();
               adyacente = G.getVertex(id);
               peso = G.getEdgeDoubleCost(actual.getId(), id);
            	if(!visitado[iterador.next()]){
            		relajacion(G,actual.getId(),adyacente.getId(),peso);
            	}
            	actual=adyacente;
            }
        }

        System.out.printf( "Distancias mas cortas iniciando en vertice %d\n" , inicial );
        for( int i = 1 ; i <= V ; ++i ){
            System.out.printf("Vertice %d , distancia mas corta = %d\n" , i , distancia[ i ] );
        }
        dijkstraEjecutado = true;
    }
    
    //Impresion del camino mas corto desde el vertice inicial y final ingresados
    public void print( int destino ){
         if( previo[ destino ] != -1 )    //si aun poseo un vertice previo
            print( previo[ destino ] );  //recursivamente sigo explorando
        System.out.printf("%d " , destino );        //terminada la recursion imprimo los vertices recorridos
    }

  
}
