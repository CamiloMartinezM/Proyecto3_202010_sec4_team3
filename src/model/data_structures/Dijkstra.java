package model.data_structures;

import java.util.Iterator;

import model.data_structures.Edge;
import model.data_structures.IGraph;
import model.data_structures.MinHeapPQ;
import model.data_structures.Vertex;

public class Dijkstra<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>, E extends Comparable<E>>
{
	private final int MAX = Integer.MAX_VALUE;  // maximo numero de v�rtices
	private final int INF = Integer.MAX_VALUE;  // definimos un valor grande que represente la distancia infinita
												  // inicial, basta conque sea superior al maximo valor del peso en
												  // alguna de las aristas

	// lista de adyacencia
	private double distancia[] = new double[MAX];          // distancia[ u ] distancia de v�rtice inicial a v�rtice con
  												          // ID = u
	private boolean visitado[] = new boolean[MAX];     // para v�rtices visitados
	private MinHeapPQ<Vertex<K, V, E>> pq;                       // priority queue.
	private int V;                                      // numero de vertices
	private int previo[] = new int[MAX];              // para la impresion de caminos
	private boolean dijkstraEjecutado;
	int tamanio;
	// inicializamos todas las distancias con valor infinito
	// inicializamos todos los v�rtices como no visitados
	// inicializamos el previo del vertice i con -1
	private void init(IGraph g )
	{
		for( int i = 0; i <= g.numberOfVertices(); ++i )
		{
			distancia[i] = INF;
			visitado[i] = false;
			previo[i] = -1;
		}
	}

	@SuppressWarnings( "unchecked" )
	private void relajacion( IGraph G, int actual, int adyacente, double peso )
	{
		// Si la distancia del origen al vertice actual + peso de su arista es menor a
		// la distancia del origen al vertice adyacente
		if( distancia[actual] + peso < distancia[adyacente] )
		{
			distancia[adyacente] = ( distancia[actual] + peso );  // relajamos el vertice actualizando la distancia
			previo[adyacente] = actual;                         // a su vez actualizamos el vertice previo
			pq.insert( G.getVertex( adyacente ) );                  // agregamos adyacente a la cola de prioridad
		}
	}

	public Dijkstra( IGraph G, int inicial, int destino )
	{
		init(G ); // inicializamos nuestros arreglos
		pq.insert( G.getVertex( inicial ) ); // Insertamos el v�rtice inicial en la Cola de Prioridad
		distancia[inicial] = 0;      // Este paso es importante, inicializamos la distancia del inicial como 0
		Vertex<K, V, E> actual, adyacente;
        double peso;
		while( !pq.isEmpty( ) )
		{                        // Mientras cola no este vacia
			actual = pq.poll( );                      // Obtengo de la cola el vertice con menor peso, en un comienzo
								                      // ser� el inicial.Sacamos el elemento de la cola
			if( visitado[actual.getId( )] )
				continue; // Si el v�rtice actual ya fue visitado entonces sigo sacando elementos de la
							 // cola
			visitado[actual.getId( )] = true;         // Marco como visitado el v�rtice actual
			Iterator<Integer> iterador = G.vertexAdjacentTo( actual.getId( ) );

			while( iterador.hasNext( ) )
			{
				int id = iterador.next( );
				adyacente = G.getVertex( id );
				peso = G.getEdgeDoubleCost( actual.getId( ), id );
				if( !visitado[iterador.next( )] )
				{
					relajacion( G, actual.getId( ), adyacente.getId( ), peso );
				}
				actual = adyacente;
			}
		}

	}

	// Impresion del camino mas corto desde el vertice inicial y final ingresados
	public void print( int destino, IGraph G )
	{
		if( previo[destino] != -1 )// si aun poseo un vertice previo
			tamanio++;
			print( previo[destino],G);  // recursivamente sigo explorando
		System.out.printf( "%d ", destino ); // terminada la recursion imprimo los vertices recorridos
	    System.out.printf(G.getVertexInfo(previo[destino]));
	    System.out.print(tamanio);
	}
	public UndirectedGraph<?,?,Integer> crearGrafo(IGraph G,int destino){
	 
	 UndirectedGraph<?, ?, Integer> g = new UndirectedGraph<>(tamanio);
	 int actual = destino;
	  while(previo[actual]!=-1)
	  {
		  g.addEdge(actual, previo[actual], G.getEdgeDoubleCost(actual, previo[actual]));
		  g.setVertexInfo(actual, G.getVertexInfo(actual));
		  g.setVertexInfo(previo[actual], G.getVertexInfo(previo[actual]));
		  actual= previo[actual];
	  }
	 

	 return g;
	}
}
