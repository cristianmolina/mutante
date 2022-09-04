package co.com.molina.mutante.infraestructura.repositorio.redis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;


/**
 * Entidad que representa un registro en redis de estadisticas ADN verificados por la api.
 * 
 * @author Cristian Molina.
 *
 */
public class StatsAdnData implements Serializable {

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
	 * Numero de ADN mutantes encontrados.
	 */
	private long cantidadMutantes = 0;

	/**
	 * Numero de ADN mutantes encontrados.
	 */
	private long cantidadHumanos = 0;
	
	/**
	 * Numero de ADN mutantes encontrados.
	 */
	private double porcentajeMutantes = 0;
	
	/**
	 * Fecha y hora de creación del registro.
	 */
	private Date fechaCreacion;

	/**
	 * constructor por defecto.
	 */
	public StatsAdnData() {
		super();
	}
	
	/**
	 * constructor por defecto.
	 */
	public StatsAdnData(String id) {
		super();
		this.id = id;
		this.fechaCreacion = new Date();
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

	public long getCantidadMutantes() {
		return cantidadMutantes;
	}

	public void setCantidadMutantes(long cantidadMutantes) {
		this.cantidadMutantes = cantidadMutantes;
	}

	public long getCantidadHumanos() {
		return cantidadHumanos;
	}

	public void setCantidadHumanos(long cantidadHumanos) {
		this.cantidadHumanos = cantidadHumanos;
	}

	public double getPorcentajeMutantes() {
		return porcentajeMutantes;
	}

	public void setPorcentajeMutantes(double porcentajeMutantes) {
		this.porcentajeMutantes = porcentajeMutantes;
	}
	
	
}
