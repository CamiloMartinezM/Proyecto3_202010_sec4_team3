package test.data_structures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.Stack;

/**
 * Test de la estructura de datos de pila.
 * @author Camilo Martínez
 */
public class TestStack
{
	/**
	 * Pila de strings.
	 */
	private Stack<String> lista;

	/**
	 * Escenario 1: Crea una pila vacía.
	 */
	@Before
	public void setUp1( )
	{
		lista = new Stack<>( );
	}

	/**
	 * Escenario 2: Crea una pila de strings. (small).
	 */
	public void setUp2( )
	{
		for( int i = 0; i < 1000; i++ )
		{
			lista.push( "" + i );
		}
	}

	@Test
	public void testPush( )
	{
		lista.push( "Hola1" );
		lista.push( "Hola2" );

		assertEquals( "El total de elementos en la lista es incorrecto.", 2, lista.getSize( ) );
		assertEquals( "No retorna el string correcto.", "Hola2", lista.pop( ) );
		assertEquals( "No retorna el string correcto.", "Hola1", lista.pop( ) );
	}

	@Test
	public void testPop( )
	{
		setUp2( );
		for( int i = 999; i >= 0; i-- )
		{
			assertEquals( "No retorna el string correcto.", "" + i, lista.pop( ) );
		}
	}

	@Test
	public void testGetSize( )
	{
		setUp2( );
		assertEquals( "No retorna el verdadero tamaño de la lista.", 1000, lista.getSize( ) );
	}

	@Test
	public void testGetTopStack( )
	{
		setUp2( );
		assertEquals( "No retorna la verdadera cabeza.", "" + 999, lista.getTopStack( ).getItem( ).toString( ) );
	}
}