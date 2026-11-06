package uva.poo.practica1;

import java.time.LocalDateTime;

public class Maleta {
	private double peso;
	private String dimensiones;
	private LocalDateTime fechaHoraRecogida;
	
	public Maleta(double peso, String dimensiones) {
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
		this.fechaHoraRecogida = fechaHora;
	}
	
	public boolean estaRecogida() {
		return this.fechaHoraRecogida != null;
	}
}
