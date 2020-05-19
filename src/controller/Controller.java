package controller;

import java.io.IOException;
import java.util.Scanner;

import model.logic.Modelo;
import view.View;

/**
 * Controlador.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Controller
{
	/**
	 * Constantes que tienen los nombres de los archivos necesarios.
	 */
	private final static String ARCHIVO_COMPARENDOS = "./data/Comparendos_DEI_2018_Bogotá_D.C.geojson";
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
		modelo = new Modelo( );
	}

	public void run( )
	{
		Scanner lector = new Scanner( System.in );
		boolean fin = false;
		while( !fin )
		{
			view.printJump( );
			view.printMenu( );

			int option = lector.nextInt( );
			switch( option )
			{
				case 0:
					view.printJump( );
					try
					{
						view.printMessage( "\nConstruyendo grafo a partir de las fuentes de datos...\n" );
						modelo.cargarGrafoFuentesDeDatos( ARCHIVO_VERTICES, ARCHIVO_ARCOS );
						view.printMessage( "¡El grafo fue construído exitosamente!" );
					}
					catch( IOException e3 )
					{
						view.printMessage( "Hubo un problema cargando el grafo.\n" );
						continue;
					}

					try
					{
						view.printMessage( "\nCargando comparendos...\n" );
						modelo.cargarComparendosEnGrafo( ARCHIVO_COMPARENDOS );
						view.printMessage( "¡Los comparendos fueron cargados exitosamente!" );
					}
					catch( IllegalStateException e3 )
					{
						view.printMessage( e3.getMessage( ) + ".\n" );
					}
					catch( IOException e3 )
					{
						view.printMessage( "* Hubo un problema cargando los comparendos *" );
						view.printMessage( "* Asegúrese que el archivo de los comparendos esté en ./data *" );
					}

					try
					{
						view.printMessage( "\nCargando estaciones de policía...\n" );
						modelo.cargarEstacionesEnGrafo( ARCHIVO_ESTACIONES );
						view.printMessage( "¡Las estaciones de policía fueron cargadas exitosamente!\n" );
					}
					catch( IllegalStateException e3 )
					{
						view.printMessage( e3.getMessage( ) + ".\n" );
					}
					catch( IOException e3 )
					{
						view.printMessage( "Hubo un problema cargando las estaciones de policía.\n" );
					}

					try
					{
						view.printMessage(
								"Actualizando los costos de los arcos según el número de comparendos entre los vértices conectados...\n" );
						modelo.actualizarCostosEnGrafo( );
						view.printMessage( "¡Los costos fueron actualizados exitosamente!\n" );
					}
					catch( IllegalStateException e3 )
					{
						view.printMessage( "* Hubo un problema actualizando los costos de los arcos *" );
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

				case 6:
					view.printJump( );
					view.printMessage( "\n¡Hasta pronto!\n" );
					view.printJump( );
					lector.close( );
					fin = true;
					break;

				default:
					view.printJump( );
					view.printMessage( "¡Opción inválida!" );
					break;
			}
		}
	}
}