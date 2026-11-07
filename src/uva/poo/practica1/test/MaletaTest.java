package uva.poo.practica1.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.Maleta;

/**
 * Pruebas unitarias de la clase Maleta.
 * 
 * @author pablmer
 * @author seraran
 */
public class MaletaTest {

	private static final double DELTA = 0.0001;
	private Maleta maleta;
	private LocalDateTime fechaRecogida;

	@Before
	public void setUp() {
		maleta = new Maleta(20.5, "55x40x20");
		fechaRecogida = LocalDateTime.of(2026, 10, 20, 14, 30);
	}

	@Test
	public void testInicializacionValida() {
		assertEquals(20.5, maleta.getPeso(), DELTA);
		assertEquals("55x40x20", maleta.getDimensiones());
		assertFalse(maleta.isFacturada());
		assertFalse(maleta.isRecogida());
		assertNotNull(maleta.toString());
	}

	@Test
	public void testInicializacionValidaAVLInfPeso() {
		Maleta maletaLigera = new Maleta(0.001, "  10x10x10  ");
		assertEquals(0.001, maletaLigera.getPeso(), DELTA);
		assertEquals("10x10x10", maletaLigera.getDimensiones());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaAVLInfPesoCero() {
		@SuppressWarnings("unused")
		Maleta invalida = new Maleta(0.0, "55x40x20");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPesoNegativo() {
		@SuppressWarnings("unused")
		Maleta invalida = new Maleta(-5.0, "55x40x20");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDimensionesNulas() {
		@SuppressWarnings("unused")
		Maleta invalida = new Maleta(15.0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDimensionesVacias() {
		@SuppressWarnings("unused")
		Maleta invalida = new Maleta(15.0, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDimensionesEnBlanco() {
		@SuppressWarnings("unused")
		Maleta invalida = new Maleta(15.0, "   ");
	}

	@Test
	public void testRegistrarRecogidaValida() {
		maleta.registrarRecogida(fechaRecogida);
		assertTrue(maleta.isRecogida());
		assertEquals(fechaRecogida, maleta.getFechaHoraRecogida());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetFechaHoraRecogidaSinEstarRecogida() {
		maleta.getFechaHoraRecogida();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegistrarRecogidaFechaNula() {
		maleta.registrarRecogida(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testRegistrarRecogidaDuplicada() {
		maleta.registrarRecogida(fechaRecogida);
		maleta.registrarRecogida(fechaRecogida.plusMinutes(5));
	}
}