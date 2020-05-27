package test.data_structures;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import model.data_structures.Queue;

/**
 * Test de la estructura de datos.
 * @author Nicolás Quintero
 */
public class TestQueue
{
	/**
	 * Cola de strings.
	 */
	private Queue<String> lista;

	/**
	 * Escenario 1: Crea una cola vacía.
	 */
	@Before
	public void setUp1( )
	{
		lista = new Queue<>( );
	}

	/**
	 * Escenario 2: Crea una Cola de strings. (small).
	 */
	public void setUp2( )
	{
		for( int i = 0; i < 1000; i++ )
		{
			lista.enQueue( "" + i );
		}
	}

	@Test
	public void testEnqueue( ) throws IllegalStateException
	{
		lista.enQueue( "Prueba" );
		lista.enQueue( "Prueba2" );

		assertEquals( "El total de elementos en la lista es incorrecto.", 2, lista.getSize( ) );
		assertEquals( "No retorna el string correcto.", "Prueba", lista.deQueue( ) );
		assertEquals( "No retorna el string correcto.", "Prueba2", lista.deQueue( ) );
	}

	@Test
	public void deQueue( ) throws IllegalStateException
	{
		setUp2( );
		for( int i = 0; i < 1000; i++ )
		{
			assertEquals( "No retorna el string correcto.", "" + i, lista.deQueue( ) );
		}
	}

	@Test
	public void testDarTamanio( )
	{
		setUp2( );
		assertEquals( "No retorna el verdadero tamaño de la lista.", 1000, lista.getSize( ) );
	}

	@Test
	public void darPrimero( )
	{
		setUp2( );
		assertEquals( "No retorna la verdadera cabeza.", "" + 0, lista.getFirst( ).getItem( ).toString( ) );
	}
}