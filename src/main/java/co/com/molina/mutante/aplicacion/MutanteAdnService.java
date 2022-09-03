package co.com.molina.mutante.aplicacion;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import co.com.molina.mutante.MutanteRuntimeException;

/**
 * Lógica para determinar si una secuencia de ADN es mutante.
 * 
 * @author Cristian Molina
 */
@Service
public class MutanteAdnService {
	private static Pattern patronLetrasPermitidas = Pattern.compile("^[ATCG]+$");
	private static Pattern patronSecuencias = Pattern.compile("([A]{4,9999})|([T]{4,9999})|([C]{4,9999})|([G]{4,9999})");

	public boolean isMutant(List<String> adn) {
		if (adn == null) {
			return false;
		}

		int n = adn.size();
		try {
			return adn.parallelStream().mapToLong(s -> {
				boolean tamanioOk = s.length() == n;
				boolean letrasValidas = patronLetrasPermitidas.matcher(s).find();
				if (tamanioOk && letrasValidas) {
					return patronSecuencias.matcher(s).results().count();
				}
				throw new MutanteRuntimeException("ADN no valido.");
			}).sum() > 1;
		} catch (MutanteRuntimeException e) {
			return false;
		}
	}

}
