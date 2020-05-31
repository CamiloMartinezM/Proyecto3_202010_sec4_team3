package model.data_structures;

/**
 * Interfaz que debe implementar las tablas de hash.
 * @author Camilo Martínez & Nicolás Quintero
 */
public interface IHashTable<K extends Comparable<K>, V extends Comparable<V>> extends Iterable<K>
{
	/**
	 * @return Tamaño de la tabla de hash.
	 */
	public int getSize( );

	/**
	 * @return True si la tabla está vacía, false de lo contrario.
	 */
	public boolean isEmpty( );

	/**
	 * Obtiene el valor asociado a la llave dada por parámetro.
	 * @param key Llave a saber su valor. key != null
	 * @return Valor de la llave, null si no se encuentra. value != null
	 */
	public V get( K key );

	/**
	 * Inserta un nuevo par llave-valor en la lista. Si el factor de carga
	 * resultante es mayor al máximo establecido, se hace el rehash a la tabla.
	 * @param key   Llave del nodo a insertar. key != null
	 * @param value Valor del nodo a insertar. value != null
	 */
	public void put( K key, V value );

	/**
	 * @param key Llave a saber si la tabla de hash la contiene. key != null
	 * @return True si la tabla de hash la contiene, false de lo contrario.
	 */
	public boolean contains( K key );

	/**
	 * Elimina el nodo que contiene la llave dada por parámetro.
	 * @param key Llave cuyo nodo se quiere eliminar. key != null
	 * @return value Valor asociado a la llave.
	 */
	public V delete( K key );
}