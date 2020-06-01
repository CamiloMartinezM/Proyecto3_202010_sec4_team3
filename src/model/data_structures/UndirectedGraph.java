package model.data_structures;

import java.util.Iterator;

/**
 * Implementaci�n de un grafo no dirigido gen�rico.
 * @author Camilo Mart�nez & Nicol�s Quintero
 * @param <K> Tipo de llave que identifica un v�rtice.
 * @param <V> Tipo de item guardado en un v�rtice.
 * @param <L> Tipo de item distintivo de un v�rtice.
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
	 * N�mero de v�rtices.
	 */
	private final int V;

	/**
	 * N�mero de arcos.
	 */
	private int E;

	/**
	 * N�mero de items guardados en v�rtices.
	 */
	private int S;

	/**
	 * N�mero de items distintivos guardados en v�rtices.
	 */
	private int D;

	/**
	 * Lista de adyacencia.
	 */
	private Bag<Edge<K, V, L>>[] adj;

	/**
	 * Tabla de hash que contiene los v�rtices. La llave es el ID del v�rtice y el
	 * valor un objeto de tipo Vertex.
	 */
	public HashTable<Integer, Vertex<K, V, L>> vertex;

	/**
	 * Inicializa un grafo con el n�mero de v�rtices dados por par�metro y 0 arcos.
	 * @param numberOfVertices N�mero de v�rtices.
	 * @throws IllegalArgumentException Si el n�mero de v�rtices es menor a 0.
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
	 * @return N�mero de v�rtices en el grafo.
	 */
	public int numberOfVertices( )
	{
		return V;
	}

	/**
	 * @return N�mero de arcos en el grafo.
	 */
	public int numberOfEdges( )
	{
		return E;
	}

	/**
	 * @return Total de items guardados en v�rtices.
	 */
	public int numberOfStoredItems( )
	{
		return S;
	}

	/**
	 * @return Total de items distintivos guardados en v�rtices.
	 */
	public int numberOfStoredDistinctiveItems( )
	{
		return D;
	}

	/**
	 * A�ade el arco entre los v�rtices dados por par�metro con su costo de tipo
	 * double.
	 * @param v Uno de los v�rtices.
	 * @param w Otro de los v�rtices.
	 * @throws IllegalArgumentException Si alguno de los v�rtices no son v�lidos.
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
	 * @param v Uno de los v�rtices del arco.
	 * @param w Otro v�rtice del arco.
	 * @return Costo del arco entre los vertices dados por par�metros.
	 * @throws IllegalArgumentException Si alguno de los v�rtices no son v�lidos.
	 */
	public double getEdgeDoubleCost( int v, int w ) throws IllegalArgumentException
	{
		validateVertex( v );
		validateVertex( w );

		for( Edge<K, V, L> e : edgesAdjacentTo( v ) )
			if( e.either( ) == w || e.other( e.either( ) ) == w ) // Comprueba que este s� sea el arco.
				return e.getDoubleCost( );

		return INFINITY; // Si el arco no existe.
	}

	/**
	 * Asigna el costo de tipo integer a un arco.
	 * @param v    Uno de los v�rtices del arco.
	 * @param w    Otro v�rtice del arco.
	 * @param cost Costo de tipo integer del arco.
	 * @throws IllegalArgumentException Si alguno de los v�rtices no son v�lidos.
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
	 * @param v ID del v�rtice.
	 * @return Grado del v�rtice.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public int degreeOf( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return adj[v].size( );
	}

	/**
	 * Le asigna al v�rtice de ID v el item (ambos dados por par�metro). El v�rtice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor que
	 * corresponde al item.
	 * @param v    v�rtice en cuesti�n.
	 * @param item Nuevo item.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
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
	 * Le asigna al v�rtice de ID v el item (ambos dados por par�metro). El v�rtice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor que
	 * corresponde al item.
	 * @param v    V�rtice en cuesti�n.
	 * @param item Nuevo item.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
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
	 * Le asigna al v�rtice la informaci�n (ambos dados por par�metro). El v�rtice
	 * es ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor
	 * que corresponde al item.
	 * @param v    V�rtice en cuesti�n.
	 * @param info Informaci�n del v�rtice.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public void setVertexInfo( int v, String info )
	{
		validateVertex( v );
		vertex.get( v ).setInfo( info );
	}

	/**
	 * @param v V�rtice. validateVertex( v ) debe retornar True.
	 * @return Informaci�n del v�rtice.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public String getVertexInfo( int v ) throws IllegalArgumentException
	{
		return vertex.get( v ).getInfo( );
	}

	/**
	 * @param v ID del v�rtice a buscar su item distintivo.
	 * @return Item distintivo del v�rtice dado por par�metro.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public L getVertexDistinctiveItem( int v )
	{
		validateVertex( v );
		return vertex.get( v ).getDistinctiveItem( );
	}

	/**
	 * Iterador sobre todos los ID's de los v�rtices adyacentes al v�rtice cuyo ID
	 * es dado por par�metro.
	 * @param v ID del v�rtice a chequear sus adyacentes.
	 * @return V�rtices adyacentes al dado por par�metro en forma de iterador.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
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
	 * Iterador sobre todos los arcos adyacentes al v�rtice cuyo ID es dado por
	 * par�metro.
	 * @param v ID del v�rtice a chequear sus arcos adyacentes.
	 * @return Arcos adyacentes al dado por par�metro en forma de iterable.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public Iterable<Edge<K, V, L>> edgesAdjacentTo( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return adj[v];
	}

	/**
	 * Iterador sobre todos los items asociados al v�rtice dado por par�metro.
	 * @param v ID del v�rtice.
	 * @return Iterador sobre todos los valores dentro de la tabla de hash con llave
	 *         igual al ID del v�rtice.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
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
	 * los v�rtices del arco concatenados con un "-".
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
	 * @param v ID del v�rtice.
	 * @return N�mero de items guardados en el v�rtice.
	 * @throws IllegalArgumentException Si el v�rtice dado no es v�lido.
	 */
	public int numberOfItemsOf( int v )
	{
		validateVertex( v );
		return vertex.get( v ).numberOfItems( );
	}

	/**
	 * Valida un v�rtice.
	 * @param v V�rtice a validar.
	 * @throws IllegalArgumentException Si el v�rtice es menor que 0 o mayor o igual
	 *                                  al n�mero total de v�rtices.
	 */
	private void validateVertex( int v ) throws IllegalArgumentException
	{
		if( v < 0 || v >= V )
			throw new IllegalArgumentException( "vertex " + v + " is not between 0 and " + ( V - 1 ) );
	}
}