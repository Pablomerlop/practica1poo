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

/**
 * Pruebas unitarias de la clase Aeropuerto.
 * 
 * @author pablmer
 * @author seraran
 */
public class AeropuertoTest {

	private Aeropuerto aeropuerto;
	private GPSCoordinate coordVillanubla;
	private GPSCoordinate coordMadrid;
	private Avion avion;
	private LocalDateTime salida;
	private LocalDateTime llegada;
	private Vuelo vuelo1;
	private Vuelo vuelo2;
	private CintaRecogida cinta1;
	private CintaRecogida cinta2;

	@Before
	public void setUp() {
		coordVillanubla = new GPSCoordinate(41.706111, -4.851944);
		coordMadrid = new GPSCoordinate(40.471926, -3.56264);
		aeropuerto = new Aeropuerto("Villanubla", "vll", "Carretera Adanero-Gijon", coordVillanubla, "España", 2, 2);

		avion = new Avion("43FE23", "Boeing 21", 20, 40, 100, 1000.0, 200);
		salida = LocalDateTime.of(2026, 10, 20, 10, 0);
		llegada = LocalDateTime.of(2026, 10, 20, 11, 30);
		vuelo1 = new Vuelo("ISH34", avion, salida, llegada, coordVillanubla, coordMadrid);
		vuelo2 = new Vuelo("IBE123", avion, salida, llegada, coordMadrid, coordVillanubla);

		cinta1 = new CintaRecogida("C1", 2000.0, 200);
		cinta1.setOperativa(true);
		cinta2 = new CintaRecogida("C2", 1500.0, 150);
	}

	@Test
	public void testEstadoInicial() {
		assertEquals("Villanubla", aeropuerto.getNombre());
		assertEquals("VLL", aeropuerto.getCodigoIATA());
		assertEquals("Carretera Adanero-Gijon", aeropuerto.getDireccion());
		assertEquals(coordVillanubla, aeropuerto.getCoordenadas());
		assertEquals("España", aeropuerto.getPais());
		assertEquals(0, aeropuerto.getVuelos().length);
		assertEquals(0, aeropuerto.getCintas().length);
		assertNotNull(aeropuerto.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaNombreNulo() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto(null, "VLL", "Carretera", coordVillanubla, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaNombreVacio() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("   ", "VLL", "Carretera", coordVillanubla, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIataNulo() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", null, "Carretera", coordVillanubla, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIataCortoAvlinf() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "VL", "Carretera", coordVillanubla, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaIataConNumeros() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "V1L", "Carretera", coordVillanubla, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaDireccionNula() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "VLL", null, coordVillanubla, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCoordenadasNulas() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "VLL", "Carretera", null, "España", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPaisVacio() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "VLL", "Carretera", coordVillanubla, "", 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxVuelosCeroAvlinf() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "VLL", "Carretera", coordVillanubla, "España", 0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxCintasCeroAvlinf() {
		@SuppressWarnings("unused")
		Aeropuerto a = new Aeropuerto("Villanubla", "VLL", "Carretera", coordVillanubla, "España", 1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddVueloNuloConAeropuertoLleno() {
		Aeropuerto lleno = new Aeropuerto("Villanubla", "VLL", "Carretera", coordVillanubla, "España", 1, 1);
		lleno.addVuelo(vuelo1);
		lleno.addVuelo(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddVueloDeOtroAeropuerto() {
		GPSCoordinate coordVallsur = new GPSCoordinate(41.623071, -4.749593);
		Vuelo ajeno = new Vuelo("VLL99", avion, salida, llegada, coordMadrid, coordVallsur);
		aeropuerto.addVuelo(ajeno);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddVueloRepetido() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddVueloExcediendoCapacidad() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo2);
		Vuelo vuelo3 = new Vuelo("VLL03", avion, salida, llegada, coordVillanubla, coordMadrid);
		aeropuerto.addVuelo(vuelo3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetVueloNulo() {
		aeropuerto.getVuelo(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetVueloNoExiste() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.getVuelo("NO_EXISTE");
	}

	@Test
	public void testAddVueloEmbarcarYEliminar() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo2);

		Viajero p1 = new Viajero("1234567Z", "Antonio", "Gil Perez", 1);
		aeropuerto.asignarPasajeroAVuelo("ISH34", p1);
		assertTrue(p1.tieneTarjetaEmbarque());

		Viajero p2 = new Viajero("12341424A", "Paco", "Sur", 1);
		p2.asignarTarjetaEmbarque(aeropuerto.emitirNuevaTarjetaEmbarque());
		aeropuerto.asignarPasajeroAVuelo("ISH34", p2);

		Viajero p3 = new Viajero("132412412D", "ana", "de", 1);
		vuelo1.addViajero(p3);

		aeropuerto.realizarEmbarque("ISH34");
		assertTrue(p1.isEmbarcado());
		assertTrue(p2.isEmbarcado());
		assertTrue(p3.isEmbarcado());

		vuelo2.cancelar();
		assertEquals(1, aeropuerto.obtenerVuelosOperativos().length);
		assertEquals(1, aeropuerto.obtenerVuelosCancelados().length);
		assertEquals(1, aeropuerto.obtenerVuelosRetrasados(salida.plusMinutes(15)).length);

		aeropuerto.eliminarVuelo("ISH34");
		assertEquals(1, aeropuerto.getVuelos().length);
		assertEquals(vuelo2, aeropuerto.getVuelos()[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAsignarPasajeroNulo() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.asignarPasajeroAVuelo("ISH34", null);
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarVueloCancelado() {
		aeropuerto.addVuelo(vuelo1);
		vuelo1.cancelar();
		aeropuerto.realizarEmbarque("ISH34");
	}

	@Test(expected = IllegalStateException.class)
	public void testEmbarcarVueloDespegado() {
		aeropuerto.addVuelo(vuelo1);
		vuelo1.registrarDespegue(salida);
		aeropuerto.realizarEmbarque("ISH34");
	}

	@Test
	public void testRealizarEmbarqueConTarjetas() {
		aeropuerto.addVuelo(vuelo1);
		Viajero pasajero = new Viajero("1234567Z", "Antonio", "Gil Perez", 1);
		aeropuerto.asignarPasajeroAVuelo("ISH34", pasajero);

		TarjetaEmbarque[] tarjetas = new TarjetaEmbarque[] { aeropuerto.emitirNuevaTarjetaEmbarque() };
		aeropuerto.realizarEmbarque("ISH34", tarjetas);
		assertTrue(pasajero.isEmbarcado());
		assertSame(tarjetas[0], pasajero.getTarjetaEmbarque());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRealizarEmbarqueArrayNulo() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.realizarEmbarque("ISH34", null);
	}

	@Test(expected = IllegalStateException.class)
	public void testRealizarEmbarqueConTarjetasVueloCancelado() {
		aeropuerto.addVuelo(vuelo1);
		vuelo1.cancelar();
		aeropuerto.realizarEmbarque("ISH34", new TarjetaEmbarque[0]);
	}

	@Test(expected = IllegalStateException.class)
	public void testRealizarEmbarqueConTarjetasVueloDespegado() {
		aeropuerto.addVuelo(vuelo1);
		vuelo1.registrarDespegue(salida);
		aeropuerto.realizarEmbarque("ISH34", new TarjetaEmbarque[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRealizarEmbarqueDistintoNumeroTarjetas() {
		aeropuerto.addVuelo(vuelo1);
		Viajero pasajero = new Viajero("1234567Z", "Antonio", "Gil Perez", 1);
		aeropuerto.asignarPasajeroAVuelo("ISH34", pasajero);
		aeropuerto.realizarEmbarque("ISH34", new TarjetaEmbarque[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRealizarEmbarqueConTarjetaNula() {
		aeropuerto.addVuelo(vuelo1);
		Viajero pasajero = new Viajero("1234567Z", "Antonio", "Gil Perez", 1);
		aeropuerto.asignarPasajeroAVuelo("ISH34", pasajero);
		aeropuerto.realizarEmbarque("ISH34", new TarjetaEmbarque[] { null });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testObtenerVuelosRetrasadosHoraNula() {
		aeropuerto.obtenerVuelosRetrasados(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddCintaNula() {
		aeropuerto.addCinta(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddCintaRepetida() {
		aeropuerto.addCinta(cinta1);
		aeropuerto.addCinta(cinta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddCintaExcediendoCapacidad() {
		aeropuerto.addCinta(cinta1);
		aeropuerto.addCinta(cinta2);
		aeropuerto.addCinta(new CintaRecogida("C3", 100.0, 10));
	}

	@Test
	public void testAddCintaDescargarEquipajeYEliminar() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo2);
		aeropuerto.addCinta(cinta1);
		aeropuerto.addCinta(cinta2);

		assertEquals(2, aeropuerto.getCintas().length);

		Viajero pasajero = new Viajero("1234567Z", "Antonio", "Gil Perez", 1);
		Maleta maleta = new Maleta(15.0, "30x30x30");
		pasajero.facturarMaleta(maleta);
		aeropuerto.asignarPasajeroAVuelo("ISH34", pasajero);

		aeropuerto.asignarCintaAVuelo("ISH34", cinta1);
		aeropuerto.asignarCintaAVuelo("ISH34", cinta1);
		aeropuerto.descargarEquipajeDeVuelo("ISH34");

		assertEquals(1, aeropuerto.consultarCintasOperativas().length);
		assertEquals(1, aeropuerto.consultarCintasConEquipajePendiente().length);
		assertSame(cinta1, aeropuerto.getCinta("C1"));

		cinta2.setOperativa(true);
		aeropuerto.asignarCintaAVuelo("IBE123", cinta2);
		vuelo2.cancelar();

		aeropuerto.eliminarCinta("C2");
		assertEquals(1, aeropuerto.getCintas().length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAsignarCintaNulaAVuelo() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.asignarCintaAVuelo("ISH34", null);
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarMismaCintaADosVuelos() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addVuelo(vuelo2);
		aeropuerto.addCinta(cinta1);

		aeropuerto.asignarCintaAVuelo("ISH34", cinta1);
		aeropuerto.asignarCintaAVuelo("IBE123", cinta1);
	}

	@Test(expected = IllegalStateException.class)
	public void testEliminarCintaConMaletas() {
		aeropuerto.addCinta(cinta1);
		cinta1.depositarMaleta(new Maleta(10.0, "30x30x20"));
		aeropuerto.eliminarCinta("C1");
	}

	@Test(expected = IllegalStateException.class)
	public void testEliminarCintaDeVueloActivo() {
		aeropuerto.addVuelo(vuelo1);
		aeropuerto.addCinta(cinta1);
		aeropuerto.asignarCintaAVuelo("ISH34", cinta1);
		aeropuerto.eliminarCinta("C1");
	}

	@Test
	public void testEquals() {
		Aeropuerto eq1 = new Aeropuerto("Otro Nombre", "VLL", "Otra Calle", coordMadrid, "España", 1, 1);
		Aeropuerto distinto = new Aeropuerto("Barajas", "MAD", "Av. Hispanidad", coordMadrid, "España", 2, 2);

		assertEquals(aeropuerto, aeropuerto);
		assertEquals(aeropuerto, eq1);
		assertEquals(aeropuerto.hashCode(), eq1.hashCode());
		assertNotEquals(aeropuerto, distinto);
		assertNotEquals(aeropuerto, null);
		assertNotEquals(aeropuerto, "VLL");
	}
}
