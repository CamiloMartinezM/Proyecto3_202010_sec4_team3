package model.logic;

import java.util.ArrayList;

/**
 * Clase que maneja la información de una estación de policía.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class EstacionPolicia implements Comparable<EstacionPolicia>
{
	/**
	 * Identificador único de la estación. OBJECT_ID
	 */
	private String id;

	/**
	 * Coordenadas; la longitud y latitud geográficas.
	 */
	private double latitud, longitud;

	/**
	 * Inicializa una estación con la información dada por parámetro.
	 * @param id       Identificador único de la estación.
	 * @param latitud  Latitud de la estación.
	 * @param longitud Longitud de la estación.
	 */
	public EstacionPolicia( String id, double latitud, double longitud )
	{
		this.id = id;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	/**
	 * @return Identificador único de la estación.
	 */
	public String darId( )
	{
		return id;
	}

	/**
	 * @return Longitud de la estación.
	 */
	public double darLongitud( )
	{
		return longitud;
	}

	/**
	 * @return Latitud de la estación.
	 */
	public double darLatitud( )
	{
		return latitud;
	}

	/**
	 * Consulta la información de la estación de policía.
	 * @param mostrar Arreglo booleano de tamaño 8 que indica si mostrar uno cierto
	 *                atributo de la estación de policía.
	 * @return Cadena con la información pedida de la estación de policía.
	 */
	public String consultarInformacion( boolean[] mostrar )
	{
		String cadena = "";
		ArrayList<String> informacion = new ArrayList<String>( );
		informacion.add( "OBJECTID: " + id );
		informacion.add( "LATITUD: " + String.valueOf( latitud ) );
		informacion.add( "LONGITUD: " + String.valueOf( longitud ) );

		for( int i = 0; i < informacion.size( ); i++ )
			if( mostrar[i] )
				cadena += "\t" + informacion.get( i ) + "\n";

		return cadena;
	}

	@Override
	public int compareTo( EstacionPolicia o )
	{
		return this.id.compareTo( o.darId( ) );
	}
}