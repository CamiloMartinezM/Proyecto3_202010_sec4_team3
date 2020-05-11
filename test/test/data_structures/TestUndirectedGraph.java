package test.data_structures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.UndirectedGraph;

public class TestUndirectedGraph {


	public final int CAPACITY =20;
	private UndirectedGraph<Integer,String> grafo;
    @Before
    public void setUp(){
    	grafo = new UndirectedGraph<Integer,String>(CAPACITY);
    	grafo.addEdge(1, 2, 2000);
    	grafo.addEdge(3, 4, 2000);
    	grafo.addEdge(4, 5, 2000);
        grafo.E();
        grafo.addVertex(2, "74.08952746199998,4.582560966000017");
        grafo.setCostArc(1, 2, 5000);
        grafo.setInfoVertex(1, "holaMundo");
        grafo.dfs(1);

    }
	@Test
	public void CreacionGrafoTest() {

		setUp();
		assertEquals("debieron crearse"+CAPACITY,20,grafo.V());
	}
	
	@Test
	public void CreacionEdgeTest() {
		setUp();
		assertEquals("debieron crearse 3",3,grafo.E());
	}
	
	@Test
	public void DfsTest(){
		setUp();
		assertEquals("solo se tiene 1 adyacencia desde 1",1,grafo.cc());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void SetCostTest(){
		setUp();
		double costo =grafo.getCostArc(1,2);
	    assertEquals("debe ser 5000",5000,costo );
	}
	public void SetInfoVertexTest(){
		setUp();
		assertEquals("debe ser hola mundo",1,grafo.getInfoVertex(1));
	}

	public void uncheck(){
		setUp();
		grafo.uncheck();
		assertEquals("Deberia retornar true si es visitado",false,grafo.visit(1));
	}
	public void ccTest(){
		setUp();
		assertEquals("El numero deberia ser 2",2, grafo.cc());
	}

}
