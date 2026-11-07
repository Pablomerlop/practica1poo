package uva.poo.practica1;
import fabricante.externo.tarjeta.TarjetaEmbarque;

public class Viajero implements Identificable {
	private static final String CREDENCIAL_CREAR = "J1M6Nv65_B1R8T3X";
	private static final String CREDENCIAL_AUTORIZAR = "G1bR13l1_A7k2M9P";
	
	private String identificador;
	private String nombre;
	private String apellidos;
	private boolean embarcado;
	private Maleta[] maletas;
	private int numMaletas;
	private TarjetaEmbarque tarjetaEmbarque;
	
	public Viajero(String identificador, String nombre, String apellidos, int maxMaletas) {
		if (identificador == null || identificador.trim().isEmpty()) {
			throw new IllegalArgumentException("El DNI o pasaporte no puede ser nulo ni vacío.");
		}
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
		}
		if (apellidos == null || apellidos.trim().isEmpty()) {
			throw new IllegalArgumentException("Los apellidos no pueden ser nulos ni vacíos.");
		}
		if (maxMaletas < 0) {
			throw new IllegalArgumentException("El máximo de maletas no puede ser negativo.");
		}
		
		this.identificador = identificador;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.embarcado = false;
		this.maletas = new Maleta[maxMaletas];
		this.numMaletas = 0;
		this.tarjetaEmbarque = null;
	}
	
	@Override
	public String getIdentificador() {
		return this.identificador;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getApellidos() {
		return this.apellidos;
	}
	
	public boolean isEmbarcado() {
		return this.embarcado;
	}
	
	public TarjetaEmbarque getTarjetaEmbarque() {
		return this.tarjetaEmbarque;
	}
	
	public void emitirTarjetaEmbarque() {
		if (this.tarjetaEmbarque != null) {
			throw new IllegalStateException("El viajero ya tiene una tarjeta de embarque emitida.");
		}
		this.tarjetaEmbarque = new TarjetaEmbarque(CREDENCIAL_CREAR);
	}
	
	public void facturarMaleta(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta no puede ser null.");
		}
		if (numMaletas >= maletas.length) {
			throw new IllegalStateException("Ha sido alcanzado el límite de maletas facturadas.");
		}
		this.maletas[this.numMaletas] = maleta;
		numMaletas++;
	}
	
	public int getNumMaletas() {
		return this.numMaletas;
	}
	
	public double getPesoTotalEquipaje() {
		double pesoTotal = 0.0;
		for (int i = 0; i < this.numMaletas; i++) {
			pesoTotal += this.maletas[i].getPeso();
		}
		return pesoTotal;
	}
	
	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[numMaletas];
		for(int i = 0; i<numMaletas; i++) {
			maletasCopia[i] = maletas[i];
		}
		return maletasCopia;
	}
	
	public void embarcar() {
		if (this.tarjetaEmbarque == null) {
			emitirTarjetaEmbarque();
		}
		embarcar(this.tarjetaEmbarque);
	}
	
	public void embarcar(TarjetaEmbarque tarjeta) {
		if(this.embarcado) {
			throw new IllegalStateException("El viajero ya ha embarcado.");
		}
		if(tarjeta == null) {
			throw new IllegalArgumentException("Se necesita una tarjeta de embarque.");
		}
		
		tarjeta.autorizaEmbarque(CREDENCIAL_AUTORIZAR);
		this.embarcado = true;
	}
	
	
}
