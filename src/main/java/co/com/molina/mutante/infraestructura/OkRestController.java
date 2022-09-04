package co.com.molina.mutante.infraestructura;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ok")
public class OkRestController {


	@RequestMapping()
	public Object isMutant() {
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

}
