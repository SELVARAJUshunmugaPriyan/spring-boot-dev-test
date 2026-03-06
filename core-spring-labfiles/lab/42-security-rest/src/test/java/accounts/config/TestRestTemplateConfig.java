package accounts.config;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestConfiguration
public class TestRestTemplateConfig {
	
	private static final Logger log = LoggerFactory.getLogger(TestRestTemplateConfig.class);

	@Bean
	RestTemplate testRestTemplate() {
		
		final String baseUrl 	  = "http://localhost:8081";
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
		
		
		// Adding Interceptors
		restTemplate.getInterceptors().add((request, body, executor) -> {
			log.info("HTTP request: {} {}", request.getMethod(), request.getURI());
			return executor.execute(request, body);
		});
		
		// Overriding default error-handlers with silent handlers for testing
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(
					URI url,
					HttpMethod method,
					ClientHttpResponse response) throws IOException {
				
				String body = StreamUtils.copyToString(
						response.getBody(), 
						StandardCharsets.UTF_8);
				
				log.error(
						"""
						HTTP request failed
						Method      : {}
						URL         : {}
						Status Code : {}
						Status Text : {}
						Response    : {}
						""",
						method,
						url,
						response.getStatusCode(),
						response.getStatusText(),
						body
				);
			}
		});
		
		return restTemplate;
	}
	
}
