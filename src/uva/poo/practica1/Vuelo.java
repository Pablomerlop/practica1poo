package uva.poo.practica1;

import java.time.LocalDateTime;
import es.uva.inf.poo.maps.GPSCoordinate;

public class Vuelo implements Identificable{
	private String codigo;
	private LocalDateTime fechaHoraSalida;
	private LocalDateTime fechaHoraLlegada;
	private GPSCoordinate posicionSalida;
	private GPSCoordinate posicionLlegada;
	private Avion avion;
	private Viajero[] viajeros;
	private int numViajeros;
	private CintaRecogida cintaAsignada;
	private boolean cancelado;
		
	private String estado; //OPERATIVO, RETRASADO, CANCELADO.
	
	public Vuelo(String codigo, Avion avion, LocalDateTime fechaHoraSalida, LocalDateTime fechaHoraLlegada, 
			GPSCoordinate posicionSalida, GPSCoordinate posicionLlegada) {
		if (codigo == null || codigo.trim().isEmpty()) {
			throw new IllegalArgumentException("El código del vuelo no puede ser nulo ni vacío.");
		}
		if (avion == null) {
			throw new IllegalArgumentException("El avión asignado no puede ser null.");
		}
		if (fechaHoraSalida == null || fechaHoraLlegada == null) {
			throw new IllegalArgumentException("Las fechas de salida y llegada no pueden ser null.");
		}
		if (!fechaHoraLlegada.isAfter(fechaHoraSalida)) {
			throw new IllegalArgumentException("La fecha de llegada debe ser posterior a la de salida.");
		}
		if (posicionSalida == null || posicionLlegada == null) {
			throw new IllegalArgumentException("Las coordenadas GPS de salida y llegada no pueden ser null.");
		}
		
		this.codigo = codigo;
		this.avion = avion;
		this.fechaHoraSalida = fechaHoraSalida;
		this.fechaHoraLlegada = fechaHoraLlegada;
		this.posicionSalida = posicionSalida;
		this.posicionLlegada = posicionLlegada;
		this.viajeros = new Viajero[avion.getPlazasTotales()];
		this.numViajeros = 0;
		this.cintaAsignada = null;
		this.cancelado = false;
	}
	
	@Override
	public String getIdentificador() {
		return this.codigo;
	}
	
	public String getCodigo() {
		return this.codigo;
	}
	
	public Avion getAvion() {
		return this.avion;
	}
	
	public LocalDateTime getFechaHoraSalida() {
		return this.fechaHoraSalida;
	}

	public LocalDateTime getFechaHoraLlegada() {
		return this.fechaHoraLlegada;
	}

	public GPSCoordinate getPosicionSalida() {
		return this.posicionSalida;
	}

	public GPSCoordinate getPosicionLlegada() {
		return this.posicionLlegada;
	}
	
	public void setPosicionLlegada(GPSCoordinate nuevaPosicionLlegada) {
		if (nuevaPosicionLlegada == null) {
			throw new IllegalArgumentException("La nueva posición GPS de llegada no puede ser null.");
		}
		this.posicionLlegada = nuevaPosicionLlegada;
	}
	
	public CintaRecogida getCintaAsignada() {
		return this.cintaAsignada;
	}
	
	public boolean isCancelado() {
		return this.cancelado;
	}

	public void cancelar() {
		this.cancelado = true;
	}
	
	public boolean isOperativo() {
		return !this.cancelado;
	}
	
	public boolean isRetrasado(LocalDateTime horaActual) {
		if (horaActual == null) {
			throw new IllegalArgumentException("La hora actual no puede ser null.");
		}
		if (this.cancelado) {
			return false;
		}
		return horaActual.isAfter(this.fechaHoraSalida) && !todosViajerosEmbarcados();
	}
	
	public boolean isRetrasado() {
		return isRetrasado(LocalDateTime.now());
	}

	private boolean todosViajerosEmbarcados() {
		if (this.numViajeros == 0) {
			return false;
		}
		for (int i = 0; i < this.numViajeros; i++) {
			if (!this.viajeros[i].isEmbarcado()) {
				return false;
			}
		}
		return true;
	}
	
	public int getNumeroMaletasTotales() {
		int totalMaletas = 0;
		for (int i = 0; i < this.numViajeros; i++) {
			totalMaletas += this.viajeros[i].getNumMaletas();
		}
		return totalMaletas;
	}

	public double getPesoTotalEquipaje() {
		double pesoTotal = 0.0;
		for (int i = 0; i < this.numViajeros; i++) {
			pesoTotal += this.viajeros[i].getPesoTotalEquipaje();
		}
		return pesoTotal;
	}
	
	public void addViajero(Viajero viajero) {
		if (viajero == null) {
			throw new IllegalArgumentException("El viajero no puede ser null.");
		}
		if (this.cancelado) {
			throw new IllegalStateException("No se pueden añadir viajeros a un vuelo cancelado.");
		}
		if(this.numViajeros >= this.avion.getPlazasTotales()) {
			throw new IllegalStateException("El vuelo está lleno.");
		}
		for (int i = 0; i < this.numViajeros; i++) {
			if (this.viajeros[i].getIdentificador().equals(viajero.getIdentificador())) {
				throw new IllegalArgumentException("El viajero ya está asignado a este vuelo.");
			}
		}
		if (getNumeroMaletasTotales() + viajero.getNumMaletas() > this.avion.getMaxMaletas()) {
			throw new IllegalStateException("El equipaje del viajero excede el número máximo de maletas del avión.");
		}
		if (getPesoTotalEquipaje() + viajero.getPesoTotalEquipaje() > this.avion.getEquipajeMaxKg()) {
			throw new IllegalStateException("El equipaje del viajero excede el peso máximo del avión.");
		}
		this.viajeros[this.numViajeros] = viajero;
		this.numViajeros++;
	}
	
	public void asignarCinta(CintaRecogida cinta) {
		if(cinta == null) {
			throw new IllegalArgumentException("La cinta asignada no puede ser null.");
		}
		this.cintaAsignada = cinta;
		this.cintaAsignada.setOperativa(true);
	}
	
	public void descargarEquipajeEnCinta() {
		if (this.cintaAsignada == null) {
			throw new IllegalStateException("El vuelo no tiene ninguna cinta de recogida asignada.");
		}
		for (int i = 0; i < this.numViajeros; i++) {
			Maleta[] maletasViajero = this.viajeros[i].getMaletas();
			for (int j = 0; j < maletasViajero.length; j++) {
				this.cintaAsignada.depositarMaleta(maletasViajero[j]);
			}
		}
	}
	
	public Viajero[] getViajeros() {
		Viajero[] viajerosCopia = new Viajero[this.numViajeros];
		for (int i = 0;i < this.numViajeros; i++) {
			viajerosCopia[i] = viajeros[i];
		}
		return viajerosCopia;
	}
	
}
