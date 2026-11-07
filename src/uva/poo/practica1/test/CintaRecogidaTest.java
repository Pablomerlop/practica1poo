package uva.poo.practica1.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.CintaRecogida;
import uva.poo.practica1.Maleta;

/**
 * Pruebas unitarias de la clase CintaRecogida.
 * 
 * @author pablmer
 * @author seraran
 */
public class CintaRecogidaTest {

	private static final double ERROR_ADMISIBLE = 0.001;
	private CintaRecogida cinta;
	private Maleta maleta1;
	private Maleta maleta2;
	private Maleta maleta3;
	private Maleta maleta4;
	private LocalDateTime horaRecogida;

	@Before
	public void crearCintaRecogida() {
		cinta = new CintaRecogida("C1", 100.0, 4);
		cinta.setOperativa(true);
		maleta1 = new Maleta(20.0, "50x40x10");
		maleta2 = new Maleta(30.0, "30x30x30");
		maleta3 = new Maleta(40.0, "20x20x20");
		maleta4 = new Maleta(20.0, "30x20x30");
		horaRecogida = LocalDateTime.of(2026, 10, 20, 18, 0);
	}

	@Test
	public void testEstadoInicial() {
		CintaRecogida c2 = new CintaRecogida("C2", 150.0, 5);
		assertEquals("C2", c2.getIdentificador());
		assertEquals(150.0, c2.getCapacidadMaxima(), ERROR_ADMISIBLE);
		assertEquals(0.0, c2.getPesoAcumulado(), ERROR_ADMISIBLE);
		assertEquals(150.0, c2.getCapacidadLibre(), ERROR_ADMISIBLE);
		assertEquals(5, c2.getMaxMaletas());
		assertEquals(0, c2.getNumMaletas());
		assertEquals(5, c2.getHuecosLibres());
		assertFalse(c2.isOperativa());
		assertFalse(c2.tieneEquipajePendiente());
		assertEquals(0, c2.getMaletas().length);
		assertNotNull(c2.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIdNulo() {
		@SuppressWarnings("unused")
		CintaRecogida c = new CintaRecogida(null, 100.0, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIdVacio() {
		@SuppressWarnings("unused")
		CintaRecogida c = new CintaRecogida("  ", 100.0, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCapacidadCeroAvlinf() {
		@SuppressWarnings("unused")
		CintaRecogida c = new CintaRecogida("C1", 0.0, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCapacidadNegativa() {
		@SuppressWarnings("unused")
		CintaRecogida c = new CintaRecogida("C1", -10.0, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxMaletasCeroAvlinf() {
		@SuppressWarnings("unused")
		CintaRecogida c = new CintaRecogida("C1", 100.0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxMaletasNegativo() {
		@SuppressWarnings("unused")
		CintaRecogida c = new CintaRecogida("C1", 100.0, -2);
	}

	@Test
	public void testDepositarYRetirarMaleta() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);

		assertTrue(cinta.tieneEquipajePendiente());
		assertTrue(cinta.contieneMaleta(maleta1));
		assertEquals(50.0, cinta.getPesoAcumulado(), ERROR_ADMISIBLE);
		assertEquals(50.0, cinta.getCapacidadLibre(), ERROR_ADMISIBLE);
		assertEquals(2, cinta.getMaletas().length);
		assertEquals(2, cinta.getHuecosLibres());

		cinta.retirarMaleta(maleta1, horaRecogida);

		assertFalse(cinta.contieneMaleta(maleta1));
		assertTrue(maleta1.isRecogida());
		assertEquals(horaRecogida, maleta1.getFechaHoraRecogida());
		assertEquals(30.0, cinta.getPesoAcumulado(), ERROR_ADMISIBLE);
		assertEquals(1, cinta.getMaletas().length);
		assertEquals(maleta2, cinta.getMaletas()[0]);
	}

	@Test
	public void testRetirarUltimaMaleta() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);
		cinta.retirarMaleta(maleta2, horaRecogida);

		assertEquals(1, cinta.getNumMaletas());
		assertEquals(maleta1, cinta.getMaletas()[0]);
	}

	@Test
	public void testSumaPesosDecimalesAvlsup() {
		CintaRecogida cintaPequena = new CintaRecogida("C3", 0.3, 2);
		cintaPequena.setOperativa(true);
		cintaPequena.depositarMaleta(new Maleta(0.1, "10x10x10"));
		cintaPequena.depositarMaleta(new Maleta(0.2, "10x10x10"));
		assertEquals(0.3, cintaPequena.getPesoAcumulado(), ERROR_ADMISIBLE);
		assertEquals(0.0, cintaPequena.getCapacidadLibre(), ERROR_ADMISIBLE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testContieneMaletaNula() {
		cinta.contieneMaleta(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositarMaletaNula() {
		cinta.depositarMaleta(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testFueraServicio() {
		cinta.setOperativa(false);
		cinta.depositarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testDepositarMaletaYaRecogida() {
		maleta1.registrarRecogida(horaRecogida);
		cinta.depositarMaleta(maleta1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositarMaletaRepetida() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testExcesoMaletas() {
		Maleta extra1 = new Maleta(10.0, "10x10x20");
		Maleta extra2 = new Maleta(10.0, "10x10x20");

		cinta.depositarMaleta(extra2);
		cinta.depositarMaleta(extra1);
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);
		cinta.depositarMaleta(maleta3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExcesoPeso() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);
		cinta.depositarMaleta(maleta3);
		cinta.depositarMaleta(maleta4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRetirarMaletaNula() {
		cinta.depositarMaleta(maleta1);
		cinta.retirarMaleta(null, horaRecogida);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRetirarMaletaFechaNula() {
		cinta.depositarMaleta(maleta1);
		cinta.retirarMaleta(maleta1, null);
	}

	@Test(expected = IllegalStateException.class)
	public void testRetirarMaletaCintaVacia() {
		cinta.retirarMaleta(maleta1, horaRecogida);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRetirarMaletaNoEstaEnCinta() {
		cinta.depositarMaleta(maleta1);
		cinta.retirarMaleta(maleta2, horaRecogida);
	}

	@Test
	public void testEquals() {
		CintaRecogida eq1 = new CintaRecogida("C1", 200.0, 10);
		CintaRecogida distinta = new CintaRecogida("C9", 100.0, 4);

		assertEquals(cinta, cinta);
		assertEquals(cinta, eq1);
		assertEquals(cinta.hashCode(), eq1.hashCode());
		assertNotEquals(cinta, distinta);
		assertNotEquals(cinta, null);
		assertNotEquals(cinta, "C1");
	}
}
