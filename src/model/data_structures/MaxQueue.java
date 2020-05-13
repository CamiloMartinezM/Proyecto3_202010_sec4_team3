package model.data_structures;

/**
 * Clase que permite manejar una cola de prioridad, por medio de una
 * implementaci�n con arreglos.
 * @author Camilo Mart�nez & Nicol�s Quintero.
 */
@SuppressWarnings( "unchecked" )
public class MaxQueue<T extends Comparable<T>> implements IPriorityQueue<T>
{
	/**
	 * Arreglo de elementos comparables.
	 */
	private T[] pq;

	/**
	 * Tama�o de la cola.
	 */
	private int size;

	/**
	 * Inicializa una cola vac�a de capacidad dada por par�metro.
	 * @param capacity Capacidad de la cola.
	 */
	public MaxQueue( int capacity )
	{
		pq = ( T[] ) new Comparable[capacity];
		size = 0;
	}

	/**
	 * @return N�mero de elementos presentes en la cola de prioridad.
	 */
	public int getSize( )
	{
		return size;
	}

	/**
	 * Agrega un elemento a la cola de prioridad.
	 * @param elemento Elemento a agregar.
	 * @throws IllegalStateException Si la capacidad de la cola ha sido superada.
	 */
	public void insert( T newElement ) throws IllegalStateException
	{
		if( isFull( ) )
			throw new IllegalStateException( "La capacidad de la cola ha sido superada." );

		if( isEmpty( ) )
			pq[0] = newElement;
		else
		{
			int i;
			for( i = size - 1; i >= 0; i-- )
				if( newElement.compareTo( pq[i] ) > 0 )
					pq[i + 1] = pq[i];
				else
					break;

			pq[i + 1] = newElement;
		}
		size++;
	}

	/**
	 * Obtiene el elemento m�ximo de la cola, sin sacarlo; null en caso de cola
	 * vac�a.
	 * @return M�ximo elemento de la cola.
	 */
	public T peek( )
	{
		if( isEmpty( ) )
			return null;

		return pq[0];
	}

	/**
	 * @param pos Posici�n buscada. pos < size.
	 * @return Elemento en la posici�n dada por par�metro.
	 */
	public T peekPosition( int pos )
	{
		if( pos >= 0 && pos < size )
			return pq[pos];

		return null;
	}

	/**
	 * Saca/atiende el elemento m�ximo en la cola y lo retorna; null en caso de cola
	 * vac�a.
	 * @return M�ximo elemento de la cola.
	 */
	public T poll( )
	{
		if( isEmpty( ) )
			return null;

		T eliminado = pq[0];
		for( int i = size - 1; i > 0; i-- )
			pq[i - 1] = pq[i];

		size--;
		return eliminado;
	}

	/**
	 * @return True si la cola est� vac�a, false de lo contrario.
	 */
	public boolean isEmpty( )
	{
		return size == 0;
	}

	/**
	 * @return True si la capacidad de la cola ha sido superada, false de lo
	 *         contrario.
	 */
	public boolean isFull( )
	{
		return size == pq.length;
	}
}