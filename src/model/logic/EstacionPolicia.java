package model.logic;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Clase que maneja la información de una estación de policía.
 * @author Camilo Martínez & Nicolás Quintero
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
	 * Identificador único de la estación. OBJECT_ID
	 */
	@JsonProperty( "OBJECTID" )
	private String id;

	/**
	 * Coordenadas; la longitud y latitud geográficas.
	 */
	@JsonProperty( "EPOLATITUD" )
	private double latitud;

	@JsonProperty( "EPOLONGITU" )
	private double longitud;

	/**
	 * @return Identificador único de la estación.
	 */
	@JsonProperty( "OBJECTID" )
	public String darId( )
	{
		return id;
	}

	/**
	 * @return Longitud de la estación.
	 */
	@JsonProperty( "EPOLONGITU" )
	public double darLongitud( )
	{
		return longitud;
	}

	/**
	 * @return Latitud de la estación.
	 */
	@JsonProperty( "EPOLATITUD" )
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