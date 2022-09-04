package co.com.molina.mutante.infraestructura.repositorio.redis;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

@Component
public class AdnDataLoader {
  private final ReactiveRedisConnectionFactory factory;
  private final ReactiveRedisOperations<String, AdnData> adnDataOps;

  public AdnDataLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, AdnData> adnDataOps) {
    this.factory = factory;
    this.adnDataOps = adnDataOps;
  }

  @PostConstruct
  public void loadData() {
	  // carga inicial de datos.
	  factory.getReactiveConnection().serverCommands().flushAll().subscribe(System.out::println);
  }
}