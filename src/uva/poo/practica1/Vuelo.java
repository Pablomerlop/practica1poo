package uva.poo.practica1;

import java.time.LocalDateTime;
import es.uva.inf.poo.maps.GPSCoordinate;

public class Vuelo implements Identificable {
	private static final double EPSILON = 1e-9;

	private String codigo;
	private Avion avion;
	private LocalDateTime fechaHoraSalida;
	private LocalDateTime fechaHoraLlegada;
	private LocalDateTime fechaHoraSalidaReal;
	private GPSCoordinate posicionSalida;
	private GPSCoordinate posicionLlegada;
	private Viajero[] viajeros;
	private int numViajeros;
	private CintaRecogida cintaAsignada;
	private boolean cancelado;
	private boolean equipajeDescargado;

	public Vuelo(String codigo, Avion avion, LocalDateTime fechaHoraSalida,
			LocalDateTime fechaHoraLlegada, GPSCoordinate posicionSalida, GPSCoordinate posicionLlegada) {
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
		this.codigo = codigo.trim();
		this.avion = avion;
		this.fechaHoraSalida = fechaHoraSalida;
		this.fechaHoraLlegada = fechaHoraLlegada;
		this.fechaHoraSalidaReal = null;
		this.posicionSalida = posicionSalida;
		this.posicionLlegada = posicionLlegada;
		this.viajeros = new Viajero[avion.getCapacidadMaximaPasajeros()];
		this.numViajeros = 0;
		this.cintaAsignada = null;
		this.cancelado = false;
		this.equipajeDescargado = false;
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

	public void setFechaHoraSalida(LocalDateTime nuevaFechaSalida) {
		if (nuevaFechaSalida == null) {
			throw new IllegalArgumentException("La nueva fecha de salida no puede ser null.");
		}
		if (!this.fechaHoraLlegada.isAfter(nuevaFechaSalida)) {
			throw new IllegalArgumentException("La fecha de salida debe ser anterior a la de llegada.");
		}
		if (this.cancelado) {
			throw new IllegalStateException("No se puede reprogramar un vuelo cancelado.");
		}
		if (isDespegado()) {
			throw new IllegalStateException("No se puede cambiar la hora estimada de salida tras despegar.");
		}
		this.fechaHoraSalida = nuevaFechaSalida;
	}

	public LocalDateTime getFechaHoraLlegada() {
		return this.fechaHoraLlegada;
	}

	public void setFechaHoraLlegada(LocalDateTime nuevaFechaLlegada) {
		if (nuevaFechaLlegada == null) {
			throw new IllegalArgumentException("La nueva fecha de llegada no puede ser null.");
		}
		if (!nuevaFechaLlegada.isAfter(this.fechaHoraSalida)) {
			throw new IllegalArgumentException("La fecha de llegada debe ser posterior a la de salida.");
		}
		if (this.cancelado) {
			throw new IllegalStateException("No se puede reprogramar un vuelo cancelado.");
		}
		this.fechaHoraLlegada = nuevaFechaLlegada;
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
		if (this.cancelado) {
			throw new IllegalStateException("No se puede cambiar el destino de un vuelo cancelado.");
		}
		this.posicionLlegada = nuevaPosicionLlegada;
	}

	public double getDistancia() {
		return this.posicionSalida.getDistanceTo(this.posicionLlegada);
	}

	public boolean tieneCintaAsignada() {
		return this.cintaAsignada != null;
	}

	public CintaRecogida getCintaAsignada() {
		if (!tieneCintaAsignada()) {
			throw new IllegalStateException("El vuelo no tiene ninguna cinta asignada.");
		}
		return this.cintaAsignada;
	}

	public boolean isCancelado() {
		return this.cancelado;
	}

	public void cancelar() {
		if (this.cancelado) {
			throw new IllegalStateException("El vuelo ya está cancelado.");
		}
		if (isDespegado()) {
			throw new IllegalStateException("No se puede cancelar un vuelo que ya ha despegado.");
		}
		this.cancelado = true;
		this.cintaAsignada = null;
	}

	public boolean isOperativo() {
		return !this.cancelado;
	}

	public boolean isDespegado() {
		return this.fechaHoraSalidaReal != null;
	}

	public LocalDateTime getFechaHoraSalidaReal() {
		if (!isDespegado()) {
			throw new IllegalStateException("El vuelo aún no ha despegado.");
		}
		return this.fechaHoraSalidaReal;
	}

	public void registrarDespegue(LocalDateTime fechaHoraReal) {
		if (fechaHoraReal == null) {
			throw new IllegalArgumentException("La fecha y hora real de salida no puede ser null.");
		}
		if (this.cancelado) {
			throw new IllegalStateException("Un vuelo cancelado no puede despegar.");
		}
		if (isDespegado()) {
			throw new IllegalStateException("El vuelo ya ha despegado.");
		}
		this.fechaHoraSalidaReal = fechaHoraReal;
	}

	public boolean isRetrasado(LocalDateTime horaActual) {
		if (horaActual == null) {
			throw new IllegalArgumentException("La hora actual no puede ser null.");
		}
		if (this.cancelado) {
			return false;
		}
		if (isDespegado()) {
			return this.fechaHoraSalidaReal.isAfter(this.fechaHoraSalida);
		}
		return horaActual.isAfter(this.fechaHoraSalida);
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

	void validarCapacidadEquipajeExtra(int bultosExtra, double pesoExtra) {
		if (this.cancelado) {
			throw new IllegalStateException("El vuelo está cancelado.");
		}
		if (isDespegado()) {
			throw new IllegalStateException("El vuelo ya ha despegado.");
		}
		if (getNumeroMaletasTotales() + bultosExtra > this.avion.getMaxMaletas()) {
			throw new IllegalStateException("El equipaje excede el número máximo de maletas del avión.");
		}
		if (getPesoTotalEquipaje() + pesoExtra > this.avion.getEquipajeMaxKg() + EPSILON) {
			throw new IllegalStateException("El equipaje excede el peso máximo del avión.");
		}
	}

	public void addViajero(Viajero viajero) {
		if (viajero == null) {
			throw new IllegalArgumentException("El viajero no puede ser null.");
		}
		for (int i = 0; i < this.numViajeros; i++) {
			if (this.viajeros[i].getIdentificador().equals(viajero.getIdentificador())) {
				throw new IllegalArgumentException("El viajero ya está asignado a este vuelo.");
			}
		}
		if (this.cancelado) {
			throw new IllegalStateException("No se pueden añadir viajeros a un vuelo cancelado.");
		}
		if (isDespegado()) {
			throw new IllegalStateException("No se pueden añadir viajeros a un vuelo que ya ha despegado.");
		}
		if (viajero.getVueloAsignado() != null) {
			throw new IllegalStateException("El viajero ya está asignado a otro vuelo.");
		}
		if (this.numViajeros >= this.avion.getPlazasTotales()) {
			throw new IllegalStateException("El vuelo está lleno según las plazas actuales del avión.");
		}
		validarCapacidadEquipajeExtra(viajero.getNumMaletas(), viajero.getPesoTotalEquipaje());
		this.viajeros[this.numViajeros] = viajero;
		this.numViajeros++;
		viajero.setVueloAsignado(this);
	}

	public void asignarCinta(CintaRecogida cinta) {
		if (cinta == null) {
			throw new IllegalArgumentException("La cinta asignada no puede ser null.");
		}
		if (this.cancelado) {
			throw new IllegalStateException("No se puede asignar cinta a un vuelo cancelado.");
		}
		if (!cinta.isOperativa()) {
			throw new IllegalStateException("La cinta asignada debe estar operativa.");
		}
		this.cintaAsignada = cinta;
	}

	public boolean isEquipajeDescargado() {
		return this.equipajeDescargado;
	}

	public void descargarEquipajeEnCinta() {
		if (this.cancelado) {
			throw new IllegalStateException("No se puede descargar equipaje de un vuelo cancelado.");
		}
		if (!tieneCintaAsignada()) {
			throw new IllegalStateException("El vuelo no tiene ninguna cinta de recogida asignada.");
		}
		if (!this.cintaAsignada.isOperativa()) {
			throw new IllegalStateException("La cinta asignada al vuelo no está operativa.");
		}
		if (this.equipajeDescargado) {
			throw new IllegalStateException("El equipaje de este vuelo ya ha sido descargado.");
		}
		if (getNumeroMaletasTotales() > this.cintaAsignada.getHuecosLibres()) {
			throw new IllegalStateException("No caben todas las maletas del vuelo en la cinta asignada.");
		}
		if (getPesoTotalEquipaje() > this.cintaAsignada.getCapacidadLibre() + EPSILON) {
			throw new IllegalStateException("El peso del equipaje del vuelo supera la capacidad libre de la cinta.");
		}
		for (int i = 0; i < this.numViajeros; i++) {
			Maleta[] maletasViajero = this.viajeros[i].getMaletas();
			for (int j = 0; j < maletasViajero.length; j++) {
				this.cintaAsignada.depositarMaleta(maletasViajero[j]);
			}
		}
		this.equipajeDescargado = true;
	}

	public int getNumViajeros() {
		return this.numViajeros;
	}

	public Viajero[] getViajeros() {
		Viajero[] viajerosCopia = new Viajero[this.numViajeros];
		for (int i = 0; i < this.numViajeros; i++) {
			viajerosCopia[i] = this.viajeros[i];
		}
		return viajerosCopia;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Vuelo otro = (Vuelo) obj;
		return this.codigo.equals(otro.codigo);
	}

	@Override
	public int hashCode() {
		return 31 + this.codigo.hashCode();
	}

	@Override
	public String toString() {
		return "Vuelo [codigo=" + this.codigo + ", avion=" + this.avion.getMatricula()
				+ ", viajeros=" + this.numViajeros + ", cancelado=" + this.cancelado + "]";
	}
}