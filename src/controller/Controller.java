package controller;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import model.logic.Modelo;
import view.View;

/**
 * Controlador.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class Controller
{
	/**
	 * Constantes que tienen los nombres de los archivos necesarios.
	 */
	private final static String ARCHIVO_ESTACIONES = "./data/estacionpolicia.geojson";
	private final static String ARCHIVO_VERTICES = "./data/bogota_vertices.txt";
	private final static String ARCHIVO_ARCOS = "./data/bogota_arcos.txt";
	private final static String ARCHIVO_JSON = "./data/grafo_en_json.json";

	/**
	 * Instancia del modelo.
	 */
	private Modelo modelo;

	/**
	 * Instancia de la vista.
	 */
	private View view;

	/**
	 * Crear la vista y el modelo del proyecto.
	 */
	public Controller( )
	{
		view = new View( );
		try
		{
			modelo = new Modelo( );
		}
		catch( IOException e )
		{
			view.printMessage( "* Hubo un problema construyendo el modelo *" );
			view.printMessage( "Excepci�n original:\n" );
			e.printStackTrace( );
		}
	}

	public void run( )
	{
		Scanner lector = new Scanner( System.in );
		boolean fin = false, ocurrioExcepcion;
		int option;
		String rutaArchivo = null;
		while( !fin )
		{
			view.printJump( );
			view.printMenuArchivo( );

			ocurrioExcepcion = true;
			while( ocurrioExcepcion )
			{
				try
				{
					option = Integer.parseInt( lector.next( ) );
					if( !opcionExiste( option, new int[] { 1, 2, 3 } ) )
						throw new Exception( "Ingrese una opci�n v�lida." );
					else
					{
						switch( option )
						{
							case 1:
								rutaArchivo = "./data/Comparendos_DEI_2018_Bogot�_D.C.geojson";
								break;
							case 2:
								rutaArchivo = "./data/Comparendos_DEI_2018_Bogot�_D.C_50000_.geojson";
								break;
							case 3:
								rutaArchivo = "./data/Comparendos_DEI_2018_Bogot�_D.C_small_50000_sorted.geojson";
								break;
						}
						if( !archivoExiste( rutaArchivo ) )
						{
							view.printMessage(
									"El archivo parece que no est� en el directorio. Escoja otro o intente de nuevo:" );
							continue;
						}
					}
					view.printMessage( "\nArchivo escogido: " + rutaArchivo );
					view.printMessage( "\n** NO OLVIDE SELECCIONAR LA OPCI�N 0 PRIMERO A CONTINUACI�N **" );
					ocurrioExcepcion = false;
				}
				catch( Exception e )
				{
					view.printMessage( "Ingrese una opci�n v�lida." );
					ocurrioExcepcion = true;
				}
			}
			fin = true;
		}

		fin = false;

		while( !fin )
		{
			view.printJump( );
			view.printMenu( );

			option = lector.nextInt( );
			switch( option )
			{
				case 0:
					view.printJump( );
					try
					{
						view.printMessage( "\nConstruyendo grafo a partir de las fuentes de datos...\n" );
						modelo.cargarGrafoFuentesDeDatos( ARCHIVO_VERTICES, ARCHIVO_ARCOS );
						view.printMessage( "�El grafo fue constru�do exitosamente!" );
					}
					catch( IOException e3 )
					{
						view.printMessage( "Hubo un problema cargando el grafo.\n" );
						continue;
					}

					try
					{
						view.printMessage( "\nCargando comparendos...\n" );
						view.printMessage( "+ Esto dura alrededor de 2 minutos, no se preocupe +\n" );
						modelo.cargarComparendosEnGrafo( rutaArchivo );
						view.printMessage( "�Los comparendos fueron cargados exitosamente!" );
					}
					catch( IllegalStateException e3 )
					{
						view.printMessage( e3.getMessage( ) + ".\n" );
					}
					catch( IOException e3 )
					{
						view.printMessage( "* Hubo un problema cargando los comparendos *" );
						view.printMessage( "* Aseg�rese que el archivo de los comparendos est� en ./data *" );
					}

					try
					{
						view.printMessage( "\nCargando estaciones de polic�a...\n" );
						modelo.cargarEstacionesEnGrafo( ARCHIVO_ESTACIONES );
						view.printMessage( "�Las estaciones de polic�a fueron cargadas exitosamente!\n" );
					}
					catch( IllegalStateException e3 )
					{
						view.printMessage( e3.getMessage( ) + ".\n" );
					}
					catch( IOException e3 )
					{
						view.printMessage( "Hubo un problema cargando las estaciones de polic�a.\n" );
					}

					try
					{
						view.printMessage(
								"Actualizando los costos de los arcos seg�n el n�mero de comparendos entre los v�rtices conectados...\n" );
						modelo.actualizarCostosEnGrafo( );
						view.printMessage( "�Los costos fueron actualizados exitosamente!\n" );
					}
					catch( NullPointerException e2 )
					{
						view.printMessage(
								"* Aseg�rese de efectuar correctamente la carga de los comparendos antes *" );
					}
					catch( IllegalStateException e3 )
					{
						view.printMessage( "* Hubo un problema actualizando los costos de los arcos *\n" );
						view.printMessage( e3.getMessage( ) );
					}

					try
					{
						view.printMessage( modelo.darReporteCompletoDeCarga( ) );
					}
					catch( IllegalStateException e )
					{
						view.printMessage( e.getMessage( ) + "\n" );
					}
					break;

				case 1:
					view.printJump( );
					view.printMessage(
							"Obtener el camino de costo m�nimo entre dos ubicaciones geogr�ficas por n�mero de comparendos" );
					view.printMessage( "Ingrese latitud del primer vertice:" );
					int latitudOr = Integer.parseInt( lector.next( ) );
					view.printMessage( "Ingrese longitud del primer vertice:" );
					int longitudOr = Integer.parseInt( lector.next( ) );
					view.printMessage( "Ingrese latitud del vertice de destino:" );
					int latitudDes = Integer.parseInt( lector.next( ) );
					view.printMessage( "Ingrese longitud del primer vertice:" );
					int longitudDes = Integer.parseInt( lector.next( ) );

					try
					{
						modelo.caminoDeCostoMinimoDistancia( latitudOr, longitudOr, latitudDes, longitudDes );
					}
					catch( IOException e2 )
					{
						view.printMessage(
								"* Aseg�rese de efectuar correctamente la carga de los comparendos antes *" );
					}
					break;

				case 3:
					view.printJump( );
					view.printMessage(
							"Obtener el camino de costo m�nimo entre dos ubicaciones geogr�ficas por n�mero de comparendos" );
					view.printMessage( "Ingrese latitud del v�rtice de origen:" );
					double lat1 = Double.parseDouble( lector.next( ) );
					view.printMessage( "Ingrese longitud del v�rtice de origen:" );
					double long1 = Double.parseDouble( lector.next( ) );
					view.printMessage( "Ingrese latitud del v�rtice de destino:" );
					double lat2 = Double.parseDouble( lector.next( ) );
					view.printMessage( "Ingrese longitud del v�rtice de destino:" );
					double long2 = Double.parseDouble( lector.next( ) );

					try
					{
						modelo.caminoDeCostoMinimoNumeroComparendos( lat1, long1, lat2, long2 );
					}
					catch( IOException e2 )
					{
						view.printMessage(
								"* Aseg�rese de efectuar correctamente la carga de los comparendos antes *" );
					}
					break;

				case 4:
					view.printJump( );
					view.printMessage( "Ingrese M = " );
					int M = 0;
					ocurrioExcepcion = true;
					while( ocurrioExcepcion )
					{
						try
						{
							M = Integer.parseInt( lector.next( ) );
							if( M <= 1 )
								throw new Exception( "Ingrese un entero positivo v�lido, M = " );
							ocurrioExcepcion = false;
						}
						catch( NullPointerException | NumberFormatException e )
						{
							view.printMessage( "Digite un entero positivo v�lido, M = " );
							ocurrioExcepcion = true;
						}
						catch( Exception e )
						{
							view.printMessage( e.getMessage( ) );
							ocurrioExcepcion = true;
						}
					}

					view.printJump( );
					view.printMessage( "" );
					try
					{
						view.printMessage( modelo.crearRedDeComunicaciones( M ) );
					}
					catch( IOException e1 )
					{
						view.printMessage(
								"* Aseg�rese de efectuar correctamente la carga de los comparendos antes *" );
					}
					break;

				case 6:
					view.printJump( );
					view.printMessage( "" );

					try
					{
						view.printMessage( modelo.identificarZonasDeImpacto( ) );
					}
					catch( NullPointerException e )
					{
						view.printMessage(
								"* Aseg�rese de efectuar correctamente la carga de los comparendos antes *\n" );
						e.printStackTrace( );
					}
					catch( IOException e )
					{
						view.printMessage( "* Aseg�rese de tener los archivos necesarios .txt para el HTML. *\n" );
					}

					break;

				case 7:
					view.printJump( );
					view.printMessage( "\n�Hasta pronto!\n" );
					view.printJump( );
					lector.close( );
					fin = true;
					break;

				default:
					view.printJump( );
					view.printMessage( "�Opci�n inv�lida!" );
					break;
			}
		}
	}

	private boolean opcionExiste( int opcion, int[] posiblesOpciones )
	{
		for( int i = 0; i < posiblesOpciones.length; i++ )
			if( opcion == posiblesOpciones[i] )
				return true;

		return false;
	}

	private boolean archivoExiste( String rutaArchivo )
	{
		File tmp = new File( rutaArchivo );
		return tmp.exists( );
	}
}