package co.com.molina.mutante.infraestructura.repositorio.redis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;


/**
 * Entidad que representa un registro en redis de ADN verificados por la api.
 * 
 * @author Cristian Molina.
 *
 */
public class AdnData implements Serializable {

	public static final long EXPIRACION_REGISTRO_MILISEGUNDOS = 2 * 60 * 60 * 1000;

	/**
	 * Serial Id Generado.
	 */
	private static final long serialVersionUID = 1693842131854470422L;

	/**
	 * Id único de la sesión.
	 */
	@Id
	private String id;

	/**
	 * Tiempo en segundos de expiración del registro, este será eliminado
	 * automaticamente por redis.
	 */
	@TimeToLive
	private Long expiration;

	/**
	 * Datos ADN.
	 */
	private List<String> adn;

	/**
	 * Es Mutante.
	 */
	private boolean mutante;
	
	/**
	 * Fecha y hora de creación del registro.
	 */
	private Date fechaCreacion;

	/**
	 * constructor por defecto.
	 */
	public AdnData() {
		super();
		this.id = UUID.randomUUID().toString();
		this.fechaCreacion = new Date();
		this.expiration = EXPIRACION_REGISTRO_MILISEGUNDOS / 1000;
	}
	
	/**
	 * Constructor por defecto.
	 * 
	 * @param usuario .
	 */
	public AdnData(List<String> adn, boolean mutante) {
		super();
		this.id = UUID.randomUUID().toString();
		this.adn = adn;
		this.mutante = mutante;
		this.fechaCreacion = new Date();
		this.expiration = EXPIRACION_REGISTRO_MILISEGUNDOS / 1000;
	}
	
	/**
	 * Constructor por defecto.
	 * 
	 * @param usuario .
	 * @param expiration tiempo de expiracion.
	 */
	public AdnData(List<String> adn, boolean mutante, Long expiration) {
		super();
		this.id = UUID.randomUUID().toString();
		this.adn = adn;
		this.mutante = mutante;
		this.fechaCreacion = new Date();
		this.expiration = expiration;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public List<String> getAdn() {
		return adn;
	}

	public void setAdn(List<String> adn) {
		this.adn = adn;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public boolean isMutante() {
		return mutante;
	}

	public void setMutante(boolean mutante) {
		this.mutante = mutante;
	}
	
}
