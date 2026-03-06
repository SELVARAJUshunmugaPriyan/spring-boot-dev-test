package accounts.services;

import accounts.RestWsApplication;
import accounts.config.TestRestTemplateConfig;

//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

// TO-DO-12a: Perform method security testing with a running server
// - Take some time to understand what each test is for
// - Remove @Disabled annotation from each test and run it
// - Make sure all tests pass

@SpringBootTest(
		classes = {RestWsApplication.class, TestRestTemplateConfig.class},
		webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = "server.port=8081"
)
class AccountServiceMethodSecurityTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
//    @Disabled
    void getAuthoritiesForUser_should_return_403_for_user() {

    	String url = "/authorities?username=%s";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "user");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity
                = restTemplate.exchange(
			                		url.formatted("user"),
			                		HttpMethod.GET,
			                		entity,
			                		String.class
                			  );
        
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
//    @Disabled
    void getAuthoritiesForUser_should_return_authorities_for_admin() {

    	String url = "/authorities?username=%s";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String[] authorities
                = restTemplate.exchange(
			                		url.formatted("admin"),
			                		HttpMethod.GET,
			                		entity,
			                		String[].class
                			  )
                			  .getBody();
        
        assertThat(authorities.length).isEqualTo(3);
        assertThat(authorities.toString().contains("ROLE_ADMIN"));
        assertThat(authorities.toString().contains("ROLE_USER"));

    }

    // TO-DO-12b: Write a test that verifies that getting authorities
    //           using "/authorities?username=superadmin" with
    //           "superadmin"/"superadmin" credential should return
    //           three roles "ROLE_SUPERADMIN", "ROLE_ADMIN", and
    //           "ROLE_USER".
    @Test
    public void getAuthoritiesForUser_should_return_authorities_for_superadmin() {

    	String url = "/authorities?username=%s";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("superadmin", "superadmin");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String[] authorities
                = restTemplate.exchange(
			                		url.formatted("superadmin"),
			                		HttpMethod.GET,
			                		entity,
			                		String[].class
                			  )
                			  .getBody();
        
        assertThat(authorities.length).isEqualTo(4);
        assertThat(authorities.toString().contains("ROLE_SUPERADMIN"));
        assertThat(authorities.toString().contains("ROLE_ADMIN"));
        assertThat(authorities.toString().contains("ROLE_USER"));

    }

}