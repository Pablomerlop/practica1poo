package p;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CintaRecogidaTest {

	private CintaRecogida cinta;
	private Maleta maleta1;
	private Maleta maleta2;
	private Maleta maleta3;
	private Maleta maleta4;
	
	@Before
	public void crearCintaRecogida() {
		cinta = new CintaRecogida(100.0, 4);
		maleta1 = new Maleta(20.0, "50x40x10");
		maleta2 = new Maleta(30.0, "30x30x30");
		maleta3 = new Maleta(40.0, "20x20x20");
		maleta4 = new Maleta(20.0, "30x20x30");
	}
	
	@Test
	public void testDepositarYRetirarMaleta() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);
		
		assertEquals(50.0, cinta.getPesoAcumulado(), 0.001);
		assertEquals(2, cinta.getMaletas().length);
		
		cinta.retirarMaleta(maleta1);
		
		assertEquals(30.0, cinta.getPesoAcumulado(), 0.001);
		assertEquals(1, cinta.getMaletas().length);
		assertEquals(maleta2, cinta.getMaletas()[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExcesoPeso() {
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);
		cinta.depositarMaleta(maleta3);
		cinta.depositarMaleta(maleta4);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExcesoMaletas() {
		Maleta extra1 = new Maleta(10.0, "10x10x20");
		Maleta extra2 = new Maleta(10.0, "10x10x20");
		
		cinta.depositarMaleta(extra2);
		cinta.depositarMaleta(extra1);
		cinta.depositarMaleta(maleta1);
		cinta.depositarMaleta(maleta2);
		cinta.depositarMaleta(maleta3);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testFueraServicio() {
		cinta.setEstado("MANTENIMIENTO");
		cinta.depositarMaleta(maleta1);
	}
	

}
