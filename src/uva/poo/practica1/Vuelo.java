package uva.poo.practica1;

public class Vuelo implements Identificable{
	private String identificador;
	private String origen;
	private String destino;
	private Avion avion;
	private Viajero[] viajeros;
	private int numViajeros;
	private CintaRecogida cintaAsignada;
	private String estado; //OPERATIVO, RETRASADO, CANCELADO.
	
	public Vuelo(String identificador, String origen, String destino, Avion avion, int maxSitios) {
		this.identificador = identificador;
		this.origen = origen;
		this.destino = destino;
		this.avion = avion;
		this.viajeros = new Viajero[maxSitios];
		this.numViajeros = 0;
		this.estado = "OPERATIVO";
		this.cintaAsignada = null;
	}
	
	public String getIdentificador() {
		return this.identificador;
	}
	
	public Avion getAvion() {
		return this.avion;
	}
	
	public CintaRecogida getCintaAsignada() {
		return this.cintaAsignada;
	}
	
	public String getEstado() {
		return this.estado;
	}
	
	/**
	 * Devuelve el origen del vuelo.
	 * @return origen del vuelo
	 */
	public String getOrigen() {
		return this.origen;
	}
	
	/**
	 * Devuelve el destino del vuelo
	 * @return destino del vuelo
	 */
	public String getDestino() {
		return this.destino;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void addViajero(Viajero viajero) {
		if(numViajeros>= viajeros.length) {
			throw new IllegalStateException("El vuelo está lleno.");
		}
		viajeros[numViajeros] = viajero;
		numViajeros++;
	}
	
	public void asignarCinta(CintaRecogida cinta) {
		if(cinta == null) {
			throw new IllegalArgumentException("La cinta asignada no puede ser null.");
		}
		this.cintaAsignada = cinta;
		cintaAsignada.setOperativa(true);
	}
	
	public Viajero[] getViajeros() {
		Viajero[] viajerosCopia = new Viajero[numViajeros];
		for (int i=0;i<numViajeros;i++) {
			viajerosCopia[i] = viajeros[i];
		}
		return viajerosCopia;
	}
	
}
