package model.data_structures;

import java.util.Iterator;

/**
 * Implementación de la búsqueda de caminos (DFS o BFS).
 * @author Camilo Martínez & Nicolás Quintero.
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class Paths
{
	public static enum Tipo
	{
		DFS, BFS
	}

	private boolean[] marked;

	private int[] edgeTo;

	private final int s;
	
	public Paths( IGraph G, int s, Tipo t )
	{
		marked = new boolean[G.numberOfVertices( )];
		edgeTo = new int[G.numberOfVertices( )];
		this.s = s;
		
		if( t == Tipo.DFS )
			dfs( G, s );
		else // BFS
			bfs( G, s );
	}
	
	public boolean hasPathTo( int v )
	{
		return marked[v];
	}
	
	public Iterable<Integer> pathTo( int v )
	{
		if( !hasPathTo( v ) ) return null;
		
		Stack<Integer> path = new Stack<>( );
		for( int x = v; x != s; x = edgeTo[x] )
			path.push( x );
		
		path.push( s );
		return path;
	}
	
	private void dfs( IGraph G, int v )
	{
		marked[v] = true;
		Iterator<Integer> iter = G.vertexAdjacentTo( v );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( !marked[w] )
			{
				edgeTo[w] = v;
				dfs( G, w );
			}
		}
	}
	
	private void bfs( IGraph G, int s )
	{
		Queue<Integer> queue = new Queue<>( );
		marked[s] = true;
		queue.enQueue( s );
		while( !queue.isEmpty( ) )
		{
			int v = queue.deQueue( );
			Iterator<Integer> iter = G.vertexAdjacentTo( v );
			while( iter.hasNext( ) )
			{
				int w = iter.next( );
				if( !marked[w] )
				{
					edgeTo[w] = v;
					marked[w] = true;
					queue.enQueue( w );
				}
			}
		}
	}
}