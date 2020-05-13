package view;

/**
 * Vista de la aplicación.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class View
{
	/**
	 * Metodo constructor.
	 */
	public View( )
	{

	}

	/**
	 * Muestra el menú de la aplicación.
	 */
	public void printMenu( )
	{
		System.out.println( "0. Cargar comparendos de la malla vial de Bogotá para el periodo 2018." );
		System.out.println( "1. Cargar grafo no dirigido a partir de las fuentes de datos." );
		System.out.println( "2. Guardar grafo no dirigido en un archivo persistido." );
		System.out.println( "3. Cargar grafo persistido." );
		System.out.println( "4. Cargar estaciones de policía." );
		System.out.println( "5. Graficar grafo resultante de la carga de la zona delimitada en Google Maps." );
		System.out.println( "6. Exit." );
		System.out.println( "" );
		System.out.println( "Dar el número de opción a resolver, luego oprimir tecla Return: (e.g., 1):" );
	}

	/**
	 * Muestra el mensaje dado por parámetro en consola.
	 * @param mensaje Mensaje a mostrar.
	 */
	public void printMessage( String mensaje )
	{
		System.out.println( mensaje );
	}

	/**
	 * Muestra un salto de línea de 120 caracteres "-".
	 */
	public void printJump( )
	{
		printMessage( new String( new char[120] ).replace( "\0", "-" ) );
	}
}