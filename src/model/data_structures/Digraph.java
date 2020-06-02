package model.data_structures;

import java.math.BigDecimal;

@SuppressWarnings( "unchecked" )
public class Digraph
{
	/**
	 * Implementación de un arco dirigido entre dos nodos con id's dados.
	 * @author Camilo Martínez
	 */
	public static class Edge
	{
		double cost;
		int from, to;

		public Edge( int from, int to, double cost )
		{
			this.from = from;
			this.to = to;
			this.cost = cost;
		}
	}

	private final int V;
	private int E;
	private Bag<Edge>[] adj;

	public Digraph( int V )
	{
		this.V = V;
		this.E = 0;
		adj = ( Bag<Edge>[] ) new Bag[V];
		for( int v = 0; v < V; v++ )
			adj[v] = new Bag<Edge>( );
	}

	/**
	 * Construye un dígrafo a partir de un grafo no dirigido según el costo que se
	 * quiera (DOUBLE o INTEGER).
	 * @param g Grafo no dirigido. g != null
	 * @param t Tipo de costo. t = {DOUBLE, INTEGER}
	 */
	public Digraph( UndirectedGraph<?, ?, ?> g, CostType t )
	{
		this( g.numberOfVertices( ) );

		if( t == CostType.INTEGER )
			g.t = CostType.INTEGER;

		for( String edge : g )
		{
			int from = Integer.valueOf( edge.split( "," )[0] );
			int to = Integer.valueOf( edge.split( "," )[1] );
			double cost;
			if( edge.split( "," )[2].contains( "E" ) ) // Detecta una notación científica
				cost = ( new BigDecimal( edge.split( "," )[2] ) ).doubleValue( );
			else
				cost = Double.valueOf( edge.split( "," )[2] );
			
			this.addEdge( new Edge( from, to, cost ) );
			this.addEdge( new Edge( to, from, cost ) );
		}
	}

	public int V( )
	{
		return V;
	}

	public int E( )
	{
		return E;
	}

	public void addEdge( Edge e )
	{
		adj[e.from].add( e );
		E++;
	}

	public Iterable<Edge> edgesAdjacentTo( int v )
	{
		return adj[v];
	}
}