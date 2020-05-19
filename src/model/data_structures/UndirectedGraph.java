package model.data_structures;

import java.util.Iterator;

/**
 * Implementación de un grafo no dirigido genérico.
 * @author Camilo Martínez & Nicolás Quintero
 * @param <K> Tipo de llave que identifica un vértice.
 * @param <V> Tipo de item guardado en un vértice.
 * @param <E> Tipo de item distintivo de un vértice.
 */
@SuppressWarnings( "unchecked" )
public class UndirectedGraph<K extends Comparable<K>, V extends Comparable<V>, E extends Comparable<E>>
		implements IGraph<K, V>
{
	/**
	 * Costo que se asigna a un arco que no existe.
	 */
	public final static double INFINITY = -1000000000000000.0;

	/**
	 * Número de vértices.
	 */
	private final int V;

	/**
	 * Número de items guardados en vértices.
	 */
	private int S;

	/**
	 * Número de items distintivos guardados en vértices.
	 */
	private int D;

	/**
	 * Lista de adyacencia.
	 */
	private Bag<Integer>[] adj;

	/**
	 * Tabla de hash que contiene los vértices. La llave es el ID del vértice y el
	 * valor un objeto de tipo Vertex.
	 */
	private HashTable<Integer, Vertex<K, V, E>> vertex;

	/**
	 * Tabla de hash que contiene los arcos. La llave es la concatenación de los
	 * ID's de los vertices con "-" separándolos y el valor, un objeto de tipo Edge.
	 */
	private HashTable<String, Edge<K, V, E>> edges;

	/**
	 * Inicializa un grafo con el número de vértices dados por parámetro y 0 arcos.
	 * @param numberOfVertices Número de vértices.
	 * @throws IllegalArgumentException Si el número de vértices es menor a 0.
	 */
	public UndirectedGraph( int numberOfVertices )
	{
		if( numberOfVertices < 0 )
			throw new IllegalArgumentException( "Number of vertices must be nonnegative" );

		this.V = numberOfVertices;
		this.D = 0;
		this.S = 0;

		this.vertex = new HashTable<>( 997, true );
		this.edges = new HashTable<>( 997, true );

		adj = ( Bag<Integer>[] ) new Bag[numberOfVertices];
		for( int v = 0; v < numberOfVertices; v++ )
		{
			vertex.put( v, new Vertex<>( v ) );
			adj[v] = new Bag<Integer>( );
		}
	}

	/**
	 * @return Número de vértices en el grafo.
	 */
	public int numberOfVertices( )
	{
		return V;
	}

	/**
	 * @return Número de arcos en el grafo.
	 */
	public int numberOfEdges( )
	{
		return edges.getSize( );
	}

	/**
	 * @return Total de items guardados en vértices.
	 */
	public int numberOfStoredItems( )
	{
		return S;
	}

	/**
	 * @return Total de items distintivos guardados en vértices.
	 */
	public int numberOfStoredDistinctiveItems( )
	{
		return D;
	}

	/**
	 * Añade el arco entre los vértices dados por parámetro con su costo de tipo
	 * double.
	 * @param v Uno de los vértices.
	 * @param w Otro de los vértices.
	 * @throws IllegalArgumentException Si alguno de los vértices no son válidos.
	 */
	public void addEdge( int v, int w, double cost )
	{
		validateVertex( v );
		validateVertex( w );
		
		if( edges.contains( v + "-" + w ) ) // Si existe ya ese arco.
			edges.get( v + "-" + w ).setDoubleCost( cost );
		else if( edges.contains( w + "-" + v ) )
			edges.get( w + "-" + v ).setDoubleCost( cost );
		// Si no existe.
		else
		{
			adj[v].add( w );
			adj[w].add( v );
			edges.put( v + "-" + w, new Edge<>( vertex.get( v ), vertex.get( w ), cost ) );
		}
	}

	/**
	 * @param v Uno de los vértices del arco.
	 * @param w Otro vértice del arco.
	 * @return Costo del arco entre los vertices dados por parámetros.
	 * @throws IllegalArgumentException Si alguno de los vértices no son válidos.
	 */
	public double getEdgeDoubleCost( int v, int w ) throws IllegalArgumentException
	{
		validateVertex( v );
		validateVertex( w );

		if( edges.contains( v + "-" + w ) )
			return edges.get( v + "-" + w ).getDoubleCost( );
		else if( edges.contains( w + "-" + v ) )
			return edges.get( w + "-" + v ).getDoubleCost( );
		else
			return INFINITY;
	}

	/**
	 * Asigna el costo de tipo integer a un arco.
	 * @param v Uno de los vértices del arco.
	 * @param w Otro vértice del arco.
	 * @param cost Costo de tipo integer del arco.
	 * @throws IllegalArgumentException Si alguno de los vértices no son válidos.
	 */
	public void setEdgeIntegerCost( int v, int w, int cost )
	{
		validateVertex( v );
		validateVertex( w );

		if( edges.contains( v + "-" + w ) )
			edges.get( v + "-" + w ).setIntegerCost( cost );
		else if( edges.contains( w + "-" + v ) )
			edges.get( w + "-" + v ).setIntegerCost( cost );
	}
	
	/**
	 * @param v ID del vértice.
	 * @return Grado del vértice.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public int degreeOf( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return adj[v].size( );
	}

	/**
	 * Le asigna al vértice de ID v el item (ambos dados por parámetro). El vértice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le añade el valor que
	 * corresponde al item.
	 * @param v    vértice en cuestión.
	 * @param item Nuevo item.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public void insertVertexItem( int v, K key, V item )
	{
		validateVertex( v );
		if( !vertex.contains( v ) )
		{
			Vertex<K, V, E> v1 = new Vertex<>( v );
			vertex.put( v, v1 );
		}
		vertex.get( v ).insertItem( key, item );
		S++;
	}

	/**
	 * Le asigna al vértice de ID v el item (ambos dados por parámetro). El vértice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le añade el valor que
	 * corresponde al item.
	 * @param v    Vértice en cuestión.
	 * @param item Nuevo item.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public void setVertexDistinctiveItem( int v, E item )
	{
		validateVertex( v );
		vertex.get( v ).setDistinctiveItem( item );
		D++;
	}

	/**
	 * Le asigna al vértice la información (ambos dados por parámetro). El vértice
	 * es ubicado dentro de la tabla de hash y a dicha llave se le añade el valor
	 * que corresponde al item.
	 * @param v    Vértice en cuestión.
	 * @param info Información del vértice.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public void setVertexInfo( int v, String info )
	{
		validateVertex( v );
		vertex.get( v ).setInfo( info );
	}

	/**
	 * @param v Vertice.
	 * @return Información del vértice.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public String getVertexInfo( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return vertex.get( v ).getInfo( );
	}

	/**
	 * @param v ID del vértice a buscar su item distintivo.
	 * @return Item distintivo del vértice dado por parámetro.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public E getVertexDistinctiveItem( int v )
	{
		validateVertex( v );
		return vertex.get( v ).getDistinctiveItem( );
	}

	/**
	 * Iterador sobre todos los ID's de los vértices adyacentes al vértice cuyo ID
	 * es dado por parámetro.
	 * @param v ID del vértice a chequear sus adyacentes.
	 * @return Vértices adyacentes al dado por parámetro en forma de iterador.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public Iterable<Integer> adj( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return adj[v];
	}

	/**
	 * Iterador sobre todos los items asociados al vértice dado por parámetro.
	 * @param v ID del vértice.
	 * @return Iterador sobre todos los valores dentro de la tabla de hash con llave
	 *         igual al ID del vértice.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public Iterator<V> vertexItems( int v ) throws IllegalArgumentException
	{
		validateVertex( v );

		return new Iterator<V>( )
		{
			private Iterator<V> iter = vertex.get( v ).items( );

			@Override
			public boolean hasNext( )
			{
				return iter.hasNext( );
			}

			@Override
			public V next( )
			{
				return iter.next( );
			}
		};
	}

	/**
	 * Iterador sobre todos los items distintivos guardados en el grafo.
	 * @return Iterador de tipo E.
	 */
	public Iterator<E> distinctiveItems( )
	{
		return new Iterator<E>( )
		{
			int i = 0;
			int j = 0;
			private E actual = null;

			@Override
			public boolean hasNext( )
			{
				return j < D;
			}

			@Override
			public E next( )
			{
				actual = null;

				while( i < numberOfVertices( ) && actual == null )
				{
					actual = getVertexDistinctiveItem( i );
					i++;
				}

				j++;
				return actual;
			}
		};
	}

	/**
	 * Iterador sobre todos los arcos existentes en forma de cadena de los ID's de
	 * los vértices del arco concatenados con un "-".
	 * @return Iterador sobre todos los arcos v-w existentes.
	 */
	public Iterator<String> edges( )
	{
		return edges.keys( );
	}

	/**
	 * @param v ID del vértice.
	 * @return Número de items guardados en el vértice.
	 * @throws IllegalArgumentException Si el vértice dado no es válido.
	 */
	public int numberOfItemsOf( int v )
	{
		validateVertex( v );
		return vertex.get( v ).numberOfItems( );
	}
	
	/**
	 * Valida un vértice.
	 * @param v Vértice a validar.
	 * @throws IllegalArgumentException Si el vértice es menor que 0 o mayor o igual
	 *                                  al número total de vértices.
	 */
	private void validateVertex( int v ) throws IllegalArgumentException
	{
		if( v < 0 || v >= V )
			throw new IllegalArgumentException( "vertex " + v + " is not between 0 and " + ( V - 1 ) );
	}
}