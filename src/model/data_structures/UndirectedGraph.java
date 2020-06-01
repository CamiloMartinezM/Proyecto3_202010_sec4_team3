package model.data_structures;

import java.util.Iterator;

/**
 * Implementación de un grafo no dirigido genérico.
 * @author Camilo Martínez & Nicolás Quintero
 * @param <K> Tipo de llave que identifica un vértice.
 * @param <V> Tipo de item guardado en un vértice.
 * @param <L> Tipo de item distintivo de un vértice.
 */
@SuppressWarnings( "unchecked" )
public class UndirectedGraph<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>>
		implements IGraph<K, V, L>
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
	 * Número de arcos.
	 */
	private int E;

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
	private Bag<Edge<K, V, L>>[] adj;

	/**
	 * Tabla de hash que contiene los vértices. La llave es el ID del vértice y el
	 * valor un objeto de tipo Vertex.
	 */
	public HashTable<Integer, Vertex<K, V, L>> vertex;

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
		this.E = 0;
		this.D = 0;
		this.S = 0;

		this.vertex = new HashTable<>( 24007, true );

		adj = ( Bag<Edge<K, V, L>>[] ) new Bag[numberOfVertices];
		for( int v = 0; v < numberOfVertices; v++ )
		{
			vertex.put( v, new Vertex<>( v ) );
			adj[v] = new Bag<Edge<K, V, L>>( );
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
		return E;
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
	public void addEdge( int v, int w, double cost ) throws IllegalArgumentException
	{
		validateVertex( v );
		validateVertex( w );

		Edge<K, V, L> e = new Edge<K, V, L>( vertex.get( v ), vertex.get( w ), cost );
		adj[v].add( e );
		adj[w].add( e );
		E++;
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

		for( Edge<K, V, L> e : edgesAdjacentTo( v ) )
			if( e.either( ) == w || e.other( e.either( ) ) == w ) // Comprueba que este sí sea el arco.
				return e.getDoubleCost( );

		return INFINITY; // Si el arco no existe.
	}

	/**
	 * Asigna el costo de tipo integer a un arco.
	 * @param v    Uno de los vértices del arco.
	 * @param w    Otro vértice del arco.
	 * @param cost Costo de tipo integer del arco.
	 * @throws IllegalArgumentException Si alguno de los vértices no son válidos.
	 */
	public void setEdgeIntegerCost( int v, int w, int cost )
	{
		validateVertex( v );
		validateVertex( w );

		for( Edge<K, V, L> e : edgesAdjacentTo( v ) )
			if( e.either( ) == w || e.other( e.either( ) ) == w )
			{
				e.setIntegerCost( cost );
				break;
			}
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
		if( !vertex.contains( v ) )
		{
			Vertex<K, V, L> v1 = new Vertex<>( v );
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
	public void setVertexDistinctiveItem( int v, L item )
	{
		validateVertex( v );
		vertex.get( v ).setDistinctiveItem( item );
		D++;
	}

	public Vertex<K, V, L> getVertex( int v )
	{
		validateVertex( v );
		return vertex.get( v );
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
	 * @param v Vértice. validateVertex( v ) debe retornar True.
	 * @return Información del vértice.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public String getVertexInfo( int v ) throws IllegalArgumentException
	{
		return vertex.get( v ).getInfo( );
	}

	/**
	 * @param v ID del vértice a buscar su item distintivo.
	 * @return Item distintivo del vértice dado por parámetro.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public L getVertexDistinctiveItem( int v )
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
	public Iterator<Integer> vertexAdjacentTo( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return new Iterator<Integer>( )
		{
			private Iterator<Edge<K, V, L>> iter = edgesAdjacentTo( v ).iterator( );

			@Override
			public boolean hasNext( )
			{
				return iter.hasNext( );
			}

			@Override
			public Integer next( )
			{
				return iter.next( ).other( v );
			}
		};
	}

	/**
	 * Iterador sobre todos los arcos adyacentes al vértice cuyo ID es dado por
	 * parámetro.
	 * @param v ID del vértice a chequear sus arcos adyacentes.
	 * @return Arcos adyacentes al dado por parámetro en forma de iterable.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public Iterable<Edge<K, V, L>> edgesAdjacentTo( int v ) throws IllegalArgumentException
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
	public Iterator<L> distinctiveItems( )
	{
		return new Iterator<L>( )
		{
			int i = 0;
			int j = 0;
			private L actual = null;

			@Override
			public boolean hasNext( )
			{
				return j < D;
			}

			@Override
			public L next( )
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
		return new Iterator<String>( )
		{
			private String actual = "";
			private int i = -1;
			private int numArcosRevisados = 0;
			private Iterator<Edge<K, V, L>> bolsaActual = null;
			private HashTable<String, Integer> arcosYaRevisados = new HashTable<String, Integer>( E, true );

			@Override
			public boolean hasNext( )
			{
				return numArcosRevisados < E;
			}

			@Override
			public String next( )
			{
				if( actual == "" || bolsaActual == null || !bolsaActual.hasNext( ) )
					buscarSiguientePosicionI( );

				boolean primeraVez = true;
				while( arcosYaRevisados.contains( actual ) || primeraVez )
				{
					primeraVez = false;
					if( !bolsaActual.hasNext( ) )
						buscarSiguientePosicionI( );

					Edge<K, V, L> e = bolsaActual.next( );
					actual = e.either( ) + "-" + e.other( e.either( ) );
				}

				arcosYaRevisados.put( actual, 0 );

				numArcosRevisados++;
				return actual;
			}

			private void buscarSiguientePosicionI( )
			{
				while( ++i < adj.length )
				{
					if( adj[i].size( ) > 0 )
					{
						bolsaActual = adj[i].iterator( );
						break;
					}
				}
			}
		};
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