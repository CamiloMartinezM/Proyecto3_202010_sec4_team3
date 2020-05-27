package model.data_structures;

/**
 * Interfaz que debe implementar una implementación de cola.
 * @author Camilo Martínez & Nicolás Quintero
 */
public interface IQueue<T> extends Iterable<T>
{
	/**
	 * Agrega el elemento dado por parámetro al final de la cola.
	 * @param elem Elemento a agregar de tipo T.
	 */
	public void enQueue( T elem );

	/**
	 * Elimina y devuelve el primer elemento de la cola.
	 * @return Primer elemento de la cola o null si ésta está vacia.
	 * @throws IllegalStateException Si la lista está vacia.
	 */
	public T deQueue( ) throws IllegalStateException;

	/**
	 * @return Primer nodo de la cola.
	 */
	public ListNode<T> getFirst( );

	/**
	 * @return Último nodo de la cola.
	 */
	public ListNode<T> getLast( );

	/**
	 * @return Tamaño de la cola.
	 */
	public int getSize( );
	
	/**
	 * @return True si está vacía, false de lo contrario.
	 */
	public boolean isEmpty( );
}