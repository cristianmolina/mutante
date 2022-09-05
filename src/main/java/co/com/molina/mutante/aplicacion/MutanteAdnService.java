package co.com.molina.mutante.aplicacion;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Service;

import co.com.molina.mutante.dominio.modelos.CodigoValidacionMutante;
import co.com.molina.mutante.dominio.modelos.MutanteRuntimeException;
import co.com.molina.mutante.infraestructura.repositorio.redis.AdnData;
import co.com.molina.mutante.infraestructura.repositorio.redis.AdnDataRepositorio;
import co.com.molina.mutante.infraestructura.repositorio.redis.StatsAdnData;
import co.com.molina.mutante.infraestructura.repositorio.redis.StatsAdnDataRepositorio;

/**
 *  Determina si el adn tiene una estructura correcta y la guarda en base de datos.
 *  Y si es valida indica si es un mutante.
 * 
 * @author Cristian Molina
 */
@Service
public class MutanteAdnService {
	private static final int MINIMO_SECUENCIA_MUTANTE = 1;
	private static final int MINIMO_LETRAS_CONSECUTIVAS = 4;
	private static final Pattern PATRON_LETRAS_PERMITIDAS = Pattern.compile("^[ATCG]+$");
	private static final Pattern PATRON_SECUENCIA_LETRAS = Pattern
			.compile("([A]{4,9999})|([T]{4,9999})|([C]{4,9999})|([G]{4,9999})");

	@Autowired
	private AdnDataRepositorio adnDataRepositorio;
	@Autowired
	private StatsAdnDataRepositorio statsRepositorio;
	
	/**
	 * Determina si el adn tiene una estructura correcta y la guarda en base de datos.
	 * Y si es valida indica si es un mutante.
	 * 
	 * @param adn .
	 * @return
	 */
	public boolean procesar(final List<String> adn) {
		if (!this.validarAdnHumanoEstructuraCorrecta(adn)) {
			return false;
		}
		
		boolean esMutante = this.isMutant(adn);
		AdnData adnData = adnDataRepositorio.registrarAdn(adn, esMutante);
		statsRepositorio.registrarStats(adnData);
		
		return esMutante;
	}
	
	/**
	 * Determina si el adn es mutante.
	 * 
	 * @param adn .
	 * @return
	 */
	private boolean isMutant(final List<String> adn) {
		if (adn == null) {
			return false;
		}
		
		int n = adn.size();

		try {
			Long suma = cantidadSecuenciasMutantesHorizontal(adn, n, 0L);
			suma = cantidadSecuenciasMutantesVerticales(adn, n, suma);
			suma = cantidadSecuenciasMutantesOblicuas(adn, n, suma);
			return suma > MINIMO_SECUENCIA_MUTANTE;
		} catch (MutanteRuntimeException e) {
			Throwable mostSpecificCause = NestedExceptionUtils.getMostSpecificCause(e);
			if (mostSpecificCause instanceof MutanteRuntimeException validacionException) {
				return CodigoValidacionMutante.MUTANTE_ENCONTRADO.equals(validacionException.getCodigoValidacion());
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Indica si el adn tiene una estructuraCorrecta
	 * @param adn .
	 * @return .
	 */
	private boolean validarAdnHumanoEstructuraCorrecta(final List<String> adn) {
		if (adn == null) {
			return false;
		}
		int n = adn.size();
		
		boolean adnNoValido = adn.parallelStream()
				.anyMatch(cadena -> !PATRON_LETRAS_PERMITIDAS.matcher(cadena).matches() || cadena.length() != n);
		
		return !adnNoValido;
	}

	/**
	 * Busca la secuencias repetidas de letras de un mutante en las horizontales de
	 * la matriz.
	 * 
	 * @param adn         .
	 * @param n           longitud NxN de la matriz.
	 * @param sumaParcial suma parcial de secuencias encontradas anteriormente.
	 * @return suma de secuencias encontradas en horizontales.
	 */
	private Long cantidadSecuenciasMutantesHorizontal(List<String> adn, int n, long sumaParcial) {
		return cantidadSecuenciasMutantesHorizontal(adn.parallelStream(), n, sumaParcial);
	}

	/**
	 * Busca la secuencias repetidas de letras de un mutante en las horizontales de
	 * la matriz.
	 * 
	 * @param adnStream   .
	 * @param n           longitud NxN de la matriz.
	 * @param sumaParcialGeneral suma parcial de secuencias encontradas anteriormente.
	 * @return suma de secuencias encontradas en horizontales.
	 */
	private Long cantidadSecuenciasMutantesHorizontal(final Stream<String> adnStream, final int n, final Long sumaParcialGeneral) {
		return adnStream.reduce(0L, (sumaParcialStream, cadena) -> {
			long suma = sumaParcialStream + PATRON_SECUENCIA_LETRAS.matcher(cadena).results().count();
			if (suma + sumaParcialGeneral > MINIMO_SECUENCIA_MUTANTE) {
				throw new MutanteRuntimeException(CodigoValidacionMutante.MUTANTE_ENCONTRADO);
			}
			return suma;
		}, Long::sum) + sumaParcialGeneral;
	}

	/**
	 * Busca la secuencias repetidas de letras de un mutante en las verticales de la
	 * matriz.
	 * 
	 * @param adn         .
	 * @param n           longitud NxN de la matriz.
	 * @param sumaParcial suma parcial de secuencias encontradas anteriormente.
	 * @return suma de secuencias encontradas en verticales.
	 */
	private Long cantidadSecuenciasMutantesVerticales(final List<String> adn, int n, Long sumaParcial) {
		final Stream<String> convertirVerticalesEnHorizontalesStream = IntStream.range(0, n)
				.mapToObj(i -> adn.parallelStream().map(c -> String.valueOf(c.charAt(i))).collect(Collectors.joining()))
				.parallel();
		return cantidadSecuenciasMutantesHorizontal(convertirVerticalesEnHorizontalesStream, n, sumaParcial);
	}

	/**
	 * Busca la secuencias repetidas de letras de un mutante en las lineas oblicuas
	 * de la matriz.
	 * 
	 * @param adn         .
	 * @param n           longitud NxN de la matriz.
	 * @param sumaParcial suma parcial de secuencias encontradas anteriormente.
	 * @return suma de secuencias encontradas en lineas oblicuas.
	 */
	private Long cantidadSecuenciasMutantesOblicuas(final List<String> adn, int n, Long sumaParcial) {
		final Stream<String> convertirOblicuasEnHorizontalesStreamSentido1 = IntStream
				.range(0, n - MINIMO_LETRAS_CONSECUTIVAS + 1)
				.boxed()
				.<String>mapMulti((i, consumer) -> {
					
					// SENTIDO IZQUIERDA -> DERECHA -> BAJANDO
					consumer.accept(IntStream
						.range(0, n - i)
						.mapToObj(j -> {
							char letra = adn.get(i+j).charAt(j);
							return String.valueOf(letra);
						})
						.collect(Collectors.joining()));
					
					consumer.accept(IntStream
							.range(1, n - i)
							.mapToObj(j -> {
								char letra = adn.get(i + j - 1).charAt(j);
								return String.valueOf(letra);
							})
							.collect(Collectors.joining()));
					
					// SENTIDO IZQUIERDA -> DERECHA -> SUBIENDO
					consumer.accept(IntStream
							.range(0, n-i)
							.mapToObj(j -> {
								char letra = adn.get(n - i - j - 1).charAt(j);
								return String.valueOf(letra);
							})
							.collect(Collectors.joining()));
					
					consumer.accept(IntStream
							.range(i + 1, n)
							.mapToObj(j -> {
								char letra = adn.get(n + i - j).charAt(j);
								return String.valueOf(letra);
							})
							.collect(Collectors.joining()));
				})
				.parallel();
		
		return  cantidadSecuenciasMutantesHorizontal(convertirOblicuasEnHorizontalesStreamSentido1, n, sumaParcial);
	}

	public StatsAdnData estadisticas() {
		return statsRepositorio.buscarStatsEnCache();
	}

}
