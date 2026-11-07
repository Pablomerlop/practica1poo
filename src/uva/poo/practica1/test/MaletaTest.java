package uva.poo.practica1.test;

import org.junit.Before;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

import org.junit.Test;

import uva.poo.practica1.Maleta;

public class MaletaTest {

	private Maleta maleta;
	
	@Before
	public void crearMaleta() {
		maleta = new Maleta(20.5, "30x30x30");
	}
	
	@Test
	public void test() {
		assertEquals(20.5, maleta.getPeso(), 0.01);
		assertEquals("30x30x30", maleta.getDimensiones());
		assertFalse(maleta.isRecogida());
		assertNull(maleta.getFechaHoraRecogida());
	}
	
	@Test
	public void testRegistrarRecogida() {
		LocalDateTime ahora = LocalDateTime.now();
		maleta.registrarRecogida(ahora);
		
		assertTrue(maleta.isRecogida());
		assertEquals(ahora, maleta.getFechaHoraRecogida());
	}

}
