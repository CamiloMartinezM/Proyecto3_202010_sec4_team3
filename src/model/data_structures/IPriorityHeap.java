package model.data_structures;

/**
 * Interfaz que debe implementar la clase MaxHeapPQ.
 * @author Camilo Martínez & Nicolás Quintero.
 */
public interface IPriorityHeap<T extends Comparable<T>>
{
	/**
	 * @return True si el heap está vacío, false de lo contrario.
	 */
	public boolean isEmpty( );

	/**
	 * @return True si la capacidad del heap ha sido superada, false de lo
	 *         contrario.
	 */
	public boolean isFull( );

	/**
	 * Vacía el heap.
	 */
	public void clear( );

	/**
	 * @return Tamaño del heap.
	 */
	public int getSize( );

	/**
	 * Obtiene el elemento máximo de la cola, sin sacarlo; null en caso de cola
	 * @return Máximo elemento del heap.
	 */
	public T peek( );

	/**
	 * Elimina la raíz del heap.
	 * @return Raíz del heap eliminada.
	 */
	public T poll( );

	/**
	 * Comprueba si un elemento está dentro del heap.
	 * @param elem Elemento a comprobar su existencia.
	 * @return True si sí existe, false de lo contrario.
	 */
	public boolean contains( T elem );

	/**
	 * Añade un elemento al heap, el cual no debe ser null.
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
	 * @param pos Posición buscada. pos < size.
	 * @return Elemento en la posición dada por parámetro.
	 */
	public T peekPosition( int i );

	/**
	 * Elimina un elemento cuya posición es aquella dada por parámetro.
	 * @param i Posición del elemento a eliminar.
	 */
	abstract T removeAt( int i );

	/**
	 * Comprueba si el elemento en la posición i es menor que aquél en la posición
	 * j.
	 * @param i Posición 1.
	 * @param j Posición 2.
	 * @return True si el elemento en la posición i es menor que aquél en la
	 *         posición j.
	 */
	abstract boolean less( int i, int j );

	/**
	 * Intercambia dos nodos, en la posición i y j.
	 * @param i Primer nodo a intercambiar.
	 * @param j Segundo nodo a intercambiar.
	 */
	abstract void exch( int i, int j );

	/**
	 * Hace el bottom-up swim.
	 * @param k Posición.
	 */
	abstract void swim( int k );

	/**
	 * Hace el top-down sink.
	 * @param k Posición.
	 */
	abstract void sink( int k );
}