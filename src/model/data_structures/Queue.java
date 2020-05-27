package model.data_structures;

import java.util.Iterator;

/**
 * Clase que implementa el modelo de cola para el manejo de datos.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Queue<T> implements IQueue<T>
{
	/**
	 * Primer nodo de la cola.
	 */
	private ListNode<T> primero;

	/**
	 * Último nodo de la cola.
	 */
	private ListNode<T> ultimo;

	/**
	 * Tamaño de la cola.
	 */
	private int tamanio = 0;

	/**
	 * Agrega el elemento dado por parámetro al final de la cola.
	 * @param elem Elemento a agregar de tipo T.
	 */
	public void enQueue( T elem )
	{
		ListNode<T> nuevoNodo = new ListNode<>( elem );
		if( tamanio == 0 )
		{
			primero = nuevoNodo;
			ultimo = nuevoNodo;
		}
		else
		{
			ListNode<T> antiguoUltimo = ultimo;
			antiguoUltimo.setNext( nuevoNodo );
			ultimo = nuevoNodo;
		}
		tamanio++;
	}

	/**
	 * Elimina y devuelve el primer elemento de la cola.
	 * @return Primer elemento de la cola o null si ésta está vacia.
	 * @throws IllegalStateException Si la lista está vacia.
	 */
	public T deQueue( ) throws IllegalStateException
	{
		if( tamanio == 0 ) throw new IllegalStateException( "La cola está vacía." );

		ListNode<T> antiguoPrimero = primero;
		T elem = primero.getItem( );
		primero = antiguoPrimero.getNext( );
		antiguoPrimero.setNext( null );
		tamanio--;
		return elem;
	}

	/**
	 * @return Tamaño de la cola.
	 */
	public int getSize( )
	{
		return tamanio;
	}

	/**
	 * @return Primer nodo de la cola.
	 */
	public ListNode<T> getFirst( )
	{
		return primero;
	}

	/**
	 * @return Último nodo de la cola.
	 */
	public ListNode<T> getLast( )
	{
		return ultimo;
	}
	
	/**
	 * @return True si está vacía, false de lo contrario.
	 */
	public boolean isEmpty( )
	{
		return tamanio == 0;
	}

	@Override
	public Iterator<T> iterator( )
	{
		return new Iterator<T>( )
		{
			@Override
			public boolean hasNext( )
			{
				return !isEmpty( );
			}

			@Override
			public T next( )
			{
				return deQueue( );
			}
		};
	}
}