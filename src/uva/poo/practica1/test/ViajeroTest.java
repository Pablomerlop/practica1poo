package uva.poo.practica1.test;

import static org.junit.Assert.*;

import fabricante.externo.tarjeta.TarjetaEmbarque;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.CintaRecogida;
import uva.poo.practica1.Maleta;
import uva.poo.practica1.Viajero;

/**
 * Pruebas unitarias de la clase Viajero.
 * 
 * @author pablmer
 * @author seraran
 */
public class ViajeroTest {

	private static final double DELTA = 0.0001;
	private static final String CREDENCIAL_CREAR = "J1M6Nv65_B1R8T3X";
	private static final String CREDENCIAL_AUTORIZAR = "G1bR13l1_A7k2M9P";

	private Viajero viajero;
	private Maleta maleta1;
	private Maleta maleta2;
	private TarjetaEmbarque tarjeta;

	@Before
	public void setUp() {
		viajero = new Viajero("12345678Z", "Antonio", "Gil Perez", 2);
		maleta1 = new Maleta(15.0, "30x30x30");
		maleta2 = new Maleta(20.0, "40x30x20");
		tarjeta = new TarjetaEmbarque(CREDENCIAL_CREAR);
	}

	@Test
	public void testInicializacionValida() {
		assertEquals("12345678Z", viajero.getIdentificador());
		assertEquals("Antonio", viajero.getNombre());
		assertEquals("Gil Perez", viajero.getApellidos());
		assertFalse(viajero.isEmbarcado());
		assertFalse(viajero.tieneTarjetaEmbarque());
		assertEquals(0, viajero.getNumMaletas());
		assertEquals(0.0, viajero.getPesoTotalEquipaje(), DELTA);
		assertEquals(0, viajero.getMaletas().length);
		assertNotNull(viajero.toString());
	}

	@Test
	public void testInicializacionValidaAVLInfCeroMaletas() {
		Viajero sinEquipaje = new Viajero("87654321X", "Ana", "Lopez", 0);
		assertEquals(0, sinEquipaje.getNumMaletas());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIdNulo() {
		@SuppressWarnings("unused")
		Viajero invalido = new Viajero(null, "Antonio", "Gil", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIdVacio() {
		@SuppressWarnings("unused")
		Viajero invalido = new Viajero("  ", "Antonio", "Gil", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaNombreNulo() {
		@SuppressWarnings("unused")
		Viajero invalido = new Viajero("12345678Z", null, "Gil", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaApellidosNulos() {
		@SuppressWarnings("unused")
		Viajero invalido = new Viajero("12345678Z", "Antonio", null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxMaletasNegativo() {
		@SuppressWarnings("unused")
		Viajero invalido = new Viajero("12345678Z", "Antonio", "Gil", -1);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetTarjetaEmbarqueSinAsignar() {
		viajero.getTarjetaEmbarque();
	}

	@Test
	public void testFacturarYRecogerMaletaValido() {
		viajero.facturarMaleta(maleta1);
		viajero.facturarMaleta(maleta2);

		assertEquals(2, viajero.getNumMaletas());
		assertEquals(35.0, viajero.getPesoTotalEquipaje(), DELTA);
		assertTrue(maleta1.isFacturada());
		assertTrue(viajero.tieneMaletaFacturada(maleta1));

		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		cinta.depositarMaleta(maleta1);

		LocalDateTime hora = LocalDateTime.of(2026, 10, 20, 19, 0);
		viajero.recogerMaleta(cinta, maleta1, hora);
		assertTrue(maleta1.isRecogida());
		assertEquals(hora, maleta1.getFechaHoraRecogida());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFacturarMaletaNula() {
		viajero.facturarMaleta(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaDuplicadaMismoODistintoViajero() {
		viajero.facturarMaleta(maleta1);
		Viajero otro = new Viajero("99999999A", "Luis", "Perez", 2);
		otro.facturarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaExcedeLimite() {
		viajero.facturarMaleta(maleta1);
		viajero.facturarMaleta(maleta2);
		viajero.facturarMaleta(new Maleta(10.0, "20x20x20"));
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaDespuesDeEmbarcar() {
		viajero.embarcar(tarjeta, CREDENCIAL_AUTORIZAR);
		viajero.facturarMaleta(maleta1);
	}

	@Test
	public void testEmbarcarConTarjetaExternaGuardaReferencia() {
		viajero.embarcar(tarjeta, CREDENCIAL_AUTORIZAR);
		assertTrue(viajero.isEmbarcado());
		assertTrue(viajero.tieneTarjetaEmbarque());
		assertSame(tarjeta, viajero.getTarjetaEmbarque());
		assertTrue(tarjeta.isEmbarcado());
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarDosVeces() {
		viajero.embarcar(tarjeta, CREDENCIAL_AUTORIZAR);
		viajero.embarcar(new TarjetaEmbarque(CREDENCIAL_CREAR), CREDENCIAL_AUTORIZAR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarCredencialIncorrecta() {
		viajero.embarcar(tarjeta, "CREDENCIAL_FALSA");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRecogerMaletaAjena() {
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		cinta.depositarMaleta(maleta1);
		viajero.recogerMaleta(cinta, maleta1, LocalDateTime.of(2026, 10, 20, 19, 0));
	}

	@Test
	public void testEqualsYHashCode() {
		Viajero igual = new Viajero("12345678Z", "Otro", "Nombre", 0);
		Viajero distinto = new Viajero("00000000T", "Antonio", "Gil Perez", 2);

		assertEquals(viajero, igual);
		assertEquals(viajero.hashCode(), igual.hashCode());
		assertNotEquals(viajero, distinto);
		assertNotEquals(viajero, null);
	}
}