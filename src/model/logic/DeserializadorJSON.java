package model.logic;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import model.logic.Comparendo;

import java.io.IOException;

@SuppressWarnings( "exports" )
public class DeserializadorJSON extends JsonDeserializer<Comparendo>
{
	@Override
	public Comparendo deserialize( JsonParser jp, DeserializationContext ctxt )
			throws IOException, JsonProcessingException
	{
		JsonNode arbol = jp.getCodec( ).readTree( jp );
		JsonNode i = arbol.get( "properties" );

		int id = i.get( "OBJECTID" ).asInt( );
		String fecha = i.get( "FECHA_HORA" ).asText( );
		String medioDeteccion = i.get( "MEDIO_DETECCION" ).asText( );
		String claseVehiculo = i.get( "CLASE_VEHICULO" ).asText( );
		String tipoServicio = i.get( "TIPO_SERVICIO" ).asText( );
		String codigoInfraccion = i.get( "INFRACCION" ).asText( );
		String descripcionInfraccion = i.get( "DES_INFRACCION" ).asText( );
		String localidad = i.get( "LOCALIDAD" ).asText( );
		JsonNode coordenada = arbol.get( "geometry" ).get( "coordinates" );
		double[] coordenadaParseada = transformarJsonArray( coordenada );

		Comparendo c = new Comparendo( id, fecha, medioDeteccion, claseVehiculo, tipoServicio, codigoInfraccion,
				descripcionInfraccion, localidad, coordenadaParseada );

		return c;
	}
	
	/**
	 * Transforma un JsonArray a un double array.
	 * @param jsonArray Arreglo a transformar.
	 * @return Arreglo de tipo double.
	 */
	static double[] transformarJsonArray( JsonNode jsonArray )
	{
		double[] fData = new double[jsonArray.size( )];

		int j = 0;
		for( final JsonNode i : jsonArray )
		{
			fData[j] = Double.parseDouble( i.asText( ) );
			j++;
		}

		return fData;
	}
}