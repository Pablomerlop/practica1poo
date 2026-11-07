package uva.poo.practica1;

import java.time.LocalDateTime;
import fabricante.externo.tarjeta.TarjetaEmbarque;

/**
 * Implementación de un tipo de dato que representa un pasajero con sus maletas y estado de embarque.
 * 
 * @author pablmer
 * @author seraran
 */
public class Viajero implements Identificable {
	private String identificador;
	private String nombre;
	private String apellidos;
	private Maleta[] maletas;
	private int numMaletas;
	private TarjetaEmbarque tarjetaEmbarque;
	private Vuelo vueloAsignado;

	/**
	 * Inicialización de un viajero indicando su documento identificativo, nombre, apellidos y límite de maletas.
	 * 
	 * @param identificador número de DNI o pasaporte
	 * @param nombre nombre de pila del viajero
	 * @param apellidos apellidos del viajero
	 * @param maxMaletas número máximo de maletas que puede facturar
	 * @throws IllegalArgumentException cuando identificador, nombre o apellidos son null o cadenas vacías
	 * @throws IllegalArgumentException cuando maxMaletas &lt; 0
	 */
	public Viajero(String identificador, String nombre, String apellidos, int maxMaletas) {
		if (identificador == null || identificador.trim().isEmpty()) {
			throw new IllegalArgumentException("El DNI o pasaporte no puede ser nulo ni vacío.");
		}
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
		}
		if (apellidos == null || apellidos.trim().isEmpty()) {
			throw new IllegalArgumentException("Los apellidos no pueden ser nulos ni vacíos.");
		}
		if (maxMaletas < 0) {
			throw new IllegalArgumentException("El máximo de maletas no puede ser negativo.");
		}
		this.identificador = identificador.trim();
		this.nombre = nombre.trim();
		this.apellidos = apellidos.trim();
		this.maletas = new Maleta[maxMaletas];
		this.numMaletas = 0;
		this.tarjetaEmbarque = null;
		this.vueloAsignado = null;
	}

	@Override
	public String getIdentificador() {
		return this.identificador;
	}

	/**
	 * Consulta el nombre del viajero.
	 * 
	 * @return nombre del viajero
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Consulta los apellidos del viajero.
	 * 
	 * @return apellidos del viajero
	 */
	public String getApellidos() {
		return this.apellidos;
	}

	/**
	 * Comprueba si el viajero ya ha embarcado en el vuelo.
	 * 
	 * @return true si ha embarcado; false en caso contrario
	 */
	public boolean isEmbarcado() {
		return this.tarjetaEmbarque != null && this.tarjetaEmbarque.isEmbarcado();
	}

	/**
	 * Comprueba si el viajero dispone de tarjeta de embarque asignada.
	 * 
	 * @return true si tiene tarjeta de embarque; false en caso contrario
	 */
	public boolean tieneTarjetaEmbarque() {
		return this.tarjetaEmbarque != null;
	}

	/**
	 * Consulta la tarjeta de embarque del viajero.
	 * 
	 * @return tarjeta de embarque asociada
	 * @throws IllegalStateException cuando el viajero no tiene tarjeta de embarque asignada
	 */
	public TarjetaEmbarque getTarjetaEmbarque() {
		if (!tieneTarjetaEmbarque()) {
			throw new IllegalStateException("El viajero no tiene tarjeta de embarque asignada.");
		}
		return this.tarjetaEmbarque;
	}

	/**
	 * Asigna una tarjeta de embarque sin usar al viajero.
	 * 
	 * @param tarjeta tarjeta de embarque a asignar
	 * @throws IllegalArgumentException cuando tarjeta == null o ya ha sido usada para embarcar
	 * @throws IllegalStateException cuando el viajero ya ha embarcado
	 */
	public void asignarTarjetaEmbarque(TarjetaEmbarque tarjeta) {
		if (tarjeta == null) {
			throw new IllegalArgumentException("La tarjeta de embarque no puede ser null.");
		}
		if (tarjeta.isEmbarcado()) {
			throw new IllegalArgumentException("No se puede asignar una tarjeta ya utilizada para embarcar.");
		}
		if (isEmbarcado()) {
			throw new IllegalStateException("El viajero ya ha embarcado.");
		}
		this.tarjetaEmbarque = tarjeta;
	}

	Vuelo getVueloAsignado() {
		return this.vueloAsignado;
	}

	void setVueloAsignado(Vuelo vuelo) {
		this.vueloAsignado = vuelo;
	}

	/**
	 * Factura una maleta asociándola al viajero y verificando la capacidad del vuelo si ya tiene uno asignado.
	 * 
	 * @param maleta maleta a facturar
	 * @throws IllegalArgumentException cuando maleta == null
	 * @throws IllegalStateException cuando el viajero ya ha embarcado, ha alcanzado su límite de maletas,
	 * la maleta ya fue facturada o recogida, o supera la capacidad del avión del vuelo asignado
	 */
	public void facturarMaleta(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta no puede ser null.");
		}
		if (isEmbarcado()) {
			throw new IllegalStateException("No se puede facturar equipaje después de embarcar.");
		}
		if (maleta.isFacturada() || maleta.isRecogida()) {
			throw new IllegalStateException("La maleta ya ha sido facturada o recogida previamente.");
		}
		if (this.numMaletas >= this.maletas.length) {
			throw new IllegalStateException("Ha sido alcanzado el límite de maletas facturadas.");
		}
		if (this.vueloAsignado != null) {
			this.vueloAsignado.validarCapacidadEquipajeExtra(1, maleta.getPeso());
		}
		maleta.marcarFacturada();
		this.maletas[this.numMaletas] = maleta;
		this.numMaletas++;
	}

	/**
	 * Comprueba si una maleta pertenece al equipaje facturado de este viajero.
	 * 
	 * @param maleta maleta a comprobar
	 * @return true si está facturada por este viajero; false en caso contrario
	 * @throws IllegalArgumentException cuando maleta == null
	 * 
	 */
	public boolean tieneMaletaFacturada(Maleta maleta) {
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta no puede ser null.");
		}
		for (int i = 0; i < this.numMaletas; i++) {
			if (this.maletas[i].equals(maleta)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retira de una cinta de recogida una maleta perteneciente a este viajero.
	 * 
	 * @param cinta cinta de recogida donde se encuentra la maleta
	 * @param maleta maleta propia a recoger
	 * @param fechaHoraRecogida fecha y hora de la recogida
	 * @throws IllegalArgumentException cuando alguno de los argumentos es null, la maleta no pertenece
	 * al viajero o no se encuentra en la cinta
	 * @throws IllegalStateException cuando la cinta está vacía
	 */
	public void recogerMaleta(CintaRecogida cinta, Maleta maleta, LocalDateTime fechaHoraRecogida) {
		if (cinta == null) {
			throw new IllegalArgumentException("La cinta de recogida no puede ser null.");
		}
		if (maleta == null) {
			throw new IllegalArgumentException("La maleta no puede ser null.");
		}
		if (fechaHoraRecogida == null) {
			throw new IllegalArgumentException("La fecha y hora de recogida no puede ser null.");
		}
		if (!tieneMaletaFacturada(maleta)) {
			throw new IllegalArgumentException("La maleta indicada no pertenece a este viajero.");
		}
		cinta.retirarMaleta(maleta, fechaHoraRecogida);
	}

	/**
	 * Consulta el número de maletas facturadas por el viajero.
	 * 
	 * @return cantidad de maletas facturadas
	 */
	public int getNumMaletas() {
		return this.numMaletas;
	}

	/**
	 * Consulta el peso total en kilogramos de todas las maletas facturadas por el viajero.
	 * 
	 * @return peso total del equipaje en kilogramos
	 */
	public double getPesoTotalEquipaje() {
		double pesoTotal = 0.0;
		for (int i = 0; i < this.numMaletas; i++) {
			pesoTotal += this.maletas[i].getPeso();
		}
		return pesoTotal;
	}

	/**
	 * Consulta las maletas facturadas asociadas al viajero.
	 * 
	 * @return array con las maletas facturadas del viajero
	 */
	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[this.numMaletas];
		for (int i = 0; i < this.numMaletas; i++) {
			maletasCopia[i] = this.maletas[i];
		}
		return maletasCopia;
	}

	/**
	 * Autoriza el embarque utilizando la tarjeta previamente asignada al viajero.
	 * 
	 * @param credencialAutorizacion credencial de seguridad para autorizar el embarque
	 * @throws IllegalArgumentException cuando la credencial es null, vacía o incorrecta
	 * @throws IllegalStateException cuando el viajero no tiene tarjeta asignada o ya ha embarcado
	 */
	public void embarcar(String credencialAutorizacion) {
		if (credencialAutorizacion == null || credencialAutorizacion.trim().isEmpty()) {
			throw new IllegalArgumentException("La credencial de autorización no puede ser nula ni vacía.");
		}
		if (!tieneTarjetaEmbarque()) {
			throw new IllegalStateException("El viajero no dispone de tarjeta de embarque.");
		}
		if (isEmbarcado()) {
			throw new IllegalStateException("El viajero ya ha embarcado.");
		}
		this.tarjetaEmbarque.autorizaEmbarque(credencialAutorizacion);
	}

	/**
	 * Autoriza el embarque del viajero proporcionando la tarjeta de embarque y la credencial de autorización.
	 * 
	 * @param tarjeta tarjeta de embarque del viajero
	 * @param credencialAutorizacion credencial de seguridad para autorizar el embarque
	 * @throws IllegalArgumentException cuando tarjeta es null, o la credencial es null, vacía o incorrecta
	 * @throws IllegalStateException cuando el viajero o la tarjeta ya figuran como embarcados
	 */
	public void embarcar(TarjetaEmbarque tarjeta, String credencialAutorizacion) {
		if (tarjeta == null) {
			throw new IllegalArgumentException("Se necesita una tarjeta de embarque.");
		}
		if (credencialAutorizacion == null || credencialAutorizacion.trim().isEmpty()) {
			throw new IllegalArgumentException("La credencial de autorización no puede ser nula ni vacía.");
		}
		if (isEmbarcado() || tarjeta.isEmbarcado()) {
			throw new IllegalStateException("El viajero o la tarjeta ya figuran como embarcados.");
		}
		tarjeta.autorizaEmbarque(credencialAutorizacion);
		this.tarjetaEmbarque = tarjeta;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Viajero otro = (Viajero) obj;
		return this.identificador.equals(otro.identificador);
	}

	@Override
	public int hashCode() {
		return 31 + this.identificador.hashCode();
	}

	@Override
	public String toString() {
		return "Viajero [id=" + this.identificador + ", nombre=" + this.nombre + " " + this.apellidos
				+ ", embarcado=" + isEmbarcado() + ", maletas=" + this.numMaletas + "]";
	}
}