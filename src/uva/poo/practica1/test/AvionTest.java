package uva.poo.practica1.test;

import org.junit.Before;
import static org.junit.Assert.*;

import org.junit.Test;

import uva.poo.practica1.Avion;

public class AvionTest {

	private Avion avion;	
	
	@Before
	public void crearAvion() {
		avion = new Avion("4563AGH", "Airbom 300", 10, 20, 120, 3000.0, 150);
	}
	
	@Test
	public void testEstadoInicial() {
		assertEquals("4563AGH", avion.getMatricula());
		assertEquals("Airbom 300", avion.getModelo());
		assertEquals(3000.0, avion.getEquipajeMaxKg(), 0.001);
		assertEquals(150, avion.getMaxMaletas());		
	}
	
	@Test
	public void testRedistribuirPlazasBien() {
		avion.redistribuirPlazas(20, 30, 100);
		
		assertEquals(20, avion.getPlazasPrimera());
		assertEquals(30, avion.getPlazasBusiness());
		assertEquals(100, avion.getPlazasTurista());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasExcede() {
		avion.redistribuirPlazas(50, 50, 100);
	}

}
