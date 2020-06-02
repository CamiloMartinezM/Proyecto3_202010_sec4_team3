package model.logic;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Clase que maneja la informaci�n de un comparendo.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
@JsonDeserialize( using = DeserializadorJSON.class )
public class Comparendo implements Comparable<Comparendo>
{
	/**
	 * Tiene los posibles comparadores que se pueden utilizar.
	 * Indica qu� caracter�stica comparar.
	 */
	public enum Comparador
	{
		/**
		 * Para saber si un comparendo es m�s grave que otro primero se mira el tipo de
		 * servicio. P�blico es m�s grave que Oficial y Oficial es m�s grave que
		 * Particular. Si dos comparendos tienen el mismo tipo de servicio se compara el
		 * c�digo de la infracci�n (campo INFRACCION) usando el orden lexicogr�fico
		 * (forma de comparaci�n de los Strings en Java, A12 es mas grave que A11 y B10
		 * es m�s grave que A10).
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
	 * Identificador �nico del comparendo. OBJECTID.
	 */
	private int id;

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
	private double[] coordenada;

	/**
	 * Indica qu� comparador es el actual.
	 */
	private Comparador comparador;

	private int idVertex;
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
	 * @param coordenada            Coordenada donde ocurri� la infracci�n. La
	 *                              primera posici�n corresponde a la longitud, la
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
	 * @return Identificador �nico del comparendo.
	 */
	public int darId( )
	{
		return id;
	}

	/**
	 * Cambia el identificador �nico actual por uno nuevo dado por par�metro.
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
	public double darLongitud( )
	{
		return coordenada[0];
	}

	/**
	 * @return Latitud de la infracci�n.
	 */
	public double darLatitud( )
	{
		return coordenada[1];
	}

	/**
	 * Cambia la coordenada actual por una nueva dada por par�metro.
	 * @param coordenada Coordenada nueva.
	 */
	public void cambiarCoordenada( double[] coordenada )
	{
		this.coordenada = coordenada;
	}

	/**
	 * Consulta la informaci�n del comparendo.
	 * @param mostrar Arreglo booleano de tama�o 9 que indica si mostrar uno cierto
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

	/**
	 * Cambia el comparador actual por aqu�l dado por par�metro.
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

			if( this.tipoServicio.equals( "P�blico" )
					&& ( o.tipoServicio.equals( "Oficial" ) || o.tipoServicio.equals( "Particular" ) ) )
				return 1;
			else if( this.tipoServicio.equals( "Oficial" ) && o.tipoServicio.equals( "P�blico" ) )
				return -1;
			else if( this.tipoServicio.equals( "Oficial" ) && o.tipoServicio.equals( "Particular" ) )
				return 1;
			else if( this.tipoServicio.equals( "Particular" )
					&& ( o.tipoServicio.equals( "P�blico" ) || o.tipoServicio.equals( "Oficial" ) ) )
				return -1;
			else // No debe ocurrir.
			{
				System.out.println( "�ERROR!" );
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
			System.out.println( "�ERROR!" );
			return 666;
		}
	}
}