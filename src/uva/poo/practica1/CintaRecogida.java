package uva.poo.practica1;

import java.time.LocalDateTime;

public class CintaRecogida implements Identificable {
	private static final double EPSILON = 1e-9;

	private String identificador;
	private double capacidadMaxima;
	private Maleta[] maletas;
	private int numMaletas;
	private boolean operativa;

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

	public double getCapacidadMaxima() {
		return this.capacidadMaxima;
	}

	public double getPesoAcumulado() {
		double sumaPesos = 0.0;
		for (int i = 0; i < this.numMaletas; i++) {
			sumaPesos += this.maletas[i].getPeso();
		}
		return sumaPesos;
	}

	public double getCapacidadLibre() {
		double libre = this.capacidadMaxima - getPesoAcumulado();
		return libre < 0.0 ? 0.0 : libre;
	}

	public int getMaxMaletas() {
		return this.maletas.length;
	}

	public int getNumMaletas() {
		return this.numMaletas;
	}

	public int getHuecosLibres() {
		return this.maletas.length - this.numMaletas;
	}

	public boolean isOperativa() {
		return this.operativa;
	}

	public void setOperativa(boolean operativa) {
		this.operativa = operativa;
	}

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

	public boolean tieneEquipajePendiente() {
		return this.numMaletas > 0;
	}

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