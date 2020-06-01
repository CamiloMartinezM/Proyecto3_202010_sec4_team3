package model.logic;

import java.awt.Color;
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

import model.data_structures.Edge;
import model.data_structures.Dijkstra;
import model.data_structures.HashTable;
import model.data_structures.IGraph;
import model.data_structures.MST;
import model.data_structures.MaxHeapPQ;
import model.data_structures.UndirectedGraph;

/**
 * Definición del modelo del mundo.
 * @author Camilo Martínez & Nicolás Quintero
 */
@SuppressWarnings( "rawtypes" )
public class Modelo
{
	/**
	 * Variables diseñadas para hacer más rápida la carga de comparendos.
	 */
	private ArrayList<double[]> ultimas = new ArrayList<double[]>( );
	private int z = 0;

	/**
	 * Tamaño de las agrupaciones de los vértices en kilómetros. Representa la
	 * distancia mínima entre vértices que debe haber para considerarse del mismo
	 * grupo.
	 */
	private static final double TAMANIO_AGRUPACIONES = 2;

	/**
	 * Número de estaciones de policía.
	 */
	private static final int NUMERO_ESTACIONES_POLICIA = 21;

	/**
	 * Número de comparendos.
	 */
	private static final int NUMERO_COMPARENDOS = 527655;

	/**
	 * Constantes para la construcción del HTML para pintar el grafo.
	 */
	private static final String GRAFO_GOOGLE_MAPS = "./data/grafo.html";
	private static final String APERTURA_HTML = "./data/HTML/Javascript_Inicio.txt";
	private static final String CIERRE_HTML = "./data/HTML/Javascript_Final.txt";
	private static final String APERTURA_SCRIPT = "./data/HTML/inicio_html.txt";
	private static final String CIERRE_SCRIPT = "./data/HTML/final_html.txt";
	private static final String CIRCULO = "./data/HTML/definicion_circulo.txt";
	private static final String LINEA = "./data/HTML/definicion_linea.txt";
	private static final String POSICION_LATITUD = "4.609537";
	private static final String POSICION_LONGITUD = "-74.078715";
	private static final double VALOR_ZOOM = 13.0;
	private static final double VALOR_RADIO = 200;
	private static String CIRCULO_BRUTO;
	private static String LINEA_BRUTA;

	/**
	 * Cola de prioridad que guarda la información de las estaciones de policía.
	 */
	private MaxHeapPQ<EstacionPolicia> estaciones;

	/**
	 * Heap de prioridad que guarda la información de los comparendos.
	 */
	private MaxHeapPQ<Comparendo> comparendos;

	/**
	 * Grafo cargado a partir de las fuentes de datos.
	 */
	private UndirectedGraph<String, Comparendo, EstacionPolicia> grafoFD;

	/**
	 * Grafo cargado a partir de un archivo JSON.
	 */
	private UndirectedGraph<String, Comparendo, EstacionPolicia> grafoJS;

	/**
	 * Agrupa los vértices más cercanos del grafo. La llave es la concatenación de
	 * la latitud y longitud de un vértice representante del grupo y el valor es la
	 * lista de ID's de los vértices del grupo.
	 */
	private HashTable<String, Integer> agrupacionesVertices;

	/**
	 * Hex-Colores usados para pintar los componentes conexos.
	 */
	private String[] colores;

	/**
	 * Constructor del modelo del mundo.
	 * @throws IOException Si hubo un problema leyendo los archivos plantilla para
	 *                     pintar en Google Maps.
	 */
	public Modelo( ) throws IOException
	{
		estaciones = null;
		agrupacionesVertices = new HashTable<String, Integer>( 7 );
		comparendos = null;
		grafoFD = null;
		grafoJS = null;

		// Lectura de formatos/plantillas para pintar en Google Maps.
		CIRCULO_BRUTO = leerArchivo( CIRCULO );
		LINEA_BRUTA = leerArchivo( LINEA );

		// Colores random para cada una de las estaciones.
		colores = new String[NUMERO_ESTACIONES_POLICIA];
		for( int i = 0; i < NUMERO_ESTACIONES_POLICIA; i++ )
		{
			int r = ( int ) Math.round( Math.random( ) * 255 );
			int g = ( int ) Math.round( Math.random( ) * 255 );
			int b = ( int ) Math.round( Math.random( ) * 255 );
			colores[i] = String.format( "#%02X%02X%02X", r, g, b );
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// CARGA Y CONSTRUCCIÓN DE JSON's
	// ---------------------------------------------------------------------------------------------------------------------------

	/**
	 * Carga los comparendos en un heap de prioridad que luego utiliza para meter
	 * los comparendos dentro del grafo en el respectivo vertice cuya distancia
	 * haversiana al comparendo sea la más cercana.
	 * @param rutaArchivo Archivo donde están guardados los comparendos. rutaArchivo
	 *                    != null, != ""
	 * @throws IOException Si hubo un problema de lectura del archivo.
	 */
	public void cargarComparendosEnGrafo( String rutaArchivo ) throws IOException, IllegalStateException
	{
		if( grafoFD != null )
		{
			cargarComparendos( rutaArchivo );
			Comparendo c;
			int verticeMasCercano;
			int i = -1;
			while( ++i < comparendos.getSize( ) )
			{
				c = comparendos.peekPosition( i );
				verticeMasCercano = darVerticeMasCercanoA( c.darLatitud( ), c.darLongitud( ), 0.5 );
				grafoFD.insertVertexItem( verticeMasCercano, verticeMasCercano + "", c );
			}
		}
	}

	/**
	 * Carga las estaciones de policía dentro del grafo.
	 * @param rutaArchivo Archivo donde están guardadas las estaciones de policía.
	 *                    rutaArchivo != null, != ""
	 * @throws IOException           Si hay un problema de lectura del archivo.
	 * @throws IllegalStateException Si alguno de los vértices no es válido.
	 */
	public void cargarEstacionesEnGrafo( String rutaArchivo ) throws IOException, IllegalStateException
	{
		if( grafoFD != null )
		{
			cargarEstacionesDePolicia( rutaArchivo );
			int i = -1;
			while( ++i < estaciones.getSize( ) )
			{
				EstacionPolicia e = estaciones.peekPosition( i );
				int verticeMasCercano = darVerticeMasCercanoA( e.darLatitud( ), e.darLongitud( ), 0.1 );
				grafoFD.setVertexDistinctiveItem( verticeMasCercano, e );
			}
		}
	}

	/**
	 * Actualiza el costo de tipo integer que tiene cada arco que corresponde al
	 * número de comparendos guardados entre los dos vértices.
	 * @throws IllegalStateException Si alguno de los ID's de los vértices no es
	 *                               válido.
	 */
	public void actualizarCostosEnGrafo( ) throws IllegalStateException
	{
		if( grafoFD != null )
		{
			Iterator<String> iter = grafoFD.edges( );
			while( iter.hasNext( ) )
			{
				String arco = iter.next( );
				int v = Integer.parseInt( arco.split( "-" )[0] );
				int w = Integer.parseInt( arco.split( "-" )[1] );
				grafoFD.setEdgeIntegerCost( v, w, grafoFD.numberOfItemsOf( v ) + grafoFD.numberOfItemsOf( w ) );
			}
		}
	}

	/**
	 * Carga los datos de los comparendos en una lista enlazada, los cuales se
	 * encuentran dentro de la ruta del archivo dada por parámetro. <b>post:<\b> La
	 * lista enlazada queda construída.
	 * @param rutaArchivo Ruta del archivo donde se encuentran los comparendos.
	 *                    rutaArchivo != null, rutaArchivo != ""
	 * @return True si la lista enlazada fue cargada correctamente, false de lo
	 *         contrario.
	 * @throws IOException Si hay un problema de lectura del archivo JSON.
	 */
	public void cargarComparendos( String rutaArchivo ) throws IOException
	{
		comparendos = new MaxHeapPQ<>( NUMERO_COMPARENDOS );

		InputStream is = new DataInputStream( new FileInputStream( rutaArchivo ) );
		ObjectMapper mapper = new ObjectMapper( );

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
				Comparendo c = mapper.readValue( jsonParser, Comparendo.class );
				comparendos.insert( c ); // Inserta el comparendo deserializado en el heap de prioridad.
			}
		}
	}

	/**
	 * Carga los datos de las estaciones de policía en un heap de prioridad.
	 * @param rutaArchivo Ruta del archivo donde se encuentran las estaciones de
	 *                    policía. rutaArchivo != null, rutaArchivo != ""
	 * @return True si la pila fue cargada correctamente, false de lo contrario.
	 * @throws IOException Si hay un problema de lectura en el archivo JSON.
	 */
	public void cargarEstacionesDePolicia( String rutaArchivo ) throws IOException
	{
		estaciones = new MaxHeapPQ<>( NUMERO_ESTACIONES_POLICIA );

		ObjectMapper mapper = new ObjectMapper( );
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
	 * @param rutaArchivoVertices Archivo donde están los vertices.
	 *                            rutaArchivoVertices != null, != ""
	 * @param rutaArchivoArcos    Archivo donde están los arcos. rutaArchivoArcos !=
	 *                            null, != ""
	 * @return Cadena con la cantidad de vertices y arcos creados como reporte.
	 * @throws IOException Si hay un problema en la lectura de los archivos.
	 */
	public void cargarGrafoFuentesDeDatos( String rutaArchivoVertices, String rutaArchivoArcos ) throws IOException
	{
		// Se lee todo el archivo para ver cuántos vertices se necesitan.
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
		double latitud, longitud, distanciaMenor, latitudMenor, longitudMenor, latitudA, longitudA, distanciaActual,
				longitudAdyacente, latitudAdyacente, costo;
		int id;
		while( vertice != null )
		{
			id = Integer.parseInt( vertice.split( "," )[0] );

			// Se quita el id de la línea y se queda con la latitud y longitud concatenadas
			// con ",".
			String info = vertice.replaceFirst( id + ",", "" );

			if( agrupacionesVertices.isEmpty( ) )
				agrupacionesVertices.put( info, id );
			else
			{
				latitud = Double.parseDouble( info.split( "," )[1] );
				longitud = Double.parseDouble( info.split( "," )[0] );
				distanciaMenor = 10000.0; // Distancia obviamente mayor que las demás.
				latitudMenor = -1;
				longitudMenor = -1;
				for( String verticeRepresentante : agrupacionesVertices )
				{
					latitudA = Double.parseDouble( verticeRepresentante.split( "," )[1] );
					longitudA = Double.parseDouble( verticeRepresentante.split( "," )[0] );
					distanciaActual = Haversine.distance( latitud, longitud, latitudA, longitudA );

					if( distanciaMenor > distanciaActual )
					{
						distanciaMenor = distanciaActual;
						latitudMenor = latitudA;
						longitudMenor = longitudA;
					}
				}

				// Si la distancia menor se pasa del límite, se crea un
				// nuevo grupo.
				if( distanciaMenor > TAMANIO_AGRUPACIONES )
					agrupacionesVertices.put( longitud + "," + latitud, id );
				// Si no, se añade el ID a la llave con la latitud y longitud correspondientes
				// al vértice representante del grupo, con el cual posee la menor distancia.
				else
					agrupacionesVertices.put( longitudMenor + "," + latitudMenor, id );
			}

			// Se añade el vértice.
			grafoFD.setVertexInfo( id, info );
			vertice = reader.readLine( );
		}

		reader.close( );

		// LECTURA DE ARCOS.
		reader = new BufferedReader( new FileReader( rutaArchivoArcos ) );
		String arco = reader.readLine( );

		int idAdyacente;
		while( arco != null )
		{
			if( !arco.startsWith( "#" ) )
			{
				String[] linea = arco.split( " " );

				// Solo aparece el id del vértice y ninguno adyacente.
				if( linea.length > 1 )
				{
					id = Integer.parseInt( linea[0] );
					String infoVertice = grafoFD.getVertexInfo( id );
					longitud = Double.parseDouble( infoVertice.split( "," )[0] );
					latitud = Double.parseDouble( infoVertice.split( "," )[1] );

					for( int i = 1; i < linea.length; i++ )
					{
						idAdyacente = Integer.parseInt( linea[i] );
						String infoAdyacente = grafoFD.getVertexInfo( idAdyacente );
						longitudAdyacente = Double.parseDouble( infoAdyacente.split( "," )[0] );
						latitudAdyacente = Double.parseDouble( infoAdyacente.split( "," )[1] );
						costo = Haversine.distance( latitud, longitud, latitudAdyacente, longitudAdyacente );

						// Se añade el arco.
						grafoFD.addEdge( id, idAdyacente, costo );
					}
				}
			}
			arco = reader.readLine( );
		}

		reader.close( );
	}

	/**
	 * Construye un archivo JSON que contiene la información del grafo.
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

			Iterator<Integer> iter = grafoFD.vertexAdjacentTo( i );
			while( iter.hasNext( ) )
			{
				w.println( "\t\t\t\t" + "{" );
				idAdyacente = iter.next( );
				w.println( "\t\t\t\t\t" + "\"id\": " + idAdyacente + "," );
				w.println( "\t\t\t\t\t" + "\"costo\": " + grafoFD.getEdgeDoubleCost( i, idAdyacente ) );

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
	 * @param rutaArchivo Archivo JSON de donde sacar la información del grafo.
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

	// ---------------------------------------------------------------------------------------------------------------------------
	// PARTE C
	// ---------------------------------------------------------------------------------------------------------------------------

	/**
	 * PARTE C. Punto 2.
	 * @return Grafo de las zonas de impacto.
	 */
	public UndirectedGraph<?, ?, Integer> crearGrafoDeZonasDeImpacto( )
	{
		// Grafo cuyos vértices contienen o la información de una estación de policía o
		// la información del número de comparendos contenidos en ellos. El elemento
		// distintivo de un vértice de un grafo es un entero que posee la cantidad de
		// comparendos guardados en ellos. En el caso de la estación de policía, tendrá
		// el número de comparendos que son atendidos por esta.
		UndirectedGraph<?, ?, Integer> g = new UndirectedGraph<>( grafoFD.numberOfVertices( ) );

		// Se inserta la información de las estaciones en los vértices 0 a
		// NUMERO_ESTACIONES_POLICIA - 1.
		for( int i = 0; i < NUMERO_ESTACIONES_POLICIA; i++ )
		{
			g.setVertexInfo( i,
					estaciones.peekPosition( i ).darLongitud( ) + "," + estaciones.peekPosition( i ).darLatitud( ) );
			g.setVertexDistinctiveItem( i, 0 );
		}

		int i = -1;
		while( ++i < grafoFD.numberOfVertices( ) )
		{
			// Mientras que haya comparendos dentro de un vértice.
			if( grafoFD.numberOfItemsOf( i ) != 0 )
			{
				int idEstacionMasCercana = -1;
				double distanciaMenor = 10000.0;

				// Se busca la estación cuya distancia sea la menor.
				for( int j = 0; j < NUMERO_ESTACIONES_POLICIA; j++ )
				{
					double latitud = Double.parseDouble( grafoFD.getVertexInfo( i ).split( "," )[1] );
					double longitud = Double.parseDouble( grafoFD.getVertexInfo( i ).split( "," )[0] );
					double distanciaActual = Haversine.distance( estaciones.peekPosition( j ).darLatitud( ),
							estaciones.peekPosition( j ).darLongitud( ), latitud, longitud );

					if( distanciaActual < distanciaMenor )
					{
						distanciaMenor = distanciaActual;
						idEstacionMasCercana = j;
					}
				}

				// Se agrega la información del vértice.
				g.setVertexInfo( i, grafoFD.getVertexInfo( i ) );

				// Se agrega a la estación el comparendo actual y el costo entero del arco que
				// los une es el número de comparendos dentro del vértice.
				g.addEdge( idEstacionMasCercana, i, distanciaMenor );
				g.setEdgeIntegerCost( idEstacionMasCercana, i, grafoFD.numberOfItemsOf( i ) );

				// Se aumenta el número de comparendos atendidos en la estación.
				g.setVertexDistinctiveItem( idEstacionMasCercana,
						g.getVertexDistinctiveItem( idEstacionMasCercana ) + grafoFD.numberOfItemsOf( i ) );
			}
		}

		return g;
	}

	/**
	 * PARTE C. Punto 2.
	 * @return Reporte con la información solicitada.
	 * @throws IOException Si hay algún problema de lectura de archivos.
	 */
	public String identificarZonasDeImpacto( ) throws IOException
	{
		String reporte = "";

		Stopwatch timer = new Stopwatch( );
		UndirectedGraph<?, ?, Integer> g = crearGrafoDeZonasDeImpacto( );
		reporte += "Tiempo que toma el algoritmo en crear el grafo: " + timer.elapsedTime( ) + " ms\n";
		reporte += "Número de vértices del grafo: " + g.numberOfVertices( ) + "\n";
		reporte += "Número de arcos del grafo: " + g.numberOfEdges( ) + "\n\n";
		for( int i = 0; i < NUMERO_ESTACIONES_POLICIA; i++ )
		{
			reporte += "Cantidad de comparendos que atiende la estación de ID " + estaciones.peekPosition( i ).darId( )
					+ ": " + g.getVertexDistinctiveItem( i ) + "\n";
		}

		pintarZonasDeImpactoGoogleMaps( g );
		return reporte;
	}

	/**
	 * PARTE C. Punto 2.
	 * @return Reporte con la información solicitada.
	 * @throws IOException Si hay un problema en la lectura de algún archivo.
	 */
	public void pintarZonasDeImpactoGoogleMaps( UndirectedGraph<?, ?, Integer> g ) throws IOException
	{
		FileWriter w = inicializarHTML( );

		// Se itera sobre los vertices.
		int i = -1;
		while( ++i < NUMERO_ESTACIONES_POLICIA )
		{
			String infoVertice = g.getVertexInfo( i );
			String latitud1 = infoVertice.split( "," )[1];
			String longitud1 = infoVertice.split( "," )[0];

			// Se cuentan los comparendos contenidos en esta estación.
			double incrementador = 3 * ( 1 + g.getVertexDistinctiveItem( i ) / grafoFD.numberOfStoredItems( ) );

			w = pintarUnCirculo( w, VALOR_RADIO * incrementador, colores[i], latitud1, longitud1 );

			// Se itera sobre las adyacencias.
			for( Edge<?, ?, Integer> e : g.edgesAdjacentTo( i ) )
			{
				String latitud2 = g.getVertexInfo( e.other( i ) ).split( "," )[1];
				String longitud2 = g.getVertexInfo( e.other( i ) ).split( "," )[0];
				w = pintarUnaLinea( w, colores[i], latitud1, longitud1, latitud2, longitud2 );
				w = pintarUnCirculo( w, VALOR_RADIO / 2, colores[i], latitud2, longitud2 );
			}

			i++;
		}

		finalizarHTML( w );
		abrirGrafoEnNavegador( );
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// PINTAR EN GOOGLE MAPS
	// ---------------------------------------------------------------------------------------------------------------------------

	public FileWriter inicializarHTML( ) throws IOException
	{
		File f = new File( GRAFO_GOOGLE_MAPS );
		FileWriter w = new FileWriter( f, false );

		// Principio del archivo HTML.
		String apertura = leerArchivo( APERTURA_HTML );
		w.append( apertura + "\n" );

		// Principio del script.
		apertura = leerArchivo( APERTURA_SCRIPT );
		apertura = apertura.replace( "VALOR_ZOOM", String.valueOf( VALOR_ZOOM ) );
		apertura = apertura.replace( "LATITUD", POSICION_LATITUD );
		apertura = apertura.replace( "LONGITUD", POSICION_LONGITUD );

		w.append( apertura + "\n" );

		return w;
	}

	public void finalizarHTML( FileWriter w ) throws IOException
	{
		// Cierre del script.
		String cierre = leerArchivo( CIERRE_SCRIPT );

		w.append( cierre + "\n" );

		// Final del archivo HTML.
		cierre = leerArchivo( CIERRE_HTML );
		w.append( cierre + "\n" );
		w.close( );
	}

	public FileWriter pintarUnCirculo( FileWriter w, double radio, String color, String latitud, String longitud )
			throws IOException
	{
		// Plantilla para construir un círculo.
		String circuloBruto = CIRCULO_BRUTO.replace( "VALOR_RADIO", String.valueOf( radio ) );

		ArrayList<String[]> remplazos = new ArrayList<String[]>( );
		remplazos.add( new String[] { "LATITUD", latitud } );
		remplazos.add( new String[] { "LONGITUD", longitud } );
		remplazos.add( new String[] { "VALOR_RADIO", String.valueOf( radio ) } );
		remplazos.add( new String[] { "COLOR", color } );

		// Se remplazan los valores de la plantilla por los proporcionados.
		String circulo = construirComponente( circuloBruto, remplazos );

		w.append( circulo + "\n" );
		return w;
	}

	public FileWriter pintarUnaLinea( FileWriter w, String color, String latitud1, String longitud1, String latitud2,
			String longitud2 ) throws IOException
	{
		ArrayList<String[]> remplazos = new ArrayList<String[]>( );
		remplazos.add( new String[] { "LATITUD_INICIO", latitud1 } );
		remplazos.add( new String[] { "LONGITUD_INICIO", longitud1 } );
		remplazos.add( new String[] { "LATITUD_FINAL", latitud2 } );
		remplazos.add( new String[] { "LONGITUD_FINAL", longitud2 } );
		remplazos.add( new String[] { "COLOR", color } );

		String linea = construirComponente( LINEA_BRUTA, remplazos );

		w.append( linea + "\n" );
		return w;
	}

	/**
	 * Muestra el grafo creado en grafo.html en el navegador.
	 * @throws IOException Si hubo un problema en la lectura del archivo.
	 */
	public void abrirGrafoEnNavegador( ) throws IOException
	{
		java.awt.Desktop.getDesktop( ).browse( ( new File( "data/grafo.html" ) ).toURI( ) );
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ALGORITMOS NECESARIOS
	// ---------------------------------------------------------------------------------------------------------------------------

	/**
	 * Retorna el id del vertice más cercano a las coordenadas dadas por parámetro.
	 * <b>pre:</b> grafoFD ya ha sido inicializado.
	 * @param latitud  Latitud a buscar más cercana.
	 * @param longitud Longitud a buscar más cercana.
	 * @param error    Tolerancia permitida en km.
	 * @return id del vertice.
	 */
	public int darVerticeMasCercanoA( double latitud, double longitud, double error )
	{
		for( double[] c : ultimas )
		{
			if( Haversine.distance( latitud, longitud, c[1], c[0] ) <= error )
				return ( int ) c[2];
		}

		int idVerticeMasCercano = -1, idActual;

		double distanciaMenor = 10000.0, longitudA, latitudA, distanciaActual;
		String verticeRepresentanteGrupo = null;

		// Se busca la agrupación cuyo vértice representante está más cerca de la
		// latitud y longitud dadas por parámetro.
		for( String verticeRepresentante : agrupacionesVertices )
		{
			longitudA = Double.parseDouble( verticeRepresentante.split( "," )[0] );
			latitudA = Double.parseDouble( verticeRepresentante.split( "," )[1] );
			distanciaActual = Haversine.distance( latitud, longitud, latitudA, longitudA );

			if( distanciaActual < distanciaMenor )
			{
				distanciaMenor = distanciaActual;
				verticeRepresentanteGrupo = verticeRepresentante;
			}
		}

		// Se itera entre los vértices de la agrupación más cercana y se añade el
		// comparendo al más cercano.
		distanciaMenor = 10000.0;
		String infoVertice;
		double longitudMenor = 0, latitudMenor = 0;
		Iterator<Integer> iter = agrupacionesVertices.valuesOf( verticeRepresentanteGrupo );
		while( iter.hasNext( ) )
		{
			idActual = iter.next( );
			infoVertice = grafoFD.getVertexInfo( idActual );
			longitudA = Double.parseDouble( infoVertice.split( "," )[0] );
			latitudA = Double.parseDouble( infoVertice.split( "," )[1] );
			distanciaActual = Haversine.distance( latitud, longitud, latitudA, longitudA );

			// Tolerancia.
			if( distanciaActual <= error )
			{
				idVerticeMasCercano = idActual;
				longitudMenor = longitudA;
				latitudMenor = latitudA;
				break;
			}

			if( distanciaActual < distanciaMenor )
			{
				distanciaMenor = distanciaActual;
				longitudMenor = longitudA;
				latitudMenor = latitudA;
				idVerticeMasCercano = idActual;
			}
		}

		if( ultimas.size( ) < 20 )
			ultimas.add( new double[] { longitudMenor, latitudMenor, idVerticeMasCercano } );
		else
		{
			if( z < ultimas.size( ) )
			{
				ultimas.set( z, new double[] { longitudMenor, latitudMenor, idVerticeMasCercano } );
				z++;
			}
			else
				z = 0;
		}

		return idVerticeMasCercano;
	}

	/**
	 * @return Comparendo con mayor OBJECTID dentro del grafo.
	 */
	public Comparendo buscarComparendoDeMayorObjectID( )
	{
		if( grafoFD != null && grafoFD.numberOfStoredItems( ) != 0 )
		{
			int i = -1;
			Comparendo mayor = null;
			while( ++i < grafoFD.numberOfVertices( ) )
			{
				Iterator<Comparendo> iter = grafoFD.vertexItems( i );

				if( mayor == null )
					mayor = iter.next( );

				while( iter.hasNext( ) )
				{
					Comparendo c = iter.next( );
					if( c.darId( ) > mayor.darId( ) )
						mayor = c;
				}
			}
			return mayor;
		}
		else
			throw new IllegalStateException(
					"El grafo aún no ha sido cargado con los comparendos respectivos en cada vertice" );
	}

	/**
	 * @return Estación de policía con mayor OBJECTID dentro del grafo.
	 */
	public EstacionPolicia buscarEstacionDeMayorObjectID( )
	{
		if( grafoFD != null && grafoFD.numberOfStoredItems( ) != 0 )
		{
			EstacionPolicia mayor = null;

			Iterator<EstacionPolicia> iter = grafoFD.distinctiveItems( );
			while( iter.hasNext( ) )
			{
				EstacionPolicia e = iter.next( );
				if( mayor == null || ( e != null && e.darId( ) > mayor.darId( ) ) )
					mayor = e;
			}

			return mayor;
		}
		else
			throw new IllegalStateException(
					"El grafo aún no ha sido cargado con los comparendos respectivos en cada vertice" );
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// RETORNO DE INFORMACIÓN EN CADENAS DE STRING
	// ---------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return Reporte de la carga de los comparendos, el grafo, las estaciones de
	 *         policía, entre otros datos que se muestran al inicio del programa.
	 */
	public String darReporteCompletoDeCarga( )
	{
		String reporte = "";
		reporte += "Número de comparendos cargados: " + grafoFD.numberOfStoredItems( ) + "\n";
		reporte += "\nComparendo con mayor OBJECTID encontrado:\n\n";
		reporte += darInformacionDeComparendo( buscarComparendoDeMayorObjectID( ) ) + "\n";
		reporte += "Número de estaciones de policía cargadas: " + grafoFD.numberOfStoredDistinctiveItems( ) + "\n";
		reporte += "\nEstación de policía con mayor OBJECTID encontrada:\n\n";
		reporte += darInformacionDeEstacion( buscarEstacionDeMayorObjectID( ) );
		reporte += darReporteGrafo( grafoFD );
		reporte += "Vertice con el mayor OBJECTID encontrado:\n";
		reporte += darInformacionDeVertice( grafoFD.numberOfVertices( ) - 1 );

		return reporte;
	}

	/**
	 * @param c Comparendo a consultar su información. c != null
	 * @return Información del comparendo.
	 */
	public String darInformacionDeComparendo( Comparendo c )
	{
		boolean[] mostrar = { true, true, true, true, true, true, true, true, true };
		return c.consultarInformacion( mostrar );
	}

	/**
	 * @param e Estación de policía a consultar su información. e != null
	 * @return Información de la estación de policía.
	 */
	public String darInformacionDeEstacion( EstacionPolicia e )
	{
		boolean[] mostrar = { true, true, true, true, true, true, true, true, true };
		return e.consultarInformacion( mostrar );
	}

	/**
	 * @param v id del vertice a consultar su información.
	 * @return id, latitud y longitud del vertice.
	 */
	public String darInformacionDeVertice( int v )
	{
		String info = "\n\tID: " + v;
		info += "\n\tLATITUD: " + grafoFD.getVertexInfo( v ).split( "," )[1];
		info += "\n\tLONGITUD: " + grafoFD.getVertexInfo( v ).split( "," )[0] + "\n";
		return info;
	}

	/**
	 * @return Cantidad de vertices y arcos creados. Se divide la cantidad de arcos
	 *         sobre 2 pues en la tabla de hash se repiten las llaves como v-w y
	 *         w-v de forma que de cualquier forma se pueda acceder al costo de
	 *         cierto arco.
	 */
	public String darReporteGrafo( IGraph grafo )
	{
		String informacion = "\n";
		informacion += "Cantidad de vertices creados: " + grafo.numberOfVertices( ) + "\n";
		informacion += "Cantidad de arcos creados: " + grafo.numberOfEdges( ) + "\n\n";
		return informacion;
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
		for( int i = 0; i < remplazo.size( ); i++ )
		{
			String original = remplazo.get( i )[0];
			String nueva = remplazo.get( i )[1];
			cadena = cadena.replace( original, nueva );
		}

		return cadena;
	}

	/**
	 * el camino de costo mínimo se debe tomar la distancia haversiana en cada arco
	 * como medida base.
	 * El punto de origen y destino son ingresados por el usuario como latitudes y
	 * longitudes (debe validarse que dichos puntos se encuentren dentro de los
	 * límites encontrados de la ciudad).
	 * Estas ubicaciones deben aproximarse a los vértices más cercanos en la malla
	 * vial.
	 */
	public void caminoDeCostoMinimoDistancia( double latitudOr, double longitudOr, double latitudDes,
			double longitudDes )
	{
	  int verticeOrigen = darVerticeMasCercanoA(latitudOr,longitudOr,50);
	  int verticeDestino= darVerticeMasCercanoA(latitudDes,longitudDes,50);
	  Dijkstra shortestPath=  new Dijkstra(grafoFD,verticeOrigen,verticeDestino);
      shortestPath.print(verticeDestino);
      

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