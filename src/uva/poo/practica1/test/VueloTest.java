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

	private static final double ERROR_ADMISIBLE = 0.001;
	private Avion avion;
	private GPSCoordinate coordVallsur;
	private GPSCoordinate coordDelibes;
	private LocalDateTime salida;
	private LocalDateTime llegada;
	private Vuelo vuelo;
	private Viajero viajero1;
	private Viajero viajero2;

	@Before
	public void crearObjetos() {
		avion = new Avion("43FE23", "Boeing 21", 0, 1, 1, 50.0, 2);
		coordVallsur = new GPSCoordinate(41.623071, -4.749593);
		coordDelibes = new GPSCoordinate(41.6372231, -4.738908);
		salida = LocalDateTime.of(2026, 10, 20, 10, 0);
		llegada = LocalDateTime.of(2026, 10, 20, 12, 0);
		vuelo = new Vuelo("ISH34", avion, salida, llegada, coordVallsur, coordDelibes);

		viajero1 = new Viajero("12341424A", "Paco", "Sur", 2);
		viajero2 = new Viajero("132412412D", "ana", "de", 2);
	}

	@Test
	public void testEstadoInicial() {
		assertEquals("ISH34", vuelo.getCodigo());
		assertEquals("ISH34", vuelo.getIdentificador());
		assertSame(avion, vuelo.getAvion());
		assertEquals(salida, vuelo.getFechaHoraSalida());
		assertEquals(llegada, vuelo.getFechaHoraLlegada());
		assertEquals(coordVallsur, vuelo.getPosicionSalida());
		assertEquals(coordDelibes, vuelo.getPosicionLlegada());
		assertEquals(1.806, vuelo.getDistancia(), ERROR_ADMISIBLE);
		assertTrue(vuelo.isOperativo());
		assertFalse(vuelo.isCancelado());
		assertFalse(vuelo.isDespegado());
		assertFalse(vuelo.tieneCintaAsignada());
		assertFalse(vuelo.isEquipajeDescargado());
		assertEquals(0, vuelo.getNumViajeros());
		assertEquals(0, vuelo.getViajeros().length);
		assertNotNull(vuelo.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCodigoNulo() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo(null, avion, salida, llegada, coordVallsur, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCodigoVacio() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("   ", avion, salida, llegada, coordVallsur, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaAvionNulo() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("ISH34", null, salida, llegada, coordVallsur, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaSalidaNula() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("ISH34", avion, null, llegada, coordVallsur, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaLlegadaNula() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("ISH34", avion, salida, null, coordVallsur, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMismaHoraAvlinf() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("ISH34", avion, salida, salida, coordVallsur, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPosicionSalidaNula() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("ISH34", avion, salida, llegada, null, coordDelibes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPosicionLlegadaNula() {
		@SuppressWarnings("unused")
		Vuelo v = new Vuelo("ISH34", avion, salida, llegada, coordVallsur, null);
	}

	@Test
	public void testCambiarFechasYDestinoVillanubla() {
		LocalDateTime nuevaSalida = salida.plusMinutes(30);
		LocalDateTime nuevaLlegada = llegada.plusHours(1);
		GPSCoordinate coordVillanubla = new GPSCoordinate(41.706111, -4.851944);

		vuelo.setFechaHoraSalida(nuevaSalida);
		vuelo.setFechaHoraLlegada(nuevaLlegada);
		vuelo.setPosicionLlegada(coordVillanubla);

		assertEquals(nuevaSalida, vuelo.getFechaHoraSalida());
		assertEquals(nuevaLlegada, vuelo.getFechaHoraLlegada());
		assertEquals(coordVillanubla, vuelo.getPosicionLlegada());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetFechaSalidaNula() {
		vuelo.setFechaHoraSalida(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetFechaSalidaMayorQueLlegada() {
		vuelo.setFechaHoraSalida(llegada);
	}

	@Test(expected = IllegalStateException.class)
	public void testSetFechaSalidaVueloCancelado() {
		vuelo.cancelar();
		vuelo.setFechaHoraSalida(salida.plusMinutes(10));
	}

	@Test(expected = IllegalStateException.class)
	public void testSetFechaSalidaYaDespegado() {
		vuelo.registrarDespegue(salida);
		vuelo.setFechaHoraSalida(salida.plusMinutes(10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetFechaLlegadaNula() {
		vuelo.setFechaHoraLlegada(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetFechaLlegadaMenorQueSalida() {
		vuelo.setFechaHoraLlegada(salida);
	}

	@Test(expected = IllegalStateException.class)
	public void testSetFechaLlegadaVueloCancelado() {
		vuelo.cancelar();
		vuelo.setFechaHoraLlegada(llegada.plusMinutes(30));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPosicionLlegadaNula() {
		vuelo.setPosicionLlegada(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testSetPosicionLlegadaVueloCancelado() {
		vuelo.cancelar();
		vuelo.setPosicionLlegada(coordVallsur);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetCintaSinAsignar() {
		vuelo.getCintaAsignada();
	}

	@Test(expected = IllegalStateException.class)
	public void testCancelarDosVeces() {
		vuelo.cancelar();
		vuelo.cancelar();
	}

	@Test(expected = IllegalStateException.class)
	public void testCancelarVueloYaDespegado() {
		vuelo.registrarDespegue(salida);
		vuelo.cancelar();
	}

	@Test(expected = IllegalStateException.class)
	public void testGetHoraSalidaRealSinDespegar() {
		vuelo.getFechaHoraSalidaReal();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegistrarDespegueNulo() {
		vuelo.registrarDespegue(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testRegistrarDespegueVueloCancelado() {
		vuelo.cancelar();
		vuelo.registrarDespegue(salida);
	}

	@Test(expected = IllegalStateException.class)
	public void testRegistrarDespegueDosVeces() {
		vuelo.registrarDespegue(salida);
		vuelo.registrarDespegue(salida.plusMinutes(5));
	}

	@Test
	public void testRetrasoYDespegue() {
		assertFalse(vuelo.isRetrasado(salida));
		assertTrue(vuelo.isRetrasado(salida.plusMinutes(1)));

		vuelo.registrarDespegue(salida);
		assertTrue(vuelo.isDespegado());
		assertEquals(salida, vuelo.getFechaHoraSalidaReal());
		assertFalse(vuelo.isRetrasado(salida.plusHours(3)));

		Vuelo vueloTarde = new Vuelo("IBE123", avion, salida, llegada, coordVallsur, coordDelibes);
		vueloTarde.registrarDespegue(salida.plusMinutes(15));
		assertTrue(vueloTarde.isRetrasado(salida.plusMinutes(20)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsRetrasadoHoraNula() {
		vuelo.isRetrasado(null);
	}

	@Test
	public void testVueloCanceladoNoEstaRetrasado() {
		vuelo.cancelar();
		assertTrue(vuelo.isCancelado());
		assertFalse(vuelo.isOperativo());
		assertFalse(vuelo.isRetrasado(salida.plusHours(5)));
	}

	@Test
	public void testAddViajero() {
		avion.redistribuirPlazas(0, 0, 1);
		vuelo.addViajero(viajero1);
		assertEquals(1, vuelo.getViajeros().length);
		assertEquals(viajero1, vuelo.getViajeros()[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddViajeroNulo() {
		vuelo.addViajero(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddViajeroRepetido() {
		vuelo.addViajero(viajero1);
		vuelo.addViajero(viajero1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddViajeroVueloCancelado() {
		vuelo.cancelar();
		vuelo.addViajero(viajero1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddViajeroVueloDespegado() {
		vuelo.registrarDespegue(salida);
		vuelo.addViajero(viajero1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddViajeroYaEnOtroVuelo() {
		Vuelo otro = new Vuelo("IBE123", avion, salida, llegada, coordVallsur, coordDelibes);
		vuelo.addViajero(viajero1);
		otro.addViajero(viajero1);
	}

	@Test(expected = IllegalStateException.class)
	public void testVueloLleno() {
		Viajero viajero3 = new Viajero("124124F", "ef", "aefe", 1);
		vuelo.addViajero(viajero1);
		vuelo.addViajero(viajero2);
		vuelo.addViajero(viajero3);
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaSuperandoPesoAvion() {
		vuelo.addViajero(viajero1);
		viajero1.facturarMaleta(new Maleta(30.0, "40x40x20"));
		viajero1.facturarMaleta(new Maleta(25.0, "40x40x20"));
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaDespuesDeCancelarVuelo() {
		vuelo.addViajero(viajero1);
		vuelo.cancelar();
		viajero1.facturarMaleta(new Maleta(10.0, "40x40x20"));
	}

	@Test(expected = IllegalStateException.class)
	public void testFacturarMaletaDespuesDeDespegar() {
		vuelo.addViajero(viajero1);
		vuelo.registrarDespegue(salida);
		viajero1.facturarMaleta(new Maleta(10.0, "40x40x20"));
	}

	@Test(expected = IllegalStateException.class)
	public void testAddViajeroSuperaMaxMaletasAvion() {
		viajero1.facturarMaleta(new Maleta(10.0, "30x30x30"));
		viajero1.facturarMaleta(new Maleta(10.0, "30x30x30"));
		vuelo.addViajero(viajero1);

		viajero2.facturarMaleta(new Maleta(5.0, "20x20x20"));
		vuelo.addViajero(viajero2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAsignarCintaNula() {
		vuelo.asignarCinta(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarCintaVueloCancelado() {
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		vuelo.cancelar();
		vuelo.asignarCinta(cinta);
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarCintaFueraServicio() {
		CintaRecogida cintaApagada = new CintaRecogida("C1", 100.0, 5);
		vuelo.asignarCinta(cintaApagada);
	}

	@Test
	public void testAsignarCintaYDescargarEquipaje() {
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
		assertEquals(20.0, cinta.getPesoAcumulado(), ERROR_ADMISIBLE);
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeVueloCancelado() {
		vuelo.cancelar();
		vuelo.descargarEquipajeEnCinta();
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeSinCinta() {
		vuelo.descargarEquipajeEnCinta();
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeCintaApagadaDespues() {
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		vuelo.asignarCinta(cinta);
		cinta.setOperativa(false);
		vuelo.descargarEquipajeEnCinta();
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeDosVeces() {
		viajero1.facturarMaleta(new Maleta(20.0, "40x40x20"));
		vuelo.addViajero(viajero1);
		CintaRecogida cinta = new CintaRecogida("C1", 100.0, 5);
		cinta.setOperativa(true);
		vuelo.asignarCinta(cinta);

		vuelo.descargarEquipajeEnCinta();
		vuelo.descargarEquipajeEnCinta();
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeSinHuecosEnCinta() {
		viajero1.facturarMaleta(new Maleta(10.0, "40x40x20"));
		viajero1.facturarMaleta(new Maleta(10.0, "40x40x20"));
		vuelo.addViajero(viajero1);

		CintaRecogida cintaPequena = new CintaRecogida("C1", 100.0, 1);
		cintaPequena.setOperativa(true);
		vuelo.asignarCinta(cintaPequena);
		vuelo.descargarEquipajeEnCinta();
	}

	@Test(expected = IllegalStateException.class)
	public void testDescargarEquipajeSuperaPesoCinta() {
		viajero1.facturarMaleta(new Maleta(30.0, "40x40x20"));
		vuelo.addViajero(viajero1);

		CintaRecogida cintaPocoPeso = new CintaRecogida("C1", 20.0, 5);
		cintaPocoPeso.setOperativa(true);
		vuelo.asignarCinta(cintaPocoPeso);
		vuelo.descargarEquipajeEnCinta();
	}

	@Test
	public void testEquals() {
		Vuelo eq1 = new Vuelo("ISH34", avion, salida.plusHours(1), llegada.plusHours(2), coordDelibes, coordVallsur);
		Vuelo distinto = new Vuelo("IBE123", avion, salida, llegada, coordVallsur, coordDelibes);

		assertEquals(vuelo, vuelo);
		assertEquals(vuelo, eq1);
		assertEquals(vuelo.hashCode(), eq1.hashCode());
		assertNotEquals(vuelo, distinto);
		assertNotEquals(vuelo, null);
		assertNotEquals(vuelo, "ISH34");
	}
}
