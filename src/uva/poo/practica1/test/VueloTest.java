package uva.poo.practica1.test;

import org.junit.Before;
import org.junit.Test;

import uva.poo.practica1.Avion;
import uva.poo.practica1.Viajero;
import uva.poo.practica1.Vuelo;

import static org.junit.Assert.*;

public class VueloTest {
	private Vuelo vuelo;
	private Avion avion;
	private Viajero viajero1;
	private Viajero viajero2;
	private Viajero viajero3;
	
	@Before
	public void crearObjetos() {
		avion = new Avion("43FE23", "Boeing 21", 20, 40, 100, 1000.0, 200);
		vuelo = new Vuelo("ISH34", "Madrid", "Chicago", avion, 2);
		viajero1 = new Viajero("12341424A", "Paco", "Sur", 1);
		viajero2 = new Viajero("132412412D", "ana", "de", 1);
		viajero3 = new Viajero("124124F", "ef", "aefe",1);
	}
		
	@Test
	public void testEstadoInicial() {
		assertEquals("ISH34", vuelo.getIdentificador());
		assertEquals(avion, vuelo.getAvion());
		assertEquals(0, vuelo.getViajeros().length);
		assertEquals(150, avion.getMaxMaletas());
		assertEquals("Madrid", vuelo.getOrigen());
		assertEquals("Chicagto", vuelo.getDestino());
	}
	
	@Test
	public void testAddViajero() {
		vuelo.addViajero(viajero1);
		vuelo.addViajero(viajero2);
		
		assertEquals(2, vuelo.getViajeros().length);
		assertEquals(viajero1, vuelo.getViajeros()[0]);
		assertEquals(viajero2, vuelo.getViajeros()[1]);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testVueloLleno() {
		vuelo.addViajero(viajero1);
		vuelo.addViajero(viajero2);
		vuelo.addViajero(viajero3);
	}

}
