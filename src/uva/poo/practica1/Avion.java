package uva.poo.practica1;

public class Avion implements Identificable {
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
		if (matricula == null || matricula.trim().isEmpty()) {
			throw new IllegalArgumentException("La matrícula no puede ser nula ni vacía.");
		}
		if (modelo == null || modelo.trim().isEmpty()) {
			throw new IllegalArgumentException("El modelo no puede ser nulo ni vacío.");
		}
		if (plazasPrimera < 0 || plazasBusiness < 0 || plazasTurista < 0) {
			throw new IllegalArgumentException("El número de plazas por clase no puede ser negativo.");
		}
		if (plazasPrimera + plazasBusiness + plazasTurista <= 0) {
			throw new IllegalArgumentException("El avión debe tener al menos una plaza.");
		}
		if (equipajeMaxKg <= 0) {
			throw new IllegalArgumentException("La capacidad máxima de equipaje debe ser positiva.");
		}
		if (maxMaletas < 0) {
			throw new IllegalArgumentException("El número máximo de maletas no puede ser negativo.");
		}
		
		this.matricula = matricula;
		this.modelo = modelo;
		this.plazasPrimera = plazasPrimera;
		this.plazasBusiness = plazasBusiness;
		this.plazasTurista = plazasTurista;
		this.equipajeMaxKg = equipajeMaxKg;
		this.maxMaletas = maxMaletas;
		plazasTotales = plazasPrimera + plazasBusiness + plazasTurista;
	}
	
	@Override
	public String getIdentificador() {
		return this.matricula;
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
	
	public int getPlazasPrimera() {
		return this.plazasPrimera;
	}
	
	public int getPlazasBusiness() {
		return this.plazasBusiness;
	}
	
	public int getPlazasTurista() {
		return this.plazasTurista;
	}
	
	public int getPlazasTotales() {
		return this.plazasTotales;
	}
	
	public double getEquipajeMaxKg() {
		return this.equipajeMaxKg;
	}
	
	/**
	 * Devuelve el número máximo de maletas o bultos que admite el avión.
	 * @return máximo de maletas
	 */
	public int getMaxMaletas() {
		return this.maxMaletas;
	}	
	
	public void redistribuirPlazas(int nuevasPrimera, int nuevasBusiness, int nuevasTurista) {
		if (nuevasPrimera < 0 || nuevasBusiness < 0 || nuevasTurista < 0) {
			throw new IllegalArgumentException("Las plazas por clase no pueden ser negativas.");
		}
		
		int nuevoTotal = nuevasPrimera + nuevasBusiness + nuevasTurista;
		if (nuevoTotal > this.plazasTotales) {
			throw new IllegalArgumentException("La nueva distribución supera el máximo de plazas del avión.");
		}
		
		this.plazasPrimera = nuevasPrimera;
		this.plazasBusiness = nuevasBusiness;
		this.plazasTurista = nuevasTurista;
	}
}


