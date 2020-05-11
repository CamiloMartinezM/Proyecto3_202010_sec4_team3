package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
				case 1:
					view.printJump( );
					try
					{
						view.printMessage( modelo.cargarGrafoFuentesDeDatos( ARCHIVO_VERTICES, ARCHIVO_ARCOS ) );
					}
					catch( IOException e2 )
					{
						System.out.println( "\nHubo un problema cargando el grafo de las fuentes de datos.\n" );
					}
					break;

				case 2:
					view.printJump( );
					try
					{
						modelo.construirJSONDelGrafo( ARCHIVO_JSON );
						view.printMessage( "\n¡El grafo fue cargado exitosamente en un archivo .json!\n" );
					}
					catch( FileNotFoundException e2 )
					{
						System.out.println( "\nHubo un problema con el nombre del archivo .json.\n" );
					}
					catch( Exception e )
					{
						view.printMessage( "\nParece que no ha cargado el grafo a partir de las fuentes de datos.\n" );
					}
					break;
				
				case 3:
					view.printJump( );
					try
					{
						view.printMessage( modelo.cargarGrafoDeJSON( ARCHIVO_JSON ) );
						view.printMessage( "¡Un nuevo grafo fue cargado exitosamente a partir del archivo .json creado!\n" );
					}
					catch( FileNotFoundException e2 )
					{
						view.printMessage( "\nParece que aún no ha ejecutado la opción 2.\n");
					}
					catch( Exception e )
					{
						view.printMessage( "\nHubo un problema cargando los datos.\n" );
						e.printStackTrace( );
					}
					break;
					
				case 4:
					view.printJump( );
					String rutaArchivo = "./data/estacionpolicia.geojson";
					@SuppressWarnings( "unused" )
					FileReader f = null;

					try
					{
						f = new FileReader( rutaArchivo );
					}
					catch( FileNotFoundException e1 )
					{
						view.printMessage( "No se encontró ningún archivo de estaciones de policía." );
					}

					boolean cargaExitosa = modelo.cargarEstacionesDePolicia( rutaArchivo );
					if( cargaExitosa )
					{
						view.printMessage( "\n¡La carga de las estaciones de policía fue exitosa!\n" );
						view.printMessage( modelo.darInformacionEstaciones( ) );
					}
					else
						view.printMessage( "Hubo un problema leyendo el archivo de estaciones de policía." );
					break;
				
				case 5:
					view.printJump( );
					view.printMessage( "" );
					try
					{
						modelo.pintarGrafoEnGoogleMaps( );
					}
					catch( IOException e )
					{
						System.out.println( "\nHubo un error abriendo el navegador. Abra grafo.html manual desde ./data.\n" );
						e.printStackTrace();
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