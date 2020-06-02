package model.data_structures;

import java.util.Iterator;

/**
 * Vértice de un grafo.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Vertex<K extends Comparable<K>, V extends Comparable<V>, E extends Comparable<E>>
		implements Comparable<Vertex<K, V, E>>
{
	/**
	 * ID asociado al vértice.
	 */
	private int id;

	/**
	 * Información del vértice.
	 */
	private String info;

	/**
	 * Items contenidos dentro del vértice.
	 */
	private HashTable<K, V> items = new HashTable<>( );

	/**
	 * Item distintivo del vértice.
	 */
	private E distinctiveItem;
     
	private int weight;
	/**
	 * Inicializa un vértice con el ID dado por parámetro.
	 * @param id ID del vértice. 0 <= id
	 */
	public Vertex( int id )
	{
		this.id = id;
		this.distinctiveItem = null;
	}

	/**
	 * Inicializa un vértice con el ID dado por parámetro y con el item distintivo
	 * dado.
	 * @param id              ID del vértice. 0 <= id
	 * @param distinctiveItem Item distintivo del vértice. distinctiveItem != null
	 */
	public Vertex( int id, E distinctiveItem )
	{
		this.id = id;
		this.distinctiveItem = distinctiveItem;
	}

	/**
	 * @return ID del vértice.
	 */
	public int getId( )
	{
		return id;
	}

	/**
	 * Inserta el par llave-valor dados por parámetro en la tabla de items del
	 * vértice.
	 * @param key  Llave del par. key != null
	 * @param item Item del par. item != null
	 */
	public void insertItem( K key, V item )
	{
		items.put( key, item );
	}

	/**
	 * @param key Llave a obtener su valor.
	 * @return Valor asociado a la llave.
	 */
	public V getItem( K key )
	{
		return items.get( key );
	}

	/**
	 * @return Item distintivo del nodo.
	 */
	public E getDistinctiveItem( )
	{
		return distinctiveItem;
	}

	/**
	 * Cambia el item distintivo del nodo por uno dado por parámetro.
	 * @param distinctiveItem Nuevo item distintivo.
	 */
	public void setDistinctiveItem( E distinctiveItem )
	{
		this.distinctiveItem = distinctiveItem;
	}

	/**
	 * Asigna la información del vértice al valor dado por parámetro.
	 * @param info Información del vértice. info != null, != ""
	 */
	public void setInfo( String info )
	{
		this.info = info;
	}

	/**
	 * @return Información del vértice.
	 */
	public String getInfo( )
	{
		return info;
	}

	/**
	 * @return Número de items guardados en el vértice.
	 */
	public int numberOfItems( )
	{
		return items.getSize( );
	}
	
	/**
	 * @return Iterador sobre todos los items guardados en el vértice.
	 */
	public Iterator<V> items( )
	{
		return new Iterator<V>( )
		{
			private Iterator<K> iter = items.keys( );

			@Override
			public boolean hasNext( )
			{
				return iter.hasNext( );
			}

			@Override
			public V next( )
			{
				return items.get( iter.next( ) );
			}
		};
	}

	@Override
	public int compareTo( Vertex<K, V, E> o )
	{
		if( this.numberOfItems( ) < o.numberOfItems( ) )
			return -1;
		else if( this.numberOfItems( ) > o.numberOfItems( ) )
			return 1;
		else
			return 0;
	}
}