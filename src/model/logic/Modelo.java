package model.logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.data_structures.MaxHeap;
import model.data_structures.MaxQueue;
import model.data_structures.UndirectedGraph;

/**
 * Definici�n del modelo del mundo.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class Modelo
{
	/**
	 * N�mero de estaciones de polic�a.
	 */
	private static final int NUMERO_ESTACIONES_POLICIA = 21;

	/**
	 * N�mero de comparendos.
	 */
	private static final int NUMERO_COMPARENDOS = 527655;

	/**
	 * Constantes para la construcci�n del HTML para pintar el grafo.
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
	 * Cola de prioridad que guarda la informaci�n de las estaciones de polic�a.
	 */
	private MaxQueue<EstacionPolicia> estaciones = new MaxQueue<>( NUMERO_ESTACIONES_POLICIA );

	/**
	 * Heap de prioridad que guarda la informaci�n de los comparendos.
	 */
	private MaxHeap<Comparendo> comparendos = new MaxHeap<>( NUMERO_COMPARENDOS );

	/**
	 * Grafo cargado a partir de las fuentes de datos.
	 */
	private UndirectedGraph<Comparendo, String, Double> grafoFD = null;

	/**
	 * Grafo cargado a partir de un archivo JSON.
	 */
	private UndirectedGraph<Comparendo, String, Double> grafoJS = null;

	/**
	 * Constructor del modelo del mundo.
	 */
	public Modelo( )
	{
	}

	/**
	 * Carga los datos de los comparendos en una lista enlazada, los cuales se
	 * encuentran dentro de la ruta del archivo dada por par�metro. <b>post:<\b> La
	 * lista enlazada queda constru�da.
	 * @param rutaArchivo Ruta del archivo donde se encuentran los comparendos.
	 *                    rutaArchivo != null, rutaArchivo != ""
	 * @return True si la lista enlazada fue cargada correctamente, false de lo
	 *         contrario.
	 * @throws IOException Si hay un problema de lectura del archivo JSON.
	 */
	public void cargarComparendos( String rutaArchivo ) throws IOException
	{
		InputStream is = new DataInputStream( new FileInputStream( rutaArchivo ) );
		ObjectMapper mapper = new ObjectMapper( );
		Comparendo c;
		
		// Crea una instancia de JsonParser
		try( JsonParser jsonParser = mapper.getFactory( ).createParser( is ) )
		{
			// Revisa que el primer token sea de inicio de arreglo o [.
			if( jsonParser.nextToken( ) != JsonToken.START_ARRAY )
			{
				throw new IllegalStateException( "Se esperaba el comienzo de un arreglo." );
			}

			// Itera los tokens hasta llegar al final del arreglo o ].
			while( jsonParser.nextToken( ) != JsonToken.END_ARRAY )
			{
				c = mapper.readValue( jsonParser, Comparendo.class );
				comparendos.insert( c ); // Inserta el comparendo deserializado en el heap de prioridad.
			}
		}
	}

	/**
	 * Carga los datos de las estaciones de polic�a en un heap de prioridad.
	 * @param rutaArchivo Ruta del archivo donde se encuentran las estaciones de
	 *                    polic�a. rutaArchivo != null, rutaArchivo != ""
	 * @return True si la pila fue cargada correctamente, false de lo contrario.
	 * @throws IOException Si hay un problema de lectura en el archivo JSON.
	 */
	public void cargarEstacionesDePolicia( String rutaArchivo ) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper( );
		
		estaciones = new MaxQueue<>( NUMERO_ESTACIONES_POLICIA );
		
		byte[] jsonData = Files.readAllBytes( Paths.get( rutaArchivo ) );
		JsonNode n = mapper.readTree( jsonData );
		Iterator<JsonNode> d = n.get( "features" ).elements( );
		while( d.hasNext( ) )
		{
			JsonNode i = d.next( ).get( "properties" );
			EstacionPolicia e = mapper.treeToValue( i, EstacionPolicia.class );
			estaciones.insert( e );
		}
	}

	/**
	 * Realiza la carga del grafo a partir de las fuentes de datos
	 * (bogota_vertices.txt y bogota_arcos.txt).
	 * @param rutaArchivoVertices Archivo donde est�n los vertices.
	 *                            rutaArchivoVertices != null, != ""
	 * @param rutaArchivoArcos    Archivo donde est�n los arcos. rutaArchivoArcos !=
	 *                            null, != ""
	 * @return Cadena con la cantidad de vertices y arcos creados como reporte.
	 * @throws IOException Si hay un problema en la lectura de los archivos.
	 */
	public String cargarGrafoFuentesDeDatos( String rutaArchivoVertices, String rutaArchivoArcos ) throws IOException
	{
		// Se lee todo el archivo para ver cu�ntos vertices se necesitan.
		Scanner sc = new Scanner( new File( rutaArchivoVertices ) );
		int numberOfVertices = 0;
		while( sc.hasNextLine( ) )
		{
			numberOfVertices++;
			sc.nextLine( );
		}
		sc.close( );

		// LECTURA DE VERTICES.
		BufferedReader reader = new BufferedReader( new FileReader( rutaArchivoVertices ) );
		String vertice = reader.readLine( );

		grafoFD = new UndirectedGraph<>( numberOfVertices );

		String info;
		int id;
		while( vertice != null )
		{
			id = Integer.parseInt( vertice.split( "," )[0] );

			// Se quita el id de la l�nea y se queda con la latitud y longitud concatenadas
			// con ",".
			info = vertice.replaceFirst( id + ",", "" );

			// Se a�ade el v�rtice.
			grafoFD.setVertexInfo( id, info );
			vertice = reader.readLine( );
		}

		reader.close( );

		// LECTURA DE ARCOS.
		reader = new BufferedReader( new FileReader( rutaArchivoArcos ) );
		String arco = reader.readLine( );

		String[] linea;
		String infoAdyacente, infoVertice;
		int idAdyacente;
		double costo, latitud, longitud, latitudAdyacente, longitudAdyacente;
		while( arco != null )
		{
			if( !arco.startsWith( "#" ) )
			{
				linea = arco.split( " " );

				// Solo aparece el id del v�rtice y ninguno adyacente.
				if( linea.length > 1 )
				{
					id = Integer.parseInt( linea[0] );
					infoVertice = grafoFD.getVertexInfo( id );
					longitud = Double.parseDouble( infoVertice.split( "," )[0] );
					latitud = Double.parseDouble( infoVertice.split( "," )[1] );

					for( int i = 1; i < linea.length; i++ )
					{
						idAdyacente = Integer.parseInt( linea[i] );
						infoAdyacente = grafoFD.getVertexInfo( idAdyacente );
						longitudAdyacente = Double.parseDouble( infoAdyacente.split( "," )[0] );
						latitudAdyacente = Double.parseDouble( infoAdyacente.split( "," )[1] );
						costo = Haversine.distance( latitud, longitud, latitudAdyacente, longitudAdyacente );

						// Se a�ade el arco.
						grafoFD.addEdge( id, idAdyacente, costo );
					}
				}
			}
			arco = reader.readLine( );
		}

		reader.close( );

		return darReporteGrafo( grafoFD );
	}

	/**
	 * Construye un archivo JSON que contiene la informaci�n del grafo.
	 * @param rutaArchivo Archivo donde guardar el archivo JSON.
	 * @throws FileNotFoundException En caso que haya un problema creando el
	 *                               archivo.
	 */
	public void construirJSONDelGrafo( String rutaArchivo ) throws FileNotFoundException
	{
		File file = new File( rutaArchivo );
		PrintWriter w = new PrintWriter( file );

		w.println( "{" );
		w.println( "\t" + "\"vertices\": [" );

		int i = 0, idAdyacente;
		String infoVertice;
		String latitud, longitud;
		while( i < grafoFD.numberOfVertices( ) )
		{
			w.println( "\t\t" + "{" );
			infoVertice = grafoFD.getVertexInfo( i );
			longitud = infoVertice.split( "," )[0];
			latitud = infoVertice.split( "," )[1];
			w.println( "\t\t\t" + "\"id\": " + i + "," );
			w.print( "\t\t\t" + "\"coordenadas\": [" );
			w.println( longitud + "," + latitud + "]," );
			w.println( "\t\t\t" + "\"adyacencias\": [" );

			Iterator<Integer> iter = grafoFD.adj( i ).iterator( );
			while( iter.hasNext( ) )
			{
				w.println( "\t\t\t\t" + "{" );
				idAdyacente = iter.next( );
				w.println( "\t\t\t\t\t" + "\"id\": " + idAdyacente + "," );
				w.println( "\t\t\t\t\t" + "\"costo\": " + grafoFD.getEdgeCost( i, idAdyacente ) );

				boolean tieneSiguiente = iter.hasNext( );
				if( tieneSiguiente )
					w.println( "\t\t\t\t" + "}," );
				else
					w.println( "\t\t\t\t" + "}" );
			}
			w.println( "\t\t\t" + "]" );
			i++;

			if( i < grafoFD.numberOfVertices( ) )
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
	 * @param rutaArchivo Archivo JSON de donde sacar la informaci�n del grafo.
	 *                    rutaArchivo != "", != null
	 * @return Cadena con la cantidad de vertices y arcos creados como reporte.
	 * @throws IOException Si hay un problema de lectura en el archivo JSON.
	 */
	public String cargarGrafoDeJSON( String rutaArchivo ) throws IOException
	{
		byte[] jsonData = Files.readAllBytes( Paths.get( rutaArchivo ) );

		grafoJS = new UndirectedGraph<>( grafoFD.numberOfVertices( ) );

		int id, idAdyacente;
		double[] coord;
		double costo;

		ObjectMapper mapper = new ObjectMapper( );
		JsonNode n = mapper.readTree( jsonData ), vertice;
		Iterator<JsonNode> d = n.get( "vertices" ).elements( );
		while( d.hasNext( ) )
		{
			vertice = d.next( );
			id = vertice.get( "id" ).asInt( );
			coord = DeserializadorJSON.transformarJsonArray( vertice.get( "coordenadas" ) );
			grafoJS.setVertexInfo( id, coord[0] + "," + coord[1] );

			for( JsonNode adyacencia : vertice.get( "adyacencias" ) )
			{
				idAdyacente = adyacencia.get( "id" ).asInt( );
				costo = adyacencia.get( "costo" ).asDouble( );

				grafoJS.addEdge( id, idAdyacente, costo );
			}
		}

		return darReporteGrafo( grafoJS );
	}

	/**
	 * Pinta el grafo con la ayuda del API de Google Maps.
	 * @throws FileNotFoundException Si no encuentra alg�n archivo referente a las
	 *                               constantes creadas al inicio.
	 * @throws IOException           Si hubo alg�n problema en la lectura de los
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

		// Formato usado para la creaci�n de un c�rculo.
		String circuloBruto = leerArchivo( CIRCULO );
		circuloBruto = circuloBruto.replace( "VALOR_RADIO", VALOR_RADIO );

		// Formato usado para la creaci�n de una l�nea.
		String lineaBruta = leerArchivo( LINEA );

		String circulo, linea, latitud, longitud, latitudAdyacente, longitudAdyacente;
		String[] remplazoLatitud, remplazoLongitud;
		ArrayList<String[]> remplazos;

		// Se itera sobre los vertices.
		int i = 0, idAdyacente;
		while( i < grafoFD.numberOfVertices( ) )
		{
			String infoVertice = grafoFD.getVertexInfo( i );
			latitud = infoVertice.split( "," )[1];
			longitud = infoVertice.split( "," )[0];
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
				latitudAdyacente = String.valueOf( grafoFD.getVertexInfo( idAdyacente ).split( "," )[1] );
				longitudAdyacente = String.valueOf( grafoFD.getVertexInfo( idAdyacente ).split( "," )[0] );

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
		f = new File( "data/grafo.html" );
		java.awt.Desktop.getDesktop( ).browse( f.toURI( ) );
	}

	/**
	 * Toma la cadena en bruto y remplaza seg�n lo especificado en el ArrayList.
	 * @param cadenaEnBruto Cadena en bruto, donde se quieren hacer los remplazos.
	 *                      cadenaEnBruto != null, != ""
	 * @param remplazo      Arreglo que tiene los remplazos, donde la primera
	 *                      posici�n es la cadena original y la segunda es la cadena
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
	 * @return Informaci�n importante de cada estaci�n de polic�a.
	 */
	public String darInformacionEstaciones( )
	{
		String informacion = "";
		boolean[] mostrar = { true, true, true };
		for( int i = 0; i < estaciones.getSize( ); i++ )
		{
			informacion += "Estaci�n " + ( i + 1 ) + ":\n\n";
			informacion += estaciones.peekPosition( i ).consultarInformacion( mostrar ) + "\n";
		}

		informacion += "Se cargaron " + estaciones.getSize( ) + " estaciones de polic�a.\n";
		return informacion;
	}

	/**
	 * @return Cantidad de vertices y arcos creados. Se divide la cantidad de arcos
	 *         sobre 2 pues en la tabla de hash se repiten las llaves como v-w y
	 *         w-v de forma que de cualquier forma se pueda acceder al costo de
	 *         cierto arco.
	 */
	public String darReporteGrafo( @SuppressWarnings( "rawtypes" ) UndirectedGraph grafo )
	{
		String informacion = "\n";
		informacion += "Cantidad de vertices creados: " + grafo.numberOfVertices( ) + "\n";
		informacion += "Cantidad de arcos creados: " + ( int ) ( grafo.numberOfEdges( ) / 2 ) + "\n";
		return informacion;
	}

	/**
	 * Lee un archivo en un String, contando los caracteres de separaci�n.
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