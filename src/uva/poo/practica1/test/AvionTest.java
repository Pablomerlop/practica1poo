package uva.poo.practica1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import uva.poo.practica1.Avion;

/**
 * Pruebas unitarias de la clase Avion.
 * 
 * @author pablmer
 * @author seraran
 */
public class AvionTest {

	private static final double ERROR_ADMISIBLE = 0.001;
	private Avion avion;

	@Before
	public void crearAvion() {
		avion = new Avion("4563AGH", "Airbom 300", 10, 20, 120, 3000.0, 150);
	}

	@Test
	public void testEstadoInicial() {
		assertEquals("4563AGH", avion.getMatricula());
		assertEquals("4563AGH", avion.getIdentificador());
		assertEquals("Airbom 300", avion.getModelo());
		assertEquals(10, avion.getPlazasPrimera());
		assertEquals(20, avion.getPlazasBusiness());
		assertEquals(120, avion.getPlazasTurista());
		assertEquals(150, avion.getPlazasTotales());
		assertEquals(150, avion.getCapacidadMaximaPasajeros());
		assertEquals(3000.0, avion.getEquipajeMaxKg(), ERROR_ADMISIBLE);
		assertEquals(150, avion.getMaxMaletas());
		assertNotNull(avion.toString());
	}

	@Test
	public void testInicializacionValidaAvlinf() {
		Avion avioneta = new Avion("1111AAA", "Avioneta Villanubla", 0, 0, 1, 0.5, 0);
		assertEquals(1, avioneta.getPlazasTotales());
		assertEquals(1, avioneta.getCapacidadMaximaPasajeros());
		assertEquals(0, avioneta.getMaxMaletas());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMatriculaNula() {
		@SuppressWarnings("unused")
		Avion a = new Avion(null, "Airbom 300", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMatriculaVacia() {
		@SuppressWarnings("unused")
		Avion a = new Avion("   ", "Airbom 300", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaModeloNulo() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", null, 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaModeloVacio() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaModeloEspacios() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "   ", 10, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaPrimeraNegativa() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", -1, 20, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaBusinessNegativa() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", 10, -1, 120, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaTuristaNegativa() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", 10, 20, -1, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaCeroPlazasAvlinf() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", 0, 0, 0, 3000.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaEquipajeCeroAvlinf() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", 10, 20, 120, 0.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaEquipajeNegativo() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", 10, 20, 120, -50.0, 150);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInicializacionInvalidaMaletasNegativas() {
		@SuppressWarnings("unused")
		Avion a = new Avion("4563AGH", "Airbom 300", 10, 20, 120, 3000.0, -1);
	}

	@Test
	public void testRedistribuirPlazasBien() {
		avion.redistribuirPlazas(20, 30, 100);
		assertEquals(20, avion.getPlazasPrimera());
		assertEquals(30, avion.getPlazasBusiness());
		assertEquals(100, avion.getPlazasTurista());
		assertEquals(150, avion.getPlazasTotales());
		assertEquals(150, avion.getCapacidadMaximaPasajeros());
	}

	@Test
	public void testRedistribuirPlazasMenosDelTotalAvlinf() {
		avion.redistribuirPlazas(0, 0, 1);
		assertEquals(1, avion.getPlazasTotales());
		assertEquals(150, avion.getCapacidadMaximaPasajeros());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasExcede() {
		avion.redistribuirPlazas(20, 30, 101);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasCeroAvlinf() {
		avion.redistribuirPlazas(0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasPrimeraNegativa() {
		avion.redistribuirPlazas(-1, 30, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasBusinessNegativa() {
		avion.redistribuirPlazas(10, -1, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedistribuirPlazasTuristaNegativa() {
		avion.redistribuirPlazas(10, 20, -1);
	}

	@Test
	public void testEquals() {
		Avion eq1 = new Avion("4563AGH", "Boeing 21", 5, 5, 5, 500.0, 10);
		Avion distinto = new Avion("99AA99", "Airbom 300", 10, 20, 120, 3000.0, 150);

		assertEquals(avion, avion);
		assertEquals(avion, eq1);
		assertEquals(eq1, avion);
		assertEquals(avion.hashCode(), eq1.hashCode());
		assertNotEquals(avion, distinto);
		assertNotEquals(avion, null);
		assertNotEquals(avion, "4563AGH");
	}
}
