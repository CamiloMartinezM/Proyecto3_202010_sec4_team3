package model.data_structures;

import java.util.Iterator;

/**
 * Interfaz que debe implementar la clase UndirectedGraph.
 * @author Camilo Martínez & Nicolás Quintero
 * @param <V> Tipo de valor o item que se guardará en cada vertice.
 */
public interface IGraph<K extends Comparable<K>, V extends Comparable<V>>
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
	 * Returns the vertices adjacent to the given vertex.
	 * @param v the vertex.
	 * @return the vertices adjacent to given vertex as an iterable.
	 * @throws IllegalArgumentException if given vertex is not valid.
	 */
	public Iterable<Integer> adj( int v );

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
}