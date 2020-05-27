package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación de una bolsa simple.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Bag<E> implements Iterable<E>
{
	/**
	 * Referencia al primer elemento de la bolsa.
	 */
	private ListNode<E> first;
	
	/**
	 * Número de elementos en la bolsa.
	 */
	private int n;

	/**
	 * Inicializa una bolsa vacía.
	 */
	public Bag( )
	{
		first = null;
		n = 0;
	}

	/**
	 * @return booleano que indica si la bolsa está vacía.
	 */
	public boolean isEmpty( )
	{
		return first == null;
	}

	/**
	 * @return Número de elmentos en al bolsa.
	 */
	public int size( )
	{
		return n;
	}

	/**
	 * Agrega el elemento al a bolsa.
	 * @param item el elementoa agregar.
	 */
	public void add( E item )
	{
		ListNode<E> oldfirst = first;
		first = new ListNode<E>( );
		first.setItem( item );
		first.setNext( oldfirst );
		n++;
	}

	/**
	 * Un iterador para la bolsa que la recorre en orden arbitratrio.
	 * @return iterador.
	 */
	public Iterator<E> iterator( )
	{
		return new LinkedIterator( first );
	}

	private class LinkedIterator implements Iterator<E>
	{
		private ListNode<E> current;

		public LinkedIterator( ListNode<E> first )
		{
			current = first;
		}

		public boolean hasNext( )
		{
			return current != null;
		}

		public void remove( )
		{
			throw new UnsupportedOperationException( );
		}

		public E next( )
		{
			if( !hasNext( ) )
				throw new NoSuchElementException( );
			
			E item = current.getItem( );
			current = current.getNext( );
			return item;
		}
	}
}