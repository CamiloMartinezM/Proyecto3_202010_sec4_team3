package model.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.data_structures.MaxHeap;
import model.data_structures.UndirectedGraph;

/**
 * Definición del modelo del mundo.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Modelo
{
	/**
	 * Constantes para la construcción del HTML para pintar el grafo.
	 */
	private static final String APERTURA_HTML = "./data/HTML/Javascript_Inicio.txt";
	private static final String CIERRE_HTML = "./data/HTML/Javascript_Final.txt";
	private static final String APERTURA_SCRIPT = "./data/HTML/inicio_html.txt";
	private static final String CIERRE_SCRIPT = "./data/HTML/final_html.txt";
	// private static final String MARCADOR = "./data/HTML/definicion_marcador.txt";
	private static final String CIRCULO = "./data/HTML/definicion_circulo.txt";
	private static final String LINEA = "./data/HTML/definicion_linea.txt";
	private static final String POSICION_LATITUD = "4.609537";
	private static final String POSICION_LONGITUD = "-74.078715";
	private static final String VALOR_ZOOM = "13";
	private static final String VALOR_RADIO = "25";

	/**
	 * Pila que guarda la información de las estaciones de policía.
	 */
	private MaxHeap<EstacionPolicia> estaciones;
	
	/**
	 * Grafo cargado a partir de las fuentes de datos.
	 */
	private UndirectedGraph<Integer, double[]> grafoFD = null;

	/**
	 * Grafo cargado a partir de un archivo JSON.
	 */
	private UndirectedGraph<Integer, double[]> grafoJS = null;
	
	/**
	 * Constructor del modelo del mundo.
	 */
	public Modelo( )
	{
		estaciones = new MaxHeap<>( 21 );
	}
	
	/**
	 * Carga los datos de las estaciones de policía en un heap de prioridad.
	 * @param rutaArchivo Ruta del archivo donde se encuentran las estaciones de
	 *                    policía. rutaArchivo != null, rutaArchivo != ""
	 * @return True si la pila fue cargada correctamente, false de lo contrario.
	 */
	public boolean cargarEstacionesDePolicia( String rutaArchivo )
	{
		Gson parser = new Gson( );
		JsonObject element = null;
		Reader file = null;

		try
		{
			file = new FileReader( rutaArchivo );
			element = parser.fromJson( file, JsonObject.class );
		}
		catch( Exception e )
		{
			return false;
		}

		JsonArray dataAsJSONArray = element.get( "features" ).getAsJsonArray( );
		String id;
		double latitud, longitud;
		JsonObject actualProperties;
		EstacionPolicia e;
		for( JsonElement dataActual : dataAsJSONArray )
		{
			actualProperties = dataActual.getAsJsonObject( ).get( "properties" ).getAsJsonObject( );

			id = actualProperties.get( "OBJECTID" ).getAsString( );
			latitud = actualProperties.get( "EPOLATITUD" ).getAsDouble( );
			longitud = actualProperties.get( "EPOLONGITU" ).getAsDouble( );

			e = new EstacionPolicia( id, latitud, longitud );
			estaciones.insert( e );
		}

		return true;
	}

	/**
	 * Realiza la carga del grafo a partir de las fuentes de datos
	 * (bogota_vertices.txt y bogota_arcos.txt).
	 * @param rutaArchivoVertices Archivo donde están los vertices.
	 *                            rutaArchivoVertices != null, != ""
	 * @param rutaArchivoArcos    Archivo donde están los arcos. rutaArchivoArcos !=
	 *                            null, != ""
	 * @return Cadena con la cantidad de vertices y arcos creados como reporte.
	 * @throws IOException Si hay un problema en la lectura de los archivos.
	 */
	public String cargarGrafoFuentesDeDatos( String rutaArchivoVertices, String rutaArchivoArcos ) throws IOException
	{
		// LECTURA DE VERTICES.
		BufferedReader reader = new BufferedReader( new FileReader( rutaArchivoVertices ) );
		String vertice = reader.readLine( );
		
		int N = 31;
		grafoFD = new UndirectedGraph<>( N );

		String[] info = new String[2];
		double[] infod;
		int id;
		while( vertice != null )
		{
			id = Integer.parseInt( vertice.split( "," )[0] );

			// Se quita el id de la línea y se queda con la latitud y longitud concatenadas
			// con ",".
			info = vertice.replaceFirst( id + ",", "" ).split( "," );
			infod = new double[] { Double.parseDouble( info[0] ), Double.parseDouble( info[1] ) };

			// Se añade el vértice.
			grafoFD.addVertex( id, infod );
			vertice = reader.readLine( );
		}

		reader.close( );
		
		// LECTURA DE ARCOS.
		reader = new BufferedReader( new FileReader( rutaArchivoArcos ) );
		String arco = reader.readLine( );
		
		String[] linea;
		double[] infoAdyacente, infoVertice;
		int idAdyacente;
		double costo;
		while( arco != null )
		{
			linea = arco.split( " " );

			// Solo aparece el id del vértice y ninguno adyacente.
			if( linea.length > 1 )
			{
				id = Integer.parseInt( linea[0] );
				infoVertice = grafoFD.getInfoVertex( id );

				for( int i = 1; i < linea.length; i++ )
				{
					idAdyacente = Integer.parseInt( linea[i] );
					infoAdyacente = grafoFD.getInfoVertex( idAdyacente );
					costo = Haversine.distance( infoVertice[1], infoVertice[0], infoAdyacente[1], infoAdyacente[0] );

					// Se añade el arco.
					grafoFD.addEdge( id, idAdyacente, costo );
				}
			}
			arco = reader.readLine( );
		}

		reader.close( );

		String reporte = "\nCantidad de vertices: " + grafoFD.V( ) + "\n";
		reporte += "Cantidad de arcos: " + grafoFD.E( ) + "\n";
		return reporte;
	}

	/**
	 * Construye un archivo JSON que contiene la información del grafo.
	 * @param rutaArchivo Archivo donde guardar el archivo JSON.
	 * @throws FileNotFoundException En caso que haya un problema creando el archivo.
	 */
	public void construirJSONDelGrafo( String rutaArchivo ) throws FileNotFoundException
	{
		File file = new File( rutaArchivo );
		PrintWriter w = new PrintWriter( file );

		w.println( "{" );
		w.println( "\t" + "\"vertices\": [" );

		int i = 0, idAdyacente;
		double[] infoVertice;
		while( i < grafoFD.V( ) )
		{
			w.println( "\t\t" + "{" );
			infoVertice = grafoFD.getInfoVertex( i );
			w.println( "\t\t\t" + "\"id\": " + i + "," );
			w.print( "\t\t\t" + "\"coordenadas\": [" );
			w.print( infoVertice[0] + ", " );
			w.println( infoVertice[1] + "]," );
			w.println( "\t\t\t" + "\"adyacencias\": [" );

			Iterator<Integer> iter = grafoFD.adj( i ).iterator( );
			while( iter.hasNext( ) )
			{
				w.println( "\t\t\t\t" + "{" );
				idAdyacente = iter.next( );
				w.println( "\t\t\t\t\t" + "\"id\": " + idAdyacente + "," );
				w.println( "\t\t\t\t\t" + "\"costo\": " + grafoFD.getCostArc( i, idAdyacente ) );

				boolean tieneSiguiente = iter.hasNext( );
				if( tieneSiguiente )
					w.println( "\t\t\t\t" + "}," );
				else
					w.println( "\t\t\t\t" + "}" );
			}
			w.println( "\t\t\t" + "]" );
			i++;

			if( i < grafoFD.V( ) )
				w.println( "\t\t" + "}," );
			else
				w.println( "\t\t" + "}" );
		}

		w.println( "\t" + "]" );
		w.print( "}" );
		w.close( );
	}

	/**
	 * Carga el grafo a partir de un archivo JSON.
	 * @param rutaArchivo Archivo JSON de donde sacar la información del grafo. rutaArchivo != "", != null
	 * @return Cadena con la cantidad de vertices y arcos creados como reporte.
	 * @throws FileNotFoundException En caso que no se encuentre el archivo JSON.
	 */
	public String cargarGrafoDeJSON( String rutaArchivo ) throws FileNotFoundException
	{
		grafoJS = new UndirectedGraph<>( grafoFD.V( ) );
		
		Gson parser = new Gson( );
		JsonObject element = null;
		Reader file = null;

		file = new FileReader( rutaArchivo );
		element = parser.fromJson( file, JsonObject.class );

		JsonArray dataAsJSONArray = element.get( "vertices" ).getAsJsonArray( );
		int id, idAdyacente;
		double[] coord; double costo;
		for( JsonElement vertice : dataAsJSONArray )
		{
			id = vertice.getAsJsonObject( ).get( "id" ).getAsInt( );
			coord = transformarJsonArray( vertice.getAsJsonObject( ).get( "coordenadas" ).getAsJsonArray( ) );
			grafoJS.addVertex( id, coord );
			for( JsonElement adyacencia : vertice.getAsJsonObject( ).get( "adyacencias" ).getAsJsonArray( ) )
			{
				idAdyacente = adyacencia.getAsJsonObject( ).get( "id" ).getAsInt( );
				costo = adyacencia.getAsJsonObject( ).get( "costo" ).getAsDouble( );
				
				if( !grafoJS.existeArco( id, idAdyacente ) )
					grafoJS.addEdge( id, idAdyacente, costo );
			}
		}

		String reporte = "\nCantidad de vertices: " + grafoJS.V( ) + "\n";
		reporte += "Cantidad de arcos: " + grafoJS.E( ) + "\n";		
		return reporte;
	}

	/**
	 * Pinta el grafo con la ayuda del API de Google Maps.
	 * @throws FileNotFoundException Si no encuentra algún archivo referente a las
	 *                               constantes creadas al inicio.
	 * @throws IOException           Si hubo algún problema en la lectura de los
	 *                               archivos.
	 */
	public void pintarGrafoEnGoogleMaps( ) throws FileNotFoundException, IOException
	{
		File f = new File( "./data/grafo.html" );
		FileWriter w = new FileWriter( f, false );

		// Principio del archivo HTML.
		String apertura = leerArchivo( APERTURA_HTML );
		w.append( apertura + "\n" );
		
		// Principio del script.
		apertura = leerArchivo( APERTURA_SCRIPT );
		apertura = apertura.replace( "VALOR_ZOOM", VALOR_ZOOM );
		apertura = apertura.replace( "LATITUD", POSICION_LATITUD );
		apertura = apertura.replace( "LONGITUD", POSICION_LONGITUD );

		w.append( apertura + "\n" );

		// Formato usado para la creación de un círculo.
		String circuloBruto = leerArchivo( CIRCULO );
		circuloBruto = circuloBruto.replace( "VALOR_RADIO", VALOR_RADIO );

		// Formato usado para la creación de una línea.
		String lineaBruta = leerArchivo( LINEA );

		String circulo, linea, latitud, longitud, latitudAdyacente, longitudAdyacente;
		String[] remplazoLatitud, remplazoLongitud;
		ArrayList<String[]> remplazos;

		// Se itera sobre los vertices.
		int i = 0, idAdyacente;
		while( i < grafoFD.V( ) )
		{
			double[] infoVertice = grafoFD.getInfoVertex( i );
			latitud = String.valueOf( infoVertice[1] );
			longitud = String.valueOf( infoVertice[0] );
			remplazoLatitud = new String[] { "LATITUD", latitud };
			remplazoLongitud = new String[] { "LONGITUD", longitud };
			remplazos = new ArrayList<String[]>( );
			remplazos.add( remplazoLatitud );
			remplazos.add( remplazoLongitud );
			circulo = construirComponente( circuloBruto, remplazos );

			w.append( circulo + "\n" );

			// Se itera sobre las adyacencias.
			Iterator<Integer> iter = grafoFD.adj( i ).iterator( );
			while( iter.hasNext( ) )
			{
				idAdyacente = iter.next( );
				latitudAdyacente = String.valueOf( grafoFD.getInfoVertex( idAdyacente )[1] );
				longitudAdyacente = String.valueOf( grafoFD.getInfoVertex( idAdyacente )[0] );

				remplazos = new ArrayList<String[]>( );
				remplazoLatitud = new String[] { "LATITUD_INICIO", latitud };
				remplazoLongitud = new String[] { "LONGITUD_INICIO", longitud };
				remplazos.add( remplazoLatitud );
				remplazos.add( remplazoLongitud );
				remplazoLatitud = new String[] { "LATITUD_FINAL", latitudAdyacente };
				remplazoLongitud = new String[] { "LONGITUD_FINAL", longitudAdyacente };
				remplazos.add( remplazoLatitud );
				remplazos.add( remplazoLongitud );

				linea = construirComponente( lineaBruta, remplazos );

				w.append( linea + "\n" );
			}
			i++;
		}

		// Cierre del script.
		String cierre = leerArchivo( CIERRE_SCRIPT );

		w.append( cierre + "\n" );
		
		// Final del archivo HTML.
		cierre = leerArchivo( CIERRE_HTML );
		w.append( cierre + "\n" );
		w.close( );

		// Se muestra el archivo HTML en el navegador.
		f = new File( "./data/grafo.html" );
		java.awt.Desktop.getDesktop( ).browse( f.toURI() );
	}

	/**
	 * Toma la cadena en bruto y remplaza según lo especificado en el ArrayList.
	 * @param cadenaEnBruto Cadena en bruto, donde se quieren hacer los remplazos.
	 *                      cadenaEnBruto != null, != ""
	 * @param remplazo      Arreglo que tiene los remplazos, donde la primera
	 *                      posición es la cadena original y la segunda es la cadena
	 *                      por la cual se va a remplazar.
	 * @return Cadena con los remplazos.
	 */
	public String construirComponente( String cadenaEnBruto, ArrayList<String[]> remplazo )
	{
		String cadena = cadenaEnBruto;
		String original, nueva;
		for( int i = 0; i < remplazo.size( ); i++ )
		{
			original = remplazo.get( i )[0];
			nueva = remplazo.get( i )[1];
			cadena = cadena.replace( original, nueva );
		}

		return cadena;
	}

	/**
	 * @return Información importante de cada estación de policía.
	 */
	public String darInformacionEstaciones( )
	{
		String informacion = "";
		boolean[] mostrar = { true, true, true };
		for( int i = 0; i < estaciones.getSize( ); i++ )
		{
			informacion += "Estación " + ( i + 1 ) + ":\n\n";
			informacion += estaciones.peekPosition( i ).consultarInformacion( mostrar ) + "\n";
		}

		informacion += "Se cargaron " + estaciones.getSize( ) + " estaciones de policía.\n";
		return informacion;
	}

	/**
	 * @return Cantidad de vertices y arcos creados.
	 */
	public String darReporteGrafo( )
	{
		String informacion = "";
		informacion += "Cantidad de vertices creados: " + grafoFD.V( ) + "\n";
		informacion += "Cantidad de arcos creados: " + grafoFD.E( ) + "\n";
		return informacion;
	}

	/**
	 * Transforma un JsonArray a un double array.
	 * @param jsonArray Arreglo a transformar.
	 * @return Float Array.
	 */
	private double[] transformarJsonArray( JsonArray jsonArray )
	{
		double[] fData = new double[jsonArray.size( )];

		for( int i = 0; i < jsonArray.size( ); i++ )
			fData[i] = Double.parseDouble( jsonArray.get( i ).getAsString( ) );

		return fData;
	}

	/**
	 * Lee un archivo en un String, contando los caracteres de separación.
	 * @param rutaArchivo Ruta del archivo a leer. rutaArchivo != null, != ""
	 * @return Cadena con todo el contenido del archivo.
	 * @throws IOException
	 */
	private static String leerArchivo( String rutaArchivo ) throws IOException
	{
		byte[] encoded = Files.readAllBytes( Paths.get( rutaArchivo ) );
		return new String( encoded, StandardCharsets.UTF_8 );
	}
}