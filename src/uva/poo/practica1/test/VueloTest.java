package uva.poo.practica1.test;

import static org.junit.Assert.*;

import es.uva.inf.poo.maps.GPSCoordinate;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.Avion;
import uva.poo.practica1.CintaRecogida;
import uva.poo.practica1.Maleta;
import uva.poo.practica1.Viajero;
import uva.poo.practica1.Vuelo;

/**
 * Pruebas unitarias de la clase Vuelo.
 * 
 * @author pablmer
 * @author seraran
 */
public class VueloTest {

	private static final double DELTA = 0.0001;
	private Avion avion;
	private GPSCoordinate origen;
	private GPSCoordinate destino;
	private LocalDateTime salida;
	private LocalDateTime llegada;
	private Vuelo vuelo;
	private Viajero viajero1;
	private Viajero viajero2;

	@Before
	public void setUp() {
		avion = new Avion("EC-100", "Boeing 737", 0, 1, 1, 50.0, 2);
		origen = new GPSCoordinate(41.623071, -4.749593);
		destino = new GPSCoordinate(41.6372231, -4.738908);
		salida = LocalDateTime.of(2026, 10, 20, 10, 0);
		llegada = LocalDateTime.of(2026, 10, 20, 12, 0);
		vuelo = new Vuelo("IB101", avion, salida, llegada, origen, destino);

		viajero1 = new Viajero("11111111A", "Paco", "Sanz", 2);
		viajero2 = new Viajero("22222222B", "Ana", "Diaz", 2);
	}

	@Test
	public void testInicializacionYDistanciaValida() {
		assertEquals("IB101", vuelo.getCodigo());
		assertEquals("IB101", vuelo.getIdentificador());
		assertSame(avion, vuelo.getAvion());
		assertEquals(salida, vuelo.getFechaHoraSalida());
		assertEquals(llegada, vuelo.getFechaHoraLlegada());
		assertEquals(origen, vuelo.getPosicionSalida());
		assertEquals(destino, vuelo.getPosicionLlegada());
		assertEquals(1.806, vuelo.getDistancia(), GPSCoordinate.PRECISION_IN_DISTANCE);
		assertTrue(vuelo.isOperativo());
		assertFalse(vuelo.isCancelado());
		assertFalse(vuelo.isDespegado());
		assertFalse(vuelo.tieneCintaAsignada());
		assertFalse(vuelo.isEquipajeDescargado());
		assertEquals(0, vuelo.getNumViajeros());
		assertNotNull(vuelo.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaFechaLlegadaIgualASalidaAVL() {
		@SuppressWarnings("unused")
		Vuelo invalido = new Vuelo("IB101", avion, salida, salida, origen, destino);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCodigoNulo() {
		@SuppressWarnings("unused")
		Vuelo invalido = new Vuelo(null, avion, salida, llegada, origen, destino);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaAvionNulo() {
		@SuppressWarnings("unused")
		Vuelo invalido = new Vuelo("IB101", null, salida, llegada, origen, destino);
	}

	@Test
	public void testRetrasoConDespegueExplicitamenteRegistrado() {
		assertFalse(vuelo.isRetrasado(salida));
		assertTrue(vuelo.isRetrasado(salida.plusMinutes(1)));

		vuelo.registrarDespegue(salida);
		assertTrue(vuelo.isDespegado());
		assertEquals(salida, vuelo.getFechaHoraSalidaReal());
		assertFalse(vuelo.isRetrasado(salida.plusHours(3)));
	}

	@Test
	public void testVueloCanceladoNoEstaRetrasado() {
		vuelo.cancelar();
		assertTrue(vuelo.isCancelado());
		assertFalse(vuelo.isOperativo());
		assertFalse(vuelo.isRetrasado(salida.plusHours(5)));
	}

	@Test
	public void testRespetoDePlazasActualesTrasRedistribucion() {
		avion.redistribuirPlazas(0, 0, 1);
		vuelo.addViajero(viajero1);
		assertEquals(1, vuelo.getNumViajeros());
	}

	@Test(expected = IllegalStateException.class)
	public void testAddViajeroSuperaPlazasActualesRedistribuidas() {
		avion.redistribuirPlazas(0, 0, 1);
		vuelo.addViajero(viajero1);
		vuelo.addViajero(viajero2);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaPosteriormenteSuperandoPesoAvion() {
		vuelo.addViajero(viajero1);
		viajero1.facturarMaleta(new Maleta(30.0, "40x40x20"));
		viajero1.facturarMaleta(new Maleta(25.0, "40x40x20"));
	}

	@Test(expected = IllegalStateException.class)
	public void testAddViajeroSuperaNumeroMaximoMaletasAvion() {
		viajero1.facturarMaleta(new Maleta(10.0, "30x30x30"));
		viajero1.facturarMaleta(new Maleta(10.0, "30x30x30"));
		vuelo.addViajero(viajero1);

		viajero2.facturarMaleta(new Maleta(5.0, "20x20x20"));
		vuelo.addViajero(viajero2);
	}

	@Test
	public void testAsignarCintaYDescargarEquipajeValido() {
		viajero1.facturarMaleta(new Maleta(20.0, "40x40x20"));
		vuelo.addViajero(viajero1);

		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		vuelo.asignarCinta(cinta);

		assertTrue(vuelo.tieneCintaAsignada());
		assertSame(cinta, vuelo.getCintaAsignada());

		vuelo.descargarEquipajeEnCinta();
		assertTrue(vuelo.isEquipajeDescargado());
		assertEquals(1, cinta.getNumMaletas());
		assertEquals(20.0, cinta.getPesoAcumulado(), DELTA);
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeEnCintaNoEsRepetible() {
		viajero1.facturarMaleta(new Maleta(20.0, "40x40x20"));
		vuelo.addViajero(viajero1);
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		vuelo.asignarCinta(cinta);

		vuelo.descargarEquipajeEnCinta();
		vuelo.descargarEquipajeEnCinta();
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarCintaNoOperativaFallaSinEfectoOculto() {
		CintaRecogida cintaInactiva = new CintaRecogida("C1", 100.0, 5);
		vuelo.asignarCinta(cintaInactiva);
	}

	@Test
	public void testReprogramarFechasYCambiarDestino() {
		LocalDateTime nuevaSalida = salida.plusMinutes(30);
		LocalDateTime nuevaLlegada = llegada.plusHours(1);
		GPSCoordinate nuevoDestino = new GPSCoordinate(40.471926, -3.56264);

		vuelo.setFechaHoraSalida(nuevaSalida);
		vuelo.setFechaHoraLlegada(nuevaLlegada);
		vuelo.setPosicionLlegada(nuevoDestino);

		assertEquals(nuevaSalida, vuelo.getFechaHoraSalida());
		assertEquals(nuevaLlegada, vuelo.getFechaHoraLlegada());
		assertEquals(nuevoDestino, vuelo.getPosicionLlegada());
	}
}