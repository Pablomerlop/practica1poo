package uva.poo.practica1;

import java.time.LocalDateTime;
import es.uva.inf.poo.maps.GPSCoordinate;

/**
 * Implementación de un tipo de dato que representa un vuelo comercial, sus pasajeros y su logística de equipaje.
 * 
 * @author pablmer
 * @author seraran
 */
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

	/**
	 * Inicialización de un vuelo con su código, avión, fechas estimadas y coordenadas de origen y destino.
	 * 
	 * @param codigo código identificativo del vuelo
	 * @param avion avión asignado al vuelo
	 * @param fechaHoraSalida fecha y hora estimada de salida
	 * @param fechaHoraLlegada fecha y hora estimada de llegada
	 * @param posicionSalida coordenadas GPS del aeropuerto de salida
	 * @param posicionLlegada coordenadas GPS del aeropuerto de llegada
	 * @throws IllegalArgumentException cuando algún argumento es null, el código está vacío
	 * o !fechaHoraLlegada.isAfter(fechaHoraSalida)
	 */
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

	/**
	 * Consulta el código del vuelo.
	 * 
	 * @return código del vuelo
	 */
	public String getCodigo() {
		return this.codigo;
	}

	/**
	 * Consulta el avión asignado al vuelo.
	 * 
	 * @return avión del vuelo
	 */
	public Avion getAvion() {
		return this.avion;
	}

	/**
	 * Consulta la fecha y hora estimada de salida.
	 * 
	 * @return fecha y hora estimada de salida
	 */
	public LocalDateTime getFechaHoraSalida() {
		return this.fechaHoraSalida;
	}

	/**
	 * Modifica la fecha y hora estimada de salida del vuelo.
	 * 
	 * @param nuevaFechaSalida nueva fecha y hora estimada de salida
	 * @throws IllegalArgumentException cuando nuevaFechaSalida == null
	 * o no es anterior a la fecha estimada de llegada
	 * @throws IllegalStateException cuando el vuelo está cancelado o ya ha despegado
	 */
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

	/**
	 * Consulta la fecha y hora estimada de llegada.
	 * 
	 * @return fecha y hora estimada de llegada
	 */
	public LocalDateTime getFechaHoraLlegada() {
		return this.fechaHoraLlegada;
	}

	/**
	 * Modifica la fecha y hora estimada de llegada del vuelo.
	 * 
	 * @param nuevaFechaLlegada nueva fecha y hora estimada de llegada
	 * @throws IllegalArgumentException cuando nuevaFechaLlegada == null
	 * o no es posterior a la fecha estimada de salida
	 * @throws IllegalStateException cuando el vuelo está cancelado
	 */
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

	/**
	 * Consulta la posición GPS del aeropuerto de salida.
	 * 
	 * @return coordenadas GPS de salida
	 */
	public GPSCoordinate getPosicionSalida() {
		return this.posicionSalida;
	}

	/**
	 * Consulta la posición GPS del aeropuerto de llegada.
	 * 
	 * @return coordenadas GPS de llegada
	 */
	public GPSCoordinate getPosicionLlegada() {
		return this.posicionLlegada;
	}

	/**
	 * Modifica la posición GPS del aeropuerto de llegada.
	 * 
	 * @param nuevaPosicionLlegada nuevas coordenadas GPS de llegada
	 * @throws IllegalArgumentException cuando nuevaPosicionLlegada == null
	 * @throws IllegalStateException cuando el vuelo está cancelado
	 */
	public void setPosicionLlegada(GPSCoordinate nuevaPosicionLlegada) {
		if (nuevaPosicionLlegada == null) {
			throw new IllegalArgumentException("La nueva posición GPS de llegada no puede ser null.");
		}
		if (this.cancelado) {
			throw new IllegalStateException("No se puede cambiar el destino de un vuelo cancelado.");
		}
		this.posicionLlegada = nuevaPosicionLlegada;
	}

	/**
	 * Consulta la distancia en kilómetros entre el aeropuerto de salida y el de llegada.
	 * 
	 * @return distancia del trayecto en kilómetros
	 */
	public double getDistancia() {
		return this.posicionSalida.getDistanceTo(this.posicionLlegada);
	}

	/**
	 * Comprueba si el vuelo tiene una cinta de recogida asignada.
	 * 
	 * @return true si tiene cinta asignada; false en caso contrario
	 */
	public boolean tieneCintaAsignada() {
		return this.cintaAsignada != null;
	}

	/**
	 * Consulta la cinta de recogida asignada al vuelo.
	 * 
	 * @return cinta de recogida asignada
	 * @throws IllegalStateException cuando el vuelo no tiene ninguna cinta asignada
	 */
	public CintaRecogida getCintaAsignada() {
		if (!tieneCintaAsignada()) {
			throw new IllegalStateException("El vuelo no tiene ninguna cinta asignada.");
		}
		return this.cintaAsignada;
	}

	/**
	 * Comprueba si el vuelo se encuentra cancelado.
	 * 
	 * @return true si está cancelado; false en caso contrario
	 */
	public boolean isCancelado() {
		return this.cancelado;
	}

	/**
	 * Cancela el vuelo y libera su cinta asignada si la tuviera.
	 * 
	 * @throws IllegalStateException cuando el vuelo ya está cancelado o ya ha despegado
	 */
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

	/**
	 * Comprueba si el vuelo se encuentra operativo (no cancelado).
	 * 
	 * @return true si está operativo; false en caso contrario
	 */
	public boolean isOperativo() {
		return !this.cancelado;
	}

	/**
	 * Comprueba si el vuelo ya ha despegado.
	 * 
	 * @return true si se ha registrado el despegue; false en caso contrario
	 */
	public boolean isDespegado() {
		return this.fechaHoraSalidaReal != null;
	}

	/**
	 * Consulta la fecha y hora real en la que despegó el vuelo.
	 * 
	 * @return fecha y hora real de salida
	 * @throws IllegalStateException cuando el vuelo aún no ha despegado
	 */
	public LocalDateTime getFechaHoraSalidaReal() {
		if (!isDespegado()) {
			throw new IllegalStateException("El vuelo aún no ha despegado.");
		}
		return this.fechaHoraSalidaReal;
	}

	/**
	 * Registra la fecha y hora real de despegue del vuelo.
	 * 
	 * @param fechaHoraReal fecha y hora en la que despega el vuelo
	 * @throws IllegalArgumentException cuando fechaHoraReal == null
	 * @throws IllegalStateException cuando el vuelo está cancelado o ya ha despegado
	 */
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

	/**
	 * Comprueba si el vuelo está retrasado con respecto a la fecha y hora actual indicada.
	 * 
	 * @param horaActual fecha y hora de referencia para la consulta
	 * @return true si el vuelo no está cancelado y acumula retraso; false en caso contrario
	 * @throws IllegalArgumentException cuando horaActual == null
	 */
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

	/**
	 * Consulta el número total de maletas facturadas entre todos los viajeros del vuelo.
	 * 
	 * @return total de maletas en el vuelo
	 */
	public int getNumeroMaletasTotales() {
		int totalMaletas = 0;
		for (int i = 0; i < this.numViajeros; i++) {
			totalMaletas += this.viajeros[i].getNumMaletas();
		}
		return totalMaletas;
	}

	/**
	 * Consulta el peso total en kilogramos del equipaje de todos los viajeros del vuelo.
	 * 
	 * @return peso total del equipaje en kilogramos
	 */
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

	/**
	 * Asigna un viajero al vuelo verificando plazas disponibles y límites de equipaje del avión.
	 * 
	 * @param viajero viajero a añadir al vuelo
	 * @throws IllegalArgumentException cuando viajero == null o ya está asignado a este vuelo
	 * @throws IllegalStateException cuando el vuelo está cancelado, ya ha despegado, está lleno,
	 * el viajero pertenece a otro vuelo o su equipaje excede la capacidad de bodega del avión
	 */
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

	/**
	 * Asigna una cinta de recogida operativa al vuelo.
	 * 
	 * @param cinta cinta de recogida a asignar
	 * @throws IllegalArgumentException cuando cinta == null
	 * @throws IllegalStateException cuando el vuelo está cancelado o la cinta no está operativa
	 */
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

	/**
	 * Comprueba si el equipaje del vuelo ya ha sido descargado en la cinta de recogida.
	 * 
	 * @return true si ya fue descargado; false en caso contrario
	 */
	public boolean isEquipajeDescargado() {
		return this.equipajeDescargado;
	}

	/**
	 * Descarga todas las maletas de los viajeros del vuelo en la cinta de recogida asignada.
	 * 
	 * @throws IllegalStateException cuando el vuelo está cancelado, no tiene cinta asignada, la cinta no
	 * está operativa, el equipaje ya fue descargado o la cinta no tiene capacidad suficiente
	 */
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

	/**
	 * Consulta el número de viajeros asignados actualmente al vuelo.
	 * 
	 * @return número de viajeros en el vuelo
	 */
	public int getNumViajeros() {
		return this.numViajeros;
	}

	/**
	 * Consulta la lista de viajeros asignados al vuelo.
	 * 
	 * @return array con los viajeros del vuelo
	 */
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