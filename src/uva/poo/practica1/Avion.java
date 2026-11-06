package uva.poo.practica1;

public class Avion {
	private String matricula;
	private String modelo;
	private int plazasPrimera;
	private int plazasBusiness;
	private int plazasTurista;
	private int plazasTotales;
	private double equipajeMaxKg;
	
	public Avion(String matricula, String modelo, int plazasPrimera, int plazasBusiness,
			int plazasTurista, double equipajeMaxKg) {
		this.matricula = matricula;
		this.modelo = modelo;
		this.plazasPrimera = plazasPrimera;
		this.plazasBusiness = plazasBusiness;
		this.plazasTurista = plazasTurista;
		this.equipajeMaxKg = equipajeMaxKg;
		plazasTotales = plazasPrimera + plazasBusiness + plazasTurista;
	}
	
	public double getEquipajeMaxKg() {
		return this.equipajeMaxKg;
	}
	
	public int getPlazasPrimera() {
		return this.plazasPrimera;
	}
	
	public int getPlazasBusiness() {
		return this.plazasBusiness;
	}
	
	public int getPlazasTurista() {
		return this.plazasTurista;
	}
	
	public void redistribuirPlazas(int nuevasPrimera, int nuevasBusiness, int nuevasTurista) {
		int nuevoTotal = nuevasPrimera + nuevasBusiness + nuevasTurista;
		
		if (nuevoTotal > plazasTotales) {
			throw new IllegalArgumentException("La nueva distribución supera el máximo de plazas del avión.");
		}
		
		this.plazasPrimera = nuevasPrimera;
		this.plazasBusiness = nuevasBusiness;
		this.plazasTurista = nuevasTurista;
	}
}


