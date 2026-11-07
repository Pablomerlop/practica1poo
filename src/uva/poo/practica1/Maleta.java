package uva.poo.practica1;

import java.time.LocalDateTime;

public class Maleta {
	private double peso;
	private String dimensiones;
	private LocalDateTime fechaHoraRecogida;
	
	public Maleta(double peso, String dimensiones) {
		if (peso <= 0) {
			throw new IllegalArgumentException("El peso debe ser mayor que cero.");
		}
		if (dimensiones == null || dimensiones.trim().isEmpty()) {
			throw new IllegalArgumentException("Las dimensiones no pueden ser nulas ni vacías.");
		}
		
		this.peso = peso;
		this.dimensiones = dimensiones;
		this.fechaHoraRecogida = null;
	}
	
	public double getPeso() {
		return this.peso;
	}
	
	public String getDimensiones() {
		return dimensiones;
	}
	
	public LocalDateTime getFechaHoraRecogida() {
		return this.fechaHoraRecogida;
	}
	
	public void registrarRecogida(LocalDateTime fechaHora) {
		if (fechaHora == null) {
			throw new IllegalArgumentException("La fecha y hora de recogida no puede ser null.");
		}
		if (this.fechaHoraRecogida != null) {
			throw new IllegalStateException("La maleta ya ha sido recogida.");
		}
		
		this.fechaHoraRecogida = fechaHora;
	}
	
	public boolean isRecogida() {
		return this.fechaHoraRecogida != null;
	}
}
