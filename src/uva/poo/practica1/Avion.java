package uva.poo.practica1;

/**
 * Implementación de un tipo de dato que representa un avión y su configuración de capacidad.
 * 
 * @author pablmer
 * @author seraran
 */
public class Avion implements Identificable {
	private String matricula;
	private String modelo;
	private int plazasPrimera;
	private int plazasBusiness;
	private int plazasTurista;
	private int capacidadMaximaPasajeros;
	private int maxMaletas;
	private double equipajeMaxKg;
	
	/**
	 * Inicialización de un avión indicando sus datos identificativos, plazas por clase y capacidad de bodega.
	 * 
	 * @param matricula matrícula única del avión
	 * @param modelo modelo del avión
	 * @param plazasPrimera número de plazas en primera clase
	 * @param plazasBusiness número de plazas en clase business
	 * @param plazasTurista número de plazas en clase turista
	 * @param equipajeMaxKg peso máximo de equipaje admitido en kilogramos
	 * @param maxMaletas número máximo de maletas o bultos admitidos
	 * @throws IllegalArgumentException cuando matrícula o modelo son null o cadenas vacías
	 * @throws IllegalArgumentException cuando alguna clase tiene plazas negativas o la suma total es menor o igual a cero
	 * @throws IllegalArgumentException cuando equipajeMaxKg <= 0 o maxMaletas < 0
	 */
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
		
		this.matricula = matricula.trim();
		this.modelo = modelo.trim();
		this.plazasPrimera = plazasPrimera;
		this.plazasBusiness = plazasBusiness;
		this.plazasTurista = plazasTurista;
		this.capacidadMaximaPasajeros = plazasPrimera + plazasBusiness + plazasTurista;
		this.equipajeMaxKg = equipajeMaxKg;
		this.maxMaletas = maxMaletas;
	}
	
	@Override
	public String getIdentificador() {
		return this.matricula;
	}
	
	/**
	 * Consulta la matrícula del avión.
	 * 
	 * @return matrícula única del avión
	 */
	public String getMatricula() {
		return this.matricula;
	}
	
	/**
	 * Consulta el modelo del avión.
	 * 
	 * @return modelo del avión
	 */
	public String getModelo() {
		return this.modelo;
	}
	
	/**
	 * Consulta el número actual de plazas de primera clase.
	 * 
	 * @return plazas en primera clase
	 */
	public int getPlazasPrimera() {
		return this.plazasPrimera;
	}
	
	/**
	 * Consulta el número actual de plazas de clase business.
	 * 
	 * @return plazas en clase business
	 */
	public int getPlazasBusiness() {
		return this.plazasBusiness;
	}
	
	/**
	 * Consulta el número actual de plazas de clase turista.
	 * 
	 * @return plazas en clase turista
	 */
	public int getPlazasTurista() {
		return this.plazasTurista;
	}
	
	/**
	 * Consulta la suma actual de plazas configuradas entre las tres clases.
	 * 
	 * @return total actual de plazas disponibles
	 */
	public int getPlazasTotales() {
		return this.plazasPrimera + this.plazasBusiness + this.plazasTurista;
	}
	
	/**
	 * Consulta el número máximo de pasajeros que puede transportar el avión.
	 * 
	 * @return capacidad máxima de pasajeros del avión
	 */
	public int getCapacidadMaximaPasajeros() {
		return this.capacidadMaximaPasajeros;
	}
	
	/**
	 * Consulta la capacidad máxima de equipaje en kilogramos.
	 * 
	 * @return peso máximo de equipaje en kilogramos
	 */
	public double getEquipajeMaxKg() {
		return this.equipajeMaxKg;
	}
	
	/**
	 * Consulta el número máximo de maletas o bultos que admite el avión.
	 * 
	 * @return número máximo de maletas
	 */
	public int getMaxMaletas() {
		return this.maxMaletas;
	}
	
	/**
	 * Modifica la distribución de plazas por clase sin superar la capacidad máxima del avión.
	 * 
	 * @param nuevasPrimera nuevo número de plazas en primera clase
	 * @param nuevasBusiness nuevo número de plazas en clase business
	 * @param nuevasTurista nuevo número de plazas en clase turista
	 * @throws IllegalArgumentException cuando algún valor es negativo, la suma es menor o igual a cero
	 * o supera getCapacidadMaximaPasajeros()
	 */
	public void redistribuirPlazas(int nuevasPrimera, int nuevasBusiness, int nuevasTurista) {
		if (nuevasPrimera < 0 || nuevasBusiness < 0 || nuevasTurista < 0) {
			throw new IllegalArgumentException("Las plazas por clase no pueden ser negativas.");
		}
		
		int nuevoTotal = nuevasPrimera + nuevasBusiness + nuevasTurista;
		if (nuevoTotal <= 0) {
			throw new IllegalArgumentException("El avión debe mantener al menos una plaza operativa.");
		}
		if (nuevoTotal > this.capacidadMaximaPasajeros) {
			throw new IllegalArgumentException("La nueva distribución supera el máximo de plazas del avión.");
		}
		
		this.plazasPrimera = nuevasPrimera;
		this.plazasBusiness = nuevasBusiness;
		this.plazasTurista = nuevasTurista;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Avion otro = (Avion) obj;
		return this.matricula.equals(otro.matricula);
	}

	@Override
	public int hashCode() {
		return 31 + this.matricula.hashCode();
	}

	@Override
	public String toString() {
		return "Avion [matricula=" + this.matricula + ", modelo=" + this.modelo
				+ ", plazasActuales=" + getPlazasTotales() + "/" + this.capacidadMaximaPasajeros + "]";
	}
}
