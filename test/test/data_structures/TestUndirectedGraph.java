package test.data_structures;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

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
	public final int V = 40;

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
		int[] x = new int[V / 2];
		int[] y = new int[V / 2];
		for( int i = 0; i < V / 2; i++ )
		{
			x[i] = i;
			y[i] = i + 20;
		}

		for( int i = 0; i < V / 2; i++ )
		{
			grafo.setVertexInfo( i, x[i] + "-INFO" );
			grafo.addEdge( x[i], y[i], Math.sqrt( x[i] * x[i] + y[i] * y[i] ) );
		}

		for( int i = 0; i < V; i++ )
			grafo.insertVertexItem( i, i + "", i + 100 );
	}

	/**
	 * Inicializa un grafo con arcos de i a i+V donde i recorre 0 a V-1 y también,
	 * de i a i+1 donde i recorre de 0 a V-2 y la información de cada vertice i es
	 * un string de i concatenado con "-INFO".
	 */
	public void setUp3( )
	{
		setUp2( );

		for( int i = 0; i < ( V - 1 ); i++ )
		{
			grafo.addEdge( i, i + 1, Math.sqrt( i * i + Math.pow( i + 1, 2 ) ) );
			grafo.insertVertexItem( i, i + "", i + 100 );
		}
	}

	@Test
	public void TestNumberOfVertices( )
	{
		setUp2( );
		assertEquals( "Falló encontrar el número de vertices.", V, grafo.numberOfVertices( ) );

		setUp3( );
		assertEquals( "Falló encontrar el número de vertices.", V, grafo.numberOfVertices( ) );
	}

	@Test
	public void TestNumberOfEdges( )
	{
		setUp2( );
		assertEquals( "Falló encontrar el número de arcos.", V / 2, grafo.numberOfEdges( ) );

		setUp3( );
		assertEquals( "Falló encontrar el número de arcos.", V / 2 + ( V - 1 ), grafo.numberOfEdges( ) );
	}

	@Test
	public void TestAddEdge( )
	{
		grafo.addEdge( 0, 1, 10.0 ); // Nuevo
		grafo.addEdge( 1, 2, 20.0 ); // Nuevo
		grafo.addEdge( 0, 1, 10.0 ); // Ya existe y tiene el mismo costo
		grafo.addEdge( 1, 0, 20.0 ); // Ya existe pero se actualiza el costo de 10.0 a 20.0

		assertEquals( "No agregó correctamente los arcos.", 2, grafo.numberOfEdges( ) );
		assertEquals( "No se actualizó el costo.", 20.0 + "", grafo.getEdgeDoubleCost( 1, 0 ) + "" );
	}

	@Test
	public void TestGetEdgeCost( )
	{
		for( int i = 0; i < V / 2; i++ )
			assertEquals( "No debería haber ningún costo.", UndirectedGraph.INFINITY + "", grafo.getEdgeDoubleCost( i, i + 20 ) + "" );
		
		setUp2( );
		
		for( int i = 0; i < V / 2; i++ )
			assertEquals( "No se encontró el costo real.", Math.sqrt( i * i + ( i + 20 ) * ( i + 20 ) ) + "",
					grafo.getEdgeDoubleCost( i, i + 20 ) + "" );
	}

	@Test
	public void TestAdj( )
	{
		Iterator<Integer> iter;
		int j;
		for( int i = 0; i < V; i++ )
		{
			iter = grafo.adj( i ).iterator( );
			j = 0;
			while( iter.hasNext( ) )
			{
				iter.next( );
				j++;
			}

			assertEquals( "No hay arcos aún.", 0, j );
		}

		setUp2( );

		for( int i = 0; i < V; i++ )
		{
			iter = grafo.adj( i ).iterator( );
			j = 0;
			while( iter.hasNext( ) )
			{
				iter.next( );
				j++;
			}

			assertEquals( "Cada vertice se planteó para tener un único adyacente.", 1, j );
		}

		setUp3( );

		for( int i = 0; i < V; i++ )
		{
			iter = grafo.adj( i ).iterator( );
			j = 0;
			while( iter.hasNext( ) )
			{
				iter.next( );
				j++;
			}

			if( i == 0 || i == ( V - 1 ) )
				assertEquals( "El vertice 0 y " + ( V - 1 ) + " deben tener 2 adyacentes.", 2, j );
			else
				assertEquals( "Cada vertice se planteó para tener 3 adyacentes.", 3, j );
		}
	}

	@Test
	public void TestDegreeOf( )
	{
		for( int i = 0; i < V; i++ )
			assertEquals( "No hay ningún arco. El grado debe ser 0.", 0, grafo.degreeOf( i ) );

		setUp2( );

		for( int i = 0; i < V; i++ )
			assertEquals( "El grado debe ser 1.", 1, grafo.degreeOf( i ) );

		setUp3( );

		assertEquals( "El grado del vertice 0 debe ser 2.", 2, grafo.degreeOf( 0 ) );

		for( int i = 1; i < V - 1; i++ )
			assertEquals( "El grado debe ser 3.", 3, grafo.degreeOf( i ) );

		assertEquals( "El grado del vertice " + ( V - 1 ) + " debe ser 2.", 2, grafo.degreeOf( V - 1 ) );
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