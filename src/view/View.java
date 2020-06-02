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
		System.out.println(
				"0. Cargar comparendos y estaciones de policía de la malla vial de Bogotá (2018) dentro del grafo hecho a partir de las fuentes de datos.\n" );
		System.out.println(
				"1. (A-1) Obtener el camino de costo mínimo entre dos ubicaciones geográficas según la distancia Haversiana." );
		System.out.println(
				"2. (A-2) Determinar la red de comunicaciones que soporte la instalación de cámaras de video en los M puntos donde se presentan los" );
		System.out.println( "         comparendos de mayor gravedad.\n" );
		System.out.println(
				"3. (B-1) Obtener el camino de costo mínimo entre dos ubicaciones geográficas según el número de comparendos." );
		System.out.println(
				"4. (B-2) Determinar la red de comunicaciones que soporte la instalación de cámaras de video entre los M puntos donde se presenta la mayor" );
		System.out.println( "         cantidad de comparendos en la ciudad.\n" );
		System.out.println(
				"5. (C-1) Obtener los caminos más cortos para que los policías puedan atender los M comparendos más graves." );
		System.out.println( "6. (C-2) Identificar las zonas de impacto de las estaciones de policía.\n" );

		System.out.println( "7. Exit." );
		System.out.println( "" );
		System.out.println( "Dar el número de opción a resolver, luego oprimir tecla Return: (e.g., 1):" );
	}

	/**
	 * Muestra el menú de la aplicación para elegir qué archivo cargar.
	 */
	public void printMenuArchivo( )
	{
		System.out.println( "Elija qué archivo cargar:\n" );
		System.out.println( "1. Archivo completo" );
		System.out.println( "2. Archivo de 50000." );
		System.out.println( "3. Archivo de 50000 small sorted." );
		System.out.println( "" );
		System.out.println( "Dar el número de opción a resolver, luego oprimir tecla Enter (e.g., 1):" );
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