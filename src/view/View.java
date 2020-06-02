package view;

/**
 * Vista de la aplicaci�n.
 * @author Camilo Mart�nez & Nicol�s Quintero
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
	 * Muestra el men� de la aplicaci�n.
	 */
	public void printMenu( )
	{
		System.out.println(
				"0. Cargar comparendos y estaciones de polic�a de la malla vial de Bogot� (2018) dentro del grafo hecho a partir de las fuentes de datos.\n" );
		System.out.println(
				"1. (A-1) Obtener el camino de costo m�nimo entre dos ubicaciones geogr�ficas seg�n la distancia Haversiana." );
		System.out.println(
				"2. (A-2) Determinar la red de comunicaciones que soporte la instalaci�n de c�maras de video en los M puntos donde se presentan los" );
		System.out.println( "         comparendos de mayor gravedad.\n" );
		System.out.println(
				"3. (B-1) Obtener el camino de costo m�nimo entre dos ubicaciones geogr�ficas seg�n el n�mero de comparendos." );
		System.out.println(
				"4. (B-2) Determinar la red de comunicaciones que soporte la instalaci�n de c�maras de video entre los M puntos donde se presenta la mayor" );
		System.out.println( "         cantidad de comparendos en la ciudad.\n" );
		System.out.println(
				"5. (C-1) Obtener los caminos m�s cortos para que los polic�as puedan atender los M comparendos m�s graves." );
		System.out.println( "6. (C-2) Identificar las zonas de impacto de las estaciones de polic�a.\n" );

		System.out.println( "7. Exit." );
		System.out.println( "" );
		System.out.println( "Dar el n�mero de opci�n a resolver, luego oprimir tecla Return: (e.g., 1):" );
	}

	/**
	 * Muestra el men� de la aplicaci�n para elegir qu� archivo cargar.
	 */
	public void printMenuArchivo( )
	{
		System.out.println( "Elija qu� archivo cargar:\n" );
		System.out.println( "1. Archivo completo" );
		System.out.println( "2. Archivo de 50000." );
		System.out.println( "3. Archivo de 50000 small sorted." );
		System.out.println( "" );
		System.out.println( "Dar el n�mero de opci�n a resolver, luego oprimir tecla Enter (e.g., 1):" );
	}

	/**
	 * Muestra el mensaje dado por par�metro en consola.
	 * @param mensaje Mensaje a mostrar.
	 */
	public void printMessage( String mensaje )
	{
		System.out.println( mensaje );
	}

	/**
	 * Muestra un salto de l�nea de 120 caracteres "-".
	 */
	public void printJump( )
	{
		printMessage( new String( new char[120] ).replace( "\0", "-" ) );
	}
}