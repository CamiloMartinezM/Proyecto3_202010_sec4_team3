package model.data_structures;

import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * Clase que implementa el modelo de pila para el manejo de datos.
 * @author Camilo Martínez
 */
public class Stack<T> implements IStack<T>
{
	/**
	 * Referencia al tope de la pila.
	 */
	private ListNode<T> topStack;

	/**
	 * Número total de nodos en la pila.
	 */
	private int size;

	/**
	 * Inicializa la pila como vacía.
	 */
	public Stack( )
	{
		topStack = null;
		size = 0;
	}

	/**
	 * Agrega un nuevo item al inicio de la pila.
	 * @param item Nuevo item.
	 */
	public void push( T item )
	{
		ListNode<T> newNode = new ListNode<>( item );
		if( topStack == null )
			topStack = newNode;
		else
		{
			newNode.setNext( topStack );
			topStack = newNode;
		}
		size++;
	}

	/**
	 * Elimina según la política LIFO y lo retorna.
	 * @throws EmptyStackException Si la pila está vacía.
	 * @return Elemento eliminado, i.e, el item asociado al nodo eliminado.
	 */
	public T pop( ) throws EmptyStackException
	{
		if( topStack == null ) throw new EmptyStackException( );

		T item = topStack.getItem( );
		ListNode<T> nextTop = topStack.getNext( );
		topStack.setNext( null );
		topStack = nextTop;
		size--;
		return item;
	}

	/**
	 * @return Tamaño de la pila, i.e, número total de nodos.
	 */
	public int getSize( )
	{
		return size;
	}

	/**
	 * @return Cabeza de la pila.
	 */
	public ListNode<T> getTopStack( )
	{
		return topStack;
	}
	
	/**
	 * @return True si está vacía, false de lo contrario.
	 */
	public boolean isEmpty( )
	{
		return size == 0;
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
				return pop( );
			}
		};
	}
}