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
		System.out.println( "0. Cargar comparendos y estaciones de policía de la malla vial de Bogotá (2018) dentro del grafo hecho" );
		System.out.println( "   a partir de las fuentes de datos." );
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