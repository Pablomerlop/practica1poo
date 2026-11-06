package p;

public class CintaRecogida implements Identificable{
	private String id;
	private double capacidadMaxima;
	private double pesoAcumulado;
	private Maleta[] maletas;
	private int numMaletas;
	private boolean operativa;
	
	public CintaRecogida(String id, double capacidadMaxima, int maxMaletas) {
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
	
	public double getPesoAcumulado() {
		return this.pesoAcumulado;
	}
	
	public double getCapacidadLibre(){
		return capacidadMaxima-pesoAcumulado;
	}
	
	public String getIdentificador() {
		return this.id;
	}
	
	public boolean getOperativa() {
		return this.operativa;
	}
	
	public void setOperativa(boolean operativa) {
		this.operativa = operativa;
	}
	
	public void depositarMaleta(Maleta maleta) {
		if(!operativa) {
			throw new IllegalStateException("La cinta no está operativa.");
		}
		if(numMaletas >= maletas.length) {
			throw new IllegalStateException("No caben más maletas en la cinta.");
		}
		if(maleta.getPeso() > getCapacidadLibre()) {
			throw new IllegalArgumentException("No hay capacidad libre suficiente.");
		}
		
		maletas[numMaletas] = maleta;
		numMaletas++;
		pesoAcumulado += maleta.getPeso();
	}
	
	public void retirarMaleta(Maleta maleta) {
		if(numMaletas==0) {
			throw new IllegalStateException("No queda equipaje en la cinta.");
		}
		
		for (int i=0; i<numMaletas; i++) {
			if(maletas[i].equals(maleta)) {
				pesoAcumulado -= maleta.getPeso();
				
				for(int j=i; j<numMaletas-1; j++) {
					maletas[j] = maletas[j+1];
				}
				
				maletas[numMaletas-1] = null;
				numMaletas--;
				break;
			}
			
		}
	}
	
	public boolean tieneEquipajePendiente() {
		return this.numMaletas>0;
	}
	
	
	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[numMaletas];
		for (int i=0; i<numMaletas; i++) {
			maletasCopia[i] = maletas[i];
		}
		return maletasCopia;
	}
	
	
	
}
