package co.com.molina.mutante.infraestructura.repositorio.redis;

import java.util.Date;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

/**
 * Gestiona los estadisticas ADN por medio la cache de redis.
 * 
 * @author Cristian Molina
 *
 */
//@Service
public class StatsAdnDataRepositorio {

	private static final String ID_STATS = "STATS";
	private final ReactiveRedisOperations<String, StatsAdnData> statsAdnDataOps;

	/**
	 * Constructor por defecto.
	 */
	public StatsAdnDataRepositorio(ReactiveRedisOperations<String, StatsAdnData> statsAdnDataOperations) {
		super();
		this.statsAdnDataOps = statsAdnDataOperations;
	}
	
	/**
	 * Crear un STAT en la cache de redis.
	 * 
	 * @param adn .
	 * @return
	 */
	public StatsAdnData registrarStats(AdnData adn) {
		StatsAdnData statsData = this.buscarStatsEnCache();
		if (statsData == null) {
			statsData = new StatsAdnData(ID_STATS);
		}

		if (adn.isMutante()) {
			statsData.setCantidadMutantes(statsData.getCantidadMutantes() + 1);
		}

		statsData.setCantidadHumanos(statsData.getCantidadHumanos() + 1);
		statsData.setFechaCreacion(new Date());
		statsData.setPorcentajeMutantes(statsData.getCantidadMutantes() / statsData.getCantidadHumanos());

		statsAdnDataOps.opsForValue().set(statsData.getId(), statsData).block();
		return statsData;
	}

	/**
	 * Busca un STATS en la cache.
	 * 
	 * @param id .
	 * @return
	 */
	public StatsAdnData buscarStatsEnCache() {
		return statsAdnDataOps.opsForValue().get(ID_STATS).block();
	}
	
	public void borrarDatos() {
		statsAdnDataOps.opsForList().delete(ID_STATS).block();
	}


}
