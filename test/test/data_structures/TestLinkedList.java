package test.data_structures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.LinkedList;

/**
 * Test de la estructura de datos de lista enlazada.
 * @author Camilo Martínez
 */
public class TestLinkedList
{
	/**
	 * Lista de strings.
	 */
	private LinkedList<String> lista;

	/**
	 * Escenario 1: Crea una cola vacía.
	 */
	@Before
	public void setUp1( )
	{
		lista = new LinkedList<>( );
	}

	/**
	 * Escenario 2: Crea una Cola de strings. (small).
	 */
	public void setUp2( )
	{
		for( int i = 0; i < 1000; i++ )
		{
			lista.append( "" + i );
		}
	}

	@Test
	public void testAppend( )
	{
		lista.append( "Prueba" );
		lista.append( "Prueba2" );

		assertEquals( "No retorna el string correcto.", "Prueba", lista.getHead( ).getItem( ) );
		assertEquals( "No retorna el string correcto.", "Prueba2", lista.getLast( ).getItem( ) );
	}

	@Test
	public void testAddFirst( )
	{
		lista.addFirst( "Prueba" );
		lista.addFirst( "Prueba2" );

		assertEquals( "No retorna el string correcto.", "Prueba2", lista.getHead( ).getItem( ) );
		assertEquals( "No retorna el string correcto.", "Prueba", lista.getLast( ).getItem( ) );
	}

	@Test
	public void testRemoveFirst( )
	{
		lista.addFirst( "Prueba" );
		lista.addFirst( "Prueba2" );

		lista.removeFirst( );
		assertEquals( "No eliminó el primer elemento correctamente.", "Prueba", lista.getHead( ).getItem( ) );

		lista.append( "Prueba3" );
		lista.append( "Prueba4" );

		lista.removeFirst( );
		assertEquals( "No eliminó el primer elemento correctamente.", "Prueba3", lista.getHead( ).getItem( ) );
	}

	@Test
	public void testRemove( )
	{
		lista.append( "Prueba3" );
		lista.append( "Prueba4" );
		lista.addFirst( "Prueba" );
		lista.addFirst( "Prueba2" );

		lista.remove( 1 );

		assertEquals( "No eliminó el elemento en la posición 1 correctamente.", "Prueba2", lista.getPosition( 0 ) );

		lista.remove( 2 );

		assertEquals( "No eliminó el primer elemento correctamente.", "Prueba3", lista.getPosition( 1 ) );

		setUp1( );
		setUp2( );

		for( int i = 1; i < 500; i++ )
		{
			lista.remove( i );
			assertEquals( "No eliminó correctamente el elemento en la posición " + i + ".", "" + i * 2,
					lista.getPosition( i ) );
		}
	}

	@Test
	public void testGetSize( )
	{
		assertEquals( "No retorna el verdadero tamaño de la lista.", 0, lista.getSize( ) );
		setUp2( );
		assertEquals( "No retorna el verdadero tamaño de la lista.", 1000, lista.getSize( ) );
	}

	@Test
	public void testIsEmpty( )
	{
		assertEquals( "Debió retornar true.", true, lista.isEmpty( ) );
		setUp2( );
		assertEquals( "Debió retornar false.", false, lista.isEmpty( ) );
	}

	@Test
	public void getHead( )
	{
		assertEquals( "Debió retornar null.", null, lista.getHead( ) );
		setUp2( );
		assertEquals( "No retorna la verdadera cabeza.", "" + 0, lista.getHead( ).getItem( ) );
	}

	@Test
	public void getLast( )
	{
		assertEquals( "Debió retornar null.", null, lista.getLast( ) );
		setUp2( );
		assertEquals( "No retorna la verdadera cabeza.", "" + 999, lista.getLast( ).getItem( ) );
	}

	@Test
	public void testGetPosition( )
	{
		assertEquals( "Debió retornar null.", null, lista.getPosition( 0 ) );
		assertEquals( "Debió retornar null.", null, lista.getPosition( 1 ) );
		assertEquals( "Debió retornar null.", null, lista.getPosition( 999 ) );
		assertEquals( "Debió retornar null.", null, lista.getPosition( 10000 ) );

		setUp2( );
		int i = 0;
		for( String s : lista )
		{
			assertEquals( "No retorna el string correcto.", "" + i, s );
			i++;
		}
	}
}