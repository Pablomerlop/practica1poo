package uva.poo.practica1;

import java.time.LocalDateTime;

public class CintaRecogida implements Identificable{
	private String id;
	private double capacidadMaxima;
	private double pesoAcumulado;
	private Maleta[] maletas;
	private int numMaletas;
	private boolean operativa;
	
	public CintaRecogida(String id, double capacidadMaxima, int maxMaletas) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("El identificador de la cinta no puede ser nulo ni vacío.");
		}
		if (capacidadMaxima <= 0) {
			throw new IllegalArgumentException("La capacidad máxima debe ser mayor que cero.");
		}
		if (maxMaletas <= 0) {
			throw new IllegalArgumentException("El número máximo de maletas debe ser mayor que cero.");
		}
		
		this.id = id;
		this.capacidadMaxima = capacidadMaxima;
		this.pesoAcumulado = 0.0;
		this.maletas = new Maleta[maxMaletas];
		this.numMaletas = 0;
		this.operativa = false;
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public String getIdentificador() {
		return this.id;
	}
	
	public double getCapacidadMaxima() {
		return this.capacidadMaxima;
	}
	
	public double getPesoAcumulado() {
		return this.pesoAcumulado;
	}
	
	public double getCapacidadLibre(){
		return capacidadMaxima-pesoAcumulado;
	}
	
	public int getMaxMaletas() {
		return this.maletas.length;
	}
	
	public boolean isOperativa() {
		return this.operativa;
	}
	
	public void setOperativa(boolean operativa) {
		this.operativa = operativa;
	}
	
	public void depositarMaleta(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta no puede ser null.");
		}
		if(!this.operativa) {
			throw new IllegalStateException("La cinta no está operativa.");
		}
		if(numMaletas >= maletas.length) {
			throw new IllegalStateException("No caben más maletas en la cinta.");
		}
		if(maleta.getPeso() > getCapacidadLibre()) {
			throw new IllegalArgumentException("No hay capacidad libre suficiente.");
		}
		
		this.maletas[this.numMaletas] = maleta;
		this.numMaletas++;
		this.pesoAcumulado += maleta.getPeso();
	}
	
	public void retirarMaleta(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta a retirar no puede ser null.");
		}
		if(numMaletas==0) {
			throw new IllegalStateException("No queda equipaje en la cinta.");
		}
		
		int posicion = -1;
		int i = 0;
		while (i < this.numMaletas && posicion == -1) {
	        if (this.maletas[i].equals(maleta)) {
	            posicion = i;
	        }
	        i++;
	    }
		
		if (posicion == -1) {
	        throw new IllegalArgumentException("La maleta indicada no se encuentra en la cinta.");
	    }
		
		this.pesoAcumulado -= maleta.getPeso();
	    
	    for (int j = posicion; j < this.numMaletas - 1; j++) {
	        this.maletas[j] = this.maletas[j + 1];
	    }
	    
	    this.maletas[this.numMaletas - 1] = null;
	    this.numMaletas--;
	    
	    if (!maleta.isRecogida()) {
	        maleta.registrarRecogida(LocalDateTime.now());
	    }
	
	}
	
	public boolean tieneEquipajePendiente() {
		return this.numMaletas > 0;
	}
	
	
	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[this.numMaletas];
		for (int i = 0; i < this.numMaletas; i++) {
			maletasCopia[i] = this.maletas[i];
		}
		return maletasCopia;
	}	
}
