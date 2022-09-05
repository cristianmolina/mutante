package co.com.molina.mutante.infraestructura.repositorio.redis;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

@Component
public class StatsAdnLoader {
  private final ReactiveRedisConnectionFactory factory;
  private final ReactiveRedisOperations<String, StatsAdnData> statsAdnDataOps;

  public StatsAdnLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, StatsAdnData> statsAdnDataOps) {
    this.factory = factory;
    this.statsAdnDataOps = statsAdnDataOps;
  }

  @PostConstruct
  public void loadData() {
	  // carga inicial de datos.
  }
}