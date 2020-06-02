package model.data_structures;

/**
 * Clase que contiene la información de un comparendo.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class ListNode<E> implements Comparable<ListNode<E>>
{
	/**
	 * Referencia al siguiente nodo.
	 */
	private ListNode<E> next;

	/**
	 * ID del nodo. En caso de no usarse, es 0.
	 */
	public int id;

	/**
	 * Valor double guardado en el nodo. En caso de no usarse, es 0.
	 */
	public double value;

	/**
	 * Referencia al item contenido dentro del Nodo.
	 */
	private E item;

	/**
	 * Inicializa un nodo vacío.
	 */
	public ListNode( )
	{
		this.next = null;
		this.item = null;
		this.value = 0;
		this.id = 0;
	}

	/**
	 * Inicializa un nodo con la información dada por parámetro.
	 * @param item Item que será contenido dentro del nodo.
	 */
	public ListNode( E item )
	{
		this.next = null;
		this.item = item;
	}

	/**
	 * Inicializa un nodo vacío con un ID y valor dado.
	 */
	public ListNode( int id, double value )
	{
		this.next = null;
		this.item = null;
		this.value = value;
		this.id = id;
	}

	/**
	 * @return Siguiente nodo.
	 */
	public ListNode<E> getNext( )
	{
		return next;
	}

	/**
	 * Cambia el nodo siguiente actual por uno nuevo dado por parámetro.
	 * @param siguiente Siguiente nodo.
	 */
	public void setNext( ListNode<E> next )
	{
		this.next = next;
	}

	/**
	 * @return Item contenido dentro del nodo.
	 */
	public E getItem( )
	{
		return item;
	}

	/**
	 * Cambia el item actual por uno nuevo dado por parámetro.
	 * @param item Nuevo item.
	 */
	public void setItem( E item )
	{
		this.item = item;
	}

	@Override
	public int compareTo( ListNode<E> o )
	{
		if( Math.abs( this.value - o.value ) < 1e-6 )
			return 0;
		
		return ( this.value - o.value ) > 0 ? +1 : -1;
	}
}