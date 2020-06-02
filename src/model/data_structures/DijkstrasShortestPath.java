package model.data_structures;

import java.util.Arrays;

@SuppressWarnings( { "rawtypes" } )
public class DijkstrasShortestPath
{
	private int n;
	private double[] dist;
	private Integer[] prev;
	private Digraph g;

	/**
	 * Inicializa el algoritmo proporcionando un grafo no dirigido, el tama�o del
	 * grafo y un tipo de costo que se usar�, dado que la implementaci�n del arco no
	 * dirigido acepta 2 costos: uno de tipo DOUBLE y otro de tipo INTEGER.
	 * @param g Grafo no dirigido. g != null
	 * @param t Tipo de costo que ser� usado para crear el grafo dirigido. t =
	 *          {DOUBLE, INTEGER}
	 */
	public DijkstrasShortestPath( UndirectedGraph g, CostType t )
	{
		this.n = g.numberOfVertices( );
		this.g = new Digraph( g, t ); // Construye el d�grafo a partir del grafo no dirigido.
	}

	/**
	 * Reconstruye el tama�o m�s corto de nodos desde el inicio hasta el final
	 * (inclusivos ambos).
	 * @return Un iterable de enteros que corresponden a los id's de los nodos en el
	 *         camino. Si no es alcanzable desde el inicio hasta el final, se
	 *         retorna un iterable vac�o.
	 */
	public Iterable<Integer> reconstructPath( int start, int end )
	{
		if( end < 0 || end >= n )
			throw new IllegalArgumentException( "Invalid node index" );
		if( start < 0 || start >= n )
			throw new IllegalArgumentException( "Invalid node index" );
		double dist = dijkstra( start, end );
		LinkedList<Integer> path = new LinkedList<>( );
		if( dist == Double.POSITIVE_INFINITY )
			return path;
		for( Integer at = end; at != null; at = prev[at] )
			path.addFirst( at );

		return path;
	}

	/**
	 * Corre el algoritmo de Dijkstra en un grafo dirigido para encontrar el camino
	 * m�s corto de un nodo de inicio a uno final.
	 * @param start Nodo de inicio.
	 * @param end   Nodo final.
	 * @return Costo m�nimo del camino.
	 */
	public double dijkstra( int start, int end )
	{
		// Arreglo de distancia m�nima.
		dist = new double[n];
		Arrays.fill( dist, Double.POSITIVE_INFINITY );
		dist[start] = 0;

		// Cola de prioridad del siguiente mejor nodo a visitar.
		MaxHeapPQ<ListNode<?>> pq = new MaxHeapPQ<ListNode<?>>( 2 * n );
		pq.insert( new ListNode<>( start, 0 ) );

		// Arreglo que guarda qu� nodos ya han sido visitados.
		boolean[] visited = new boolean[n];
		prev = new Integer[n];

		while( !pq.isEmpty( ) )
		{
			ListNode<?> node = pq.poll( );
			visited[node.id] = true;

			// Ya se encontr� un mejor camino antes de que se procesara este nodo, as� que
			// se puede ignorar.
			if( dist[node.id] < node.value )
				continue;

			Iterable<model.data_structures.Digraph.Edge> edges = g.edgesAdjacentTo( node.id );
			for( model.data_structures.Digraph.Edge edge : edges )
			{
				if( visited[edge.to] )
					continue;

				// Relajaci�n en el caso que aplique.
				double newDist = dist[edge.from] + edge.cost;
				if( newDist < dist[edge.to] )
				{
					prev[edge.to] = edge.from;
					dist[edge.to] = newDist;
					pq.insert( new ListNode( edge.to, dist[edge.to] ) );
				}
			}

			// Una vez que se haya visitado todos los nodos que se desprenden del final, se
			// sabe que se puede retornar la m�nima distancia a este nodo porque no se puede
			// encontrar un camino mejor despu�s de este punto.
			if( node.id == end )
				return dist[end];
		}
		// No existe un camino entre el inicio y el final.
		return Double.POSITIVE_INFINITY;
	}
}