package uva.poo.practica1;

import java.time.LocalDateTime;

/**
 * Representa una maleta o bulto de equipaje asociado a un viajero.
 * 
 * @author pablmer
 * @author seraran
 */
public class Maleta {
	private double peso;
	private String dimensiones;
	private LocalDateTime fechaHoraRecogida;
	private boolean facturada;
	
	/**
	 * Inicialización de una nueva maleta indicando su peso y dimensiones.
	 * 
	 * @param peso peso de la maleta en kilogramos
	 * @param dimensiones dimensiones de la maleta
	 * @throws IllegalArgumentException cuando peso <= 0
	 * @throws IllegalArgumentException cuando dimensiones es null o una cadena vacía
	 */
	public Maleta(double peso, String dimensiones) {
		if (peso <= 0) {
			throw new IllegalArgumentException("El peso debe ser mayor que cero.");
		}
		if (dimensiones == null || dimensiones.trim().isEmpty()) {
			throw new IllegalArgumentException("Las dimensiones no pueden ser nulas ni vacías.");
		}
		
		this.peso = peso;
		this.dimensiones = dimensiones.trim();
		this.fechaHoraRecogida = null;
		this.facturada = false;
	}
	
	/**
	 * Consulta el peso de la maleta en kilogramos.
	 * 
	 * @return peso de la maleta
	 */
	public double getPeso() {
		return this.peso;
	}
	
	/**
	 * Consulta las dimensiones de la maleta.
	 * 
	 * @return cadena con las dimensiones
	 */
	public String getDimensiones() {
		return dimensiones;
	}
	
	/**
	 * Comprueba si la maleta ya ha sido facturada.
	 * 
	 * @return true si ha sido facturada; false en caso contrario
	 */
	public boolean isFacturada() {
		return this.facturada;
	}
	
	void marcarFacturada() {
		this.facturada = true;
	}
	
	/**
	 * Comprueba si la maleta ya ha sido recogida.
	 * 
	 * @return true si ha sido recogida; false en caso contrario
	 */
	public boolean isRecogida() {
		return this.fechaHoraRecogida != null;
	}

	/**
	 * Consulta la fecha y hora en la que se recogió la maleta.
	 * 
	 * @return fecha y hora de recogida
	 * @throws IllegalStateException cuando la maleta todavía no ha sido recogida
	 */
	public LocalDateTime getFechaHoraRecogida() {
		return this.fechaHoraRecogida;
	}
	
	/**
	 * Registra la fecha y hora de recogida de la maleta.
	 * 
	 * @param fechaHora fecha y hora de la recogida
	 * @throws IllegalArgumentException cuando fechaHora == null
	 * @throws IllegalStateException cuando la maleta ya figura como recogida
	 */
	public void registrarRecogida(LocalDateTime fechaHora) {
		if (fechaHora == null) {
			throw new IllegalArgumentException("La fecha y hora de recogida no puede ser null.");
		}
		if (this.fechaHoraRecogida != null) {
			throw new IllegalStateException("La maleta ya ha sido recogida.");
		}
		
		this.fechaHoraRecogida = fechaHora;
	}
	
	@Override
	public String toString() {
		return "Maleta [peso=" + this.peso + " kg, dimensiones=" + this.dimensiones
				+ ", facturada=" + this.facturada + ", recogida=" + isRecogida() + "]";
	}
}
