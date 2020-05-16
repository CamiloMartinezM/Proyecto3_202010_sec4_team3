package model.data_structures;

import java.util.Iterator;

/**
 * Implementaci�n de un grafo no dirigido gen�rico.
 * @author Camilo Mart�nez & Nicol�s Quintero
 * @param <V> Tipo de valor o item que se guardar� en cada vertice.
 * @param <I> Tipo de informaci�n que distingue un vertice de otro
 *            cualitativamente.
 * @param <C> Tipo de costo que tiene un arco entre vertices.
 * @param <L> Tipo de elemento distintivo dentro de los nodos de las tablas de
 *            hash.
 */
@SuppressWarnings( "unchecked" )
public class UndirectedGraph<V extends Comparable<V>, L extends Comparable<L>, I extends Comparable<I>, C extends Comparable<C>>
		implements IGraph<V, I, C>
{
	/**
	 * Number of vertices.
	 */
	private final int V;

	/**
	 * Number of edges.
	 */
	private int E;

	/**
	 * Number of items stored.
	 */
	private int S;
	
	/**
	 * Number of distinctive items stored.
	 */
	private int D;

	/**
	 * Adjacency list.
	 */
	private Bag<Integer>[] adj;

	/**
	 * Tabla de hash que contiene los items de tipo V de cada vertice. La llave es
	 * el id
	 * del vertice y el valor su item.
	 */
	private HashTable<Integer, V, L> vertexItems;

	/**
	 * Tabla de hash que contiene la informaci�n de cada vertice. La llave es el id
	 * del vertice y el valor es la informaci�n cualitativa de dicho vertice.
	 */
	private HashTable<Integer, I, L> vertexInfo;

	/**
	 * Tabla de hash que contiene el costo de los arcos. La llave es la
	 * concatenaci�n de los id's de los vertices con "-" separ�ndolos y el valor el
	 * costo asociado.
	 */
	public HashTable<String, C, L> costs;

	/**
	 * Initializes an empty graph with the number of vertices given in parameters
	 * and 0 edges.
	 * @param numberOfVertices Number of vertices.
	 * @throws IllegalArgumentException if given parameter is less than or equal to
	 *                                  0.
	 */
	public UndirectedGraph( int numberOfVertices )
	{
		if( numberOfVertices < 0 )
			throw new IllegalArgumentException( "Number of vertices must be nonnegative" );
		this.V = numberOfVertices;
		this.E = 0;
		this.D = 0;
		this.S = 0;
		
		// Se usa true, pues se quiere que se remplace el valor si la
		// misma llave es proporcionada al pretender insertar.
		this.vertexInfo = new HashTable<>( 997, true );
		this.costs = new HashTable<>( 997, true );

		this.vertexItems = new HashTable<>( 997 );

		adj = ( Bag<Integer>[] ) new Bag[numberOfVertices];
		for( int v = 0; v < numberOfVertices; v++ )
			adj[v] = new Bag<Integer>( );
	}

	/**
	 * @return N�mero de vertices en el grafo.
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
	 * @return Total de items guardados en vertices.
	 */
	public int numberOfStoredItems( )
	{
		return S;
	}

	/**
	 * @return Total de items distintivos guardados en vertices.
	 */
	public int numberOfStoredDistinctiveItems( )
	{
		return D;
	}

	/**
	 * Adds the undirected edge v-w to this graph.
	 * @param v one vertex in the edge.
	 * @param w the other vertex in the edge.
	 * @throws IllegalArgumentException if any of them is not a valid vertex.
	 */
	public void addEdge( int v, int w, C cost )
	{
		validateVertex( v );
		validateVertex( w );
		if( ! ( costs.contains( v + "-" + w ) || costs.contains( w + "-" + v ) ) ) // Si no existe ya ese arco.
		{
			E++;
			adj[v].add( w );
			adj[w].add( v );
		}
		costs.put( v + "-" + w, cost );
	}

	/**
	 * @param v Uno de los vertices del arco.
	 * @param w Otro vertice del arco.
	 * @return Costo del arco entre los vertices dados por par�metros.
	 */
	public C getEdgeCost( int v, int w )
	{
		validateVertex( v );
		return costs.get( v + "-" + w );
	}

	/**
	 * Returns the degree of the given vertex.
	 * @param v the vertex.
	 * @return the degree of given vertex.
	 * @throws IllegalArgumentException if given vertex is not valid.
	 */
	public int degreeOf( int v )
	{
		validateVertex( v );
		return adj[v].size( );
	}

	/**
	 * Le asigna al vertice de id v el item (ambos dados por par�metro). El vertice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor que
	 * corresponde al item.
	 * @param v    vertice en cuesti�n.
	 * @param item Nuevo item.
	 */
	public void setVertexItem( int v, V item )
	{
		vertexItems.put( v, item );
		
		if( item != null )
			S++;
	}

	/**
	 * Le asigna al vertice de id v el item (ambos dados por par�metro). El vertice
	 * es
	 * ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor que
	 * corresponde al item.
	 * @param v    vertice en cuesti�n.
	 * @param item Nuevo item.
	 */
	public void setVertexDistinctiveItem( int v, L item )
	{
		vertexItems.putDistinctiveItem( v, item );
		D++;
	}

	/**
	 * Le asigna al vertice la informaci�n (ambos dados por par�metro). El vertice
	 * es ubicado dentro de la tabla de hash y a dicha llave se le a�ade el valor
	 * que corresponde al item.
	 * @param v    vertice en cuesti�n.
	 * @param info Informaci�n del vertice.
	 */
	public void setVertexInfo( int v, I info )
	{
		vertexInfo.put( v, info );
	}

	/**
	 * @param v Vertice.
	 * @return Informaci�n del vertice.
	 */
	public I getVertexInfo( int v )
	{
		return vertexInfo.get( v );
	}

	/**
	 * @param v id del vertice a buscar su item distintivo.
	 * @return Item distintivo del vertice dado por par�metro.
	 */
	public L getVertexDistinctiveItem( int v )
	{
		return vertexItems.getDistinctiveItem( v );
	}

	/**
	 * Returns the vertices adjacent to the given vertex.
	 * @param v the vertex.
	 * @return the vertices adjacent to given vertex as an iterable.
	 * @throws IllegalArgumentException if given vertex is not valid.
	 */
	public Iterable<Integer> adj( int v )
	{
		validateVertex( v );
		return adj[v];
	}

	/**
	 * Iterador sobre todos los items asociados al vertice dado por par�metro.
	 * @param v id del vertice.
	 * @return Iterador sobre todos los valores dentro de la tabla de hash con llave
	 *         igual al id del vertice.
	 */
	public Iterator<V> vertexItems( int v )
	{
		validateVertex( v );
		return vertexItems.valuesOf( v );
	}

	/**
	 * Iterador sobre todos los items distintivos guardados en el grafo.
	 * @return Iterador de tipo L.
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
	 * Iterador sobre todos los arcos existentes.
	 * @return Iterador sobre todos los arcos v-w existentes.
	 */
	public Iterator<String> edges( )
	{
		return costs.keys( );
	}

	/**
	 * Validates a vertex.
	 * @param v Vertex to be validated.
	 * @throws IllegalArgumentException if given vertex is not greater than 0.
	 */
	private void validateVertex( int v )
	{
		if( v < 0 || v >= V )
			throw new IllegalArgumentException( "vertex " + v + " is not between 0 and " + ( V - 1 ) );
	}
}