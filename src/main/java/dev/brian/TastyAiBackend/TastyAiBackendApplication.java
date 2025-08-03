package dev.brian.TastyAiBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TastyAiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TastyAiBackendApplication.class, args);
	}
	@GetMapping("/")
	public String root(){
		return "Hello World";
	}

}
