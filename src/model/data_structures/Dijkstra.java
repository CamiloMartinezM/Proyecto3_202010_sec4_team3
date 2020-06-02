package model.data_structures;

import java.util.Iterator;

import model.data_structures.IGraph;
import model.data_structures.MinHeapPQ;
import model.data_structures.Vertex;

/**
 * Implementación del algoritmo de Dijkstra para encontrar el camino más corto.
 * @author Camilo Martínez & Nicolás Quintero
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class Dijkstra<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>, E extends Comparable<E>>
{
	public enum TipoCosto
	{
		/**
		 * Usa el costo de tipo double de un arco.
		 */
		DOUBLE,

		/**
		 * Usa el costo de tipo integer de un arco.
		 */
		INTEGER
	}

	// definimos un valor grande que represente la distancia infinita
	// inicial, basta conque sea superior al maximo valor del peso en
	// alguna de las aristas
	private static final int INF = Integer.MAX_VALUE;

	// lista de adyacencia
	private double distancia[];          // distancia[ u ] distancia de vértice inicial a vértice con
								          // ID = u
	private boolean visitado[];     // para vértices visitados
	private MinHeapPQ<Vertex<K, V, E>> pq;                       // priority queue.
	private int previo[];              // para la impresion de caminos
	public int tamanio = 0;
	public double costoMinimo = 0;
	public double totalIntegerCost = 0;
	public double totalDoubleCost = 0;

	// inicializamos todas las distancias con valor infinito
	// inicializamos todos los vértices como no visitados
	// inicializamos el previo del vertice i con -1
	private void init( IGraph g )
	{
		for( int i = 0; i < g.numberOfVertices( ); i++ )
		{
			distancia[i] = INF;
			visitado[i] = false;
			previo[i] = -1;
		}
	}

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
		this( G, inicial, destino, TipoCosto.DOUBLE );
	}

	public Dijkstra( IGraph G, int inicial, int destino, TipoCosto t )
	{
		MinHeapPQ<Vertex<K, V, E>> pq = new MinHeapPQ<>( G.numberOfVertices( ) );
		distancia = new double[G.numberOfVertices( )];
		visitado = new boolean[G.numberOfVertices( )];
		previo = new int[G.numberOfVertices( )];

		init( G ); // inicializamos nuestros arreglos
		pq.insert( G.getVertex( inicial ) ); // Insertamos el vértice inicial en la Cola de Prioridad
		distancia[inicial] = 0;      // Este paso es importante, inicializamos la distancia del inicial como 0
		Vertex<K, V, E> actual, adyacente;
		double peso;
		while( !pq.isEmpty( ) )
		{                        // Mientras cola no este vacia
			actual = pq.poll( );                      // Obtengo de la cola el vertice con menor peso, en un comienzo
								                      // será el inicial.Sacamos el elemento de la cola
			if( visitado[actual.getId( )] )
				continue; // Si el vértice actual ya fue visitado entonces sigo sacando elementos de la
							 // cola
			visitado[actual.getId( )] = true;         // Marco como visitado el vértice actual
			Iterator<Integer> iterador = G.vertexAdjacentTo( actual.getId( ) );

			while( iterador.hasNext( ) )
			{
				int id = iterador.next( );
				adyacente = G.getVertex( id );

				if( t == TipoCosto.DOUBLE )
					peso = G.getEdgeDoubleCost( actual.getId( ), id );
				else // INTEGER
					peso = G.getEdgeIntegerCost( actual.getId( ), id );

				if( !visitado[id] )
				{
					relajacion( G, actual.getId( ), adyacente.getId( ), peso );
				}
				actual = adyacente;
			}
		}

		costoMinimo = distancia[destino];
	}

	// Impresion del camino mas corto desde el vertice inicial y final ingresados
	public String print( int destino, IGraph G )
	{
		String secuencia = "";

		if( previo[destino] != -1 )// si aun poseo un vertice previo
		{
			tamanio++;
			totalIntegerCost += G.getVertex( destino ).numberOfItems( );
			totalDoubleCost += G.getEdgeDoubleCost( destino, previo[destino] );
		}

		secuencia += print( previo[destino], G ) + "\n";  // recursivamente sigo explorando
		secuencia += String.format( "%d", destino ) + "\n"; // terminada la recursion imprimo los vertices recorridos
		secuencia += G.getVertexInfo( previo[destino] ) + "\n";

		return secuencia;
	}

	public UndirectedGraph<?, ?, Integer> crearGrafo( IGraph G, int destino )
	{
		UndirectedGraph<?, ?, Integer> g = new UndirectedGraph<>( tamanio );
		int actual = destino;
		while( previo[actual] != -1 )
		{
			g.addEdge( actual, previo[actual], G.getEdgeDoubleCost( actual, previo[actual] ) );
			g.setVertexInfo( actual, G.getVertexInfo( actual ) );
			g.setVertexInfo( previo[actual], G.getVertexInfo( previo[actual] ) );
			actual = previo[actual];
		}

		return g;
	}
}