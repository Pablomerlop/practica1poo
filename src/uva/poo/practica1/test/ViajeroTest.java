package uva.poo.practica1.test;

import fabricante.externo.tarjeta.TarjetaEmbarque;
import uva.poo.practica1.Maleta;
import uva.poo.practica1.Viajero;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ViajeroTest {
	private Viajero viajero;
	private Maleta maleta1;
	private Maleta maleta2;
	private TarjetaEmbarque tarjeta;
	
	@Before
	public void crearObjetos() {
		viajero = new Viajero("1234567Z", "Antonio", "Gil Perez", 2);
		
		maleta1 = new Maleta(15.0, "30x30x30");
		maleta2 = new Maleta(20.0, "15x20x30");
		
		tarjeta = new TarjetaEmbarque("J1M6Nv65_B1R8T3X");
	}
	
	@Test
	public void testEstadoInicial() {
		assertEquals("1234567Z", viajero.getIdentificador());
		assertEquals("Antonio", viajero.getNombre());
		assertEquals("Gil Perez", viajero.getApellidos());
		assertFalse(viajero.getEmbarcado());
		assertEquals(0, viajero.getMaletas().length);
	}
	
	@Test
	public void testFacturarMaleta() {
		viajero.facturarMaleta(maleta1);
		viajero.facturarMaleta(maleta2);
		
		assertEquals(2, viajero.getMaletas().length);
		assertEquals(maleta1, viajero.getMaletas()[0]);
		assertEquals(maleta2, viajero.getMaletas()[1]);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExcesoMaletasFacturadas() {
		viajero.facturarMaleta(maleta1);
		viajero.facturarMaleta(maleta2);
		
		Maleta maleta3 = new Maleta(20.0, "10x20x30");
		viajero.facturarMaleta(maleta3);
	}
	
	@Test
	public void testEmbarcarUnaVez() {
		viajero.embarcar(tarjeta);
		
		assertTrue(viajero.getEmbarcado());
		assertTrue(tarjeta.isEmbarcado());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testEmbarcarVariasVeces() {
		viajero.embarcar(tarjeta);
		
		TarjetaEmbarque tarjeta2 = new TarjetaEmbarque("J1M6Nv65_B1R8T3X");
		viajero.embarcar(tarjeta2);
	}

}
