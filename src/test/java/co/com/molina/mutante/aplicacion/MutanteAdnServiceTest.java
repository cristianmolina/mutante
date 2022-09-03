package co.com.molina.mutante.aplicacion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MutanteAdnServiceTest {
	@Autowired
	private MutanteAdnService mutanteAdnService;

	@Test
	void debeValidarLaSecuenciaNoTengaValorNull() {
		assertFalse(mutanteAdnService.isMutant(null), "adn null no es mutante.");
	}

	@Test
	void casoBasicoExitoso() {
		assertTrue(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCC", "TCGA")), "Es Mutante.");
	}

	@Test
	void debeValidarEnLaSecuenciaSoloLetrasATCG() {
		assertFalse(mutanteAdnService.isMutant(List.of("AAAV", "TTTT", "CCCC", "GGGG")), "La letra V no es mutante.");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAa", "TTTT", "CCCC", "GGGG")), "'a' minuscula .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTt", "CCCC", "GGGG")), "'t' minuscula .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCc", "GGGG")), "'c' minuscula .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCC", "GGGg")), "'g' minuscula .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "PTTT", "CCCC", "GGGG")), "La letra P .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "*CCC", "GGGG")), "El simbolo '*' .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCC", ".GGG")), "La simbolo '.' .");

	}

	@Test
	void debeValidarEnLaSecuenciaElTamanioNxN() {
		assertFalse(mutanteAdnService.isMutant(List.of("AAA", "TTT", "CCC", "GGG")), "Tamaño 4x3 .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCC")), "Tamaño 3x4 .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCC", "GGGG", "GGGG")), "Tamaño 5x4 .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAAA", "TTTTT", "CCCCC", "GGGGG")), "Tamaño 4x5 .");

		assertTrue(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CCCC", "GGGG")), " 4x4 Es Mutante.");
	}

	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnDistintasHorizontales() {
		assertFalse(mutanteAdnService.isMutant(List.of("AAA", "TTT", "CCC")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAG", "TTTA", "CCCA", "GGGT")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TGCT", "CGGT", "TCGA")), "solo 1 sec, no es mutante.");
		assertTrue(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CGGT", "TCGA")), "minimo 2 sec, Es Mutante.");
		assertTrue(mutanteAdnService.isMutant(List.of("TCAG", "TAAT", "CCCC", "GGGG")), "minimo 2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidar2SecuenciaCon4LetrasIgualesEnMismaHorizontal() {
		List<String> adn = List.of(
				"AAAAGGGG",
				"ATGACTAT",
				"TTCCCTAT",
				"ATGCGAAG",
				"ATCAGAAG",
				"ATGACTAT",
				"ATCCCTAG",
				"ATGCGAAG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarSoloUnaSecuenciaCon8LetrasIgualesEnMismaHorizontal() {
		// 8 letras iguales cuentan como una secuencia, por lo tanto de retornar falso
		// porque el minimo es de dos secuencias.
		List<String> adn = List.of(
				"AAAAAAAA",
				"ATGACTAT",
				"TTCCCTAT",
				"AAGCGCAG",
				"ATCAGAAG",
				"CTGACTAT",
				"AACCCTAG",
				"ATGCGAAG");
		assertFalse(mutanteAdnService.isMutant(adn), "1 sec, NO es mutante.");
		
	}
	@Test
	void debeValidarDosSecuenciaCon8LetrasIgualesEnMismaHorizontal() {
		List<String> adn2 = List.of(
				"AAAAAAAA",
				"ATGACTAT",
				"TTCCCTAT",
				"AAGCGAAG",
				"CTCAGCAG",
				"ATGACTAT",
				"AACCCTAG",
				"GGGGGGGG");
		assertTrue(mutanteAdnService.isMutant(adn2), "2 sec, Es Mutante.");
		
	}
}
