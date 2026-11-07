package uva.poo.practica1;

import java.time.LocalDateTime;

public class Maleta {
	private double peso;
	private String dimensiones;
	private LocalDateTime fechaHoraRecogida;
	private boolean facturada;

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

	public double getPeso() {
		return this.peso;
	}

	public String getDimensiones() {
		return this.dimensiones;
	}

	public boolean isFacturada() {
		return this.facturada;
	}

	void marcarFacturada() {
		if (this.facturada) {
			throw new IllegalStateException("La maleta ya ha sido facturada.");
		}
		this.facturada = true;
	}

	public boolean isRecogida() {
		return this.fechaHoraRecogida != null;
	}

	public LocalDateTime getFechaHoraRecogida() {
		if (!isRecogida()) {
			throw new IllegalStateException("La maleta aún no ha sido recogida.");
		}
		return this.fechaHoraRecogida;
	}

	public void registrarRecogida(LocalDateTime fechaHora) {
		if (fechaHora == null) {
			throw new IllegalArgumentException("La fecha y hora de recogida no puede ser null.");
		}
		if (isRecogida()) {
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