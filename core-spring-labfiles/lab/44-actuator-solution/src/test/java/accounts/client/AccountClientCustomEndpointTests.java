package accounts.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wavefront.spring.actuate.WavefrontEndpointAutoConfiguration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {WavefrontEndpointAutoConfiguration.class})
public class AccountClientCustomEndpointTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void restaurant_custom_endpoint_returns_valid_data() {
        ResponseEntity<String> responseEntity
                = restTemplate.withBasicAuth("actuator", "actuator")
                              .getForEntity("/actuator/restaurant", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("restaurant.location").contains("New York");
    }

}