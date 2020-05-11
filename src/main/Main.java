package main;

import controller.Controller;

/**
 * Clase principal de la aplicación.
 * @author Camilo Martínez & Nicolás Quintero
 */
public class Main
{
	public static void main( String[] args )
	{
		Controller controlador = new Controller( );
		controlador.run( );
	}
}