package model.logic;

import java.util.ArrayList;

/**
 * Clase que maneja la informaci�n de una estaci�n de polic�a.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class EstacionPolicia implements Comparable<EstacionPolicia>
{
	/**
	 * Identificador �nico de la estaci�n. OBJECT_ID
	 */
	private String id;

	/**
	 * Coordenadas; la longitud y latitud geogr�ficas.
	 */
	private double latitud, longitud;

	/**
	 * Inicializa una estaci�n con la informaci�n dada por par�metro.
	 * @param id       Identificador �nico de la estaci�n.
	 * @param latitud  Latitud de la estaci�n.
	 * @param longitud Longitud de la estaci�n.
	 */
	public EstacionPolicia( String id, double latitud, double longitud )
	{
		this.id = id;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	/**
	 * @return Identificador �nico de la estaci�n.
	 */
	public String darId( )
	{
		return id;
	}

	/**
	 * @return Longitud de la estaci�n.
	 */
	public double darLongitud( )
	{
		return longitud;
	}

	/**
	 * @return Latitud de la estaci�n.
	 */
	public double darLatitud( )
	{
		return latitud;
	}

	/**
	 * Consulta la informaci�n de la estaci�n de polic�a.
	 * @param mostrar Arreglo booleano de tama�o 8 que indica si mostrar uno cierto
	 *                atributo de la estaci�n de polic�a.
	 * @return Cadena con la informaci�n pedida de la estaci�n de polic�a.
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