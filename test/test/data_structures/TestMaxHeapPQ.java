package test.data_structures;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.MaxHeapPQ;

/**
 * Test de la estructura de datos.
 * @author Camilo Martínez
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
	 * Escenario 1: Inicializa una cola de prioridad vacía.
	 */
	@Before
	public void setUp1( )
	{
		heap = new MaxHeapPQ<>( CAPACITY );
	}

	/**
	 * Escenario 2: Llena la cola de prioridad con números aleatorios entre 0 y
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
		assertEquals( "Debió retornar 0.", 0, heap.getSize( ) );

		setUp2( );

		assertEquals( "Debió retornar " + CAPACITY, CAPACITY, heap.getSize( ) );
	}

	@Test
	public void TestContains( )
	{
		assertEquals( "Debió retornar false.", false, heap.contains( 1 ) );
		assertEquals( "Debió retornar false.", false, heap.contains( 0 ) );

		setUp2( );

		assertEquals( "Debió retornar true.", true, heap.contains( 101 ) );
		assertEquals( "Debió retornar true.", true, heap.contains( 102 ) );
		assertEquals( "Debió retornar true.", true, heap.contains( 103 ) );
		assertEquals( "Debió retornar false.", false, heap.contains( 104 ) );
	}

	@Test
	public void TestInsert( )
	{
		setUp2( );

		assertEquals( "No insertó bien.", 103, heap.peek( ).intValue( ) );

		heap.remove( 103 );
		heap.insert( 104 );

		assertEquals( "No insertó bien.", 104, heap.peek( ).intValue( ) );
		assertEquals( "No insertó bien.", 104, heap.peekPosition( 0 ).intValue( ) );
	}

	@Test
	public void TestPeek( )
	{
		assertEquals( "Debió retornar null.", null, heap.peek( ) );

		setUp2( );

		assertEquals( "Debió retornar 103.", 103, heap.peek( ).intValue( ) );

		heap.poll( );
		heap.insert( 104 );

		assertEquals( "Debió retornar 103.", 104, heap.peek( ).intValue( ) );
	}

	@Test
	public void TestPeekPosition( )
	{
		assertEquals( "Debió retornar null.", null, heap.peekPosition( 0 ) );
		assertEquals( "Debió retornar null.", null, heap.peekPosition( 1 ) );

		setUp2( );

		assertEquals( "Debió retornar 103.", 103, heap.peekPosition( 0 ).intValue( ) );
		assertEquals( "Debió retornar 102.", 102, heap.peekPosition( 1 ).intValue( ) );
		assertEquals( "Debió retornar 101.", 101, heap.peekPosition( 2 ).intValue( ) );
	}

	@Test
	public void TestRemove( )
	{
		assertEquals( "Debió retornar false.", false, heap.remove( 0 ) );
		assertEquals( "Debió retornar false.", false, heap.remove( 1 ) );

		setUp2( );

		assertEquals( "Debió retornar true.", true, heap.remove( 103 ) );
		assertEquals( "Debió retornar true.", true, heap.remove( 102 ) );
		assertEquals( "Debió retornar true.", true, heap.remove( 101 ) );
		assertEquals( "Debió retornar " + ( CAPACITY - 3 ), CAPACITY - 3, heap.getSize( ) );
	}

	@Test
	public void TestPoll( )
	{
		assertEquals( "Debió retornar null.", null, heap.peek( ) );

		setUp2( );

		assertEquals( "Debió retornar 103.", 103, heap.poll( ).intValue( ) );
		assertEquals( "Debió retornar " + ( CAPACITY - 1 ), CAPACITY - 1, heap.getSize( ) );
		assertEquals( "Debió retornar 102.", 102, heap.poll( ).intValue( ) );
		assertEquals( "Debió retornar 101.", 101, heap.poll( ).intValue( ) );
		assertEquals( "Debió retornar " + ( CAPACITY - 3 ), CAPACITY - 3, heap.getSize( ) );

		assertEquals( "Debió retornar null.", null, heap.peekPosition( CAPACITY ) );
	}

	@Test
	public void TestIsEmpty( )
	{
		assertEquals( "Debió retornar true.", true, heap.isEmpty( ) );

		setUp2( );

		assertEquals( "Debió retornar false.", false, heap.isEmpty( ) );
	}

	@Test
	public void TestIsFull( )
	{
		assertEquals( "Debió retornar false.", false, heap.isFull( ) );

		setUp2( );

		assertEquals( "Debió retornar true.", true, heap.isFull( ) );
	}

	@Test
	public void TestClear( )
	{
		heap.clear( );
		assertEquals( "Debió retornar null.", null, heap.peek( ) );

		setUp2( );

		assertEquals( "Debió retornar " + CAPACITY + ".", CAPACITY, heap.getSize( ) );

		heap.clear( );

		assertEquals( "Debió retornar 0.", 0, heap.getSize( ) );
	}
}