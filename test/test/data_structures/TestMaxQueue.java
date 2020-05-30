package test.data_structures;

import static org.junit.Assert.assertEquals;


import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.MaxQueue;

/**
 * Test de la estructura de datos.
 * @author Camilo Martínez
 */
public class TestMaxQueue
{
	/**
	 * Capacidad de la cola.
	 */
	private static final int CAPACITY = 10;

	/**
	 * Cola de prioridad de Integers.
	 */
	private MaxQueue<Integer> pq;

	/**
	 * Escenario 1: Inicializa una cola de prioridad vacía.
	 */
	@Before
	public void setUp1( )
	{
		pq = new MaxQueue<>( CAPACITY );
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
			if( i == CAPACITY / 2 )
				pq.insert( 101 ); // Inserta el máximo en la mitad para verificar que quede en la primera posición.
			else
				pq.insert( new Random( ).nextInt( ( max - min ) + 1 ) + min );
		}
	}

	@Test
	public void TestGetSize( )
	{
		assertEquals( "Debió retornar 0.", 0, pq.getSize( ) );

		setUp2( );

		assertEquals( "Debió retornar " + CAPACITY, CAPACITY, pq.getSize( ) );
	}

	@Test
	public void TestInsert( )
	{
		assertEquals( "Debió retornar 0.", 0, pq.getSize( ) );

		setUp2( );

		assertEquals( "No está ordenado ascendemente.", true, ordenadoDescendentemente( pq ) );
		assertEquals( "No insertó bien.", 101, pq.peek( ).intValue( ) );
	}

	@Test
	public void TestPeekMax( )
	{
		assertEquals( "Debió retornar null.", null, pq.peek( ) );

		setUp2( );

		assertEquals( "Debió retornar 101.", 101, pq.peek( ).intValue( ) );
	}

	@Test
	public void TestDelMax( )
	{
		assertEquals( "Debió retornar null.", null, pq.peek( ) );

		setUp2( );

		assertEquals( "Debió retornar 101.", 101, pq.poll( ).intValue( ) );
		assertEquals( "Debió retornar " + ( CAPACITY - 1 ), CAPACITY - 1, pq.getSize( ) );
		assertEquals( "Debió retornar null.", null, pq.peekPosition( CAPACITY ) );
	}

	@Test
	public void TestIsEmpty( )
	{
		assertEquals( "Debió retornar true.", true, pq.isEmpty( ) );

		setUp2( );

		assertEquals( "Debió retornar false.", false, pq.isEmpty( ) );
	}

	@Test
	public void TestIsFull( )
	{
		assertEquals( "Debió retornar false.", false, pq.isFull( ) );

		setUp2( );

		assertEquals( "Debió retornar true.", true, pq.isFull( ) );
	}

	/**
	 * Prueba si los elementos del arreglo de objetos comparables está ordenado de
	 * forma ascendente.
	 * @param pq Arreglo cuyo ordenamiento se quiere verificar.
	 * @return True si el arreglo está ordenado de forma ascendente, false de lo
	 *         contrario.
	 */
	private static boolean ordenadoDescendentemente( MaxQueue<Integer> pq )
	{
		for( int i = 0; i < pq.getSize( ) - 1; i++ )
			if( pq.peekPosition( i ).compareTo( pq.peekPosition( i + 1 ) ) < 0 )
				return false;

		return true;
	}
}