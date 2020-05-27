package model.data_structures;

/**
 * Interfaz que debe implementar la clase MaxHeapPQ.
 * @author Camilo Mart�nez & Nicol�s Quintero.
 */
public interface IPriorityHeap<T extends Comparable<T>>
{
	/**
	 * @return True si el heap est� vac�o, false de lo contrario.
	 */
	public boolean isEmpty( );

	/**
	 * @return True si la capacidad del heap ha sido superada, false de lo
	 *         contrario.
	 */
	public boolean isFull( );

	/**
	 * Vac�a el heap.
	 */
	public void clear( );

	/**
	 * @return Tama�o del heap.
	 */
	public int getSize( );

	/**
	 * Obtiene el elemento m�ximo de la cola, sin sacarlo; null en caso de cola
	 * @return M�ximo elemento del heap.
	 */
	public T peek( );

	/**
	 * Elimina la ra�z del heap.
	 * @return Ra�z del heap eliminada.
	 */
	public T poll( );

	/**
	 * Comprueba si un elemento est� dentro del heap.
	 * @param elem Elemento a comprobar su existencia.
	 * @return True si s� existe, false de lo contrario.
	 */
	public boolean contains( T elem );

	/**
	 * A�ade un elemento al heap, el cual no debe ser null.
	 * @param elem Elemento a agregar.
	 */
	public void insert( T elem );

	/**
	 * Elimina un elemento particular del heap.
	 * @param element Elemento a eliminar.
	 * @return True si se pudo eliminar, false de lo contrario.
	 */
	public boolean remove( T element );

	/**
	 * @param pos Posici�n buscada. pos < size.
	 * @return Elemento en la posici�n dada por par�metro.
	 */
	public T peekPosition( int i );

	/**
	 * Elimina un elemento cuya posici�n es aquella dada por par�metro.
	 * @param i Posici�n del elemento a eliminar.
	 */
	abstract T removeAt( int i );

	/**
	 * Comprueba si el elemento en la posici�n i es menor que aqu�l en la posici�n
	 * j.
	 * @param i Posici�n 1.
	 * @param j Posici�n 2.
	 * @return True si el elemento en la posici�n i es menor que aqu�l en la
	 *         posici�n j.
	 */
	abstract boolean less( int i, int j );

	/**
	 * Intercambia dos nodos, en la posici�n i y j.
	 * @param i Primer nodo a intercambiar.
	 * @param j Segundo nodo a intercambiar.
	 */
	abstract void exch( int i, int j );

	/**
	 * Hace el bottom-up swim.
	 * @param k Posici�n.
	 */
	abstract void swim( int k );

	/**
	 * Hace el top-down sink.
	 * @param k Posici�n.
	 */
	abstract void sink( int k );
}