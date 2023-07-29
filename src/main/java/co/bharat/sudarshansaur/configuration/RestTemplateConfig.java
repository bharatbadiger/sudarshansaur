package co.bharat.sudarshansaur.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	
	//@Autowired
	//private RestTemplateBuilder restTemplateBuilder;
	
	//RestTemplate restTemplate = restTemplateBuilder.build();
	@Bean
	public RestTemplate restTemplate() {
        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        int timeout = 20000; // in milliseconds
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);
        return restTemplate;
	}

}
