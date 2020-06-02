package model.data_structures;

import java.util.Iterator;

/**
 * V�rtice de un grafo.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class Vertex<K extends Comparable<K>, V extends Comparable<V>, E extends Comparable<E>>
		implements Comparable<Vertex<K, V, E>>
{
	/**
	 * ID asociado al v�rtice.
	 */
	private int id;

	/**
	 * Informaci�n del v�rtice.
	 */
	private String info;

	/**
	 * Items contenidos dentro del v�rtice.
	 */
	private HashTable<K, V> items = new HashTable<>( );

	/**
	 * Item distintivo del v�rtice.
	 */
	private E distinctiveItem;
     
	private int weight;
	/**
	 * Inicializa un v�rtice con el ID dado por par�metro.
	 * @param id ID del v�rtice. 0 <= id
	 */
	public Vertex( int id )
	{
		this.id = id;
		this.distinctiveItem = null;
	}

	/**
	 * Inicializa un v�rtice con el ID dado por par�metro y con el item distintivo
	 * dado.
	 * @param id              ID del v�rtice. 0 <= id
	 * @param distinctiveItem Item distintivo del v�rtice. distinctiveItem != null
	 */
	public Vertex( int id, E distinctiveItem )
	{
		this.id = id;
		this.distinctiveItem = distinctiveItem;
	}

	/**
	 * @return ID del v�rtice.
	 */
	public int getId( )
	{
		return id;
	}

	/**
	 * Inserta el par llave-valor dados por par�metro en la tabla de items del
	 * v�rtice.
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
	 * Cambia el item distintivo del nodo por uno dado por par�metro.
	 * @param distinctiveItem Nuevo item distintivo.
	 */
	public void setDistinctiveItem( E distinctiveItem )
	{
		this.distinctiveItem = distinctiveItem;
	}

	/**
	 * Asigna la informaci�n del v�rtice al valor dado por par�metro.
	 * @param info Informaci�n del v�rtice. info != null, != ""
	 */
	public void setInfo( String info )
	{
		this.info = info;
	}

	/**
	 * @return Informaci�n del v�rtice.
	 */
	public String getInfo( )
	{
		return info;
	}

	/**
	 * @return N�mero de items guardados en el v�rtice.
	 */
	public int numberOfItems( )
	{
		return items.getSize( );
	}
	
	/**
	 * @return Iterador sobre todos los items guardados en el v�rtice.
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