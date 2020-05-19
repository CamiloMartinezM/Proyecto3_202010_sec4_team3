package model.data_structures;

import java.util.Iterator;

/**
 * Implementaci�n de un grafo no dirigido gen�rico.
 * @author Camilo Mart�nez & Nicol�s Quintero
 * @param <K> Tipo de llave que identifica un v�rtice.
 * @param <V> Tipo de item guardado en un v�rtice.
 * @param <E> Tipo de item distintivo de un v�rtice.
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
	 * N�mero de v�rtices.
	 */
	private final int V;

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
	private Bag<Integer>[] adj;

	/**
	 * Tabla de hash que contiene los v�rtices. La llave es el ID del v�rtice y el
	 * valor un objeto de tipo Vertex.
	 */
	private HashTable<Integer, Vertex<K, V, E>> vertex;

	/**
	 * Tabla de hash que contiene los arcos. La llave es la concatenaci�n de los
	 * ID's de los vertices con "-" separ�ndolos y el valor, un objeto de tipo Edge.
	 */
	private HashTable<String, Edge<K, V, E>> edges;

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
		return edges.getSize( );
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
	 * @param v Uno de los v�rtices del arco.
	 * @param w Otro v�rtice del arco.
	 * @return Costo del arco entre los vertices dados por par�metros.
	 * @throws IllegalArgumentException Si alguno de los v�rtices no son v�lidos.
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
	 * @param v Uno de los v�rtices del arco.
	 * @param w Otro v�rtice del arco.
	 * @param cost Costo de tipo integer del arco.
	 * @throws IllegalArgumentException Si alguno de los v�rtices no son v�lidos.
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
	 * Le asigna al v�rtice de ID v el item (ambos dados por par�metro). El v�rtice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor que
	 * corresponde al item.
	 * @param v    V�rtice en cuesti�n.
	 * @param item Nuevo item.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public void setVertexDistinctiveItem( int v, E item )
	{
		validateVertex( v );
		vertex.get( v ).setDistinctiveItem( item );
		D++;
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
	 * @param v Vertice.
	 * @return Informaci�n del v�rtice.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public String getVertexInfo( int v ) throws IllegalArgumentException
	{
		validateVertex( v );
		return vertex.get( v ).getInfo( );
	}

	/**
	 * @param v ID del v�rtice a buscar su item distintivo.
	 * @return Item distintivo del v�rtice dado por par�metro.
	 * @throws IllegalArgumentException Si el v�rtice no es v�lido.
	 */
	public E getVertexDistinctiveItem( int v )
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
	public Iterable<Integer> adj( int v ) throws IllegalArgumentException
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
	 * los v�rtices del arco concatenados con un "-".
	 * @return Iterador sobre todos los arcos v-w existentes.
	 */
	public Iterator<String> edges( )
	{
		return edges.keys( );
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