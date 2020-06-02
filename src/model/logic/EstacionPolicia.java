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

	@JsonProperty( "OBJECTID" )
	private int id;

	@JsonProperty( "EPODESCRIP" )
	private String descripcion;

	@JsonProperty( "EPODIR_SITIO" )
	private String dirSitio;

	@JsonProperty( "EPOSERVICIO" )
	private String servicio;

	@JsonProperty( "EPOHORARIO" )
	private String horario;

	@JsonProperty( "EPOTELEFON" )
	private String telefono;

	@JsonProperty( "EPOIULOCAL" )
	private String local;

	@JsonProperty( "EPOLATITUD" )
	private double latitud;

	@JsonProperty( "EPOLONGITU" )
	private double longitud;

	@JsonProperty( "OBJECTID" )
	public int darId( )
	{
		return id;
	}

	@JsonProperty( "EPOLONGITU" )
	public double darLongitud( )
	{
		return longitud;
	}

	@JsonProperty( "EPOLATITUD" )
	public double darLatitud( )
	{
		return latitud;
	}

	/**
	 * Consulta la informaci�n de la estaci�n de polic�a.
	 * @param mostrar Arreglo booleano de tama�o 9 que indica si mostrar uno cierto
	 *                atributo de la estaci�n de polic�a.
	 * @return Cadena con la informaci�n pedida de la estaci�n de polic�a.
	 */
	@JsonIgnore
	public String consultarInformacion( boolean[] mostrar )
	{
		String cadena = "";
		ArrayList<String> informacion = new ArrayList<String>( );
		informacion.add( "OBJECTID: " + id );
		informacion.add( "EPODESCRIP: " + descripcion );
		informacion.add( "EPODIR_SITIO: " + dirSitio );
		informacion.add( "EPOLATITUD: " + String.valueOf( latitud ) );
		informacion.add( "EPOLONGITUD: " + String.valueOf( longitud ) );
		informacion.add( "EPOSERVICIO: " + servicio );
		informacion.add( "EPOHORARIO: " + horario );
		informacion.add( "EPOTELEFON: " + telefono );
		informacion.add( "EPOIULOCAL: " + local );

		for( int i = 0; i < informacion.size( ); i++ )
			if( mostrar[i] )
				cadena += "\t" + informacion.get( i ) + "\n";

		return cadena;
	}

	@Override
	@JsonIgnore
	public int compareTo( EstacionPolicia o )
	{
		if( this.id < o.darId( ) )
			return -1;
		else if( this.id > o.darId( ) )
			return 1;
		else
			return 0;
	}
}