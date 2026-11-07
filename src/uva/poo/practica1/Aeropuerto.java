package uva.poo.practica1;

import es.uva.inf.poo.maps.GPSCoordinate;
import fabricante.externo.tarjeta.TarjetaEmbarque;
import java.time.LocalDateTime;

public class Aeropuerto {
	private static final int LONGITUD_IATA = 3;
	
	private String nombre;
	private String codigoIATA;
	private String direccion;
	private String pais;
	private GPSCoordinate coordenadas;
	private Vuelo[] vuelos;
	private int numVuelos;
	private CintaRecogida[] cintas;
	private int numCintas;
	
	public Aeropuerto(String nombre, String codigoIATA, String direccion, 
			GPSCoordinate coordenadas, String pais, int maxVuelos, int maxCintas) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del aeropuerto no puede ser nulo ni vacío.");
		}
		if (codigoIATA == null || codigoIATA.length() != LONGITUD_IATA) {
			throw new IllegalArgumentException("El código IATA debe tener exactamente 3 letras.");
		}
		for (int i = 0; i < LONGITUD_IATA; i++) {
			if (!Character.isLetter(codigoIATA.charAt(i))) {
				throw new IllegalArgumentException("El código IATA solo puede contener letras.");
			}
		}
		if (direccion == null || direccion.trim().isEmpty()) {
			throw new IllegalArgumentException("La dirección no puede ser nula ni vacía.");
		}
		if (coordenadas == null) {
			throw new IllegalArgumentException("Las coordenadas GPS no pueden ser null.");
		}
		if (pais == null || pais.trim().isEmpty()) {
			throw new IllegalArgumentException("El país de origen no puede ser nulo ni vacío.");
		}
		if (maxVuelos <= 0 || maxCintas <= 0) {
			throw new IllegalArgumentException("Las capacidades máximas deben ser mayores que cero.");
		}
		
		this.nombre = nombre;
		this.codigoIATA = codigoIATA.toUpperCase();
		this.direccion = direccion;
		this.coordenadas = coordenadas;
		this.pais = pais;
		this.vuelos = new Vuelo[maxVuelos];
		this.numVuelos = 0;
		this.cintas = new CintaRecogida[maxCintas];
		this.numCintas = 0;
	}
	
	public String getNombre() {
		return this.nombre;
	}

	public String getCodigoIATA() {
		return this.codigoIATA;
	}

	public String getDireccion() {
		return this.direccion;
	}
	
	public GPSCoordinate getCoordenadas() {
		return this.coordenadas;
	}
	
	public String getPais() {
		return this.pais;
	}
	
	public void addVuelo(Vuelo vuelo) {
		if(this.numVuelos >= this.vuelos.length) {
			throw new IllegalStateException("Capacidad máxima de vuelos alcanzada.");
		}
		if(vuelo == null) {
			throw new IllegalArgumentException("El vuelo no puede ser null.");
		}
		if (existeElemento(this.vuelos, this.numVuelos, vuelo.getIdentificador())) {
			throw new IllegalArgumentException("Ya existe un vuelo con ese código en el aeropuerto.");
		}
		vuelos[this.numVuelos] = vuelo;
		this.numVuelos++;
	}
	
	public void eliminarVuelo(String identificadorVuelo) {
		int posicion = buscarPosicion(this.vuelos, this.numVuelos, identificadorVuelo, "Vuelo no encontrado.");
	
		for(int i = posicion; i < this.numVuelos - 1; i++) {
			this.vuelos[i] = this.vuelos[i + 1];
		}
		this.numVuelos--;
		this.vuelos[this.numVuelos] = null;
	}
	
	public void asignarPasajeroAVuelo(String idVuelo, Viajero viajero) {
		int posicion = buscarPosicion(this.vuelos, this.numVuelos, idVuelo, "Vuelo no encontrado.");
		this.vuelos[posicion].addViajero(viajero);
	}
	
	
	public Vuelo[] getVuelos() {
		Vuelo[] vuelosCopia = new Vuelo[this.numVuelos];
        for (int i = 0; i < this.numVuelos; i++) {
            vuelosCopia[i] = this.vuelos[i];
        }
        return vuelosCopia;
	}
	
	public void realizarEmbarque(String idVuelo, TarjetaEmbarque[] tarjetas) {
		if (tarjetas == null) {
			throw new IllegalArgumentException("El array de tarjetas no puede ser null.");
		}
		
		int posicion = buscarPosicion(this.vuelos, this.numVuelos, idVuelo, "Vuelo no encontrado.");
		
		Viajero[] pasajeros = this.vuelos[posicion].getViajeros();
		if(pasajeros.length != tarjetas.length) {
			throw new IllegalArgumentException("Número de tarjetas no coincide con número de pasajeros.");
		}
		for(int i = 0; i < pasajeros.length; i++) {
			pasajeros[i].embarcar(tarjetas[i]);
		}
	}
	
	public void realizarEmbarque(String idVuelo) {
		int posicion = buscarPosicion(this.vuelos, this.numVuelos, idVuelo, "Vuelo no encontrado.");
		Viajero[] pasajeros = this.vuelos[posicion].getViajeros();
		for (int i = 0; i < pasajeros.length; i++) {
			pasajeros[i].embarcar();
		}
	}
	
	public Vuelo[] obtenerVuelosOperativos() {
		int contador = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isOperativo()) {
				contador++;
			}
		}
		Vuelo[] operativos = new Vuelo[contador];
		int indice = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isOperativo()) {
				operativos[indice] = this.vuelos[i];
				indice++;
			}
		}
		return operativos;
	}

	public Vuelo[] obtenerVuelosRetrasados(LocalDateTime horaActual) {
		int contador = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isRetrasado(horaActual)) {
				contador++;
			}
		}
		Vuelo[] retrasados = new Vuelo[contador];
		int indice = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isRetrasado(horaActual)) {
				retrasados[indice] = this.vuelos[i];
				indice++;
			}
		}
		return retrasados;
	}
	
	public Vuelo[] obtenerVuelosCancelados() {
		int contador = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isCancelado()) {
				contador++;
			}
		}
		Vuelo[] cancelados = new Vuelo[contador];
		int indice = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isCancelado()) {
				cancelados[indice] = this.vuelos[i];
				indice++;
			}
		}
		return cancelados;
	}
	
	public void addCinta(CintaRecogida cinta) {
        if (this.numCintas >= this.cintas.length) {
            throw new IllegalStateException("No hay espacio para más cintas de recogida.");
        }
        if (cinta == null) {
            throw new IllegalArgumentException("La cinta no puede ser null.");
        }
        if (existeElemento(this.cintas, this.numCintas, cinta.getIdentificador())) {
			throw new IllegalArgumentException("Ya existe una cinta con ese identificador.");
		}
        
        this.cintas[this.numCintas] = cinta;
        this.numCintas++;
    }
	
	public void eliminarCinta(String idCinta) {
		int posicion = buscarPosicion(this.cintas, this.numCintas, idCinta, "Cinta no encontrada.");
		
		for(int i = posicion; i < this.numCintas-1; i++) {
			this.cintas[i] = this.cintas[i+1];
		}
		this.numCintas--;
		this.cintas[this.numCintas] = null;
	}
	
	public void asignarCintaAVuelo(String idVuelo, CintaRecogida cinta) {
		int posicionVuelo = buscarPosicion(this.vuelos, this.numVuelos, idVuelo, "Vuelo no encontrado.");
		
		if (cinta == null) {
			throw new IllegalArgumentException("La cinta no puede ser null.");
		}
		
		buscarPosicion(this.cintas, this.numCintas, cinta.getIdentificador(), "La cinta no pertenece al aeropuerto.");
		this.vuelos[posicionVuelo].asignarCinta(cinta);
		
	}
	
	public void descargarEquipajeDeVuelo(String idVuelo) {
		int posicionVuelo = buscarPosicion(this.vuelos, this.numVuelos, idVuelo, "Vuelo no encontrado.");
		this.vuelos[posicionVuelo].descargarEquipajeEnCinta();
	}
	
	public CintaRecogida[] consultarCintasOperativas() {
		int contador = 0;
		for (int i = 0; i < this.numCintas; i++) {
			if (this.cintas[i].isOperativa()) {
				contador++;
			}
		}
		
		CintaRecogida[] cintasOperativas = new CintaRecogida[contador];
		int indice = 0;
		for(int i = 0; i < this.numCintas; i++) {
			if(this.cintas[i].isOperativa()){
				cintasOperativas[indice] = this.cintas[i];
				indice++;
			}
		}
		return cintasOperativas;
	}
	
	public CintaRecogida[] consultarCintasConEquipajePendiente() {
		int contador = 0;
        for (int i = 0; i < this.numCintas; i++) {
            if (this.cintas[i].tieneEquipajePendiente()) contador++;
        }
        
        CintaRecogida[] pendientes = new CintaRecogida[contador];
        int indice = 0;
        for (int i = 0; i < this.numCintas; i++) {
            if (this.cintas[i].tieneEquipajePendiente()) {
            	pendientes[indice] = this.cintas[i];
            	indice++;
            }
        }
        return pendientes;
	}

    public CintaRecogida[] getCintas() {
        CintaRecogida[] cintasCopia = new CintaRecogida[this.numCintas];
        for (int i = 0; i < this.numCintas; i++) {
            cintasCopia[i] = this.cintas[i];
        }
        return cintasCopia;
    }
    
    private boolean existeElemento(Identificable[] array, int numElementos, String idBuscado) {
		for (int i = 0; i < numElementos; i++) {
			if (array[i].getIdentificador().equals(idBuscado)) {
				return true;
			}
		}
		return false;
	}
    
    public int buscarPosicion(Identificable[] array, int numElementos, String idBuscado, String mensajeError) {
    	if (idBuscado == null) {
			throw new IllegalArgumentException("El identificador buscado no puede ser null.");
		}
    	
    	for(int i = 0; i < numElementos; i++) {
    		if(array[i].getIdentificador().equals(idBuscado)) {
    			return i;
    		}
    	}
		throw new IllegalArgumentException(mensajeError);
    }
	
}
