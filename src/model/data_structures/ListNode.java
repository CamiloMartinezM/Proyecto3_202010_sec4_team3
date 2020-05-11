package model.data_structures;

/**
 * Clase que contiene la informaci�n de un comparendo.
 * @author Camilo Mart�nez & Nicol�s Quintero
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
	 * Inicializa un nodo vac�o.
	 */
	public ListNode( )
	{
		this.next = null;
		this.item = null;
	}

	/**
	 * Inicializa un nodo con la informaci�n dada por par�metro.
	 * @param item Item que ser� contenido dentro del nodo.
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
	 * Cambia el nodo siguiente actual por uno nuevo dado por par�metro.
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
	 * Cambia el item actual por uno nuevo dado por par�metro.
	 * @param item Nuevo item.
	 */
	public void setItem( E item )
	{
		this.item = item;
	}
}