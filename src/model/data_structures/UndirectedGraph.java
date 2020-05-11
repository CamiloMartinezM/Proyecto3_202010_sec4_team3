package model.data_structures;

import java.util.Iterator;

/**
 * Implementación de un grafo no dirigido genérico.
 * @author Camilo Martínez & Nicolás Quintero
 * @param <V> Tipo de valor o item que se guardará en cada vertice.
 * @param <I> Tipo de información que distingue un vertice de otro
 *            cualitativamente.
 * @param <C> Tipo de costo que tiene un arco entre vertices.
 */
@SuppressWarnings( "unchecked" )
public class UndirectedGraph<V extends Comparable<V>, I extends Comparable<I>, C extends Comparable<C>>
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
	 * Adjacency list.
	 */
	private Bag<Integer>[] adj;

	/**
	 * Tabla de hash que contiene los items de cada vertice. La llave es el id
	 * del vertice y el valor su item.
	 */
	private HashTable<Integer, V> vertexItems;

	/**
	 * Tabla de hash que contiene la información de cada vertice. La llave es el id
	 * del vertice y el valor es la información cualitativa de dicho vertice.
	 */
	private HashTable<Integer, I> vertexInfo;

	/**
	 * Tabla de hash que contiene el costo de los arcos. La llave es la
	 * concatenación de los id's de los vertices con "-" separándolos y el valor el
	 * costo asociado.
	 */
	public HashTable<String, C> costs;

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

		// Se usa true, pues se quiere que se remplace el valor si la
		// misma llave es proporcionada al pretender insertar.
		this.vertexInfo = new HashTable<>( 997, true );
		this.costs = new HashTable<>( 997, true );

		this.vertexItems = new HashTable<>( 997 );

		adj = ( Bag<Integer>[] ) new Bag[numberOfVertices];
		for( int v = 0; v < numberOfVertices; v++ )
			adj[v] = new Bag<Integer>( );
	}

	public int numberOfVertices( )
	{
		return V;
	}

	public int numberOfEdges( )
	{
		return E;
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
		if( ! ( costs.contains( v + "-" + w ) && costs.contains( w + "-" + v ) ) ) // Si no existe ya ese arco.
		{
			E++;
			adj[v].add( w );
			adj[w].add( v );
		}
		costs.put( v + "-" + w, cost );
		costs.put( w + "-" + v, cost );
	}

	/**
	 * @param v Uno de los vertices del arco.
	 * @param w Otro vertice del arco.
	 * @return Costo del arco entre los vertices dados por parámetros.
	 */
	public C getEdgeCost( int v, int w )
	{
		validateVertex( v );
		return costs.get( v + "-" + w );
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
	 * Le asigna al vertice el item (ambos dados por parámetro). El vertice es
	 * ubicado dentro de la tabla de hash y a dicha llave se le añade el valor que
	 * corresponde al item.
	 * @param v    vertice en cuestión.
	 * @param item Nuevo item.
	 */
	public void setVertexItem( int v, V item )
	{
		vertexItems.put( v, item );
	}

	/**
	 * Le asigna al vertice la información (ambos dados por parámetro). El vertice
	 * es ubicado dentro de la tabla de hash y a dicha llave se le añade el valor
	 * que corresponde al item.
	 * @param v    vertice en cuestión.
	 * @param info Información del vertice.
	 */
	public void setVertexInfo( int v, I info )
	{
		vertexInfo.put( v, info );
	}

	/**
	 * @param v Vertice.
	 * @return Información del vertice.
	 */
	public I getVertexInfo( int v )
	{
		return vertexInfo.get( v );
	}

	/**
	 * Iterador sobre todos los items asociados al vertice dado por parámetro.
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