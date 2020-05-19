package model.data_structures;

/**
 * Clase que representa una entrada para la tabla de hash.
 * @author Camilo Martínez & Nicolás Quintero.
 */
public class HashNode<K extends Comparable<K>, V> implements Comparable<HashNode<K, V>>
{
	/**
	 * Referencia al siguiente nodo. Esta será la lista enlazada primaria.
	 */
	private HashNode<K, V> next = null;

	/**
	 * Referencia a una lista enlazada secundaria que contiene otros HashNodes con
	 * la misma llave.
	 */
	private LinkedList<V> list = new LinkedList<>( );
	
	/**
	 * Referencia a la llave.
	 */
	public K key;

	/**
	 * Hash.
	 */
	int hash;

	/**
	 * Inicializa un nodo vacío.
	 */
	public HashNode( )
	{
		this.key = null;
		this.hash = key.hashCode( );
	}

	/**
	 * Inicializa un nodo con la información de los parámetros. El valor es añadido
	 * a la lista secundaria.
	 * @param key   Llave del nodo.
	 * @param value Valor del nodo.
	 */
	public HashNode( K key, V value )
	{
		this.key = key;
		this.list.addFirst( value );
		this.hash = key.hashCode( );
	}

	/**
	 * Inicializa un nodo cuyo nodo posterior es aquél dado por parámetro. El valor
	 * es añadido a la lista secundaria.
	 * @param key   Llave del nodo.
	 * @param value Valor del nodo.
	 * @param next  Nodo posterior del nodo.
	 */
	public HashNode( K key, V value, HashNode<K, V> next )
	{
		this.next = next;
		this.key = key;
		this.list.addFirst( value );
		this.hash = key.hashCode( );
	}

	/**
	 * @return Siguiente nodo en la lista enlazada principal.
	 */
	public HashNode<K, V> getNext( )
	{
		return next;
	}

	/**
	 * @return Lista enlazada secundaria.
	 */
	public LinkedList<V> getListOfItems( )
	{
		return list;
	}

	/**
	 * Cambia el nodo siguiente actual de la lista enlazada primaria por uno nuevo
	 * dado por parámetro.
	 * @param nextSecondary Siguiente nodo en la lista enlazada primaria.
	 */
	public void setNext( HashNode<K, V> next )
	{
		this.next = next;
	}

	/**
	 * Añade un nuevo valor a la lista secundaria.
	 * @param value Valor nuevo.
	 */
	public void add( V value )
	{
		list.append( value );
	}
	
	/**
	 * @return Valor del primer elemento en la lista.
	 */
	public V getValue( )
	{
		return list.getHead( ).getItem( );
	}

	/**
	 * Remplaza el valor inicial de la lista secundaria por otro dado por parámetro.
	 * @param value Nuevo valor.
	 */
	public void replaceValue( V value )
	{
		list.removeFirst( );
		list.addFirst( value );
	}
	
	/**
	 * @return Tamaño de la lista enlazada secundaria. 
	 */
	public int getSize( )
	{
		return list.getSize( );
	}

	@Override
	public int compareTo( HashNode<K, V> o )
	{
		return this.key.compareTo( o.key );
	}
}