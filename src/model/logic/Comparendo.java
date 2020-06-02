package model.logic;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Clase que maneja la información de un comparendo.
 * @author Camilo Martínez & Nicolás Quintero
 */
@JsonDeserialize( using = DeserializadorJSON.class )
public class Comparendo implements Comparable<Comparendo>
{
	/**
	 * Tiene los posibles comparadores que se pueden utilizar.
	 * Indica qué característica comparar.
	 */
	public enum Comparador
	{
		/**
		 * Para saber si un comparendo es más grave que otro primero se mira el tipo de
		 * servicio. Público es más grave que Oficial y Oficial es más grave que
		 * Particular. Si dos comparendos tienen el mismo tipo de servicio se compara el
		 * código de la infracción (campo INFRACCION) usando el orden lexicográfico
		 * (forma de comparación de los Strings en Java, A12 es mas grave que A11 y B10
		 * es más grave que A10).
		 */
		GRAVEDAD,

		/**
		 * Valor de latitud.
		 */
		LATITUD,

		/**
		 * Fecha-hora de los comparendos.
		 */
		FECHA_HORA,
		
		/**
		 * ID de los comparendos.
		 */
		ID
	}

	/**
	 * Identificador único del comparendo. OBJECTID.
	 */
	private int id;

	/**
	 * Fecha del comparendo. FECHA_HORA.
	 */
	private String fecha;

	/**
	 * Medio de detención. MEDIO_DETE.
	 */
	private String medioDeteccion;

	/**
	 * Clase de vehículo. CLASE_VEHI.
	 */
	private String claseVehiculo;

	/**
	 * Tipo de servicio del automóvil. TIPO_SERVI.
	 */
	private String tipoServicio;

	/**
	 * Código del comparendo cometido. INFRACCION
	 */
	private String codigoInfraccion;

	/**
	 * Descripción del comparendo. DES_INFRAC.
	 */
	private String descripcionInfraccion;

	/**
	 * Localidad donde fue cometida la infracción. LOCALIDAD.
	 */
	private String localidad;

	/**
	 * Coordenada, donde está la longitud y latitud geográficas.
	 */
	private double[] coordenada;

	/**
	 * Indica qué comparador es el actual.
	 */
	private Comparador comparador;

	private int idVertex;
	/**
	 * Inicializa un comparendo con la información dada por parámetro.
	 * @param id                    Identificador único del comparendo.
	 * @param fecha                 Fecha del comparendo.
	 * @param medioDeteccion        Medio por el cual fue realizada la detención.
	 * @param claseVehiculo         Tipo de vehículo. Ejemplo: AUTOMOVIL, BICICLETA,
	 *                              BUS, ...
	 * @param tipoServicio          Tipo de servicio que prestaba el automóvil.
	 *                              Ejemplo: Particular, público, ...
	 * @param codigoInfraccion      Código de la infracción cometida.
	 * @param descripcionInfraccion Descripción de la infracción cometida.
	 * @param localidad             Localidad en la ciudad del comparendo.
	 * @param coordenada            Coordenada donde ocurrió la infracción. La
	 *                              primera posición corresponde a la longitud, la
	 *                              segunda a la latitud.
	 */
	public Comparendo( int id, String fecha, String medioDetencion, String claseVehiculo, String tipoServicio,
			String codigoInfraccion, String causaInfraccion, String localidad, double[] coordenada )
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
		this.comparador = Comparador.LATITUD; // Comparador predeterminado es la gravedad.
	}

	/**
	 * @return Identificador único del comparendo.
	 */
	public int darId( )
	{
		return id;
	}

	/**
	 * Cambia el identificador único actual por uno nuevo dado por parámetro.
	 * @param id Nuevo ID.
	 */
	public void cambiarId( int id )
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
	 * Cambia la fecha actual por una nueva dada por parámetro.
	 * @param fecha Nueva fecha.
	 */
	public void cambiarFecha( String fecha )
	{
		this.fecha = fecha;
	}

	/**
	 * @return medioDeteccion Medio por el cual se detuvo el automóvil.
	 */
	public String darMedioDetencion( )
	{
		return medioDeteccion;
	}

	/**
	 * Cambia el medio de detención actual por uno nuevo dado por parámetro.
	 * @param medioDeteccion Nuevo medio de detención.
	 */
	public void cambiarMedioDetencion( String medioDetencion )
	{
		this.medioDeteccion = medioDetencion;
	}

	/**
	 * @return claseVehiculo Tipo de vehículo.
	 */
	public String darClaseVehiculo( )
	{
		return claseVehiculo;
	}

	/**
	 * Cambia el tipo de vehículo actual por uno nuevo dado por parámetro.
	 * @param claseVehiculo Nuevo tipo de vehículo.
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
	 * Cambia el tipo de servicio actual por uno nuevo dado por parámetro.
	 * @param tipoServicio Nuevo tipo de servicio.
	 */
	public void cambiarTipoServicio( String tipoServicio )
	{
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return codigoInfraccion Código de la infracción cometida.
	 */
	public String darCodigoInfraccion( )
	{
		return codigoInfraccion;
	}

	/**
	 * Cambia el código de la infracción actual por uno nuevo dado por parámetro.
	 * @param codigoInfraccion Nuevo código de la infracción.
	 */
	public void cambiarCodigoInfraccion( String codigoInfraccion )
	{
		this.codigoInfraccion = codigoInfraccion;
	}

	/**
	 * @return descripcionInfraccion Descripción de la infracción.
	 */
	public String darDescripcionInfraccion( )
	{
		return descripcionInfraccion;
	}

	/**
	 * Cambia la causa de la infracción actual por una nueva dada por parámetro.
	 * @param causaInfraccion Nueva causa de la infracción.
	 */
	public void cambiarCausaInfraccion( String causaInfraccion )
	{
		this.descripcionInfraccion = causaInfraccion;
	}

	/**
	 * @return localidad Localidad donde ocurrió la infracción.
	 */
	public String darLocalidad( )
	{
		return localidad;
	}

	/**
	 * Cambia la localidad actual por una nueva dada por parámetro.
	 * @param localidad Nueva localidad.
	 */
	public void cambiarLocalidad( String localidad )
	{
		this.localidad = localidad;
	}

	/**
	 * @return Longitud de la infracción.
	 */
	public double darLongitud( )
	{
		return coordenada[0];
	}

	/**
	 * @return Latitud de la infracción.
	 */
	public double darLatitud( )
	{
		return coordenada[1];
	}

	/**
	 * Cambia la coordenada actual por una nueva dada por parámetro.
	 * @param coordenada Coordenada nueva.
	 */
	public void cambiarCoordenada( double[] coordenada )
	{
		this.coordenada = coordenada;
	}

	/**
	 * Consulta la información del comparendo.
	 * @param mostrar Arreglo booleano de tamaño 9 que indica si mostrar uno cierto
	 *                atributo del comparendo.
	 * @return Cadena con la información pedida del comparendo.
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

	/**
	 * Cambia el comparador actual por aquél dado por parámetro.
	 * @param comparador Nuevo comparador. comparador = {LATITUD, GRAVEDAD}
	 */
	public void cambiarComparador( Comparador comparador )
	{
		this.comparador = comparador;
	}

	@Override
	public int compareTo( Comparendo o )
	{
		if( comparador == Comparador.GRAVEDAD )
		{
			if( this.tipoServicio.equals( o.tipoServicio ) ) // Si son iguales.
				return this.darCodigoInfraccion( ).compareTo( o.darCodigoInfraccion( ) );

			if( this.tipoServicio.equals( "Público" )
					&& ( o.tipoServicio.equals( "Oficial" ) || o.tipoServicio.equals( "Particular" ) ) )
				return 1;
			else if( this.tipoServicio.equals( "Oficial" ) && o.tipoServicio.equals( "Público" ) )
				return -1;
			else if( this.tipoServicio.equals( "Oficial" ) && o.tipoServicio.equals( "Particular" ) )
				return 1;
			else if( this.tipoServicio.equals( "Particular" )
					&& ( o.tipoServicio.equals( "Público" ) || o.tipoServicio.equals( "Oficial" ) ) )
				return -1;
			else // No debe ocurrir.
			{
				System.out.println( "¡ERROR!" );
				return 666;
			}
		}
		else if( comparador == Comparador.LATITUD )
		{
			if( this.darLatitud( ) < o.darLatitud( ) )
				return -1;
			else if( this.darLatitud( ) > o.darLatitud( ) )
				return 1;
			else
				return 0;
		}
		else if( comparador == Comparador.ID )
		{
			if( this.id < o.darId( ) )
				return -1;
			else if( this.id > o.darId( ) )
				return 1;
			else
				return 0;
		}
		else if( comparador == Comparador.FECHA_HORA )
			return this.fecha.compareTo( o.darFecha( ) );
		else // No debe ocurrir.
		{
			System.out.println( "¡ERROR!" );
			return 666;
		}
	}
}