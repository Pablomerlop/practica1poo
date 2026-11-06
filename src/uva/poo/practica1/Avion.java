package uva.poo.practica1;

public class Avion {
	private String matricula;
	private String modelo;
	private int plazasPrimera;
	private int plazasBusiness;
	private int plazasTurista;
	private int plazasTotales;
	private int maxMaletas;
	private double equipajeMaxKg;
	
	public Avion(String matricula, String modelo, int plazasPrimera, int plazasBusiness,
			int plazasTurista, double equipajeMaxKg, int maxMaletas) {
		this.matricula = matricula;
		this.modelo = modelo;
		this.plazasPrimera = plazasPrimera;
		this.plazasBusiness = plazasBusiness;
		this.plazasTurista = plazasTurista;
		this.equipajeMaxKg = equipajeMaxKg;
		this.maxMaletas = maxMaletas;
		plazasTotales = plazasPrimera + plazasBusiness + plazasTurista;
	}
	
	public double getEquipajeMaxKg() {
		return this.equipajeMaxKg;
	}
	
	public int getPlazasPrimera() {
		return this.plazasPrimera;
	}
	
	public int getPlazasBusiness() {
		return this.plazasBusiness;
	}
	
	public int getPlazasTurista() {
		return this.plazasTurista;
	}
	
	/**
	 * Devuelve la matrícula del avión.
	 * @return matrícula única del avión
	 */
	public String getMatricula() {
		return this.matricula;
	}
	
	/**
	 * Devuelve el modelo del avión.
	 * @return modelo del avión
	 */
	public String getModelo() {
		return this.modelo;
	}
	
	/**
	 * Devuelve el número máximo de maletas o bultos que admite el avión.
	 * @return máximo de maletas
	 */
	public int getMaxMaletas() {
		return this.maxMaletas;
	}
	
	public void redistribuirPlazas(int nuevasPrimera, int nuevasBusiness, int nuevasTurista) {
		int nuevoTotal = nuevasPrimera + nuevasBusiness + nuevasTurista;
		
		if (nuevoTotal > plazasTotales) {
			throw new IllegalArgumentException("La nueva distribución supera el máximo de plazas del avión.");
		}
		
		this.plazasPrimera = nuevasPrimera;
		this.plazasBusiness = nuevasBusiness;
		this.plazasTurista = nuevasTurista;
	}
}


