package uva.poo.practica1.test;

import static org.junit.Assert.*;

import es.uva.inf.poo.maps.GPSCoordinate;
import fabricante.externo.tarjeta.TarjetaEmbarque;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.Aeropuerto;
import uva.poo.practica1.Avion;
import uva.poo.practica1.CintaRecogida;
import uva.poo.practica1.Maleta;
import uva.poo.practica1.Viajero;
import uva.poo.practica1.Vuelo;

public class AeropuertoTest {

	private Aeropuerto aeropuerto;
	private GPSCoordinate coordMadrid;
	private GPSCoordinate coordValladolid;
	private Avion avion;
	private LocalDateTime salida;
	private LocalDateTime llegada;
	private Vuelo vuelo1;
	private CintaRecogida cinta1;

	@Before
	public void setUp() {
		coordMadrid = new GPSCoordinate(40.471926, -3.56264);
		coordValladolid = new GPSCoordinate(41.706111, -4.851944);
		aeropuerto = new Aeropuerto("Barajas", "mad", "Av. Hispanidad", coordMadrid, "España", 2, 2);

		avion = new Avion("EC-MAD", "Airbus A320", 10, 20, 100, 2000.0, 100);
		salida = LocalDateTime.of(2026, 10, 20, 10, 0);
		llegada = LocalDateTime.of(2026, 10, 20, 11, 30);
		vuelo1 = new Vuelo("IB200", avion, salida, llegada, coordMadrid, coordValladolid);

		cinta1 = new CintaRecogida("C1", 500.0, 50);
		cinta1.setOperativa(true);
	}

	@Test
	public void testInicializacionValida() {
		assertEquals("Barajas", aeropuerto.getNombre());
		assertEquals("MAD", aeropuerto.getCodigoIATA());
		assertEquals("Av. Hispanidad", aeropuerto.getDireccion());
		assertEquals(coordMadrid, aeropuerto.getCoordenadas());
		assertEquals("España", aeropuerto.getPais());
		assertEquals(0, aeropuerto.getVuelos().length);
		assertEquals(0, aeropuerto.getCintas().length);
		assertNotNull(aeropuerto.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCodigoIATALongitudMenorDeTres() {
		@SuppressWarnings("unused")
		Aeropuerto invalido = new Aeropuerto("Barajas", "MA", "Dir", coordMadrid, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCodigoIATAConDigitos() {
		@SuppressWarnings("unused")
		Aeropuerto invalido = new Aeropuerto("Barajas", "M1D", "Dir", coordMadrid, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddVueloNuloConAeropuertoLlenoLanzaIllegalArgumentPrimero() {
		Aeropuerto lleno = new Aeropuerto("Barajas", "MAD", "Dir", coordMadrid, "España", 1, 1);
		lleno.addVuelo(vuelo1);
		lleno.addVuelo(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddVueloAjenoACoordenadasDelAeropuerto() {
		GPSCoordinate otra = new GPSCoordinate(41.623071, -4.749593);
		Vuelo ajeno = new Vuelo("IB999", avion, salida, llegada, coordValladolid, otra);
		aeropuerto.addVuelo(ajeno);
	}

	@Test
	public void testGestionVuelosEstadosYEmbarque() {
		Vuelo vuelo2 = new Vuelo("IB201", avion, salida, llegada, coordValladolid, coordMadrid);
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo2);

		Viajero pasajero = new Viajero("12345678Z", "Laura", "Gomez", 1);
		aeropuerto.asignarPasajeroAVuelo("IB200", pasajero);
		assertTrue(pasajero.tieneTarjetaEmbarque());

		aeropuerto.realizarEmbarque("IB200");
		assertTrue(pasajero.isEmbarcado());

		vuelo2.cancelar();
		assertEquals(1, aeropuerto.obtenerVuelosOperativos().length);
		assertEquals(1, aeropuerto.obtenerVuelosCancelados().length);
		assertEquals(1, aeropuerto.obtenerVuelosRetrasados(salida.plusMinutes(15)).length);

		aeropuerto.eliminarVuelo("IB201");
		assertEquals(1, aeropuerto.getVuelos().length);
	}

	@Test
	public void testRealizarEmbarqueConTarjetasExternas() {
		aeropuerto.addVuelo(vuelo1);
		Viajero pasajero = new Viajero("12345678Z", "Laura", "Gomez", 1);
		aeropuerto.asignarPasajeroAVuelo("IB200", pasajero);

		TarjetaEmbarque[] tarjetas = new TarjetaEmbarque[] { aeropuerto.emitirNuevaTarjetaEmbarque() };
		aeropuerto.realizarEmbarque("IB200", tarjetas);
		assertTrue(pasajero.isEmbarcado());
		assertSame(tarjetas[0], pasajero.getTarjetaEmbarque());
	}

	@Test
	public void testAsignarCintaYDescargaEquipaje() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addCinta(cinta1);

		Viajero pasajero = new Viajero("12345678Z", "Laura", "Gomez", 1);
		Maleta maleta = new Maleta(15.0, "40x30x20");
		pasajero.facturarMaleta(maleta);
		aeropuerto.asignarPasajeroAVuelo("IB200", pasajero);

		aeropuerto.asignarCintaAVuelo("IB200", cinta1);
		aeropuerto.descargarEquipajeDeVuelo("IB200");

		assertEquals(1, aeropuerto.consultarCintasOperativas().length);
		assertEquals(1, aeropuerto.consultarCintasConEquipajePendiente().length);
		assertSame(cinta1, aeropuerto.getCinta("C1"));
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarMismaCintaADosVuelosOperativos() {
		Vuelo vuelo2 = new Vuelo("IB201", avion, salida, llegada, coordValladolid, coordMadrid);
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo2);
		aeropuerto.addCinta(cinta1);

		aeropuerto.asignarCintaAVuelo("IB200", cinta1);
		aeropuerto.asignarCintaAVuelo("IB201", cinta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testEliminarCintaAsignadaAVueloOperativo() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addCinta(cinta1);
		aeropuerto.asignarCintaAVuelo("IB200", cinta1);
		aeropuerto.eliminarCinta("C1");
	}
}