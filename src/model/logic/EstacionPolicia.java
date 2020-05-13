package model.logic;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Clase que maneja la informaci�n de una estaci�n de polic�a.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class EstacionPolicia implements Comparable<EstacionPolicia>
{
	@JsonInclude( JsonInclude.Include.NON_NULL )
	@JsonPropertyOrder( { "OBJECTID", "EPOCOD_PLAN", "EPOCOD_ENT", "EPOCOD_PROY", "EPOANIO_GEO", "EPOFECHA_INI",
			"EPOFECHA_FIN", "EPODESCRIP", "EPOEST_PROY", "EPOINTERV_ESP", "EPODIR_SITIO", "EPOCOD_SITIO", "EPOLATITUD",
			"EPOLONGITU", "EPOSERVICIO", "EPOHORARIO", "EPOTELEFON", "EPOCELECTR", "EPOCONTACT", "EPOPWEB",
			"EPOIUUPLAN", "EPOIUSCATA", "EPOIULOCAL", "EPOEASOCIA", "EPOFUNCION", "EPOTEQUIPA", "EPONOMBRE",
			"EPOIDENTIF", "EPOFECHA_C" } )

	/**
	 * Identificador �nico de la estaci�n. OBJECT_ID
	 */
	@JsonProperty( "OBJECTID" )
	private String id;

	/**
	 * Coordenadas; la longitud y latitud geogr�ficas.
	 */
	@JsonProperty( "EPOLATITUD" )
	private double latitud;

	@JsonProperty( "EPOLONGITU" )
	private double longitud;

	/**
	 * @return Identificador �nico de la estaci�n.
	 */
	@JsonProperty( "OBJECTID" )
	public String darId( )
	{
		return id;
	}

	/**
	 * @return Longitud de la estaci�n.
	 */
	@JsonProperty( "EPOLONGITU" )
	public double darLongitud( )
	{
		return longitud;
	}

	/**
	 * @return Latitud de la estaci�n.
	 */
	@JsonProperty( "EPOLATITUD" )
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
	@JsonIgnore
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
	@JsonIgnore
	public int compareTo( EstacionPolicia o )
	{
		return this.id.compareTo( o.darId( ) );
	}
}