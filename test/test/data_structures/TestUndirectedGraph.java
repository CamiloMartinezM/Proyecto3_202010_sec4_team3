package test.data_structures;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.HashTable;
import model.data_structures.UndirectedGraph;

/**
 * Test de la implementación del grafo no dirigido.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class TestUndirectedGraph
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
	public void TestNumberOfVertices( )
	{
		setUp2( );
		assertEquals( "Falló encontrar el número de vertices.", V, grafo.numberOfVertices( ) );
	}

	@Test
	public void TestNumberOfEdges( )
	{
		setUp2( );
		assertEquals( "Falló encontrar el número de arcos.", 10, grafo.numberOfEdges( ) );
	}

	@Test
	public void TestAddEdge( )
	{
		grafo.addEdge( 0, 1, 10.0 );
		assertEquals( "No agregó correctamente los arcos.", 1, grafo.numberOfEdges( ) );

		setUp2( );
		
		grafo.addEdge( 0, 1, 10.0 );
		assertEquals( "No agregó correctamente los arcos.", 11, grafo.numberOfEdges( ) );
		assertEquals( "No guardó bien el costo.", 10.0 + "", grafo.getEdgeDoubleCost( 0, 1 ) + "" );
	}

	@Test
	public void TestGetEdgeCost( )
	{
		assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "",
				grafo.getEdgeDoubleCost( 1, 6 ) + "" );
		assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "",
				grafo.getEdgeDoubleCost( 1, 8 ) + "" );
		assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "",
				grafo.getEdgeDoubleCost( 7, 5 ) + "" );

		setUp2( );

		assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "",
				grafo.getEdgeDoubleCost( 1, 6 ) + "" );
		assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "",
				grafo.getEdgeDoubleCost( 1, 8 ) + "" );
		assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "",
				grafo.getEdgeDoubleCost( 7, 5 ) + "" );

		assertEquals( "No se encontró el costo real.", Math.sqrt( 1 + 2 * 2 ) + "",
				grafo.getEdgeDoubleCost( 1, 2 ) + "" );
		assertEquals( "No se encontró el costo real.", Math.sqrt( 1 + 3 * 3 ) + "",
				grafo.getEdgeDoubleCost( 1, 3 ) + "" );
		assertEquals( "No se encontró el costo real.", Math.sqrt( 1 + 5 * 5 ) + "",
				grafo.getEdgeDoubleCost( 1, 5 ) + "" );
		assertEquals( "No se encontró el costo real.", Math.sqrt( 1 + 4 * 4 ) + "",
				grafo.getEdgeDoubleCost( 1, 4 ) + "" );
		assertEquals( "No se encontró el costo real.", Math.sqrt( 2 * 2 + 3 * 3 ) + "",
				grafo.getEdgeDoubleCost( 2, 3 ) + "" );
	}

	@Test
	public void TestVertexAdjacentTo( )
	{
		Iterator<Integer> iter;
		int j;
		for( int i = 0; i < V; i++ )
		{
			iter = grafo.vertexAdjacentTo( i );
			j = 0;
			while( iter.hasNext( ) )
			{
				iter.next( );
				j++;
			}

			assertEquals( "No hay arcos aún.", 0, j );
		}

		setUp2( );

		iter = grafo.vertexAdjacentTo( 1 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 2 && w != 3 && w != 5 && w != 4 )
				fail( "Falló al encontrar los adyacentes de 1." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 2 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 1 && w != 3 )
				fail( "Falló al encontrar los adyacentes de 2." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 3 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 2 && w != 1 && w != 5 )
				fail( "Falló al encontrar los adyacentes de 3." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 4 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 1 && w != 6 && w != 7 )
				fail( "Falló al encontrar los adyacentes de 4." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 5 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 3 && w != 1 )
				fail( "Falló al encontrar los adyacentes de 5." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 6 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 4 && w != 7 )
				fail( "Falló al encontrar los adyacentes de 6." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 7 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 4 && w != 6 && w != 8 )
				fail( "Falló al encontrar los adyacentes de 7." );
				
		}
		
		iter = grafo.vertexAdjacentTo( 8 );
		while( iter.hasNext( ) )
		{
			int w = iter.next( );
			if( w != 7 )
				fail( "Falló al encontrar los adyacentes de 8." );
				
		}
	}

	@Test
	public void TestDegreeOf( )
	{
		for( int i = 0; i < V; i++ )
			assertEquals( "No hay ningún arco. El grado debe ser 0.", 0, grafo.degreeOf( i ) );

		setUp2( );

		HashTable<Integer, Integer> grados = new HashTable<Integer, Integer>( 11 );
		grados.put( 1, 4 );
		grados.put( 2, 2 );
		grados.put( 3, 3 );
		grados.put( 4, 3 );
		grados.put( 5, 2 );
		grados.put( 6, 2 );
		grados.put( 7, 3 );
		grados.put( 8, 1 );
	}

	@Test
	public void TestSetVertexItem( )
	{
		grafo.insertVertexItem( 0, 1 + "", 1 );
		Iterator<Integer> iter = grafo.vertexItems( 0 );
		assertEquals( "No guardó el item.", 1, iter.next( ).intValue( ) );
		assertEquals( "No guardó el item.", false, iter.hasNext( ) );

		grafo.insertVertexItem( 1, 1 + "", 1 );
		grafo.insertVertexItem( 1, 2 + "", 2 );
		grafo.insertVertexItem( 1, 3 + "", 3 );
		iter = grafo.vertexItems( 1 );
		assertEquals( "No guardó el item.", 1, iter.next( ).intValue( ) );
		assertEquals( "No guardó el item.", 2, iter.next( ).intValue( ) );
		assertEquals( "No guardó el item.", 3, iter.next( ).intValue( ) );
		assertEquals( "No guardó el item.", false, iter.hasNext( ) );
	}

	@Test
	public void TestSetVertexInfo( )
	{
		grafo.setVertexInfo( 0, "1" );
		assertEquals( "No guardó la información.", "1", grafo.getVertexInfo( 0 ) );

		grafo.setVertexInfo( 1, "1" );
		assertEquals( "No guardó la información.", "1", grafo.getVertexInfo( 1 ) );

		grafo.setVertexInfo( 1, "2" );
		assertEquals( "No guardó la información.", "2", grafo.getVertexInfo( 1 ) );
	}

	@Test
	public void TestGetVertexInfo( )
	{
		setUp2( );

		for( int i = 0; i < V / 2; i++ )
			assertEquals( "No se obtuvo la información que era.", i + "-INFO", grafo.getVertexInfo( i ) );
	}

	@Test
	public void TestVertexItems( )
	{
		for( int i = 0; i < V; i++ )
			assertEquals( "No debería haber items en ningún vertice.", false, grafo.vertexItems( i ).hasNext( ) );
	}
}