package uva.poo.practica1.test;

import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.practica1.Aeropuerto;
import uva.poo.practica1.Avion;
import uva.poo.practica1.CintaRecogida;
import uva.poo.practica1.Vuelo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AeropuertoTest {

    private Aeropuerto aeropuerto;
    private GPSCoordinate coordenadas;
    private Vuelo vuelo1;
    private CintaRecogida cinta1;

    @Before
    public void setUp() {
        // Suponemos que el constructor de GPSCoordinate recibe latitud y longitud.
        // Si al compilar te pide otros parámetros, ajústalos según su documentación HTML.
        coordenadas = new GPSCoordinate(40.471926, -3.56264); 
        
        // Creamos un aeropuerto con un límite estricto de 1 vuelo y 1 cinta para las pruebas
        aeropuerto = new Aeropuerto("Madrid", coordenadas, 1, 1);
        
        Avion avion = new Avion("43FE23", "Boeing 21", 20, 40, 100, 1000.0, 200);
        vuelo1 = new Vuelo("ISH34", "Madrid", "Chicago", avion, 2);
        
        // Suponiendo que el constructor de CintaRecogida recibe un identificador
        cinta1 = new CintaRecogida("C1", 2000.0, 200); 
    }

    @Test
    public void testEstadoInicial() {
        assertEquals("Madrid", aeropuerto.getCiudad());
        assertEquals(coordenadas, aeropuerto.getCoordenadas());
        assertEquals(0, aeropuerto.getVuelos().length);
        assertEquals(0, aeropuerto.getCintas().length);
    }

    @Test
    public void testAñadirVuelo() {
        aeropuerto.añadirVuelo(vuelo1);
        
        assertEquals(1, aeropuerto.getVuelos().length);
        assertEquals(vuelo1, aeropuerto.getVuelos()[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testAñadirVueloExcediendoCapacidad() {
        aeropuerto.añadirVuelo(vuelo1);
        
        Avion avion2 = new Avion("99AA99", "Airbus A320", 15, 30, 80, 800.0, 150);
        Vuelo vuelo2 = new Vuelo("IBE123", "Madrid", "Londres", avion2, 2);
        aeropuerto.añadirVuelo(vuelo2); 
    }

    @Test
    public void testAñadirCinta() {
        aeropuerto.añadirCinta(cinta1);
        
        assertEquals(1, aeropuerto.getCintas().length);
        assertEquals(cinta1, aeropuerto.getCintas()[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testAñadirCintaExcediendoCapacidad() {
        aeropuerto.añadirCinta(cinta1);
        
        CintaRecogida cinta2 = new CintaRecogida("C2", 1500.0, 150);
        aeropuerto.añadirCinta(cinta2); 
    }
}