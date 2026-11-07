package uva.poo.practica1;

import java.time.LocalDateTime;

/**
 * Implementación de un tipo de dato que representa una cinta de recogida de equipajes.
 * 
 * @author pablmer
 * @author seraran
 */
public class CintaRecogida implements Identificable {
	private static final double EPSILON = 1e-9;

	private String identificador;
	private double capacidadMaxima;
	private Maleta[] maletas;
	private int numMaletas;
	private boolean operativa;
	
	/**
	 * Inicialización de una cinta de recogida indicando su identificador, peso máximo y límite de bultos.
	 * 
	 * @param identificador código identificativo de la cinta
	 * @param capacidadMaxima peso máximo soportado en kilogramos
	 * @param maxMaletas número máximo de maletas simultáneas
	 * @throws IllegalArgumentException cuando identificador es null o vacío
	 * @throws IllegalArgumentException cuando capacidadMaxima <= 0 o maxMaletas <= 0
	 */
	public CintaRecogida(String identificador, double capacidadMaxima, int maxMaletas) {
		if (identificador == null || identificador.trim().isEmpty()) {
			throw new IllegalArgumentException("El identificador de la cinta no puede ser nulo ni vacío.");
		}
		if (capacidadMaxima <= 0) {
			throw new IllegalArgumentException("La capacidad máxima debe ser mayor que cero.");
		}
		if (maxMaletas <= 0) {
			throw new IllegalArgumentException("El número máximo de maletas debe ser mayor que cero.");
		}
		this.identificador = identificador.trim();
		this.capacidadMaxima = capacidadMaxima;
		this.maletas = new Maleta[maxMaletas];
		this.numMaletas = 0;
		this.operativa = false;
	}

	@Override
	public String getIdentificador() {
		return this.identificador;
	}
	
	/**
	 * Consulta la capacidad máxima de peso de la cinta en kilogramos.
	 * 
	 * @return capacidad máxima en kilogramos
	 */
	public double getCapacidadMaxima() {
		return this.capacidadMaxima;
	}
	
	/**
	 * Consulta el peso acumulado actual de las maletas depositadas en la cinta.
	 * 
	 * @return peso acumulado en kilogramos
	 */
	public double getPesoAcumulado() {
		double sumaPesos = 0.0;
		for (int i = 0; i < this.numMaletas; i++) {
			sumaPesos += this.maletas[i].getPeso();
		}
		return sumaPesos;
	}
	
	/**
	 * Consulta la capacidad de peso libre disponible en la cinta en kilogramos.
	 * 
	 * @return capacidad libre en kilogramos
	 */
	public double getCapacidadLibre() {
		double libre = this.capacidadMaxima - getPesoAcumulado();
		return libre < 0.0 ? 0.0 : libre;
	}
	
	/**
	 * Consulta el número máximo de maletas que admite la cinta.
	 * 
	 * @return número máximo de maletas
	 */
	public int getMaxMaletas() {
		return this.maletas.length;
	}
	
	/**
	 * Consulta el número de maletas depositadas actualmente en la cinta.
	 * 
	 * @return cantidad de maletas en la cinta
	 */
	public int getNumMaletas() {
		return this.numMaletas;
	}

	/**
	 * Consulta cuántos bultos más caben actualmente en la cinta.
	 * 
	 * @return número de huecos disponibles
	 */
	public int getHuecosLibres() {
		return this.maletas.length - this.numMaletas;
	}

	/**
	 * Comprueba si la cinta de recogida se encuentra operativa.
	 * 
	 * @return true si está operativa; false en caso contrario
	 */
	public boolean isOperativa() {
		return this.operativa;
	}

	/**
	 * Cambia el estado operativo de la cinta de recogida.
	 * 
	 * @param operativa true para activar la cinta; false para desactivarla
	 */
	public void setOperativa(boolean operativa) {
		this.operativa = operativa;
	}

	/**
	 * Comprueba si una maleta concreta se encuentra actualmente en la cinta.
	 * 
	 * @param maleta maleta a buscar
	 * @return true si la maleta está en la cinta; false en caso contrario
	 * @throws IllegalArgumentException cuando maleta == null
	 */
	public boolean contieneMaleta(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta a consultar no puede ser null.");
		}
		for (int i = 0; i < this.numMaletas; i++) {
			if (this.maletas[i].equals(maleta)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Deposita una maleta en la cinta de recogida.
	 * 
	 * @param maleta maleta a depositar
	 * @throws IllegalArgumentException cuando maleta == null, ya está en la cinta
	 * o su peso supera getCapacidadLibre()
	 * @throws IllegalStateException cuando la cinta no está operativa, está llena de bultos
	 * o la maleta ya fue recogida
	 */
	public void depositarMaleta(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta no puede ser null.");
		}
		if (!this.operativa) {
			throw new IllegalStateException("La cinta no está operativa.");
		}
		if (maleta.isRecogida()) {
			throw new IllegalStateException("No se puede depositar en la cinta una maleta ya recogida.");
		}
		if (contieneMaleta(maleta)) {
			throw new IllegalArgumentException("La maleta ya se encuentra en la cinta.");
		}
		if (this.numMaletas >= this.maletas.length) {
			throw new IllegalStateException("No caben más maletas en la cinta.");
		}
		if (maleta.getPeso() > getCapacidadLibre() + EPSILON) {
			throw new IllegalArgumentException("No hay capacidad libre suficiente.");
		}
		this.maletas[this.numMaletas] = maleta;
		this.numMaletas++;
	}

	/**
	 * Retira una maleta de la cinta y registra su fecha y hora de recogida.
	 * 
	 * @param maleta maleta a retirar
	 * @param fechaHoraRecogida fecha y hora en la que se retira
	 * @throws IllegalArgumentException cuando maleta == null, fechaHoraRecogida == null
	 * o la maleta no se encuentra en la cinta
	 * @throws IllegalStateException cuando no queda equipaje en la cinta
	 */
	public void retirarMaleta(Maleta maleta, LocalDateTime fechaHoraRecogida) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta a retirar no puede ser null.");
		}
		if (fechaHoraRecogida == null) {
			throw new IllegalArgumentException("La fecha y hora de recogida no puede ser null.");
		}
		if (this.numMaletas == 0) {
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
		for (int j = posicion; j < this.numMaletas - 1; j++) {
			this.maletas[j] = this.maletas[j + 1];
		}
		this.maletas[this.numMaletas - 1] = null;
		this.numMaletas--;
		maleta.registrarRecogida(fechaHoraRecogida);
	}

	/**
	 * Comprueba si queda alguna maleta pendiente de recoger en la cinta.
	 * 
	 * @return true si hay al menos una maleta; false en caso contrario
	 */
	public boolean tieneEquipajePendiente() {
		return this.numMaletas > 0;
	}

	/**
	 * Consulta las maletas depositadas actualmente en la cinta.
	 * 
	 * @return array con las maletas presentes en la cinta
	 */
	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[this.numMaletas];
		for (int i = 0; i < this.numMaletas; i++) {
			maletasCopia[i] = this.maletas[i];
		}
		return maletasCopia;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CintaRecogida otra = (CintaRecogida) obj;
		return this.identificador.equals(otra.identificador);
	}

	@Override
	public int hashCode() {
		return 31 + this.identificador.hashCode();
	}

	@Override
	public String toString() {
		return "CintaRecogida [id=" + this.identificador + ", operativa=" + this.operativa
				+ ", maletas=" + this.numMaletas + "/" + this.maletas.length + "]";
	}
}