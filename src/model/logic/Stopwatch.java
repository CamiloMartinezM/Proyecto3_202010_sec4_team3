package model.logic;

/**
 * Clase que permite contar el tiempo de ejecuci�n.
 * @author Camilo Mart�nez & Nicol�s Quintero.
 */
public class Stopwatch
{
	/**
	 * Guarda el tiempo en que se inicializ� el objeto de tipo Stopwatch.
	 */
	private final long start;

	/**
	 * Inicializa el Stopwatch.
	 */
	public Stopwatch( )
	{
		start = System.nanoTime( );
	}

	/**
	 * @return Tiempo en segundos transcurrido luego de haber inicializo el
	 *         Stopwatch hasta haber llamado este m�todo.
	 */
	public double elapsedTime( )
	{
		long now = System.nanoTime( );
		return ( now - start ) / 1.0e6;
	}
}