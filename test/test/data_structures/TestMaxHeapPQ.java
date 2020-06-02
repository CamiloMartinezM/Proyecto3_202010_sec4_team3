package test.data_structures;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.MaxHeapPQ;

/**
 * Test de la estructura de datos.
 * @author Camilo Mart�nez
 */
public class TestMaxHeapPQ
{
	/**
	 * Capacidad de la cola.
	 */
	private static final int CAPACITY = 10;

	/**
	 * Cola de prioridad de Integers.
	 */
	private MaxHeapPQ<Integer> heap;

	/**
	 * Escenario 1: Inicializa una cola de prioridad vac�a.
	 */
	@Before
	public void setUp1( )
	{
		heap = new MaxHeapPQ<>( CAPACITY );
	}

	/**
	 * Escenario 2: Llena la cola de prioridad con n�meros aleatorios entre 0 y
	 * 1000.
	 */
	public void setUp2( )
	{
		int min = 0;
		int max = 100;
		for( int i = 0; i < CAPACITY; i++ )
		{
			if( i == Math.floorDiv( CAPACITY, 2 ) )
				heap.insert( 101 );
			else if( i == Math.floorDiv( CAPACITY, 3 ) )
				heap.insert( 102 );
			else if( i == Math.floorDiv( CAPACITY, 6 ) )
				heap.insert( 103 );
			else
				heap.insert( new Random( ).nextInt( ( max - min ) + 1 ) + min );
		}
	}

	@Test
	public void TestGetSize( )
	{
		assertEquals( "Debi� retornar 0.", 0, heap.getSize( ) );

		setUp2( );

		assertEquals( "Debi� retornar " + CAPACITY, CAPACITY, heap.getSize( ) );
	}

	@Test
	public void TestContains( )
	{
		assertEquals( "Debi� retornar false.", false, heap.contains( 1 ) );
		assertEquals( "Debi� retornar false.", false, heap.contains( 0 ) );

		setUp2( );

		assertEquals( "Debi� retornar true.", true, heap.contains( 101 ) );
		assertEquals( "Debi� retornar true.", true, heap.contains( 102 ) );
		assertEquals( "Debi� retornar true.", true, heap.contains( 103 ) );
		assertEquals( "Debi� retornar false.", false, heap.contains( 104 ) );
	}

	@Test
	public void TestInsert( )
	{
		setUp2( );

		assertEquals( "No insert� bien.", 103, heap.peek( ).intValue( ) );

		heap.remove( 103 );
		heap.insert( 104 );

		assertEquals( "No insert� bien.", 104, heap.peek( ).intValue( ) );
		assertEquals( "No insert� bien.", 104, heap.peekPosition( 0 ).intValue( ) );
	}

	@Test
	public void TestPeek( )
	{
		assertEquals( "Debi� retornar null.", null, heap.peek( ) );

		setUp2( );

		assertEquals( "Debi� retornar 103.", 103, heap.peek( ).intValue( ) );

		heap.poll( );
		heap.insert( 104 );

		assertEquals( "Debi� retornar 103.", 104, heap.peek( ).intValue( ) );
	}

	@Test
	public void TestPeekPosition( )
	{
		assertEquals( "Debi� retornar null.", null, heap.peekPosition( 0 ) );
		assertEquals( "Debi� retornar null.", null, heap.peekPosition( 1 ) );

		setUp2( );

		assertEquals( "Debi� retornar 103.", 103, heap.peekPosition( 0 ).intValue( ) );
		assertEquals( "Debi� retornar 102.", 102, heap.peekPosition( 1 ).intValue( ) );
		assertEquals( "Debi� retornar 101.", 101, heap.peekPosition( 2 ).intValue( ) );
	}

	@Test
	public void TestRemove( )
	{
		assertEquals( "Debi� retornar false.", false, heap.remove( 0 ) );
		assertEquals( "Debi� retornar false.", false, heap.remove( 1 ) );

		setUp2( );

		assertEquals( "Debi� retornar true.", true, heap.remove( 103 ) );
		assertEquals( "Debi� retornar true.", true, heap.remove( 102 ) );
		assertEquals( "Debi� retornar true.", true, heap.remove( 101 ) );
		assertEquals( "Debi� retornar " + ( CAPACITY - 3 ), CAPACITY - 3, heap.getSize( ) );
	}

	@Test
	public void TestPoll( )
	{
		assertEquals( "Debi� retornar null.", null, heap.peek( ) );

		setUp2( );

		assertEquals( "Debi� retornar 103.", 103, heap.poll( ).intValue( ) );
		assertEquals( "Debi� retornar " + ( CAPACITY - 1 ), CAPACITY - 1, heap.getSize( ) );
		assertEquals( "Debi� retornar 102.", 102, heap.poll( ).intValue( ) );
		assertEquals( "Debi� retornar 101.", 101, heap.poll( ).intValue( ) );
		assertEquals( "Debi� retornar " + ( CAPACITY - 3 ), CAPACITY - 3, heap.getSize( ) );

		assertEquals( "Debi� retornar null.", null, heap.peekPosition( CAPACITY ) );
	}

	@Test
	public void TestIsEmpty( )
	{
		assertEquals( "Debi� retornar true.", true, heap.isEmpty( ) );

		setUp2( );

		assertEquals( "Debi� retornar false.", false, heap.isEmpty( ) );
	}

	@Test
	public void TestIsFull( )
	{
		assertEquals( "Debi� retornar false.", false, heap.isFull( ) );

		setUp2( );

		assertEquals( "Debi� retornar true.", true, heap.isFull( ) );
	}

	@Test
	public void TestClear( )
	{
		heap.clear( );
		assertEquals( "Debi� retornar null.", null, heap.peek( ) );

		setUp2( );

		assertEquals( "Debi� retornar " + CAPACITY + ".", CAPACITY, heap.getSize( ) );

		heap.clear( );

		assertEquals( "Debi� retornar 0.", 0, heap.getSize( ) );
	}
}