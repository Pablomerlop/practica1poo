package uva.poo.practica1;

import java.time.LocalDateTime;
import fabricante.externo.tarjeta.TarjetaEmbarque;

public class Viajero implements Identificable {
	private String identificador;
	private String nombre;
	private String apellidos;
	private Maleta[] maletas;
	private int numMaletas;
	private TarjetaEmbarque tarjetaEmbarque;
	private Vuelo vueloAsignado;

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

	public String getNombre() {
		return this.nombre;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public boolean isEmbarcado() {
		return this.tarjetaEmbarque != null && this.tarjetaEmbarque.isEmbarcado();
	}

	public boolean tieneTarjetaEmbarque() {
		return this.tarjetaEmbarque != null;
	}

	public TarjetaEmbarque getTarjetaEmbarque() {
		if (!tieneTarjetaEmbarque()) {
			throw new IllegalStateException("El viajero no tiene tarjeta de embarque asignada.");
		}
		return this.tarjetaEmbarque;
	}

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

	public int getNumMaletas() {
		return this.numMaletas;
	}

	public double getPesoTotalEquipaje() {
		double pesoTotal = 0.0;
		for (int i = 0; i < this.numMaletas; i++) {
			pesoTotal += this.maletas[i].getPeso();
		}
		return pesoTotal;
	}

	public Maleta[] getMaletas() {
		Maleta[] maletasCopia = new Maleta[this.numMaletas];
		for (int i = 0; i < this.numMaletas; i++) {
			maletasCopia[i] = this.maletas[i];
		}
		return maletasCopia;
	}

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