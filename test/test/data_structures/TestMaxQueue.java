package test.data_structures;

import static org.junit.Assert.assertEquals;


import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.MaxQueue;

/**
 * Test de la estructura de datos.
 * @author Camilo Mart�nez
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
	 * Escenario 1: Inicializa una cola de prioridad vac�a.
	 */
	@Before
	public void setUp1( )
	{
		pq = new MaxQueue<>( CAPACITY );
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
			if( i == CAPACITY / 2 )
				pq.insert( 101 ); // Inserta el m�ximo en la mitad para verificar que quede en la primera posici�n.
			else
				pq.insert( new Random( ).nextInt( ( max - min ) + 1 ) + min );
		}
	}

	@Test
	public void TestGetSize( )
	{
		assertEquals( "Debi� retornar 0.", 0, pq.getSize( ) );

		setUp2( );

		assertEquals( "Debi� retornar " + CAPACITY, CAPACITY, pq.getSize( ) );
	}

	@Test
	public void TestInsert( )
	{
		assertEquals( "Debi� retornar 0.", 0, pq.getSize( ) );

		setUp2( );

		assertEquals( "No est� ordenado ascendemente.", true, ordenadoDescendentemente( pq ) );
		assertEquals( "No insert� bien.", 101, pq.peek( ).intValue( ) );
	}

	@Test
	public void TestPeekMax( )
	{
		assertEquals( "Debi� retornar null.", null, pq.peek( ) );

		setUp2( );

		assertEquals( "Debi� retornar 101.", 101, pq.peek( ).intValue( ) );
	}

	@Test
	public void TestDelMax( )
	{
		assertEquals( "Debi� retornar null.", null, pq.peek( ) );

		setUp2( );

		assertEquals( "Debi� retornar 101.", 101, pq.poll( ).intValue( ) );
		assertEquals( "Debi� retornar " + ( CAPACITY - 1 ), CAPACITY - 1, pq.getSize( ) );
		assertEquals( "Debi� retornar null.", null, pq.peekPosition( CAPACITY ) );
	}

	@Test
	public void TestIsEmpty( )
	{
		assertEquals( "Debi� retornar true.", true, pq.isEmpty( ) );

		setUp2( );

		assertEquals( "Debi� retornar false.", false, pq.isEmpty( ) );
	}

	@Test
	public void TestIsFull( )
	{
		assertEquals( "Debi� retornar false.", false, pq.isFull( ) );

		setUp2( );

		assertEquals( "Debi� retornar true.", true, pq.isFull( ) );
	}

	/**
	 * Prueba si los elementos del arreglo de objetos comparables est� ordenado de
	 * forma ascendente.
	 * @param pq Arreglo cuyo ordenamiento se quiere verificar.
	 * @return True si el arreglo est� ordenado de forma ascendente, false de lo
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