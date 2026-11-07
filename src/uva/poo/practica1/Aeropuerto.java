package uva.poo.practica1;

import java.time.LocalDateTime;
import es.uva.inf.poo.maps.GPSCoordinate;
import fabricante.externo.tarjeta.TarjetaEmbarque;

/**
 * Implementación de un tipo de dato que representa un aeropuerto y la gestión de sus vuelos y cintas de recogida.
 * 
 * @author pablmer
 * @author seraran
 */
public class Aeropuerto {
	private static final int LONGITUD_IATA = 3;
	private static final String CREDENCIAL_CREAR_TARJETA = "J1M6Nv65_B1R8T3X";
	private static final String CREDENCIAL_AUTORIZAR_EMBARQUE = "G1bR13l1_A7k2M9P";

	private String nombre;
	private String codigoIATA;
	private String direccion;
	private String pais;
	private GPSCoordinate coordenadas;
	private Vuelo[] vuelos;
	private int numVuelos;
	private CintaRecogida[] cintas;
	private int numCintas;

	/**
	 * Inicialización de un aeropuerto indicando sus datos de registro y capacidades máximas operativas.
	 * 
	 * @param nombre nombre oficial del aeropuerto
	 * @param codigoIATA código IATA de tres letras del aeropuerto
	 * @param direccion dirección postal del aeropuerto
	 * @param coordenadas posición GPS del aeropuerto
	 * @param pais país donde se ubica el aeropuerto
	 * @param maxVuelos número máximo de vuelos gestionables
	 * @param maxCintas número máximo de cintas de recogida instalables
	 * @throws IllegalArgumentException cuando algún texto es null o vacío, coordenadas == null,
	 * el código IATA no tiene tres letras o las capacidades máximas son menores o iguales a cero
	 */
	public Aeropuerto(String nombre, String codigoIATA, String direccion,
			GPSCoordinate coordenadas, String pais, int maxVuelos, int maxCintas) {
		validarCadenaNoVacia(nombre, "El nombre del aeropuerto no puede ser nulo ni vacío.");
		validarCodigoIATA(codigoIATA);
		validarCadenaNoVacia(direccion, "La dirección no puede ser nula ni vacía.");
		if (coordenadas == null) {
			throw new IllegalArgumentException("Las coordenadas GPS no pueden ser null.");
		}
		validarCadenaNoVacia(pais, "El país de origen no puede ser nulo ni vacío.");
		if (maxVuelos <= 0 || maxCintas <= 0) {
			throw new IllegalArgumentException("Las capacidades máximas deben ser mayores que cero.");
		}
		this.nombre = nombre.trim();
		this.codigoIATA = codigoIATA.toUpperCase();
		this.direccion = direccion.trim();
		this.coordenadas = coordenadas;
		this.pais = pais.trim();
		this.vuelos = new Vuelo[maxVuelos];
		this.numVuelos = 0;
		this.cintas = new CintaRecogida[maxCintas];
		this.numCintas = 0;
	}

	private void validarCadenaNoVacia(String cadena, String mensajeError) {
		if (cadena == null || cadena.trim().isEmpty()) {
			throw new IllegalArgumentException(mensajeError);
		}
	}

	private void validarCodigoIATA(String codigo) {
		if (codigo == null || codigo.length() != LONGITUD_IATA) {
			throw new IllegalArgumentException("El código IATA debe tener exactamente 3 letras.");
		}
		for (int i = 0; i < LONGITUD_IATA; i++) {
			if (!Character.isLetter(codigo.charAt(i))) {
				throw new IllegalArgumentException("El código IATA solo puede contener letras.");
			}
		}
	}

	/**
	 * Consulta el nombre del aeropuerto.
	 * 
	 * @return nombre del aeropuerto
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Consulta el código IATA de tres letras del aeropuerto.
	 * 
	 * @return código IATA en mayúsculas
	 */
	public String getCodigoIATA() {
		return this.codigoIATA;
	}

	/**
	 * Consulta la dirección del aeropuerto.
	 * 
	 * @return dirección del aeropuerto
	 */
	public String getDireccion() {
		return this.direccion;
	}

	/**
	 * Consulta la posición GPS del aeropuerto.
	 * 
	 * @return coordenadas GPS del aeropuerto
	 */
	public GPSCoordinate getCoordenadas() {
		return this.coordenadas;
	}

	/**
	 * Consulta el país del aeropuerto.
	 * 
	 * @return país de origen
	 */
	public String getPais() {
		return this.pais;
	}

	/**
	 * Registra un nuevo vuelo en el aeropuerto.
	 * 
	 * @param vuelo vuelo a registrar
	 * @throws IllegalArgumentException cuando vuelo == null, ya existe un vuelo con el mismo
	 * código o ni su salida ni su llegada coinciden con las coordenadas del aeropuerto
	 * @throws IllegalStateException cuando se ha alcanzado la capacidad máxima de vuelos
	 */
	public void addVuelo(Vuelo vuelo) {
		if (vuelo == null) {
			throw new IllegalArgumentException("El vuelo no puede ser null.");
		}
		if (!vuelo.getPosicionSalida().equals(this.coordenadas)
				&& !vuelo.getPosicionLlegada().equals(this.coordenadas)) {
			throw new IllegalArgumentException("El vuelo debe salir o llegar a las coordenadas de este aeropuerto.");
		}
		if (existeElemento(this.vuelos, this.numVuelos, vuelo.getIdentificador())) {
			throw new IllegalArgumentException("Ya existe un vuelo con ese código en el aeropuerto.");
		}
		if (this.numVuelos >= this.vuelos.length) {
			throw new IllegalStateException("Capacidad máxima de vuelos alcanzada.");
		}
		this.vuelos[this.numVuelos] = vuelo;
		this.numVuelos++;
	}

	/**
	 * Elimina un vuelo registrado en el aeropuerto a partir de su código.
	 * 
	 * @param identificadorVuelo código del vuelo a eliminar
	 * @throws IllegalArgumentException cuando el código es null o no corresponde a ningún vuelo registrado
	 */
	public void eliminarVuelo(String identificadorVuelo) {
		eliminarDeArray(this.vuelos, this.numVuelos, identificadorVuelo, "Vuelo no encontrado.");
		this.numVuelos--;
	}

	/**
	 * Consulta un vuelo registrado a partir de su código identificativo.
	 * 
	 * @param idVuelo código del vuelo buscado
	 * @return vuelo correspondiente al código indicado
	 * @throws IllegalArgumentException cuando idVuelo == null o el vuelo no existe
	 */
	public Vuelo getVuelo(String idVuelo) {
		int posicion = buscarPosicion(this.vuelos, this.numVuelos, idVuelo, "Vuelo no encontrado.");
		return this.vuelos[posicion];
	}

	/**
	 * Consulta todos los vuelos registrados en el aeropuerto.
	 * 
	 * @return array con los vuelos registrados
	 */
	public Vuelo[] getVuelos() {
		Vuelo[] vuelosCopia = new Vuelo[this.numVuelos];
		for (int i = 0; i < this.numVuelos; i++) {
			vuelosCopia[i] = this.vuelos[i];
		}
		return vuelosCopia;
	}

	/**
	 * Emite una nueva tarjeta de embarque utilizando la credencial de creación del sistema.
	 * 
	 * @return nueva tarjeta de embarque inicializada
	 */
	public TarjetaEmbarque emitirNuevaTarjetaEmbarque() {
		return new TarjetaEmbarque(CREDENCIAL_CREAR_TARJETA);
	}

	/**
	 * Asigna un pasajero a un vuelo del aeropuerto y le emite tarjeta de embarque si no disponía de una.
	 * 
	 * @param idVuelo código del vuelo
	 * @param viajero pasajero a asignar
	 * @throws IllegalArgumentException cuando idVuelo == null, viajero == null,
	 * el vuelo no existe o el pasajero ya está en ese vuelo
	 * @throws IllegalStateException cuando el vuelo no admite al pasajero o a su equipaje
	 */
	public void asignarPasajeroAVuelo(String idVuelo, Viajero viajero) {
		if (viajero == null) {
			throw new IllegalArgumentException("El viajero no puede ser null.");
		}
		Vuelo vuelo = getVuelo(idVuelo);
		vuelo.addViajero(viajero);
		if (!viajero.tieneTarjetaEmbarque()) {
			viajero.asignarTarjetaEmbarque(emitirNuevaTarjetaEmbarque());
		}
	}

	/**
	 * Realiza el embarque de todos los pasajeros de un vuelo con las tarjetas proporcionadas.
	 * 
	 * @param idVuelo código del vuelo a embarcar
	 * @param tarjetas array de tarjetas de embarque correspondientes a los pasajeros
	 * @throws IllegalArgumentException cuando algún argumento es null, el vuelo no existe,
	 * la longitud de tarjetas no coincide con el número de pasajeros o alguna tarjeta es null
	 * @throws IllegalStateException cuando el vuelo está cancelado, ya ha despegado
	 * o algún pasajero ya había embarcado
	 */
	public void realizarEmbarque(String idVuelo, TarjetaEmbarque[] tarjetas) {
		if (tarjetas == null) {
			throw new IllegalArgumentException("El array de tarjetas no puede ser null.");
		}
		Vuelo vuelo = getVuelo(idVuelo);
		if (vuelo.isCancelado()) {
			throw new IllegalStateException("No se puede realizar el embarque de un vuelo cancelado.");
		}
		if (vuelo.isDespegado()) {
			throw new IllegalStateException("El vuelo ya ha despegado.");
		}
		Viajero[] pasajeros = vuelo.getViajeros();
		if (pasajeros.length != tarjetas.length) {
			throw new IllegalArgumentException("Número de tarjetas no coincide con número de pasajeros.");
		}
		for (int i = 0; i < tarjetas.length; i++) {
			if (tarjetas[i] == null) {
				throw new IllegalArgumentException("Ninguna tarjeta de embarque puede ser null.");
			}
		}
		for (int i = 0; i < pasajeros.length; i++) {
			pasajeros[i].embarcar(tarjetas[i], CREDENCIAL_AUTORIZAR_EMBARQUE);
		}
	}

	/**
	 * Realiza el embarque de todos los pasajeros asignados a un vuelo autorizando sus tarjetas.
	 * 
	 * @param idVuelo código del vuelo a embarcar
	 * @throws IllegalArgumentException cuando idVuelo == null o el vuelo no existe
	 * @throws IllegalStateException cuando el vuelo está cancelado, ya ha despegado
	 * o algún pasajero ya había embarcado
	 */
	public void realizarEmbarque(String idVuelo) {
		Vuelo vuelo = getVuelo(idVuelo);
		if (vuelo.isCancelado()) {
			throw new IllegalStateException("No se puede realizar el embarque de un vuelo cancelado.");
		}
		if (vuelo.isDespegado()) {
			throw new IllegalStateException("El vuelo ya ha despegado.");
		}
		Viajero[] pasajeros = vuelo.getViajeros();
		for (int i = 0; i < pasajeros.length; i++) {
			if (!pasajeros[i].tieneTarjetaEmbarque()) {
				pasajeros[i].asignarTarjetaEmbarque(emitirNuevaTarjetaEmbarque());
			}
			pasajeros[i].embarcar(CREDENCIAL_AUTORIZAR_EMBARQUE);
		}
	}

	/**
	 * Consulta todos los vuelos operativos del aeropuerto.
	 * 
	 * @return array con los vuelos operativos
	 */
	public Vuelo[] obtenerVuelosOperativos() {
		int contador = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isOperativo()) {
				contador++;
			}
		}
		Vuelo[] operativos = new Vuelo[contador];
		int indice = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isOperativo()) {
				operativos[indice] = this.vuelos[i];
				indice++;
			}
		}
		return operativos;
	}

	/**
	 * Consulta todos los vuelos que se encuentran con retraso respecto a la hora indicada.
	 * 
	 * @param horaActual fecha y hora de referencia
	 * @return array con los vuelos retrasados
	 * @throws IllegalArgumentException cuando horaActual == null
	 */
	public Vuelo[] obtenerVuelosRetrasados(LocalDateTime horaActual) {
		if (horaActual == null) {
			throw new IllegalArgumentException("La hora actual no puede ser null.");
		}
		int contador = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isRetrasado(horaActual)) {
				contador++;
			}
		}
		Vuelo[] retrasados = new Vuelo[contador];
		int indice = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isRetrasado(horaActual)) {
				retrasados[indice] = this.vuelos[i];
				indice++;
			}
		}
		return retrasados;
	}

	/**
	 * Consulta todos los vuelos cancelados del aeropuerto.
	 * 
	 * @return array con los vuelos cancelados
	 */
	public Vuelo[] obtenerVuelosCancelados() {
		int contador = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isCancelado()) {
				contador++;
			}
		}
		Vuelo[] cancelados = new Vuelo[contador];
		int indice = 0;
		for (int i = 0; i < this.numVuelos; i++) {
			if (this.vuelos[i].isCancelado()) {
				cancelados[indice] = this.vuelos[i];
				indice++;
			}
		}
		return cancelados;
	}

	/**
	 * Añade una cinta de recogida de equipajes al aeropuerto.
	 * 
	 * @param cinta cinta de recogida a añadir
	 * @throws IllegalArgumentException cuando cinta == null o ya existe otra con igual identificador
	 * @throws IllegalStateException cuando se ha alcanzado el límite máximo de cintas del aeropuerto
	 */
	public void addCinta(CintaRecogida cinta) {
		if (cinta == null) {
			throw new IllegalArgumentException("La cinta no puede ser null.");
		}
		if (existeElemento(this.cintas, this.numCintas, cinta.getIdentificador())) {
			throw new IllegalArgumentException("Ya existe una cinta con ese identificador.");
		}
		if (this.numCintas >= this.cintas.length) {
			throw new IllegalStateException("No hay espacio para más cintas de recogida.");
		}
		this.cintas[this.numCintas] = cinta;
		this.numCintas++;
	}

	/**
	 * Elimina una cinta de recogida del aeropuerto a partir de su identificador.
	 * 
	 * @param idCinta identificador de la cinta a eliminar
	 * @throws IllegalArgumentException cuando idCinta == null o la cinta no existe
	 * @throws IllegalStateException cuando la cinta tiene equipaje sin recoger o está asignada a un vuelo operativo
	 */
	public void eliminarCinta(String idCinta) {
		int posicion = buscarPosicion(this.cintas, this.numCintas, idCinta, "Cinta no encontrada.");
		CintaRecogida cinta = this.cintas[posicion];
		if (cinta.tieneEquipajePendiente()) {
			throw new IllegalStateException("No se puede eliminar una cinta con equipaje pendiente de recoger.");
		}
		if (estaCintaAsignadaAVueloActivo(cinta, null)) {
			throw new IllegalStateException("No se puede eliminar una cinta asignada a un vuelo operativo.");
		}
		eliminarDeArray(this.cintas, this.numCintas, idCinta, "Cinta no encontrada.");
		this.numCintas--;
	}

	/**
	 * Consulta una cinta de recogida del aeropuerto a partir de su identificador.
	 * 
	 * @param idCinta identificador de la cinta buscada
	 * @return cinta de recogida correspondiente
	 * @throws IllegalArgumentException cuando idCinta == null o la cinta no existe
	 */
	public CintaRecogida getCinta(String idCinta) {
		int posicion = buscarPosicion(this.cintas, this.numCintas, idCinta, "Cinta no encontrada.");
		return this.cintas[posicion];
	}

	/**
	 * Asigna una cinta de recogida del aeropuerto a un vuelo registrado.
	 * 
	 * @param idVuelo código del vuelo
	 * @param cinta cinta de recogida a asignar
	 * @throws IllegalArgumentException cuando algún argumento es null, el vuelo no existe
	 * o la cinta no pertenece al aeropuerto
	 * @throws IllegalStateException cuando el vuelo está cancelado, la cinta no está operativa
	 * o ya está asignada a otro vuelo operativo
	 */
	public void asignarCintaAVuelo(String idVuelo, CintaRecogida cinta) {
		if (cinta == null) {
			throw new IllegalArgumentException("La cinta no puede ser null.");
		}
		Vuelo vuelo = getVuelo(idVuelo);
		buscarPosicion(this.cintas, this.numCintas, cinta.getIdentificador(), "La cinta no pertenece al aeropuerto.");
		if (estaCintaAsignadaAVueloActivo(cinta, vuelo)) {
			throw new IllegalStateException("La cinta ya está asignada a otro vuelo operativo.");
		}
		vuelo.asignarCinta(cinta);
	}

	private boolean estaCintaAsignadaAVueloActivo(CintaRecogida cinta, Vuelo vueloExcluido) {
		for (int i = 0; i < this.numVuelos; i++) {
			Vuelo actual = this.vuelos[i];
			if (actual != vueloExcluido && actual.isOperativo() && actual.tieneCintaAsignada()
					&& actual.getCintaAsignada().equals(cinta)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Descarga el equipaje de un vuelo en la cinta de recogida que tiene asignada.
	 * 
	 * @param idVuelo código del vuelo cuyo equipaje se descarga
	 * @throws IllegalArgumentException cuando idVuelo == null o el vuelo no existe
	 * @throws IllegalStateException cuando el vuelo no puede descargar su equipaje en la cinta
	 */
	public void descargarEquipajeDeVuelo(String idVuelo) {
		Vuelo vuelo = getVuelo(idVuelo);
		vuelo.descargarEquipajeEnCinta();
	}

	/**
	 * Consulta las cintas de recogida del aeropuerto que se encuentran operativas.
	 * 
	 * @return array con las cintas operativas
	 */
	public CintaRecogida[] consultarCintasOperativas() {
		int contador = 0;
		for (int i = 0; i < this.numCintas; i++) {
			if (this.cintas[i].isOperativa()) {
				contador++;
			}
		}
		CintaRecogida[] cintasOperativas = new CintaRecogida[contador];
		int indice = 0;
		for (int i = 0; i < this.numCintas; i++) {
			if (this.cintas[i].isOperativa()) {
				cintasOperativas[indice] = this.cintas[i];
				indice++;
			}
		}
		return cintasOperativas;
	}

	/**
	 * Consulta las cintas de recogida donde queda algún equipaje pendiente de recoger.
	 * 
	 * @return array con las cintas que contienen equipaje sin recoger
	 */
	public CintaRecogida[] consultarCintasConEquipajePendiente() {
		int contador = 0;
		for (int i = 0; i < this.numCintas; i++) {
			if (this.cintas[i].tieneEquipajePendiente()) {
				contador++;
			}
		}
		CintaRecogida[] pendientes = new CintaRecogida[contador];
		int indice = 0;
		for (int i = 0; i < this.numCintas; i++) {
			if (this.cintas[i].tieneEquipajePendiente()) {
				pendientes[indice] = this.cintas[i];
				indice++;
			}
		}
		return pendientes;
	}

	/**
	 * Consulta todas las cintas de recogida registradas en el aeropuerto.
	 * 
	 * @return array con las cintas del aeropuerto
	 */
	public CintaRecogida[] getCintas() {
		CintaRecogida[] cintasCopia = new CintaRecogida[this.numCintas];
		for (int i = 0; i < this.numCintas; i++) {
			cintasCopia[i] = this.cintas[i];
		}
		return cintasCopia;
	}

	private boolean existeElemento(Identificable[] array, int numElementos, String idBuscado) {
		for (int i = 0; i < numElementos; i++) {
			if (array[i].getIdentificador().equals(idBuscado)) {
				return true;
			}
		}
		return false;
	}

	private void eliminarDeArray(Identificable[] array, int numElementos, String idBuscado, String mensajeError) {
		int posicion = buscarPosicion(array, numElementos, idBuscado, mensajeError);
		for (int i = posicion; i < numElementos - 1; i++) {
			array[i] = array[i + 1];
		}
		array[numElementos - 1] = null;
	}

	private int buscarPosicion(Identificable[] array, int numElementos, String idBuscado, String mensajeError) {
		if (idBuscado == null) {
			throw new IllegalArgumentException("El identificador buscado no puede ser null.");
		}
		for (int i = 0; i < numElementos; i++) {
			if (array[i].getIdentificador().equals(idBuscado)) {
				return i;
			}
		}
		throw new IllegalArgumentException(mensajeError);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Aeropuerto otro = (Aeropuerto) obj;
		return this.codigoIATA.equals(otro.codigoIATA);
	}

	@Override
	public int hashCode() {
		return 31 + this.codigoIATA.hashCode();
	}

	@Override
	public String toString() {
		return "Aeropuerto [nombre=" + this.nombre + ", codigoIATA=" + this.codigoIATA
				+ ", pais=" + this.pais + ", vuelos=" + this.numVuelos + ", cintas=" + this.numCintas + "]";
	}
}