package main;

import controller.Controller;

/**
 * Clase principal de la aplicaci�n.
 * @author Camilo Mart�nez & Nicol�s Quintero
 */
public class Main
{
	public static void main( String[] args )
	{
		Controller controlador = new Controller( );
		controlador.run( );
	}
}