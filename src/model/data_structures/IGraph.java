package model.data_structures;

import java.util.Iterator;

/**
 * Interfaz que debe implementar la clase UndirectedGraph.
 * @author Camilo Martínez & Nicolás Quintero
 * @param <K> Tipo de llave que se guardará en las tablas de hash con la
 *            información.
 * @param <V> Tipo de valor o item que se guardará en cada vertice.
 */
public interface IGraph<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>>
{
	public int numberOfVertices( );

	public int numberOfEdges( );

	/**
	 * Adds the undirected edge v-w to this graph.
	 * @param v one vertex in the edge.
	 * @param w the other vertex in the edge.
	 * @throws IllegalArgumentException if any of them is not a valid vertex.
	 */
	public void addEdge( int v, int w, double cost );

	/**
	 * @param v Uno de los vertices del arco.
	 * @param w Otro vertice del arco.
	 * @return Costo del arco entre los vertices dados por parámetros.
	 */
	public double getEdgeDoubleCost( int v, int w );

	/**
	 * @param v Uno de los vértices del arco.
	 * @param w Otro vértice del arco.
	 * @return Costo de tipo integer del arco entre los vértices dados por
	 *         parámetros.
	 * @throws IllegalArgumentException Si alguno de los vértices no son válidos.
	 */
	public int getEdgeIntegerCost( int v, int w ) throws IllegalArgumentException;

	/**
	 * Returns the degree of the given vertex.
	 * @param v the vertex.
	 * @return the degree of given vertex.
	 * @throws IllegalArgumentException if given vertex is not valid.
	 */
	public int degreeOf( int v );

	/**
	 * Le asigna al vertice el item (ambos dados por parámetro). El vertice es
	 * ubicado dentro de la tabla de hash y a dicha llave se le añade el valor que
	 * corresponde al item.
	 * @param v    vertice en cuestión.
	 * @param item Nuevo item.
	 */
	public void insertVertexItem( int v, K key, V item );

	/**
	 * Le asigna al vertice la información (ambos dados por parámetro). El vertice
	 * es ubicado dentro de la tabla de hash y a dicha llave se le añade el valor
	 * que corresponde al item.
	 * @param v    vertice en cuestión.
	 * @param info Información del vertice.
	 */
	public void setVertexInfo( int v, String info );

	/**
	 * metodo para obtener el vertice basado en el numero
	 * @param v
	 * @return vertice solicitado.
	 */
	public Vertex<K, V, L> getVertex( int v );

	/**
	 * @param v Vertice.
	 * @return Información del vertice.
	 */
	public String getVertexInfo( int v );

	/**
	 * Iterador sobre todos los items asociados al vertice.
	 * @param v id del vertice.
	 * @return Iterador sobre todos los valores dentro de la tabla de hash con llave
	 *         igual al id del vertice.
	 */
	public Iterator<V> vertexItems( int v );

	/**
	 * Iterador sobre todos los ID's de los vértices adyacentes al vértice cuyo ID
	 * es dado por parámetro.
	 * @param v ID del vértice a chequear sus adyacentes.
	 * @return Vértices adyacentes al dado por parámetro en forma de iterador.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public Iterator<Integer> vertexAdjacentTo( int v ) throws IllegalArgumentException;

	/**
	 * Iterador sobre todos los arcos adyacentes al vértice cuyo ID es dado por
	 * parámetro.
	 * @param v ID del vértice a chequear sus arcos adyacentes.
	 * @return Arcos adyacentes al dado por parámetro en forma de iterable.
	 * @throws IllegalArgumentException Si el vértice no es válido.
	 */
	public Iterable<Edge<K, V, L>> edgesAdjacentTo( int v ) throws IllegalArgumentException;
}