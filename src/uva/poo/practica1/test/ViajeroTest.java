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

	private static final double ERROR_ADMISIBLE = 0.001;
	private static final String CLAVE_CREAR = "J1M6Nv65_B1R8T3X";
	private static final String CLAVE_AUTORIZAR = "G1bR13l1_A7k2M9P";

	private Viajero viajero;
	private Maleta maleta1;
	private Maleta maleta2;
	private TarjetaEmbarque tarjeta;
	private LocalDateTime horaRecogida;

	@Before
	public void crearObjetos() {
		viajero = new Viajero("1234567Z", "Antonio", "Gil Perez", 2);
		maleta1 = new Maleta(15.0, "30x30x30");
		maleta2 = new Maleta(20.0, "15x20x30");
		tarjeta = new TarjetaEmbarque(CLAVE_CREAR);
		horaRecogida = LocalDateTime.of(2026, 10, 20, 19, 0);
	}

	@Test
	public void testEstadoInicial() {
		assertEquals("1234567Z", viajero.getIdentificador());
		assertEquals("Antonio", viajero.getNombre());
		assertEquals("Gil Perez", viajero.getApellidos());
		assertFalse(viajero.isEmbarcado());
		assertFalse(viajero.tieneTarjetaEmbarque());
		assertEquals(0, viajero.getNumMaletas());
		assertEquals(0.0, viajero.getPesoTotalEquipaje(), ERROR_ADMISIBLE);
		assertEquals(0, viajero.getMaletas().length);
		assertNotNull(viajero.toString());
	}

	@Test
	public void testInicializacionSinMaletasAvlinf() {
		Viajero v = new Viajero("71122334A", "Paco", "Sur", 0);
		assertEquals(0, v.getNumMaletas());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDniNulo() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero(null, "Antonio", "Gil Perez", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDniVacio() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero("  ", "Antonio", "Gil Perez", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaNombreNulo() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero("1234567Z", null, "Gil Perez", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaNombreVacio() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero("1234567Z", "   ", "Gil Perez", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaApellidosNulos() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero("1234567Z", "Antonio", null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaApellidosVacios() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero("1234567Z", "Antonio", "   ", 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxMaletasNegativo() {
		@SuppressWarnings("unused")
		Viajero v = new Viajero("1234567Z", "Antonio", "Gil Perez", -1);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetTarjetaSinAsignar() {
		viajero.getTarjetaEmbarque();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAsignarTarjetaNula() {
		viajero.asignarTarjetaEmbarque(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAsignarTarjetaYaUsada() {
		tarjeta.autorizaEmbarque(CLAVE_AUTORIZAR);
		viajero.asignarTarjetaEmbarque(tarjeta);
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarTarjetaCuandoYaEmbarco() {
		viajero.embarcar(tarjeta, CLAVE_AUTORIZAR);
		viajero.asignarTarjetaEmbarque(new TarjetaEmbarque(CLAVE_CREAR));
	}

	@Test
	public void testFacturarMaleta() {
		viajero.facturarMaleta(maleta1);
		viajero.facturarMaleta(maleta2);

		assertEquals(2, viajero.getMaletas().length);
		assertEquals(maleta1, viajero.getMaletas()[0]);
		assertEquals(maleta2, viajero.getMaletas()[1]);
		assertEquals(35.0, viajero.getPesoTotalEquipaje(), ERROR_ADMISIBLE);
		assertTrue(maleta1.isFacturada());
		assertTrue(viajero.tieneMaletaFacturada(maleta1));
		assertFalse(viajero.tieneMaletaFacturada(new Maleta(5.0, "10x10x10")));

		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		cinta.depositarMaleta(maleta1);

		viajero.recogerMaleta(cinta, maleta1, horaRecogida);
		assertTrue(maleta1.isRecogida());
		assertEquals(horaRecogida, maleta1.getFechaHoraRecogida());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFacturarMaletaNula() {
		viajero.facturarMaleta(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMismaMaletaPorDosViajeros() {
		viajero.facturarMaleta(maleta1);
		Viajero otro = new Viajero("132412412D", "ana", "de", 2);
		otro.facturarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaYaRecogida() {
		maleta1.registrarRecogida(horaRecogida);
		viajero.facturarMaleta(maleta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testExcesoMaletasFacturadas() {
		viajero.facturarMaleta(maleta1);
		viajero.facturarMaleta(maleta2);

		Maleta maleta3 = new Maleta(20.0, "10x20x30");
		viajero.facturarMaleta(maleta3);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarDespuesDeEmbarcar() {
		viajero.embarcar(tarjeta, CLAVE_AUTORIZAR);
		viajero.facturarMaleta(maleta1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTieneMaletaFacturadaNula() {
		viajero.tieneMaletaFacturada(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRecogerMaletaCintaNula() {
		viajero.facturarMaleta(maleta1);
		viajero.recogerMaleta(null, maleta1, horaRecogida);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRecogerMaletaNula() {
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		viajero.recogerMaleta(cinta, null, horaRecogida);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRecogerMaletaFechaNula() {
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		viajero.facturarMaleta(maleta1);
		viajero.recogerMaleta(cinta, maleta1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRecogerMaletaDeOtro() {
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		cinta.depositarMaleta(maleta1);
		viajero.recogerMaleta(cinta, maleta1, horaRecogida);
	}

	@Test
	public void testEmbarcarConTarjetaYaAsignada() {
		viajero.asignarTarjetaEmbarque(tarjeta);
		assertFalse(viajero.isEmbarcado());
		viajero.embarcar(CLAVE_AUTORIZAR);
		assertTrue(viajero.isEmbarcado());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarClaveNula() {
		viajero.asignarTarjetaEmbarque(tarjeta);
		viajero.embarcar(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarClaveVacia() {
		viajero.asignarTarjetaEmbarque(tarjeta);
		viajero.embarcar("   ");
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarSinTarjetaAsignada() {
		viajero.embarcar(CLAVE_AUTORIZAR);
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarDosVecesConTarjetaAsignada() {
		viajero.asignarTarjetaEmbarque(tarjeta);
		viajero.embarcar(CLAVE_AUTORIZAR);
		viajero.embarcar(CLAVE_AUTORIZAR);
	}

	@Test
	public void testEmbarcarUnaVez() {
		viajero.embarcar(tarjeta, CLAVE_AUTORIZAR);

		assertTrue(viajero.isEmbarcado());
		assertTrue(viajero.tieneTarjetaEmbarque());
		assertSame(tarjeta, viajero.getTarjetaEmbarque());
		assertTrue(tarjeta.isEmbarcado());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarTarjetaNula() {
		viajero.embarcar(null, CLAVE_AUTORIZAR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarConTarjetaYClaveNula() {
		viajero.embarcar(tarjeta, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarConTarjetaYClaveVacia() {
		viajero.embarcar(tarjeta, "   ");
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarVariasVeces() {
		viajero.embarcar(tarjeta, CLAVE_AUTORIZAR);

		TarjetaEmbarque tarjeta2 = new TarjetaEmbarque(CLAVE_CREAR);
		viajero.embarcar(tarjeta2, CLAVE_AUTORIZAR);
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarConTarjetaYaUsadaAntes() {
		tarjeta.autorizaEmbarque(CLAVE_AUTORIZAR);
		viajero.embarcar(tarjeta, CLAVE_AUTORIZAR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmbarcarClaveMal() {
		viajero.embarcar(tarjeta, "CLAVE_MAL");
	}

	@Test
	public void testEquals() {
		Viajero eq1 = new Viajero("1234567Z", "Paco", "Sur", 0);
		Viajero distinto = new Viajero("124124F", "Antonio", "Gil Perez", 2);

		assertEquals(viajero, viajero);
		assertEquals(viajero, eq1);
		assertEquals(viajero.hashCode(), eq1.hashCode());
		assertNotEquals(viajero, distinto);
		assertNotEquals(viajero, null);
		assertNotEquals(viajero, "1234567Z");
	}
}
