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

import model.data_structures.MaxHeapPQ;
import model.data_structures.MaxQueue;
import model.data_structures.UndirectedGraph;

/**
 * Definición del modelo del mundo.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Modelo
{
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
	 * Cola de prioridad que guarda la información de las estaciones de policía.
	 */
	private MaxQueue<EstacionPolicia> estaciones = new MaxQueue<>( NUMERO_ESTACIONES_POLICIA );

	/**
	 * Heap de prioridad que guarda la información de los comparendos.
	 */
	private MaxHeapPQ<Comparendo> comparendos = null;

	/**
	 * Grafo cargado a partir de las fuentes de datos.
	 */
	private UndirectedGraph<String, Comparendo, EstacionPolicia> grafoFD = null;

	/**
	 * Grafo cargado a partir de un archivo JSON.
	 */
	private UndirectedGraph<String, Comparendo, EstacionPolicia> grafoJS = null;

	/**
	 * Constructor del modelo del mundo.
	 */
	public Modelo( )
	{
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
			while( !comparendos.isEmpty( ) )
			{
				c = comparendos.poll( );
				verticeMasCercano = darVerticeMasCercanoA( c.darLatitud( ), c.darLongitud( ), 5 );
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
			EstacionPolicia e;
			int verticeMasCercano;
			while( !estaciones.isEmpty( ) )
			{
				e = estaciones.poll( );
				verticeMasCercano = darVerticeMasCercanoA( e.darLatitud( ), e.darLongitud( ), 0.1 );
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
			String arco;
			int v, w;
			Iterator<String> iter = grafoFD.edges( );
			while( iter.hasNext( ) )
			{
				arco = iter.next( );
				v = Integer.parseInt( arco.split( "-" )[0] );
				w = Integer.parseInt( arco.split( "-" )[1] );
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
	 * Carga los datos de las estaciones de policía en un heap de prioridad.
	 * @param rutaArchivo Ruta del archivo donde se encuentran las estaciones de
	 *                    policía. rutaArchivo != null, rutaArchivo != ""
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

		String info;
		int id;
		while( vertice != null )
		{
			id = Integer.parseInt( vertice.split( "," )[0] );

			// Se quita el id de la línea y se queda con la latitud y longitud concatenadas
			// con ",".
			info = vertice.replaceFirst( id + ",", "" );

			// Se añade el vértice.
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

				// Solo aparece el id del vértice y ninguno adyacente.
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
	// ALGORITMOS NECESARIOS
	// ---------------------------------------------------------------------------------------------------------------------------

	/**
	 * Retorna el id del vertice más cercano a las coordenadas dadas por parámetro.
	 * <b>pre:</b> grafoFD ya ha sido inicializado.
	 * @param latitud  Latitud a buscar más cercana.
	 * @param longitud Longitud a buscar más cercana.
	 * @return id del vertice.
	 */
	public int darVerticeMasCercanoA( double latitud, double longitud, double error )
	{
		int id = 0;
		int i = 0;
		String infoVertice;
		double latitudA, longitudA, distanciaMenor = 0, distanciaActual;
		while( i < grafoFD.numberOfVertices( ) )
		{
			infoVertice = grafoFD.getVertexInfo( i );
			longitudA = Double.parseDouble( infoVertice.split( "," )[0] );
			latitudA = Double.parseDouble( infoVertice.split( "," )[1] );
			distanciaActual = Haversine.distance( latitud, longitud, latitudA, longitudA );

			if( distanciaActual <= error )
			{
				id = i;
				break;
			}

			if( distanciaActual < distanciaMenor )
			{
				distanciaMenor = distanciaActual;
				id = i;
			}
			i++;
		}

		return id;
	}

	/**
	 * @return Comparendo con mayor OBJECTID dentro del grafo.
	 */
	public Comparendo buscarComparendoDeMayorObjectID( )
	{
		if( grafoFD != null && grafoFD.numberOfStoredItems( ) != 0 )
		{
			int i = 0;
			Comparendo c;
			Comparendo mayor = null;
			while( i < grafoFD.numberOfVertices( ) )
			{
				Iterator<Comparendo> iter = grafoFD.vertexItems( i );
				while( iter.hasNext( ) )
				{
					c = iter.next( );

					if( mayor == null || ( c != null && c.darId( ) > mayor.darId( ) ) )
						mayor = c;
				}
				i++;
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
			EstacionPolicia e;
			EstacionPolicia mayor = null;

			Iterator<EstacionPolicia> iter = grafoFD.distinctiveItems( );
			while( iter.hasNext( ) )
			{
				e = iter.next( );
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
	public String darReporteGrafo( @SuppressWarnings( "rawtypes" ) UndirectedGraph grafo )
	{
		String informacion = "\n";
		informacion += "Cantidad de vertices creados: " + grafo.numberOfVertices( ) + "\n";
		informacion += "Cantidad de arcos creados: " + grafo.numberOfEdges( ) + "\n\n";
		return informacion;
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
		w.append( apertura + "\n\n" );

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

		String circulo, linea, latitud1, longitud1, latitud2, longitud2;
		String[] remplazoLatitud, remplazoLongitud;
		ArrayList<String[]> remplazos;

		// Se itera sobre los vertices.
		int i = 0;
		while( i < grafoFD.numberOfVertices( ) )
		{
			String infoVertice = grafoFD.getVertexInfo( i );
			latitud1 = infoVertice.split( "," )[1];
			longitud1 = infoVertice.split( "," )[0];
			remplazoLatitud = new String[] { "LATITUD", latitud1 };
			remplazoLongitud = new String[] { "LONGITUD", longitud1 };
			remplazos = new ArrayList<String[]>( );
			remplazos.add( remplazoLatitud );
			remplazos.add( remplazoLongitud );
			circulo = construirComponente( circuloBruto, remplazos );

			w.append( circulo + "\n" );
			i++;
		}

		// Se itera sobre los arcos existentes.
		Iterator<String> iter = grafoFD.edges( );
		String arco;
		int id1, id2;
		while( iter.hasNext( ) )
		{
			arco = iter.next( );
			id1 = Integer.valueOf( arco.split( "-" )[0] );
			id2 = Integer.valueOf( arco.split( "-" )[1] );
			latitud1 = String.valueOf( grafoFD.getVertexInfo( id1 ).split( "," )[1] );
			longitud1 = String.valueOf( grafoFD.getVertexInfo( id1 ).split( "," )[0] );

			latitud2 = String.valueOf( grafoFD.getVertexInfo( id2 ).split( "," )[1] );
			longitud2 = String.valueOf( grafoFD.getVertexInfo( id2 ).split( "," )[0] );

			remplazos = new ArrayList<String[]>( );
			remplazoLatitud = new String[] { "LATITUD_INICIO", latitud1 };
			remplazoLongitud = new String[] { "LONGITUD_INICIO", longitud1 };
			remplazos.add( remplazoLatitud );
			remplazos.add( remplazoLongitud );
			remplazoLatitud = new String[] { "LATITUD_FINAL", latitud2 };
			remplazoLongitud = new String[] { "LONGITUD_FINAL", longitud2 };
			remplazos.add( remplazoLatitud );
			remplazos.add( remplazoLongitud );

			linea = construirComponente( lineaBruta, remplazos );

			w.append( linea + "\n" );
		}

		// Cierre del script.
		String cierre = leerArchivo( CIERRE_SCRIPT );

		w.append( "\n\n" + cierre + "\n" );

		// Final del archivo HTML.
		cierre = leerArchivo( CIERRE_HTML );
		w.append( cierre + "\n" );
		w.close( );

		// Se muestra el archivo HTML en el navegador.
		f = new File( "data/grafo.html" );
		java.awt.Desktop.getDesktop( ).browse( f.toURI( ) );
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