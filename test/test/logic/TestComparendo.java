package test.logic;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import model.logic.Comparendo;

/**
 * Test de la clase comparendo.
 * @author Camilo Martínez
 */
public class TestComparendo
{
	private Comparendo[] c;

	@Before
	public void setUp( )
	{
		c = new Comparendo[6];
	}

	public void setUp1( )
	{
		c[0] = new Comparendo( 0, "", "", "", "Público", "A10", "", "", new double[] { 1, 1 } );
		c[1] = new Comparendo( 1, "", "", "", "Público", "A11", "", "", new double[] { 1, 2 } );
		c[2] = new Comparendo( 2, "", "", "", "Público", "A12", "", "", new double[] { 1, 3 } );
		c[3] = new Comparendo( 3, "", "", "", "Público", "B10", "", "", new double[] { 1, 4 } );
		c[4] = new Comparendo( 4, "", "", "", "Público", "B11", "", "", new double[] { 1, 5 } );
		c[5] = new Comparendo( 4, "", "", "", "Público", "B12", "", "", new double[] { 1, 5 } );
	}

	public void setUp2( )
	{
		c[0] = new Comparendo( 0, "", "", "", "Particular", "A10", "", "", new double[] { 1, 1 } );
		c[1] = new Comparendo( 1, "", "", "", "Particular", "A11", "", "", new double[] { 1, 1 } );
		c[2] = new Comparendo( 2, "", "", "", "Oficial", "B10", "", "", new double[] { 1, 2 } );
		c[3] = new Comparendo( 3, "", "", "", "Oficial", "B11", "", "", new double[] { 1, 2 } );
		c[4] = new Comparendo( 4, "", "", "", "Público", "B12", "", "", new double[] { 1, 3 } );
		c[5] = new Comparendo( 5, "", "", "", "Público", "B13", "", "", new double[] { 1, 3 } );
	}

	@Test
	public void TestCompareTo1( )
	{
		setUp1( );

		for( int i = 0; i < c.length; i++ )
			for( int j = 0; j < c.length; j++ )
				if( i != j )
				{
					if( j < i )
					{
						if( c[i].compareTo( c[j] ) <= 0 )
							fail( "El comparendo " + i + " debe ser más grave que el " + j );
					}
					else
					{
						if( c[i].compareTo( c[j] ) >= 0 )
							fail( "El comparendo " + i + " debe ser menos grave que el " + j );
					}
				}
	}

	@Test
	public void TestCompareTo2( )
	{
		setUp2( );

		for( int i = 0; i < c.length; i++ )
			for( int j = 0; j < c.length; j++ )
				if( i != j )
				{
					if( j < i )
					{
						if( c[i].compareTo( c[j] ) <= 0 )
							fail( "El comparendo " + i + " debe ser más grave que el " + j );
					}
					else
					{
						if( c[i].compareTo( c[j] ) >= 0 )
							fail( "El comparendo " + i + " debe ser menos grave que el " + j );
					}
				}
	}
}