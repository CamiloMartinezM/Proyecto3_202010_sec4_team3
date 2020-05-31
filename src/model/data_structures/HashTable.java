package model.data_structures;

import java.util.Collections;
import java.util.Iterator;

/**
 * Implementación de una tabla de hash con encadenamiento separado con una lista
 * encadenada.
 * @author Camilo Martínez & Nicolás Quintero
 */
@SuppressWarnings( "unchecked" )
public class HashTable<K extends Comparable<K>, V extends Comparable<V>> implements IHashTable<K, V>
{
	/**
	 * Capacidad predeterminada del arreglo.
	 */
	public static final int M = 7;

	/**
	 * Factor de carga (size/capacity) máximo.
	 */
	private static final double MAXIMUM_LOAD_FACTOR = 5;

	/**
	 * Arreglo de cadenas.
	 */
	private HashNode<K, V>[] st;

	/**
	 * Número de posiciones ocupadas en el arreglo por al menos una llave, capacidad
	 * real del arreglo, tamaño, número de rehashes hechos y factor de carga actual.
	 */
	private int height, capacity, size, numOfRehashes = 0;
	private double loadFactor = 0.0;

	/**
	 * Contienen la llave menor y mayor respectivamente.
	 */
	private K minKey, maxKey;

	/**
	 * Indica si remplazar el valor de una llave ya existente o no.
	 */
	private boolean replaceValue;

	/**
	 * Inicializa una tabla de hash de encadenamiento separado de capacidad
	 * predeterminada.
	 */
	public HashTable( )
	{
		this( M, false );
	}

	/**
	 * Inicializa una tabla de hash de encadenamiento separado de capacidad
	 * predeterminada y permite decidir si remplazar o no valores con llave igual.
	 */
	public HashTable( boolean replaceValue )
	{
		this( M, replaceValue );
	}

	/**
	 * Inicializa una tabla de hash de encadenamiento separado de capacidad
	 * dada por parámetro.
	 * @param capacity Capacidad deseada.
	 */
	public HashTable( int capacity )
	{
		this( capacity, false );
	}

	/**
	 * Inicializa una tabla de hash de encadenamiento separado.
	 * @param capacity     Capacidad inicial que se quiere de la tabla. capacity > 0
	 * @param replaceValue True si se remplaza el valor anterior cuando la llave ya
	 *                     existe, false de lo contrario.
	 */
	public HashTable( int capacity, boolean replaceValue )
	{
		this.capacity = Math.max( M, capacity );
		st = ( HashNode<K, V>[] ) new HashNode[this.capacity];
		minKey = null;
		maxKey = null;
		this.replaceValue = replaceValue;
	}

	/**
	 * @return Tamaño de la tabla de hash.
	 */
	public int getSize( )
	{
		return size;
	}

	/**
	 * @return True si la tabla está vacía, false de lo contrario.
	 */
	public boolean isEmpty( )
	{
		return size == 0;
	}

	/**
	 * @return Factor de carga (N/M).
	 */
	public double getLoadFactor( )
	{
		return loadFactor;
	}

	/**
	 * Actualiza el valor del factor de carga actual de la tabla de hash.
	 */
	private void updateLoadFactor( )
	{
		loadFactor = ( double ) size / capacity;
	}

	/**
	 * Convierte un valor de hash de la llave dada por un índice.
	 * @param key LLave dada. key != null
	 * @return Índice correspondiente a la llave dada por parámetro.
	 */
	private int hash( K key )
	{
		return ( key.hashCode( ) & 0x7fffffff ) % capacity;
	}

	/**
	 * Obtiene el valor asociado a la llave dada por parámetro. En caso que el nodo
	 * contenedor tenga varios valores (la lista secundaria tiene tamaño mayor a 1),
	 * este retorna el primer valor.
	 * @param key Llave a saber su valor. key != null
	 * @return Valor de la llave, null si no se encuentra. value != null
	 */
	public V get( K key )
	{
		HashNode<K, V> x = containingNode( key );
		if( x != null )
			return x.getValue( );
		return null;
	}

	/**
	 * Inserta un nuevo par llave-valor en la lista. Si el factor de carga
	 * resultante es mayor al máximo establecido, se hace el rehash a la tabla.
	 * @param key   Llave del nodo a insertar. key != null
	 * @param value Valor del nodo a insertar. value != null
	 */
	public void put( K key, V value ) throws IllegalStateException
	{
		insert( key, value );

		updateLoadFactor( );

		if( loadFactor > MAXIMUM_LOAD_FACTOR )
		{
			rehash( );
			updateLoadFactor( );
		}
	}

	/**
	 * Pone un nuevo par llave-valor en la lista de la posición correspondiente. Si
	 * no se encuentra una llave existente en la lista, se inserta el nuevo nodo; si
	 * sí, se inserta en la lista enlazada secundaria de dicho nodo.
	 * @param key   Llave del nodo a insertar. key != null
	 * @param value Valor del nodo a insertar. value != null
	 */
	private void insert( K key, V value )
	{
		HashNode<K, V> x = containingNode( key );

		// Ya hay un nodo con dicha llave asociada.
		if( x != null )
			if( !replaceValue ) // Se añade a la lista secundaria.
				x.add( value );
			else // Se remplaza el valor actual.
			{
				x.replaceValue( value );
				return; // Se termina aquí el método pues si se remplaza el valor, no aumentó el número
						// de pares llave-valor.
			}

		// No hay un nodo con dicha llave asociada, por lo cual se crea y se ajusta el
		// atributo height.
		else
		{
			if( maxKey == null || key.compareTo( maxKey ) > 0 )
				maxKey = key;
			else if( minKey == null || key.compareTo( minKey ) < 0 )
				minKey = key;

			int i = hash( key );

			if( st[i] == null )
				height++;	// Se aumenta el número de posiciones ocupadas en la tabla o arreglo st.

			st[i] = new HashNode<K, V>( key, value, st[i] );
		}

		size++; // Se aumenta el número de pares llave-valor existentes en la tabla de hash.
	}

	/**
	 * @param key Llave a conocer el nodo que la contiene. key != null
	 * @return Nodo contenedor de la llave dada por parámetro, null si no se
	 *         encuentra.
	 */
	public HashNode<K, V> containingNode( K key )
	{
		int i = hash( key );
		for( HashNode<K, V> x = st[i]; x != null; x = x.getNext( ) )
			if( key.equals( x.key ) )
				return x;

		return null;
	}

	/**
	 * @param key Llave a saber si la tabla de hash la contiene. key != null
	 * @return True si la tabla de hash la contiene, false de lo contrario.
	 */
	public boolean contains( K key )
	{
		return containingNode( key ) != null;
	}

	/**
	 * Elimina el nodo que contiene la llave dada por parámetro.
	 * @param key Llave cuyo nodo se quiere eliminar. key != null
	 * @return value Valor asociado a la llave eliminada.
	 */
	public V delete( K key )
	{
		HashNode<K, V> x = containingNode( key );

		if( x != null )
		{
			int i = hash( key );
			if( st[i] == x )
			{
				// st[i] ahora apunta al siguiente del primero
				st[i] = x.getNext( );

				// Libera memoria.
				x.setNext( null );

				// El número de espacios ocupados en la tabla disminuye o el número de
				// posiciones en el arreglo ocupados por al menos una llave, si st[i] queda
				// apuntando a null.
				if( st[i] == null )
					height--;
			}
			else
			{
				HashNode<K, V> prev = null;
				for( HashNode<K, V> y = st[i]; y != null; y = y.getNext( ) )
				{
					if( x == y )
					{
						// Se elimina el nodo x.
						prev.setNext( x.getNext( ) );
						break;
					}
					prev = y;
				}
			}

			// Se disminuye la cantidad de pares llave-valor presentes en la tabla.
			size--;

			return x.getValue( );
		}
		return null;
	}
	
	/**
	 * Reinicializa la tabla de hash. <b>post:</b> Todos los pares llave-valor
	 * previamente en la tabla son eliminados. Los atributos size y height son
	 * iniciados en 0.
	 */
	private void clear( )
	{
		st = ( HashNode<K, V>[] ) new HashNode[capacity];
		size = 0;
		height = 0;
	}

	/**
	 * Hace el rehash a la tabla. <b>post:</b> La tabla queda poblada nuevamente con
	 * sus pares llave-valor y el atributo numOfRehashes aumenta en 1.
	 */
	private void rehash( )
	{
		numOfRehashes++;
		capacity *= 2;				// Se duplica la capacidad de la tabla.
		HashNode<K, V>[] temp = st; // Guarda los pares llave-valor.
		clear( );					// Reinicializa la tabla.

		for( int i = 0; i < temp.length; i++ )
			for( HashNode<K, V> x = temp[i]; x != null; x = x.getNext( ) )
				// Se insertan los nodos dentro de la lista enlazada secundaria del nodo.
				for( V y : x.getListOfItems( ) )
					insert( x.key, y );
	}

	/**
	 * @return Número de veces que se le ha hecho rehash a la tabla de hash desde el
	 *         momento en que fue creada.
	 */
	public int getNumberOfRehashesMade( )
	{
		return numOfRehashes;
	}

	/**
	 * @return Capacidad actual de la tabla de hash.
	 */
	public int getCapacity( )
	{
		return capacity;
	}

	/**
	 * @return Mínima llave en la tabla.
	 */
	public K getMinKey( )
	{
		return minKey;
	}

	/**
	 * @return Máxima llave en la tabla.
	 */
	public K getMaxKey( )
	{
		return maxKey;
	}

	/**
	 * @return Mínimo valor en la tabla.
	 */
	public V getMinValue( )
	{
		V minValue = null;
		Iterator<K> iter = keys( );
		while( iter.hasNext( ) )
		{
			K key = iter.next( );
			Iterator<V> iter2 = valuesOf( key );
			while( iter2.hasNext( ) )
			{
				V value = iter2.next( );
				if( minValue == null || value.compareTo( minValue ) < 0 )
					minValue = value;
			}
		}

		return minValue;
	}

	/**
	 * @return Máximo valor en la tabla.
	 */
	public V getMaxValue( )
	{
		V maxValue = null;
		Iterator<K> iter = keys( );
		while( iter.hasNext( ) )
		{
			K key = iter.next( );
			Iterator<V> iter2 = valuesOf( key );
			while( iter2.hasNext( ) )
			{
				V value = iter2.next( );
				if( maxValue == null || value.compareTo( maxValue ) > 0 )
					maxValue = value;
			}
		}

		return maxValue;
	}

	/**
	 * @return Iterador sobre todas las llaves de la tabla de hash.
	 */
	public Iterator<K> keys( )
	{
		return new Iterator<K>( )
		{
			int iActual = 0;
			int hActual = 0;
			private HashNode<K, V> actual = null;

			@Override
			public boolean hasNext( )
			{
				return iActual < capacity && hActual < height;
			}

			@Override
			public K next( )
			{
				if( actual != null && actual.getNext( ) != null )
					actual = actual.getNext( );
				else
					while( hActual < height )
					{
						if( st[iActual] != null )
						{
							actual = st[iActual];
							iActual++;
							hActual++;
							break;
						}
						iActual++;
					}

				return actual.key;
			}
		};
	}

	/**
	 * @param key Llave cuyos valores serán iterados.
	 * @return Iterador sobre todos los valores de una cierta llave.
	 */
	public Iterator<V> valuesOf( K key )
	{
		if( !contains( key ) ) // Si la llave no existe, se retorna un iterador vacío.
			return Collections.emptyIterator( );

		return containingNode( key ).getListOfItems( ).iterator( );
	}

	@Override
	public Iterator<K> iterator( )
	{
		return keys( );
	}
}