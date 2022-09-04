package co.com.molina.mutante.infraestructura;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.molina.mutante.aplicacion.MutanteAdnService;

@RestController
@RequestMapping("/mutant")
public class MutanteAdnRestController {

	@Autowired
	private MutanteAdnService mutanteAdnService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object isMutant(@RequestBody List<String> adn) {

		boolean esMutante = mutanteAdnService.procesar(adn);
		
		return esMutante ? ResponseEntity.status(HttpStatus.OK).body("SI_ES_MUTANTE.")
				: ResponseEntity.status(HttpStatus.FORBIDDEN).body("NO_ES_MUTANTE.");
	}

}
