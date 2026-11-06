package uva.poo.practica1;
import fabricante.externo.tarjeta.TarjetaEmbarque;

public class Viajero {
	private String identificador;
	private String nombre;
	private String apellidos;
	private boolean embarcado;
	private Maleta[] maletas;
	private int numMaletas;
	
	public Viajero(String identificador, String nombre, String apellidos, int maxMaletas) {
		this.identificador = identificador;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.embarcado = false;
		this.maletas = new Maleta[maxMaletas];
		this.numMaletas = 0;
	}
	
	public String getIdentificador() {
		return this.identificador;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getApellidos() {
		return this.apellidos;
	}
	
	public boolean getEmbarcado() {
		return this.embarcado;
	}
	
	public void facturarMaleta(Maleta maleta) {
		if (numMaletas >= maletas.length) {
			throw new IllegalStateException("Ha sido alcanzado el límite de maletas facturadas.");
		}
		maletas[numMaletas] = maleta;
		numMaletas++;
	}
	
	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[numMaletas];
		for(int i = 0; i<numMaletas; i++) {
			maletasCopia[i] = maletas[i];
		}
		return maletasCopia;
	}
	
	public void embarcar(TarjetaEmbarque tarjeta) {
		if(this.embarcado) {
			throw new IllegalStateException("El viajero ya ha embarcado.");
		}
		if(tarjeta == null) {
			throw new IllegalArgumentException("Se necesita una tarjeta de embarque.");
		}
		
		tarjeta.autorizaEmbarque("G1bR13l1_A7k2M9P");
		this.embarcado = true;
	}
	
	
}
