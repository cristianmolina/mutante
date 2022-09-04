package co.com.molina.mutante.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.com.molina.mutante.infraestructura.repositorio.redis.AdnData;
import co.com.molina.mutante.infraestructura.repositorio.redis.AdnDataRepositorio;
import co.com.molina.mutante.infraestructura.repositorio.redis.StatsAdnData;
import co.com.molina.mutante.infraestructura.repositorio.redis.StatsAdnDataRepositorio;

@SpringBootTest
class MutanteAdnServiceTest {
	@Autowired
	private MutanteAdnService mutanteAdnService;
	
	@Autowired
	private AdnDataRepositorio adnDataRepositorio;
	@Autowired
	private StatsAdnDataRepositorio statsRepositorio;

	@BeforeEach
	public void before() {
		adnDataRepositorio.borrarDatos();
		statsRepositorio.borrarDatos();
	}
	
	@Test
	void debeValidarLaSecuenciaNoTengaValorNull() {
		assertFalse(mutanteAdnService.procesar(null), "adn null no es mutante.");
		assertEquals(null, adnDataRepositorio.buscarUltimo());
		assertEquals(null, statsRepositorio.buscarStatsEnCache());
		
	}

	@Test
	void casoBasicoExitoso() {
		List<String> adn = List.of("AAAA", "TTTT", "CCCC", "TCGA");
		assertTrue(mutanteAdnService.procesar(adn), "Es Mutante.");

		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
	}

	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnDistintasHorizontales() {
		assertFalse(mutanteAdnService.procesar(List.of("AAA", "TTT", "CCC")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.procesar(List.of("AAAG", "TTTA", "CCCA", "GGGT")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.procesar(List.of("AAAA", "TGCT", "CGGT", "TCGA")), "solo 1 sec, no es mutante.");
		assertTrue(mutanteAdnService.procesar(List.of("AAAA", "TTTT", "CGGT", "TCGA")), "minimo 2 sec, Es Mutante.");
		List<String> adn = List.of("TCAG", "TAAT", "CCCC", "GGGG");
		assertTrue(mutanteAdnService.procesar(adn), "minimo 2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(5, stats.getCantidadHumanos());
		assertEquals(2, stats.getCantidadMutantes());
		assertEquals(2/5, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
	}
	
	@Test
	void debeValidarMinimo2SecuenciaCon4LetrasIgualesEnDistintasVerticales() {
		assertFalse(mutanteAdnService.procesar(List.of("ATG", "ATG", "ATG")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.procesar(List.of("ATGA", "ATGA", "ATGA", "GGAG")), "solo 3 letras iguales .");
		assertFalse(mutanteAdnService.procesar(List.of("ATGC", "ATGC", "ATGC", "AGCA")), "solo 1 sec, no es mutante.");
		assertTrue(mutanteAdnService.procesar(List.of("ATGC", "ATGC", "ATGC", "ATCG")), "minimo 2 sec, Es Mutante.");
		List<String> adn = List.of("ATCG", "ATGC", "ATGC", "ATGC");
		assertTrue(mutanteAdnService.procesar(adn), "minimo 2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(5, stats.getCantidadHumanos());
		assertEquals(2, stats.getCantidadMutantes());
		assertEquals(2/5, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGCGA",
				"CATTGC",
				"GTATAT",
				"ATAATG",
				"CCTCTA",
				"TCAGTG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGCGA",
				"CGTTGC",
				"GTATAT",
				"AGAATG",
				"CCGCTA",
				"TCAGTG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(3, stats.getCantidadHumanos());
		assertEquals(3, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTAA",
				"CGTAGC",
				"GTAGAA",
				"TAAGGG",
				"CCTATA",
				"TCAGTG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGTAGC",
				"GTAGAA",
				"TAAGGG",
				"ACTATA",
				"TCAGTG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGTAGC",
				"GTGGAA",
				"TAAGAG",
				"ACTATA",
				"TCAGTG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGGAGC",
				"GTGGAA",
				"TAATAG",
				"ACTAGA",
				"TCAGTG");
		assertFalse(mutanteAdnService.procesar(adn), "1 sec, No Es Mutante.");
		
		adn = List.of(
				"ATGTGA",
				"CGGAGC",
				"GTGGTA",
				"TAATAG",
				"ACTAGA",
				"TTAGTG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGTGAA",
				"CATAGCC",
				"GTGGTAG",
				"TAACAGC",
				"ACTTGAA",
				"TTAGTGC",
				"TTGGTGC");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(7, stats.getCantidadHumanos());
		assertEquals(6, stats.getCantidadMutantes());
		assertEquals(6/7, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGACTAT",
				"AGGACTAT",
				"ATCCCTAT",
				"ATGCGCTG",
				"TGCAGAAG",
				"CTGACTTG",
				"AACCCTAG",
				"ATGCGAAT");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		adn = List.of(
				"ATGACTAT",
				"CGGACTAT",
				"ATCCCTAT",
				"ATGCGCTT",
				"TGCCGAAC",
				"CTGCCTTG",
				"AACCCTAG",
				"ATGCGAAT");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(3, stats.getCantidadHumanos());
		assertEquals(3, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertFalse(mutanteAdnService.procesar(adn), "1 sec, NO es mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(0, stats.getCantidadMutantes());
		assertEquals(0, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(false, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
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
		assertFalse(mutanteAdnService.procesar(adn), "1 sec, NO es mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(0, stats.getCantidadMutantes());
		assertEquals(0, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(false, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
	}
	
	@Test
	void debeValidarDosSecuenciaCon8LetrasIgualesEnMismaHorizontal() {
		List<String> adn = List.of(
				"AAAAAAAA",
				"ATGACTAT",
				"TTCCCTAT",
				"AAGCGAAG",
				"CTCAGCAG",
				"ATGACTAT",
				"AACCCTAG",
				"GGGGGGGG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
		
	}
	
	@Test
	void debeValidarDosSecuenciaCon8LetrasIgualesEnMismaVertical() {
		List<String> adn = List.of(
				"ATGACTAT",
				"AGGACTAT",
				"ATCCCTAT",
				"AGGCGTAG",
				"ATCAGTAG",
				"ATGAATAT",
				"AACTCTAG",
				"AACTCTAG");
		assertTrue(mutanteAdnService.procesar(adn), "2 sec, Es Mutante.");
		
		StatsAdnData stats = statsRepositorio.buscarStatsEnCache();
		assertEquals(1, stats.getCantidadHumanos());
		assertEquals(1, stats.getCantidadMutantes());
		assertEquals(1, stats.getPorcentajeMutantes());
		
		AdnData saveData = adnDataRepositorio.buscarUltimo();
		assertEquals(true, saveData.isMutante());
		assertEquals(adn, saveData.getAdn());
	}
}
