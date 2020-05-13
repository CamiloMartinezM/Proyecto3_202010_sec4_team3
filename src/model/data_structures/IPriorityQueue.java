package model.data_structures;

/**
 * Interfaz que debe implementar la clase MaxQueue.
 * @author Camilo Mart�nez & Nicol�s Quintero.
 */
public interface IPriorityQueue<T extends Comparable<T>>
{
	/**
	 * @return N�mero de elementos presentes en la cola de prioridad.
	 */
	public int getSize( );

	/**
	 * Agrega un elemento a la cola de prioridad.
	 * @param newElement Elemento a agregar.
	 */
	public void insert( T newElement );

	/**
	 * Saca/atiende el elemento m�ximo en la cola y lo retorna; null en caso de cola
	 * vac�a.
	 * @return M�ximo elemento de la cola.
	 */
	public T poll( );

	/**
	 * @param pos Posici�n buscada. pos < size.
	 * @return Elemento en la posici�n dada por par�metro.
	 */
	public T peekPosition( int pos );
	
	/**
	 * Obtiene el elemento m�ximo de la cola, sin sacarlo; null en caso de cola
	 * vac�a.
	 * @return M�ximo elemento de la cola.
	 */
	public T peek( );

	/**
	 * @return True si la cola est� vac�a, false de lo contrario.
	 */
	public boolean isEmpty( );
	
	/**
	 * @return True si la capacidad de la cola ha sido superada, false de lo contrario.
	 */
	public boolean isFull( );
}