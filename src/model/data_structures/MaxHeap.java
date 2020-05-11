package model.data_structures;

/**
 * Clase que permite manejar un Heap de prioridad.
 * @author Camilo Martínez & Nicolás Quintero.
 */
@SuppressWarnings( "unchecked" )
public class MaxHeap<T extends Comparable<T>>
{
	/**
	 * Número de elementos en el heap.
	 */
	private int heapSize;

	/**
	 * Capacidad del heap.
	 */
	private int heapCapacity;

	/**
	 * Lista enlazada que guarda la información de los elementos dentro del heap.
	 */
	private T[] heap;

	/**
	 * Construye un heap de prioridad vacío.
	 * @param heapCapacity Capacidad del heap.
	 */
	public MaxHeap( int heapCapacity )
	{
		heap = ( T[] ) new Comparable[heapCapacity];
		heapSize = 0;
		this.heapCapacity = heapCapacity;
	}

	/**
	 * @return True si el heap está vacío, false de lo contrario.
	 */
	public boolean isEmpty( )
	{
		return heapSize == 0;
	}

	/**
	 * @return True si la capacidad del heap ha sido superada, false de lo
	 *         contrario.
	 */
	public boolean isFull( )
	{
		return heapSize == heapCapacity;
	}

	/**
	 * Vacía el heap.
	 */
	public void clear( )
	{
		for( int i = 0; i < heapCapacity; i++ )
			heap[i] = null;

		heapSize = 0;
	}

	/**
	 * @return Tamaño del heap.
	 */
	public int getSize( )
	{
		return heapSize;
	}

	/**
	 * Obtiene el elemento máximo de la cola, sin sacarlo; null en caso de cola
	 * @return Máximo elemento del heap.
	 */
	public T peek( )
	{
		if( isEmpty( ) )
			return null;

		return heap[0];
	}

	/**
	 * Elimina la raíz del heap.
	 * @return Raíz del heap eliminada.
	 */
	public T poll( )
	{
		return removeAt( 0 );
	}

	/**
	 * Comprueba si un elemento está dentro del heap.
	 * @param elem Elemento a comprobar su existencia.
	 * @return True si sí existe, false de lo contrario.
	 */
	public boolean contains( T elem )
	{
		for( int i = 0; i < heapSize; i++ )
			if( heap[i].equals( elem ) )
				return true;

		return false;
	}

	/**
	 * Añade un elemento al heap, el cual no debe ser null.
	 * @param elem Elemento a agregar. elem != null
	 */
	public void insert( T elem )
	{
		if( heapSize < heapCapacity )
			heap[heapSize] = elem;
		else
			throw new IllegalStateException( "La capacidad del heap ha sido superada." );

		swim( heapSize++ );
	}

	/**
	 * Elimina un elemento particular del heap.
	 * @param element Elemento a eliminar.
	 * @return True si se pudo eliminar, false de lo contrario.
	 */
	public boolean remove( T element )
	{
		if( element == null )
			return false;

		for( int i = 0; i < heapSize; i++ )
		{
			if( element.equals( heap[i] ) )
			{
				removeAt( i );
				return true;
			}
		}
		return false;
	}

	/**
	 * @param pos Posición buscada. pos < size.
	 * @return Elemento en la posición dada por parámetro.
	 */
	public T peekPosition( int i )
	{
		if( i >= 0 && i < heapSize )
			return heap[i];

		return null;
	}

	/**
	 * Elimina un elemento cuya posición es aquella dada por parámetro.
	 * @param i Posición del elemento a eliminar.
	 */
	private T removeAt( int i )
	{
		if( isEmpty( ) )
			return null;

		heapSize--;
		T removed = heap[i];
		exch( i, heapSize );

		// Destruye el valor.
		heap[heapSize] = null;

		// Chequea si el último momento fue removido
		if( i == heapSize )
			return removed;
		T elem = heap[i];

		// Intenta sinking
		sink( i );

		// Si no funciona el sinking, se trata swimming
		if( heap[i].equals( elem ) )
			swim( i );

		return removed;
	}

	/**
	 * Comprueba si el elemento en la posición i es menor que aquél en la posición
	 * j.
	 * @param i Posición 1.
	 * @param j Posición 2.
	 * @return True si el elemento en la posición i es menor que aquél en la
	 *         posición j.
	 */
	private boolean less( int i, int j )
	{
		return heap[i].compareTo( heap[j] ) <= 0;
	}

	/**
	 * Intercambia dos nodos, en la posición i y j.
	 * @param i Primer nodo a intercambiar.
	 * @param j Segundo nodo a intercambiar.
	 */
	private void exch( int i, int j )
	{
		T temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	/**
	 * Hace el bottom-up swim.
	 * @param k Posición.
	 */
	private void swim( int k )
	{
		while( k > 0 && less( k / 2, k ) )
		{
			exch( k, k / 2 );
			k = k / 2;
		}
	}

	/**
	 * Hace el top-down sink.
	 * @param k Posición.
	 */
	private void sink( int k )
	{
		while( true )
		{
			int left = 2 * k + 1;
			int right = 2 * k + 2;
			int smallest = left;

			if( right < heapSize && less( right, left ) )
				smallest = right;

			if( left >= heapSize || less( k, smallest ) )
				break;

			exch( smallest, k );
			k = smallest;
		}
	}
}