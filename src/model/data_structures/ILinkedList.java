package model.data_structures;

/**
 * Interfaz que debe implementar la clase LinkedList.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public interface ILinkedList<E> extends Iterable<E>
{
	/**
	 * Agrega un nuevo item al inicio de la lista enlazada.
	 * @param item Nuevo item.
	 */
	public void addFirst( E item );

	/**
	 * Agrega un nuevo item al final de la lista enlazada.
	 * @param item Nuevo item.
	 */
	public void append( E item );

	/**
	 * Elimina la cabeza de la lista.
	 */
	public void removeFirst( );
	
	/**
	 * Elimina el nodo de la lista ubicado en la posici�n dada por par�metro.
	 * @param pos Posici�n del elemento a eliminar. 0 < pos < size.
	 */
	public void remove( int pos );
	
	/**
	 * @return Tama�o de la lista, i.e, n�mero total de nodos.
	 */
	public int getSize( );
	
	/**
	 * @return Item en la posici�n dada por par�metro.
	 */
	public E getPosition( int i );
	
	/**
	 * @return True si la lista est� vac�a, false de lo contrario.
	 */
	public boolean isEmpty( );
}