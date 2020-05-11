package model.data_structures;

import java.util.Iterator;

/**
 * Clase principal que permite manejar una lista enlazada de nodos.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class LinkedList<T> implements ILinkedList<T>
{
	/**
	 * Cabeza de la lista.
	 */
	private ListNode<T> head;

	/**
	 * �ltimo nodo de la lista.
	 */
	private ListNode<T> last;

	/**
	 * N�mero total de nodos en la lista.
	 */
	private int size;

	/**
	 * Inicializa la lista enlazada como vac�a.
	 */
	public LinkedList( )
	{
		head = null;
		last = null;
		size = 0;
	}

	/**
	 * Agrega un nuevo item al final de la lista enlazada.
	 * @param item Nuevo item.
	 */
	public void append( T item )
	{
		if( head == null )
		{
			head = new ListNode<T>( item );
			last = head;
		}
		else
		{
			last.setNext( new ListNode<T>( item ) );
			last = last.getNext( );
		}
		size++;
	}

	/**
	 * Agrega un nuevo item al inicio de la lista enlazada.
	 * @param item Nuevo item.
	 */
	public void addFirst( T item )
	{
		ListNode<T> nuevoNodo = new ListNode<>( item );
		nuevoNodo.setNext( head );
		head = nuevoNodo;
		size++;

		if( last == null )
			last = head;
	}

	/**
	 * Elimina la cabeza de la lista.
	 */
	public void removeFirst( )
	{
		if( head != null )
		{
			head = head.getNext( );
			size--;
		}
	}

	/**
	 * Elimina el nodo de la lista ubicado en la posici�n dada por par�metro.
	 * @param pos Posici�n del elemento a eliminar. pos < lista.getSize( ).
	 */
	public void remove( int pos )
	{
		int i = 0;
		ListNode<T> actual = head;
		while( i < pos - 1 )
		{
			actual = actual.getNext( );
			i++;
		}

		actual.setNext( actual.getNext( ).getNext( ) );
		size--;
	}

	/**
	 * @return Tama�o de la lista, i.e, n�mero total de nodos.
	 */
	public int getSize( )
	{
		return size;
	}

	/**
	 * @return True si la lista est� vac�a, false de lo contrario.
	 */
	public boolean isEmpty( )
	{
		return size == 0;
	}

	/**
	 * @return Cabeza de la lista de comparendos.
	 */
	public ListNode<T> getHead( )
	{
		return head;
	}

	/**
	 * @return �ltimo nodo de la lista.
	 */
	public ListNode<T> getLast( )
	{
		return last;
	}

	/**
	 * Obtiene el item en la posici�n dada por par�metro.
	 * @param pos Posici�n buscada. pos < lista.getSize( ).
	 * @return Item en la posici�n dada.
	 */
	public T getPosition( int pos )
	{
		int i = 0;
		ListNode<T> actual = head;
		while( i < pos && actual != null )
		{
			actual = actual.getNext( );
			i++;
		}
		if( actual != null )
			return actual.getItem( );
		else
			return null;
	}

	/**
	 * Iterador.
	 */
	public Iterator<T> iterator( )
	{
		return new Iterator<T>( )
		{
			int i = 0;
			private ListNode<T> actual = null;

			@Override
			public boolean hasNext( )
			{
				if( size == 1 )
					if( i == 0 )
						return true;
					else
						return false;
					
				return actual != last;
			}

			@Override
			public T next( )
			{
				if( size == 1 )
				{
					i++;
					return head.getItem( );
				}
				
				if( actual == null )
				{
					actual = head;
				}
				else
				{
					actual = actual.getNext( );
				}
				return actual.getItem( );
			}
		};
	}
}