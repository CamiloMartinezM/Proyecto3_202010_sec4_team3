package test.data_structures;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.HashTable;

/**
 * Test de la implementación de una tabla de hash con encadenamiento separado
 * con una lista encadenada.
 * @author Camilo Martínez
 */
public class TestHashTable
{
	/**
	 * Tabla de hash.
	 */
	private HashTable<String, Integer> table;

	/**
	 * Arreglo de strings que contiene las llaves y valores ingresados. Se usan para
	 * el test del iterador.
	 */
	private ArrayList<String> insertedKeys;
	private ArrayList<Integer> insertedValues;

	/**
	 * Capacidad inicial de la tabla / Número de keys a ingresar.
	 */
	private final static int N = 1000;

	/**
	 * Escenario 1: Inicializa una tabla de hash de encadenamiento separado vacía.
	 */
	@Before
	public void setUp1( )
	{
		table = new HashTable<>( 97 );
	}

	/**
	 * Escenario 2: Llena la tabla con pares llave-valor.
	 */
	public void setUp2( )
	{
		insertedKeys = new ArrayList<String>( );
		insertedValues = new ArrayList<Integer>( );

		for( int i = N - 1; i >= 0; i-- )
		{
			String key = "S" + i;
			Integer value = i;
			table.put( key, value );
			insertedKeys.add( key );
			insertedValues.add( value );
		}
	}

	@Test
	public void TestGetSize( )
	{
		assertEquals( "No retornó el tamaño esperado.", 0, table.getSize( ) );

		setUp2( );

		assertEquals( "No retornó el tamaño esperado.", N, table.getSize( ) );
	}

	@Test
	public void TestIsEmpty( )
	{
		assertEquals( "Falló en saber si estaba vacía o no la tabla.", true, table.isEmpty( ) );

		setUp2( );

		assertEquals( "Falló en saber si estaba vacía o no la tabla.", false, table.isEmpty( ) );
	}

	@Test
	public void TestContains( )
	{
		assertEquals( "Falló el contains.", false, table.contains( "S" + 0 ) );
		assertEquals( "Falló el contains.", false, table.contains( "S" + 1 ) );

		setUp2( );

		int i;
		for( i = 0; i < N; i++ )
			assertEquals( "Falló el contains.", true, table.contains( "S" + i ) );

		assertEquals( "Falló el contains.", false, table.contains( "S" + i ) );
	}

	@Test
	public void TestDelete( )
	{
		assertEquals( "Falló el delete.", null, table.delete( "S" + 0 ) );
		assertEquals( "Falló el delete.", null, table.delete( "S" + 1 ) );

		setUp2( );

		int i;
		for( i = 0; i < N; i++ )
			assertEquals( "Falló el delete.", i, table.delete( "S" + i ).intValue( ) );

		assertEquals( "Falló el delete.", null, table.delete( "S" + i ) );
		assertEquals( "Falló el delete.", 0, table.getSize( ) );
	}

	@Test
	public void TestGet( )
	{
		assertEquals( "Falló el get.", null, table.get( "S" + 0 ) );
		assertEquals( "Falló el get.", null, table.get( "S" + 1 ) );

		setUp2( );

		for( int i = 0; i < N; i++ )
			assertEquals( "Falló el get.", i, table.get( "S" + i ).intValue( ) );
	}

	@Test
	public void TestPut( )
	{
		table.put( "Hola", 10 );
		assertEquals( "Falló el put.", 10, table.get( "Hola" ).intValue( ) );
		table.put( "Hola2", 11 );
		assertEquals( "Falló el put.", 11, table.get( "Hola2" ).intValue( ) );
		table.put( "Hola3", 13 );
		assertEquals( "Falló el put.", 13, table.get( "Hola3" ).intValue( ) );

		assertEquals( "Falló el get.", null, table.get( "S" + 1 ) );

		setUp1( );
		setUp2( );

		for( int i = 0; i < N; i++ )
			assertEquals( "Falló el get.", i, table.get( "S" + i ).intValue( ) );
	}

	@Test
	public void TestIterator( )
	{
		setUp2( );

		Iterator<String> iter = table.keys( );
		while( iter.hasNext( ) )
		{
			String key = iter.next( );
			assertEquals( "Falló el iterador.", true, insertedKeys.contains( key ) );
			assertEquals( "Falló el iterador.", true, insertedValues.contains( table.get( key ) ) );

			assertEquals( "Falló el iterador.", true, insertedKeys.remove( key ) );
			assertEquals( "Falló el iterador.", true, insertedValues.remove( table.get( key ) ) );
		}

		assertEquals( "El arreglo insertedKeys debe quedar vacío.", 0, insertedKeys.size( ) );
		assertEquals( "El arreglo insertedValues debe quedar vacío.", 0, insertedValues.size( ) );
	}
}