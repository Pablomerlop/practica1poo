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

	private static final double DELTA = 0.0001;
	private CintaRecogida cinta;
	private Maleta maleta1;
	private Maleta maleta2;
	private LocalDateTime horaRecogida;

	@Before
	public void setUp() {
		cinta = new CintaRecogida("C1", 50.0, 2);
		cinta.setOperativa(true);
		maleta1 = new Maleta(20.0, "50x40x20");
		maleta2 = new Maleta(30.0, "60x40x30");
		horaRecogida = LocalDateTime.of(2026, 10, 20, 18, 0);
	}

	@Test
	public void testInicializacionValida() {
		CintaRecogida nueva = new CintaRecogida("C2", 100.0, 5);
		assertEquals("C2", nueva.getIdentificador());
		assertEquals(100.0, nueva.getCapacidadMaxima(), DELTA);
		assertEquals(0.0, nueva.getPesoAcumulado(), DELTA);
		assertEquals(100.0, nueva.getCapacidadLibre(), DELTA);
		assertEquals(5, nueva.getMaxMaletas());
		assertEquals(0, nueva.getNumMaletas());
		assertEquals(5, nueva.getHuecosLibres());
		assertFalse(nueva.isOperativa());
		assertFalse(nueva.tieneEquipajePendiente());
		assertEquals(0, nueva.getMaletas().length);
		assertNotNull(nueva.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIdNulo() {
		@SuppressWarnings("unused")
		CintaRecogida invalida = new CintaRecogida(null, 100.0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIdVacio() {
		@SuppressWarnings("unused")
		CintaRecogida invalida = new CintaRecogida("  ", 100.0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCapacidadCeroAVLInf() {
		@SuppressWarnings("unused")
		CintaRecogida invalida = new CintaRecogida("C1", 0.0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxMaletasCeroAVLInf() {
		@SuppressWarnings("unused")
		CintaRecogida invalida = new CintaRecogida("C1", 100.0, 0);
	}

	@Test
	public void testDepositarYRetirarMaletaAVLSupCapacidadExacta() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);

		assertTrue(cinta.tieneEquipajePendiente());
		assertTrue(cinta.contieneMaleta(maleta1));
		assertEquals(50.0, cinta.getPesoAcumulado(), DELTA);
		assertEquals(0.0, cinta.getCapacidadLibre(), DELTA);
		assertEquals(2, cinta.getNumMaletas());
		assertEquals(0, cinta.getHuecosLibres());

		cinta.retirarMaleta(maleta1, horaRecogida);

		assertFalse(cinta.contieneMaleta(maleta1));
		assertTrue(maleta1.isRecogida());
		assertEquals(horaRecogida, maleta1.getFechaHoraRecogida());
		assertEquals(30.0, cinta.getPesoAcumulado(), DELTA);
		assertEquals(20.0, cinta.getCapacidadLibre(), DELTA);
		assertEquals(1, cinta.getNumMaletas());
		assertSame(maleta2, cinta.getMaletas()[0]);
	}

	@Test
	public void testPrecisionComaFlotanteAVLSup() {
		CintaRecogida cintaDecimal = new CintaRecogida("CD", 0.3, 2);
		cintaDecimal.setOperativa(true);
		cintaDecimal.depositarMaleta(new Maleta(0.1, "10x10x10"));
		cintaDecimal.depositarMaleta(new Maleta(0.2, "10x10x10"));
		assertEquals(0.3, cintaDecimal.getPesoAcumulado(), DELTA);
		assertEquals(0.0, cintaDecimal.getCapacidadLibre(), DELTA);
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
	public void testDepositarMaletaCintaNoOperativa() {
		cinta.setOperativa(false);
		cinta.depositarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testDepositarMaletaYaRecogida() {
		maleta1.registrarRecogida(horaRecogida);
		cinta.depositarMaleta(maleta1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositarMaletaDuplicadaEnCinta() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testDepositarMaletaSuperaNumeroMaximoMaletas() {
		Maleta ligera1 = new Maleta(5.0, "10x10x10");
		Maleta ligera2 = new Maleta(5.0, "10x10x10");
		Maleta ligera3 = new Maleta(5.0, "10x10x10");
		cinta.depositarMaleta(ligera1);
		cinta.depositarMaleta(ligera2);
		cinta.depositarMaleta(ligera3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositarMaletaSuperaCapacidadPeso() {
		Maleta pesada = new Maleta(30.1, "50x50x50");
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(pesada);
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
	public void testRetirarMaletaNoPresenteEnCinta() {
		cinta.depositarMaleta(maleta1);
		cinta.retirarMaleta(maleta2, horaRecogida);
	}

	@Test
	public void testEqualsYHashCode() {
		CintaRecogida igual = new CintaRecogida("C1", 200.0, 10);
		CintaRecogida distinta = new CintaRecogida("C9", 50.0, 2);

		assertEquals(cinta, igual);
		assertEquals(cinta.hashCode(), igual.hashCode());
		assertNotEquals(cinta, distinta);
		assertNotEquals(cinta, null);
	}
}