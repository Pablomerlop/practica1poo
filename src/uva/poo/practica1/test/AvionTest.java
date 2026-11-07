package uva.poo.practica1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.Avion;

public class AvionTest {

	private static final double DELTA = 0.0001;
	private Avion avion;

	@Before
	public void setUp() {
		avion = new Avion("EC-ABC", "Airbus A320", 10, 20, 120, 3000.0, 150);
	}

	@Test
	public void testInicializacionValida() {
		assertEquals("EC-ABC", avion.getMatricula());
		assertEquals("EC-ABC", avion.getIdentificador());
		assertEquals("Airbus A320", avion.getModelo());
		assertEquals(10, avion.getPlazasPrimera());
		assertEquals(20, avion.getPlazasBusiness());
		assertEquals(120, avion.getPlazasTurista());
		assertEquals(150, avion.getPlazasTotales());
		assertEquals(150, avion.getCapacidadMaximaPasajeros());
		assertEquals(3000.0, avion.getEquipajeMaxKg(), DELTA);
		assertEquals(150, avion.getMaxMaletas());
		assertNotNull(avion.toString());
	}

	@Test
	public void testInicializacionValidaAVLInf() {
		Avion avionMinimo = new Avion("EC-MIN", "Cessna", 0, 0, 1, 0.1, 0);
		assertEquals(1, avionMinimo.getPlazasTotales());
		assertEquals(1, avionMinimo.getCapacidadMaximaPasajeros());
		assertEquals(0, avionMinimo.getMaxMaletas());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMatriculaNula() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion(null, "Airbus A320", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMatriculaVacia() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("   ", "Airbus A320", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaModeloNulo() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", null, 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaModeloVacio() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPlazasPrimeraNegativas() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "Airbus", -1, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPlazasBusinessNegativas() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "Airbus", 10, -1, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPlazasTuristaNegativas() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "Airbus", 10, 20, -1, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaTotalPlazasCero() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "Airbus", 0, 0, 0, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaEquipajeMaxCero() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "Airbus", 10, 20, 120, 0.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaxMaletasNegativo() {
		@SuppressWarnings("unused")
		Avion invalido = new Avion("EC-ABC", "Airbus", 10, 20, 120, 3000.0, -1);
	}

	@Test
	public void testRedistribuirPlazasValidoAVLSup() {
		avion.redistribuirPlazas(20, 30, 100);
		assertEquals(20, avion.getPlazasPrimera());
		assertEquals(30, avion.getPlazasBusiness());
		assertEquals(100, avion.getPlazasTurista());
		assertEquals(150, avion.getPlazasTotales());
		assertEquals(150, avion.getCapacidadMaximaPasajeros());
	}

	@Test
	public void testRedistribuirPlazasValidoMenorAlMaximoAVLInf() {
		avion.redistribuirPlazas(0, 0, 1);
		assertEquals(1, avion.getPlazasTotales());
		assertEquals(150, avion.getCapacidadMaximaPasajeros());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasInvalidoSuperaMaximoAVLSup() {
		avion.redistribuirPlazas(20, 30, 101);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasInvalidoTotalCeroAVLInf() {
		avion.redistribuirPlazas(0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasInvalidoPrimeraNegativa() {
		avion.redistribuirPlazas(-1, 30, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasInvalidoBusinessNegativa() {
		avion.redistribuirPlazas(10, -1, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasInvalidoTuristaNegativa() {
		avion.redistribuirPlazas(10, 20, -1);
	}

	@Test
	public void testEqualsYHashCode() {
		Avion igual = new Avion("EC-ABC", "Otro Modelo", 5, 5, 5, 500.0, 10);
		Avion distinto = new Avion("EC-XYZ", "Airbus A320", 10, 20, 120, 3000.0, 150);

		assertEquals(avion, avion);
		assertEquals(avion, igual);
		assertEquals(igual, avion);
		assertEquals(avion.hashCode(), igual.hashCode());
		assertNotEquals(avion, distinto);
		assertNotEquals(avion, null);
		assertNotEquals(avion, "EC-ABC");
	}
}