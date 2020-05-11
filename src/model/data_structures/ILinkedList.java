package model.data_structures;

/**
 * Interfaz que debe implementar la clase LinkedList.
 * @author Camilo Martínez & Nicolás Quintero
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
	 * Elimina el nodo de la lista ubicado en la posición dada por parámetro.
	 * @param pos Posición del elemento a eliminar. 0 < pos < size.
	 */
	public void remove( int pos );
	
	/**
	 * @return Tamaño de la lista, i.e, número total de nodos.
	 */
	public int getSize( );
	
	/**
	 * @return Item en la posición dada por parámetro.
	 */
	public E getPosition( int i );
	
	/**
	 * @return True si la lista está vacía, false de lo contrario.
	 */
	public boolean isEmpty( );
}