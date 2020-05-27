package model.data_structures;

/**
 * Implementación de los algoritmos que permiten hallar el árbol de expansión
 * mínima de un grafo.
 * @author Camilo Martínez & Nicolás Quintero
 */
@SuppressWarnings( "rawtypes" )
public class MST<K extends Comparable<K>, V extends Comparable<V>, L extends Comparable<L>>
{
	private boolean[] marked;

	private Queue<Edge> mst;

	private MinHeapPQ<Edge<K, V, L>> pq;

	private double cost;
	
	/**
	 * Construye el árbol de expansión mínima con el algoritmo Lazy Prim.
	 * @param G Grafo.
	 */
	public MST( IGraph G )
	{
		cost = 0;
		pq = new MinHeapPQ<Edge<K, V, L>>( G.numberOfVertices( ) - 1 );
		marked = new boolean[G.numberOfVertices( )];
		mst = new Queue<Edge>( );
		visit( G, 0 );
		while( !pq.isEmpty( ) )
		{
			Edge<K, V, L> e = pq.poll( );
			int v = e.either( ), w = e.other( v );
			if( marked[v] && marked[w] )
				continue;
			
			mst.enQueue( e );
			cost += e.getDoubleCost( );
			if( !marked[v] )
				visit( G, v );
			if( !marked[w] )
				visit( G, w );
		}
	}

	@SuppressWarnings( "unchecked" )
	private void visit( IGraph G, int v )
	{
		marked[v] = true;
		Iterable<Edge<K, V, L>> adyacents = G.edgesAdjacentTo( v );
		for( Edge<K, V, L> e : adyacents )
			if( !marked[e.other( v )] )
				pq.insert( e );
	}

	public Iterable<Edge> edges( )
	{
		return mst;
	}

	public double cost( )
	{
		return cost;
	}
}