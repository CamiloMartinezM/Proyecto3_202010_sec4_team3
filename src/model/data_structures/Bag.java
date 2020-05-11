package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item>
{
	private Node<Item> first;
	private int n;

	// helper linked list class
	private static class Node<Item>
	{
		private Item item;
		private Node<Item> next;
	}

	/**
	 * Inicializa una bolsa vacia.
	 */
	public Bag( )
	{
		first = null;
		n = 0;
	}

	/**
	 * Retorna verdadero si la bolsa esta vacia.
	 * @return booleano que indica si la bolsa esta bacia;
	 */
	public boolean isEmpty( )
	{
		return first == null;
	}

	/**
	 * retorna numero de elementos en la bolsa.
	 * @return numero de elmentos en al bolsa.
	 */
	public int size( )
	{
		return n;
	}

	/**
	 * Agrega el elemento al a bolsa.
	 * @param el elementoa agregar
	 */
	public void add( Item item )
	{
		Node<Item> oldfirst = first;
		first = new Node<Item>( );
		first.item = item;
		first.next = oldfirst;
		n++;
	}

	/**
	 * Un iterador para la bolsa que la recorre en orden arbitratrio.
	 * @return iterador
	 */
	public Iterator<Item> iterator( )
	{
		return new LinkedIterator( first );
	}

	private class LinkedIterator implements Iterator<Item>
	{
		private Node<Item> current;

		public LinkedIterator( Node<Item> first )
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

		public Item next( )
		{
			if( !hasNext( ) )
				throw new NoSuchElementException( );
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

}
