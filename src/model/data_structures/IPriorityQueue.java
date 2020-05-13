package model.data_structures;

/**
 * Interfaz que debe implementar la clase MaxQueue.
 * @author Camilo Martínez & Nicolás Quintero.
 */
public interface IPriorityQueue<T extends Comparable<T>>
{
	/**
	 * @return Número de elementos presentes en la cola de prioridad.
	 */
	public int getSize( );

	/**
	 * Agrega un elemento a la cola de prioridad.
	 * @param newElement Elemento a agregar.
	 */
	public void insert( T newElement );

	/**
	 * Saca/atiende el elemento máximo en la cola y lo retorna; null en caso de cola
	 * vacía.
	 * @return Máximo elemento de la cola.
	 */
	public T poll( );

	/**
	 * @param pos Posición buscada. pos < size.
	 * @return Elemento en la posición dada por parámetro.
	 */
	public T peekPosition( int pos );
	
	/**
	 * Obtiene el elemento máximo de la cola, sin sacarlo; null en caso de cola
	 * vacía.
	 * @return Máximo elemento de la cola.
	 */
	public T peek( );

	/**
	 * @return True si la cola está vacía, false de lo contrario.
	 */
	public boolean isEmpty( );
	
	/**
	 * @return True si la capacidad de la cola ha sido superada, false de lo contrario.
	 */
	public boolean isFull( );
}