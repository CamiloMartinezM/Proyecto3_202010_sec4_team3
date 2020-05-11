package model.data_structures;

/**
 * Clase que contiene la información de un comparendo.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class ListNode<E>
{
	/**
	 * Referencia al siguiente nodo.
	 */
	private ListNode<E> next;

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
}