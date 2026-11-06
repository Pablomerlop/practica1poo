package uva.poo.practica1;
import es.uva.inf.poo.maps.GPSCoordinate;
import fabricante.externo.tarjeta.TarjetaEmbarque;

public class Aeropuerto {
	private String ciudad;
	private GPSCoordinate coordenadas;
	private Vuelo[] vuelos;
	private int numVuelos;
	private CintaRecogida[] cintas;
	private int numCintas;
	
	public Aeropuerto(String ciudad, GPSCoordinate coordenadas, int maxVuelos, int maxCintas) {
		this.ciudad = ciudad;
		this.coordenadas = coordenadas;
		this.vuelos = new Vuelo[maxVuelos];
		this.numVuelos = 0;
		this.cintas = new CintaRecogida[maxCintas];
		this.numCintas = 0;
	}
	
	public String getCiudad() {
		return this.ciudad;
	}
	
	public GPSCoordinate getCoordenadas() {
		return this.coordenadas;
	}
	
	public void añadirVuelo(Vuelo vuelo) {
		if(numVuelos>=vuelos.length) {
			throw new IllegalStateException("Capacidad máxima de vuelos alcanzada.");
		}
		if(vuelo == null) {
			throw new IllegalArgumentException("El vuelo no puede ser null.");
		}
		vuelos[numVuelos] = vuelo;
		numVuelos++;
	}
	
	public void eliminarVuelo(String identificadorVuelo) {
		int posicion = buscarPosicion(vuelos, numVuelos, identificadorVuelo, "Vuelo no encontrado.");
	
		for(int i=posicion;i<numVuelos-1;i++) {
			vuelos[i] = vuelos[i+1];
		}
		numVuelos--;
		vuelos[numVuelos] = null;
	}
	
	public Vuelo[] getVuelos() {
		Vuelo[] vuelosCopia = new Vuelo[numVuelos];
        for (int i = 0; i < numVuelos; i++) {
            vuelosCopia[i] = vuelos[i];
        }
        return vuelosCopia;
	}
	
	public void realizarEmbarque(String idVuelo, TarjetaEmbarque[] tarjetas) {
		int posicion = buscarPosicion(vuelos, numVuelos, idVuelo, "Vuelo no encontrado.");
		
		Viajero[] pasajeros = vuelos[posicion].getViajeros();
		if(pasajeros.length!=tarjetas.length) {
			throw new IllegalArgumentException("Número de tarjetas no coincide con número de pasajeros.");
		}
		for(int i=0; i<pasajeros.length;i++) {
			pasajeros[i].embarcar(tarjetas[i]);
		}
	}
	
	public void añadirCinta(CintaRecogida cinta) {
        if (numCintas >= cintas.length) {
            throw new IllegalStateException("No hay espacio para más cintas de recogida.");
        }
        if (cinta == null) {
            throw new IllegalArgumentException("La cinta no puede ser null.");
        }
        cintas[numCintas] = cinta;
        numCintas++;
    }
	
	public Vuelo[] obtenerVuelosPorEstado(String estado) {
		int contador = 0;
		for(int i = 0; i<numVuelos; i++) {
			if(vuelos[i].getEstado().equals(estado)) {
				contador++;
			}
		}
		
		Vuelo[] filtrados = new Vuelo[contador];
		int indice = 0;
		for(int i=0; i<numVuelos;i++) {
			if(vuelos[i].getEstado().equals(estado)) {
				filtrados[indice++] = vuelos[i];
			}
		}
		
		return filtrados;
	}
	
	public void eliminarCinta(String idCinta) {
		int posicion = buscarPosicion(cintas, numCintas, idCinta, "Cinta no encontrada.");
		
		for(int i=posicion;i<numCintas-1;i++) {
			cintas[i] = cintas[i+1];
		}
		numCintas--;
		cintas[numCintas] = null;
	}
	
	public void asignarCintaAVuelo(String idVuelo, CintaRecogida cinta) {
		int posicion = buscarPosicion(vuelos, numVuelos, idVuelo, "Vuelo no encontrado.");
		vuelos[posicion].asignarCinta(cinta);
		
	}
	
	public CintaRecogida[] consultarCintasOperativas() {
		int contador = 0;
		for(int i=0; i<numCintas; i++) {
			if(cintas[i].getOperativa()) {
				contador++;
			}
		}
		
		CintaRecogida[] cintasOperativas = new CintaRecogida[contador];
		int indice = 0;
		for(int i=0; i<numCintas; i++) {
			if(cintas[i].getOperativa()){
				cintasOperativas[indice++] = cintas[i];
			}
		}
		return cintasOperativas;
	}
	
	public CintaRecogida[] consultarCintasConEquipajePendiente() {
		int contador = 0;
        for (int i = 0; i < numCintas; i++) {
            if (cintas[i].tieneEquipajePendiente()) contador++;
        }
        
        CintaRecogida[] pendientes = new CintaRecogida[contador];
        int indice = 0;
        for (int i = 0; i < numCintas; i++) {
            if (cintas[i].tieneEquipajePendiente()) pendientes[indice++] = cintas[i];
        }
        return pendientes;
	}

    public CintaRecogida[] getCintas() {
        CintaRecogida[] cintasCopia = new CintaRecogida[numCintas];
        for (int i = 0; i < numCintas; i++) {
            cintasCopia[i] = cintas[i];
        }
        return cintasCopia;
    }
    
    public int buscarPosicion(Identificable[] array, int numElementos, String idBuscado, String mensajeError) {
    	for(int i=0; i<numElementos; i++) {
    		if(array[i].getIdentificador().equals(idBuscado)) {
    			return i;
    		}
    	}
		throw new IllegalArgumentException(mensajeError);
    }
	
}
