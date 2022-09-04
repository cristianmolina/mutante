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
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnDistintasHorizontales() {
		assertFalse(mutanteAdnService.isMutant(List.of("AAA", "TTT", "CCC")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAG", "TTTA", "CCCA", "GGGT")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.isMutant(List.of("AAAA", "TGCT", "CGGT", "TCGA")), "solo 1 sec, no es mutante.");
		assertTrue(mutanteAdnService.isMutant(List.of("AAAA", "TTTT", "CGGT", "TCGA")), "minimo 2 sec, Es Mutante.");
		assertTrue(mutanteAdnService.isMutant(List.of("TCAG", "TAAT", "CCCC", "GGGG")), "minimo 2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnDistintasVerticales() {
		assertFalse(mutanteAdnService.isMutant(List.of("ATG", "ATG", "ATG")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.isMutant(List.of("ATGA", "ATGA", "ATGA", "GGAG")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.isMutant(List.of("ATGC", "ATGC", "ATGC", "AGCA")), "solo 1 sec, no es mutante.");
		assertTrue(mutanteAdnService.isMutant(List.of("ATGC", "ATGC", "ATGC", "ATCG")), "minimo 2 sec, Es Mutante.");
		assertTrue(mutanteAdnService.isMutant(List.of("ATCG", "ATGC", "ATGC", "ATGC")), "minimo 2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnVerticalYHorizontal() {
		List<String> adn = List.of(
				"TTGCGA",
				"CAGTGC",
				"TTATGT",
				"AGATGG",
				"CCCCTA",
				"TCACTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnVerticalYOblicua() {
		List<String> adn = List.of(
				"ATGCGA",
				"CAGTGC",
				"TTATGT",
				"AGAAGG",
				"CCTCTA",
				"TCACTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarMinimo2SecuenciaDistintasLineasOblicuasFlujoIzquierdaDerechaBajando() {
		List<String> adn = List.of(
				"ATGCGA",
				"CAGTGC",
				"GTATAT",
				"AGAAGG",
				"CCGCTA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGCGA",
				"CATTGC",
				"GTATAT",
				"ATAATG",
				"CCTCTA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGCGA",
				"CGTTGC",
				"GTATAT",
				"AGAATG",
				"CCGCTA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarMinimo2SecuenciaDistintasLineasOblicuasFlujoIzquierdaDerechaSubiendo() {
		
		List<String> adn = List.of(
				"ATGTGA",
				"CGTTAC",
				"GTAAAT",
				"TGAATG",
				"CCTCTA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTAA",
				"CGTAGC",
				"GTAGAA",
				"TAAGGG",
				"CCTATA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGTAGC",
				"GTAGAA",
				"TAAGGG",
				"ACTATA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGTAGC",
				"GTGGAA",
				"TAAGAG",
				"ACTATA",
				"TCAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGGAGC",
				"GTGGAA",
				"TAATAG",
				"ACTAGA",
				"TCAGTG");
		assertFalse(mutanteAdnService.isMutant(adn), "1 sec, No Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGGAGC",
				"GTGGTA",
				"TAATAG",
				"ACTAGA",
				"TTAGTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGAA",
				"CATAGCC",
				"GTGGTAG",
				"TAACAGC",
				"ACTTGAA",
				"TTAGTGC",
				"TTGGTGC");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnHorizontalYOblicua() {
		List<String> adn = List.of(
				"ATGCGA",
				"CAGTGC",
				"TTATAT",
				"AAAAGG",
				"CCTCTA",
				"TCACTG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	
	@Test
	void debeValidar2SecuenciaCon4LetrasIgualesEnMismaHorizontal() {
		List<String> adn = List.of(
				"AAAAGGGG",
				"ATGACTAT",
				"TTCCCTAT",
				"AAGCGCTG",
				"ATCAGAAG",
				"CTGACTTT",
				"AACCCTAG",
				"ATGCGAAG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
	}
	

	@Test
	void debeValidar2SecuenciaCon4LetrasIgualesEnMismaVertical() {
		List<String> adn = List.of(
				"ATGACTAT",
				"ATGACTAT",
				"ATCCCTAT",
				"ATGCGCTG",
				"TGCAGAAG",
				"CTGACTTT",
				"AACCCTAG",
				"ATGCGAAG");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGACTAT",
				"AGGACTAT",
				"ATCCCTAT",
				"ATGCGCTG",
				"TGCAGAAG",
				"CTGACTTG",
				"AACCCTAG",
				"ATGCGAAT");
		assertTrue(mutanteAdnService.isMutant(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGACTAT",
				"CGGACTAT",
				"ATCCCTAT",
				"ATGCGCTT",
				"TGCCGAAC",
				"CTGCCTTG",
				"AACCCTAG",
				"ATGCGAAT");
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
				"AAGCGCTG",
				"ATCAGAAG",
				"CTGACTTT",
				"AACCCTAG",
				"ATGCGAAG");
		assertFalse(mutanteAdnService.isMutant(adn), "1 sec, NO es mutante.");
	}
	
	@Test
	void debeValidarSoloUnaSecuenciaCon8LetrasIgualesEnMismaVertical() {
		// 8 letras iguales cuentan como una secuencia, por lo tanto de retornar falso
		// porque el minimo es de dos secuencias.
		List<String> adn = List.of(
				"ATGACTAT",
				"ATGACTAT",
				"ATCCCTAT",
				"AAGCGCTG",
				"ATCAGAAG",
				"ATGACTTT",
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
	
	@Test
	void debeValidarDosSecuenciaCon8LetrasIgualesEnMismaVertical() {
		List<String> adn2 = List.of(
				"ATGACTAT",
				"AGGACTAT",
				"ATCCCTAT",
				"AGGCGTAG",
				"ATCAGTAG",
				"ATGAATAT",
				"AACTCTAG",
				"AACTCTAG");
		assertTrue(mutanteAdnService.isMutant(adn2), "2 sec, Es Mutante.");
		
	}
}
