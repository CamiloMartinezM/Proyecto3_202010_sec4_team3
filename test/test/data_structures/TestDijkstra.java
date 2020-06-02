package test.data_structures;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.CostType;
import model.data_structures.DijkstrasShortestPath;
import model.data_structures.UndirectedGraph;

public class TestDijkstra
{
	/**
	 * Número de vertices.
	 */
	public final int V = 9;

	private UndirectedGraph<String, Integer, Integer> grafo;

	/**
	 * Inicializa un grafo vacío.
	 */
	@Before
	public void setUp( )
	{
		grafo = new UndirectedGraph<>( V );
	}

	/**
	 * Inicializa un grafo con arcos de i a i+V donde i recorre 0 a V - 1 y la
	 * información de cada vertice i es un string de i concatenado con "-INFO".
	 */
	public void setUp2( )
	{
		setUp( );

		grafo.addEdge( 1, 2, Math.sqrt( 1 + 2 * 2 ) );
		grafo.addEdge( 1, 3, Math.sqrt( 1 + 3 * 3 ) );
		grafo.addEdge( 1, 5, Math.sqrt( 1 + 5 * 5 ) );
		grafo.addEdge( 1, 4, Math.sqrt( 1 + 4 * 4 ) );
		grafo.addEdge( 2, 3, Math.sqrt( 2 * 2 + 3 * 3 ) );
		grafo.addEdge( 3, 5, Math.sqrt( 3 * 3 + 5 * 5 ) );
		grafo.addEdge( 4, 6, Math.sqrt( 4 * 4 + 6 * 6 ) );
		grafo.addEdge( 4, 7, Math.sqrt( 4 * 4 + 7 * 7 ) );
		grafo.addEdge( 7, 6, Math.sqrt( 7 * 7 + 6 * 6 ) );
		grafo.addEdge( 7, 8, Math.sqrt( 7 * 7 + 8 * 8 ) );

		for( int i = 0; i < V; i++ )
		{
			grafo.setVertexInfo( i, i + "-INFO" );
			grafo.insertVertexItem( i, i + "", i + 100 );
		}
	}

	@Test
	public void TestReconstructPath( )
	{
		setUp2( );

		DijkstrasShortestPath dsp = new DijkstrasShortestPath( grafo, CostType.DOUBLE );
		Iterator<Integer> iter = dsp.reconstructPath( 1, 8 ).iterator( );

		while( iter.hasNext( ) )
			System.out.println( iter.next( ) );
	}
}
