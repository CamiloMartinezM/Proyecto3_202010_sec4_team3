package model.data_structures;

import java.util.EmptyStackException;

/**
 * Interfaz para la clase que maneja la pila.
 * @author Camilo Martínez
 */
public interface IStack<T> extends Iterable<T>
{
	/**
	 * Agrega un nuevo item al inicio de la pila.
	 * @param item Nuevo item.
	 */
	public void push( T item );

	/**
	 * Elimina según la política LIFO y lo retorna.
	 * @throws EmptyStackException Si la pila está vacía.
	 * @return Elemento eliminado.
	 */
	public T pop( ) throws EmptyStackException;

	/**
	 * @return Tamaño de la pila, i.e, número total de nodos.
	 */
	public int getSize( );

	/**
	 * @return Cabeza de la pila.
	 */
	public ListNode<T> getTopStack( );
	
	/**
	 * @return True si está vacío, false de lo contrario.
	 */
	public boolean isEmpty( );
}