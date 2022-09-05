package co.com.molina.mutante.infraestructura.repositorio.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
public class AdnDataConfiguration {

	@Bean(name ="AdnDataRedisOperations")
	ReactiveRedisOperations<String, AdnData> usuarioDataRedisOperations(ReactiveRedisConnectionFactory factory) {
		Jackson2JsonRedisSerializer<AdnData> serializer = new Jackson2JsonRedisSerializer<>(AdnData.class);

		RedisSerializationContext.RedisSerializationContextBuilder<String, AdnData> builder = RedisSerializationContext
				.newSerializationContext(new StringRedisSerializer());

		RedisSerializationContext<String, AdnData> context = builder.value(serializer).build();

		return new ReactiveRedisTemplate<>(factory, context);
	}
}
