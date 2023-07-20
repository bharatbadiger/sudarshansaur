package co.bharat.sudarshansaur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Sudarshan Saur API", version = "0.0.1", description = "Api contract"))
public class SudarshansaurApplication {

	public static void main(String[] args) {
		SpringApplication.run(SudarshansaurApplication.class, args);
	}

}
