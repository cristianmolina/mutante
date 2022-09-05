package co.com.molina.mutante.infraestructura.repositorio.redis;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

/**
 * Gestiona los ADN por medio la cache de redis.
 * 
 * @author Cristian Molina
 *
 */
@Service
public class AdnDataRepositorio {

	private static final String LISTA_ADNS = "LISTA_ADNS";
	private final ReactiveRedisOperations<String, AdnData> sessionDataOps;

	/**
	 * Constructor por defecto.
	 */
	public AdnDataRepositorio(ReactiveRedisOperations<String, AdnData> adnDataRedisOperations) {
		super();
		this.sessionDataOps = adnDataRedisOperations;
	}

	/**
	 * Crear un ADN en la cache de redis.
	 * 
	 * @param usuario .
	 * @return
	 */
	public AdnData registrarAdn(List<String> adn, boolean mutante) {
		return registrarAdn(adn, mutante, AdnData.EXPIRACION_REGISTRO_MILISEGUNDOS);
	}
	
	/**
	 * Crear un ADN en la cache de redis.
	 * 
	 * @param usuario .
	 * @param expirationMilisegundos tiempo de expiracion en milisegundos, si viene null no se le asigna.
	 * @return
	 */
	public AdnData registrarAdn(List<String> adn, boolean mutante, Long expirationMilisegundos) {
		AdnData adnData = new AdnData(adn, mutante, expirationMilisegundos);
		sessionDataOps.opsForList().rightPush(LISTA_ADNS, adnData).block();
		return adnData;
	}
	
	public AdnData buscarUltimo() {
		return sessionDataOps.opsForList().range(LISTA_ADNS, 0, -1).blockLast();
	} 

	public void borrarDatos() {
		sessionDataOps.opsForList().delete(LISTA_ADNS).block();
	}

}
