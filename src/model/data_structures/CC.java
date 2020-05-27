package model.data_structures;

import java.util.Iterator;

/**
 * Implementaci�n de la b�squeda de componentes conectados.
 * @author Camilo Mart�nez & Nicol�s Quintero.
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class CC
{
	private boolean[] marked;

	private int[] id;

	private int count;

	public CC( IGraph G )
	{
		marked = new boolean[G.numberOfVertices( )];
		id = new int[G.numberOfVertices( )];
		for( int s = 0; s < G.numberOfVertices( ); s++ )
			if( !marked[s] )
			{
				dfs( G, s );
				count++;
			}
	}

	public boolean connected( int v, int w )
	{
		return id[v] == id[w];
	}

	public int count( )
	{
		return count;
	}

	private void dfs( IGraph G, int v )
	{
		marked[v] = true;
		Iterator<Integer> iter = G.vertexAdjacentTo( v );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( !marked[w] )
				dfs( G, w );
		}
	}
}