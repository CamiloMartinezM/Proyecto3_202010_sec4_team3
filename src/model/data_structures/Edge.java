package model.data_structures;

/**
 * Arco de un grafo.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Edge<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>>
		implements Comparable<Edge<K, V, L>>
{
	private Vertex<K, V, L> v1;

	private Vertex<K, V, L> v2;

	private double doubleCost;

	private int integerCost;

	/**
	 * Inicializa un arco entre los dos vértices dados por parámetro.
	 * @param v1   Vértice 1 del arco. v1 != null
	 * @param v2   Vértice 2 del arco. v2 != null
	 * @param cost Costo de tipo double del arco. cost != null
	 */
	public Edge( Vertex<K, V, L> v1, Vertex<K, V, L> v2, double cost )
	{
		this.v1 = v1;
		this.v2 = v2;
		this.doubleCost = cost;
	}

	/**
	 * @return Vértice 1 del arco.
	 */
	public Vertex<K, V, L> getV1( )
	{
		return v1;
	}

	/**
	 * @param v1 Vértice 1 a asignar.
	 */
	public void setV1( Vertex<K, V, L> v1 )
	{
		this.v1 = v1;
	}

	/**
	 * @return Vértice 2 del arco.
	 */
	public Vertex<K, V, L> getV2( )
	{
		return v2;
	}

	/**
	 * @param v2 Vértice 2 a asignar.
	 */
	public void setV2( Vertex<K, V, L> v2 )
	{
		this.v2 = v2;
	}

	/**
	 * @return El costo de tipo double del arco.
	 */
	public double getDoubleCost( )
	{
		return doubleCost;
	}

	/**
	 * @param doubleCost El costo de tipo double a asignar.
	 */
	public void setDoubleCost( double doubleCost )
	{
		this.doubleCost = doubleCost;
	}

	/**
	 * @return El costo de tipo integer del arco.
	 */
	public int getIntegerCost( )
	{
		return integerCost;
	}

	/**
	 * @param integerCost El costo de tipo integer a asignar.
	 */
	public void setIntegerCost( int integerCost )
	{
		this.integerCost = integerCost;
	}

	@Override
	public int compareTo( Edge<K, V, L> o )
	{
		// TODO Auto-generated method stub
		return 0;
	}
}