package accounts.client;

//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
//import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import accounts.config.TestRestTemplateConfig;

import static org.assertj.core.api.Assertions.*;

// TO-DO-24: Run the tests checking security configuration for the actuator endpoints
// - Take some time to understand what each test is for
// - Remove @Disabled annotation from each test and run it
// - Make sure all tests pass
@SpringBootTest(
		classes		   = TestRestTemplateConfig.class,
		webEnvironment = WebEnvironment.DEFINED_PORT,
		properties	   = "server.port=8081"
)
public class AccountClientSecurityTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
//    @Disabled
    public void any_user_can_access_health_endpoint() {
        ResponseEntity<String> responseEntity
                = restTemplate.getForEntity("/actuator/health", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
//    @Disabled
    public void any_user_can_access_info_endpoint() {
        ResponseEntity<String> responseEntity
                = restTemplate.getForEntity("/actuator/info", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
//    @Disabled
    public void any_user_cannot_access_conditions_endpoint() {
    	String url = "/actuator/conditions";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("anyuser", "anyuser");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.GET,
			                		entity,
			                		String.class
                			  );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
//    @Disabled
    public void user_in_ADMIN_role_can_access_conditions_endpoint() {
    	String url = "/actuator/conditions";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.GET,
			                		entity,
			                		String.class
                			  );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
//    @Disabled
    public void user_in_ACTUATOR_role_cannot_access_conditions_endpoint() {
    	String url = "/actuator/conditions";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("actuator", "actuator");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.GET,
			                		entity,
			                		String.class
                			  );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}