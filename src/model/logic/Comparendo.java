package model.logic;

import java.util.ArrayList;

/**
 * Clase que maneja la informaci�n de un comparendo.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class Comparendo implements Comparable<Comparendo>
{
	/**
	 * Identificador �nico del comparendo. OBJECTID.
	 */
	private String id;

	/**
	 * Fecha del comparendo. FECHA_HORA.
	 */
	private String fecha;

	/**
	 * Medio de detenci�n. MEDIO_DETE.
	 */
	private String medioDeteccion;

	/**
	 * Clase de veh�culo. CLASE_VEHI.
	 */
	private String claseVehiculo;

	/**
	 * Tipo de servicio del autom�vil. TIPO_SERVI.
	 */
	private String tipoServicio;

	/**
	 * C�digo del comparendo cometido. INFRACCION
	 */
	private String codigoInfraccion;

	/**
	 * Descripci�n del comparendo. DES_INFRAC.
	 */
	private String descripcionInfraccion;

	/**
	 * Localidad donde fue cometida la infracci�n. LOCALIDAD.
	 */
	private String localidad;

	/**
	 * Coordenada, donde est� la longitud y latitud geogr�ficas.
	 */
	private float[] coordenada;

	/**
	 * Inicializa un comparendo con la informaci�n dada por par�metro.
	 * @param id                    Identificador �nico del comparendo.
	 * @param fecha                 Fecha del comparendo.
	 * @param medioDeteccion        Medio por el cual fue realizada la detenci�n.
	 * @param claseVehiculo         Tipo de veh�culo. Ejemplo: AUTOMOVIL, BICICLETA,
	 *                              BUS, ...
	 * @param tipoServicio          Tipo de servicio que prestaba el autom�vil.
	 *                              Ejemplo: Particular, p�blico, ...
	 * @param codigoInfraccion      C�digo de la infracci�n cometida.
	 * @param descripcionInfraccion Descripci�n de la infracci�n cometida.
	 * @param localidad             Localidad en la ciudad del comparendo.
	 * @param coordenada            Coordenada donde ocurri� la infracci�n.
	 */
	public Comparendo( String id, String fecha, String medioDetencion, String claseVehiculo, String tipoServicio,
			String codigoInfraccion, String causaInfraccion, String localidad, float[] coordenada )
	{
		this.id = id;
		this.fecha = fecha;
		this.medioDeteccion = medioDetencion;
		this.claseVehiculo = claseVehiculo;
		this.tipoServicio = tipoServicio;
		this.codigoInfraccion = codigoInfraccion;
		this.descripcionInfraccion = causaInfraccion;
		this.localidad = localidad;
		this.coordenada = coordenada;
	}

	/**
	 * @return Identificador �nico del comparendo.
	 */
	public String darId( )
	{
		return id;
	}

	/**
	 * Cambia el identificador �nico actual por uno nuevo dado por par�metro.
	 * @param id Nuevo ID.
	 */
	public void cambiarId( String id )
	{
		this.id = id;
	}

	/**
	 * @return Fecha del comparendo.
	 */
	public String darFecha( )
	{
		return fecha;
	}

	/**
	 * Cambia la fecha actual por una nueva dada por par�metro.
	 * @param fecha Nueva fecha.
	 */
	public void cambiarFecha( String fecha )
	{
		this.fecha = fecha;
	}

	/**
	 * @return medioDeteccion Medio por el cual se detuvo el autom�vil.
	 */
	public String darMedioDetencion( )
	{
		return medioDeteccion;
	}

	/**
	 * Cambia el medio de detenci�n actual por uno nuevo dado por par�metro.
	 * @param medioDeteccion Nuevo medio de detenci�n.
	 */
	public void cambiarMedioDetencion( String medioDetencion )
	{
		this.medioDeteccion = medioDetencion;
	}

	/**
	 * @return claseVehiculo Tipo de veh�culo.
	 */
	public String darClaseVehiculo( )
	{
		return claseVehiculo;
	}

	/**
	 * Cambia el tipo de veh�culo actual por uno nuevo dado por par�metro.
	 * @param claseVehiculo Nuevo tipo de veh�culo.
	 */
	public void cambiarClaseVehiculo( String claseVehiculo )
	{
		this.claseVehiculo = claseVehiculo;
	}

	/**
	 * @return tipoServicio Tipo de servicio.
	 */
	public String darTipoServicio( )
	{
		return tipoServicio;
	}

	/**
	 * Cambia el tipo de servicio actual por uno nuevo dado por par�metro.
	 * @param tipoServicio Nuevo tipo de servicio.
	 */
	public void cambiarTipoServicio( String tipoServicio )
	{
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return codigoInfraccion C�digo de la infracci�n cometida.
	 */
	public String darCodigoInfraccion( )
	{
		return codigoInfraccion;
	}

	/**
	 * Cambia el c�digo de la infracci�n actual por uno nuevo dado por par�metro.
	 * @param codigoInfraccion Nuevo c�digo de la infracci�n.
	 */
	public void cambiarCodigoInfraccion( String codigoInfraccion )
	{
		this.codigoInfraccion = codigoInfraccion;
	}

	/**
	 * @return descripcionInfraccion Descripci�n de la infracci�n.
	 */
	public String darDescripcionInfraccion( )
	{
		return descripcionInfraccion;
	}

	/**
	 * Cambia la causa de la infracci�n actual por una nueva dada por par�metro.
	 * @param causaInfraccion Nueva causa de la infracci�n.
	 */
	public void cambiarCausaInfraccion( String causaInfraccion )
	{
		this.descripcionInfraccion = causaInfraccion;
	}

	/**
	 * @return localidad Localidad donde ocurri� la infracci�n.
	 */
	public String darLocalidad( )
	{
		return localidad;
	}

	/**
	 * Cambia la localidad actual por una nueva dada por par�metro.
	 * @param localidad Nueva localidad.
	 */
	public void cambiarLocalidad( String localidad )
	{
		this.localidad = localidad;
	}

	/**
	 * @return Longitud de la infracci�n.
	 */
	public float darLongitud( )
	{
		return coordenada[0];
	}

	/**
	 * @return Latitud de la infracci�n.
	 */
	public float darLatitud( )
	{
		return coordenada[1];
	}

	/**
	 * Cambia la coordenada actual por una nueva dada por par�metro.
	 * @param coordenada Coordenada nueva.
	 */
	public void cambiarCoordenada( float[] coordenada )
	{
		this.coordenada = coordenada;
	}

	/**
	 * Consulta la informaci�n del comparendo.
	 * @param mostrar Arreglo booleano de tama�o 8 que indica si mostrar uno cierto
	 *                atributo del comparendo.
	 * @return Cadena con la informaci�n pedida del comparendo.
	 */
	public String consultarInformacion( boolean[] mostrar )
	{
		String cadena = "";
		ArrayList<String> informacion = new ArrayList<String>( );
		informacion.add( "OBJECTID: " + id );
		informacion.add( "FECHA_HORA: " + fecha );
		informacion.add( "MEDIO_DETECCION: " + medioDeteccion );
		informacion.add( "CLASE_VEHI: " + claseVehiculo );
		informacion.add( "TIPO_SERVI: " + tipoServicio );
		informacion.add( "INFRACCION: " + codigoInfraccion );
		informacion.add( "LATITUD: " + String.valueOf( darLatitud( ) ) );
		informacion.add( "LONGITUD: " + String.valueOf( darLongitud( ) ) );
		informacion.add( "LOCALIDAD: " + localidad );

		for( int i = 0; i < informacion.size( ); i++ )
			if( mostrar[i] )
				cadena += "\t" + informacion.get( i ) + "\n";
		
		return cadena;
	}

	@Override
	public int compareTo( Comparendo o )
	{
		return this.id.compareTo( o.darId( ) );
	}
}