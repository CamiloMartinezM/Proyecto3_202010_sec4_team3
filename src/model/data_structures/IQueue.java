package model.data_structures;

/**
 * Interfaz que debe implementar una implementaci�n de cola.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public interface IQueue<T> extends Iterable<T>
{
	/**
	 * Agrega el elemento dado por par�metro al final de la cola.
	 * @param elem Elemento a agregar de tipo T.
	 */
	public void enQueue( T elem );

	/**
	 * Elimina y devuelve el primer elemento de la cola.
	 * @return Primer elemento de la cola o null si �sta est� vacia.
	 * @throws IllegalStateException Si la lista est� vacia.
	 */
	public T deQueue( ) throws IllegalStateException;

	/**
	 * @return Primer nodo de la cola.
	 */
	public ListNode<T> getFirst( );

	/**
	 * @return �ltimo nodo de la cola.
	 */
	public ListNode<T> getLast( );

	/**
	 * @return Tama�o de la cola.
	 */
	public int getSize( );
	
	/**
	 * @return True si est� vac�a, false de lo contrario.
	 */
	public boolean isEmpty( );
}