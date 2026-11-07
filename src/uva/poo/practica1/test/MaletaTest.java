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

	private static final double ERROR_ADMISIBLE = 0.01;
	private Maleta maleta;
	private LocalDateTime fechaRecogida;

	@Before
	public void crearMaleta() {
		maleta = new Maleta(20.5, "30x30x30");
		fechaRecogida = LocalDateTime.of(2026, 10, 20, 14, 30);
	}

	@Test
	public void testEstadoInicial() {
		assertEquals(20.5, maleta.getPeso(), ERROR_ADMISIBLE);
		assertEquals("30x30x30", maleta.getDimensiones());
		assertFalse(maleta.isFacturada());
		assertFalse(maleta.isRecogida());
		assertNotNull(maleta.toString());
	}

	@Test
	public void testInicializacionValidaAvlinf() {
		Maleta maletaPequena = new Maleta(0.1, "10x10x10");
		assertEquals(0.1, maletaPequena.getPeso(), ERROR_ADMISIBLE);
		assertEquals("10x10x10", maletaPequena.getDimensiones());
	}

	@Test
	public void testInicializacionConEspacios() {
		Maleta maletaEspacios = new Maleta(15.0, "  50x40x10  ");
		assertEquals("50x40x10", maletaEspacios.getDimensiones());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPesoCeroAvlinf() {
		@SuppressWarnings("unused")
		Maleta m = new Maleta(0.0, "30x30x30");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPesoNegativo() {
		@SuppressWarnings("unused")
		Maleta m = new Maleta(-5.0, "30x30x30");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDimensionesNulas() {
		@SuppressWarnings("unused")
		Maleta m = new Maleta(15.0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDimensionesVacias() {
		@SuppressWarnings("unused")
		Maleta m = new Maleta(15.0, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDimensionesEnBlanco() {
		@SuppressWarnings("unused")
		Maleta m = new Maleta(15.0, "   ");
	}

	@Test
	public void testRegistrarRecogida() {
		maleta.registrarRecogida(fechaRecogida);
		assertTrue(maleta.isRecogida());
		assertEquals(fechaRecogida, maleta.getFechaHoraRecogida());
	}

	@Test
	public void testToStringMaletaRecogida() {
		maleta.registrarRecogida(fechaRecogida);
		assertTrue(maleta.toString().contains("true"));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetFechaRecogidaSinRecoger() {
		maleta.getFechaHoraRecogida();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegistrarRecogidaNula() {
		maleta.registrarRecogida(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testRegistrarRecogidaDosVeces() {
		maleta.registrarRecogida(fechaRecogida);
		maleta.registrarRecogida(fechaRecogida.plusMinutes(10));
	}
}
