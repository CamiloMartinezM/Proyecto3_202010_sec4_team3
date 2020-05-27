package model.data_structures;

import java.util.EmptyStackException;

/**
 * Interfaz para la clase que maneja la pila.
 * @author Camilo Mart�nez
 */
public interface IStack<T> extends Iterable<T>
{
	/**
	 * Agrega un nuevo item al inicio de la pila.
	 * @param item Nuevo item.
	 */
	public void push( T item );

	/**
	 * Elimina seg�n la pol�tica LIFO y lo retorna.
	 * @throws EmptyStackException Si la pila est� vac�a.
	 * @return Elemento eliminado.
	 */
	public T pop( ) throws EmptyStackException;

	/**
	 * @return Tama�o de la pila, i.e, n�mero total de nodos.
	 */
	public int getSize( );

	/**
	 * @return Cabeza de la pila.
	 */
	public ListNode<T> getTopStack( );
	
	/**
	 * @return True si est� vac�o, false de lo contrario.
	 */
	public boolean isEmpty( );
}