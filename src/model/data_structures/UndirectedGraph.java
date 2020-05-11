package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

public class UndirectedGraph<K, V>
{
	private final int V;
	private int E;
	private Bag<K>[] adj;
	private double[][] costos;
	private ArrayList<V> informacion;
	private boolean[] visited;

	public UndirectedGraph( int V )
	{
		if( V < 0 )
			throw new IllegalArgumentException( "Numero de vertices debe ser positivo" );
		this.V = V;
		this.E = 0;
		adj = ( Bag<K>[] ) new Bag[V];
		informacion = new ArrayList<V>( V );
		visited = new boolean[V];
		costos = new double[V][V];
		for( int v = 0; v < V; v++ )
		{
			adj[v] = new Bag<K>( );
		}
	}

	/**
	 * Devuelve el número de vertices.
	 * @return Número de vertices.
	 */
	public int V( )
	{
		return V;
	}

	/**
	 * Devuelve el número de arcos.
	 * @return Número de arcos.
	 */
	public int E( )
	{
		return E;
	}

	private void validateVertex( int v )
	{
		if( v < 0 || v >= V )
			throw new IllegalArgumentException( "vertex " + v + " is not between 0 and " + ( V - 1 ) );
	}

	/**
	 * Adicionar el arco No dirigido entre el vértice IdVertexIni y el vértice
	 * IdVertexFin.
	 * El arco tiene el costo cost
	 * @param idVertexIni un vertice
	 * @param idVertexFin otro vertice
	 */
	public void addEdge( K idVertexIni, K idVertexFin, double costo )
	{
		E++;
		adj[( int ) idVertexIni].add( idVertexFin );
		adj[( int ) idVertexFin].add( idVertexIni );
		costos[( int ) idVertexIni][( int ) idVertexFin] = costo;
	}

	/**
	 * Adiciona un vértice con un Id único. El vértice tiene la información
	 * InfoVertex.
	 * @param idVertexIni un vertice.
	 * @param infoVertex  la info.
	 */
	public void addVertex( K idVertexIni, V infoVertex )
	{
		informacion.add( ( int ) idVertexIni, infoVertex );
	}

	/**
	 * Obtener la información de un vértice. Si el vértice no existe retorna null.
	 * @param idVertex
	 * @return
	 */
	public V getInfoVertex( K idVertex )
	{
		return ( V ) informacion.get( ( int ) idVertex );
	}

	/**
	 * @param idVertex
	 * @param infoVertex
	 */
	public void setInfoVertex( K idVertex, V infoVertex )
	{
		informacion.set( ( int ) idVertex, infoVertex );
	}

	/**
	 * Modifica el costo del arco no dirigido entre los vértices idVertexIni e
	 * idVertexFin.
	 * @param idVertexIni
	 * @param idVertexFin
	 * @param cost
	 */
	public double getCostArc( K idVertexIni, K idVertexFin )
	{
		return costos[( int ) idVertexIni][( int ) idVertexFin];
	}

	/**
	 * Modifica el costo del arco No dirigido entre los vértices idVertexIni e
	 * idVertexFin
	 * @param idVertexIni
	 * @param idVertexFin
	 * @param cost
	 */
	public void setCostArc( K idVertexIni, K idVertexFin, double cost )
	{
		if( existeArco( idVertexIni, idVertexFin ) )
		{
			costos[( int ) idVertexIni][( int ) idVertexFin] = cost;
			costos[( int ) idVertexFin][( int ) idVertexIni] = cost;
		}
	}

	/**
	 * Devuelve el grado del vertice.
	 * @param vertice
	 * @return grado del vertice
	 */
	public int grado( int v )
	{
		validateVertex( v );
		return adj[v].size( );
	}

	/**
	 * Obtiene la cantidad de componentes conectados del grafo. Cada vértice debe
	 * quedar marcado y debe reconocer a cuál componente conectada pertenece. En
	 * caso de que el grafo esté vacío, retorna 0.
	 * @return cantidad de componentes conectados del grafo.
	 */
	public int cc( )
	{
		boolean marked[] = new boolean[V];
		int id[] = new int[V];
		return ccRecursive( marked, id );
	}

	public int ccRecursive( boolean[] marked, int[] id )
	{
		int count = 0;
		for( int s = 0; s < V; s++ )
		{
			if( !marked[s] )
			{
				dfs( s );
				count++;
			}
		}
		return count;
	}

	/**
	 * <<<<<<< HEAD
	 * =======
	 * desmarca todos los vertices del grafo.
	 */
	public void uncheck( )
	{
		for( int i = 0; i < visited.length; i++ )
		{
			visited[i] = true;
		}
	}

	/**
	 * Ejecuta la búsqueda de profundidad (DFS) sobre el grafo con el vértice s como
	 * origen. Los vértices resultado de la búsqueda quedan marcados y deben tener
	 * información que pertenecen a una misma componente conectada.
	 * @param
	 */
	public void dfs( int v )
	{
		visited = new boolean[V];
		recursion( v, visited );
	}

	void recursion( int v, boolean marcado[] )
	{
		marcado[v] = true;

		Iterator<Integer> i = ( Iterator<Integer> ) adj[v].iterator( );
		while( i.hasNext( ) )
		{
			int n = i.next( );
			if( !marcado[n] )
				recursion( n, marcado );
		}
	}

	/**
	 * retorna los vertices adjacentes al vertice .
	 * @param vertice
	 * @return los vertices adjacentes como un iterable
	 */
	public Iterable<K> adj( K v )
	{
		int a = ( int ) v;
		return adj[a];
	}

	/**
	 * Obtiene los vértices alcanzados a partir del vértice idVertex después de la
	 * ejecución de los metodos dfs(K) y cc().
	 * @param vertice
	 * @return los vertices adjacentes como un iterable
	 */
	public Iterable<K> getCC( K idVertex )
	{
		int a = ( int ) idVertex;
		for( int v = 0; v < V; v++ )
		{
			if( visited[v] == true )
				return adj[v];
		}
		return null;
	}

	/**
	 * revisa si un arco esta marcado
	 * @param a
	 * @return true or false
	 */
	public boolean visit( int a )
	{
		return visited[a];
	}

	/**
	 * revisa si ya existe un arco entre dos vertices.
	 */
	public boolean existeArco( K init, K fin )
	{
		Iterator<K> i = adj[( int ) init].iterator( );
		while( i.hasNext( ) )
		{
			K n = i.next( );
			if( n == fin )
				return true;
		}
		return false;
	}
}