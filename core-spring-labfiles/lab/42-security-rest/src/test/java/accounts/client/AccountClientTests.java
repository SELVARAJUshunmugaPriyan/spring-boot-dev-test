package accounts.client;

import accounts.RestWsApplication;
import accounts.config.TestRestTemplateConfig;
import common.money.Percentage;
import rewards.internal.account.Account;
import rewards.internal.account.Beneficiary;

//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.Random;

// TO-DO-07a: Perform security testing against a running server
// - Take some time to understand what each test is for
// - Remove @Disabled annotation from each test and run it
// - Make sure all tests pass

@SpringBootTest(
		classes = {RestWsApplication.class, TestRestTemplateConfig.class},
        webEnvironment = WebEnvironment.DEFINED_PORT,
        properties = "server.port=8081"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountClientTests {

    @Autowired
    private RestTemplate restTemplate;
    
    private final Random random = new Random();

    @Test
//    @Disabled
    public void listAccounts_using_invalid_user_should_return_401() throws Exception {
    	String url = "/accounts";
    	HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("invalid", "invalid");
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
    public void listAccounts_using_valid_user_should_succeed() {
        String url = "/accounts";
        // we have to use Account[] instead of List<Account>, or Jackson won't know what type to unmarshal to
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "user");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Account[]> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.GET,
			                		entity,
			                		Account[].class
                			  );
        
        Account[] accounts = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accounts.length >= 21).withFailMessage("Expected 21 accounts, but found " + accounts.length).isTrue();
        assertThat(accounts[0].getName()).isEqualTo("Keith and Keri Donald");
        assertThat(accounts[0].getBeneficiaries().size()).isEqualTo(2);
        assertThat(accounts[0].getBeneficiary("Annabelle").getAllocationPercentage()).isEqualTo(Percentage.valueOf("50%"));
    }

    @Test
//    @Disabled
    public void listAccounts_using_valid_admin_should_succeed() {
        String url = "/accounts";
        // we have to use Account[] instead of List<Account>, or Jackson won't know what type to unmarshal to
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Account[]> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.GET,
			                		entity,
			                		Account[].class
                			  );
        
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Account[] accounts = responseEntity.getBody();
        assertThat(accounts.length >= 21).withFailMessage("Expected 21 accounts, but found " + accounts.length).isTrue();
        assertThat(accounts[0].getName()).isEqualTo("Keith and Keri Donald");
        assertThat(accounts[0].getBeneficiaries().size()).isEqualTo(2);
        assertThat(accounts[0].getBeneficiary("Annabelle").getAllocationPercentage()).isEqualTo(Percentage.valueOf("50%"));
    }

    @Test
//    @Disabled
    public void getAccount_using_valid_user_should_succeed() {
        String url = "/accounts/%d";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "user");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Account> responseEntity
                = restTemplate.exchange(
			                		url.formatted(0),
			                		HttpMethod.GET,
			                		entity,
			                		Account.class
                			  );
        
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Account account = responseEntity.getBody();
        assertThat(account.getName()).isEqualTo("Keith and Keri Donald");
        assertThat(account.getBeneficiaries().size()).isEqualTo(2);
        assertThat(account.getBeneficiary("Annabelle").getAllocationPercentage()).isEqualTo(Percentage.valueOf("50%"));
    }

    @Test
//    @Disabled
    public void createAccount_using_admin_should_return_201() {
        String url = "/accounts";
        // use a unique number to avoid conflicts
        String number = String.format("12345%4d", random.nextInt(10000));
        Account account = new Account(number, "John Doe");
        account.addBeneficiary("Jane Doe");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);

        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.POST,
			                		entity,
			                		Void.class
			                  );
        
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    // TO-DO-07b: Write a test that verifies that "user"/"user"
    //          is not permitted to create a new Account
    // - Use the code above as a guidance
    @Test
    public void createAccount_using_user_should_return_403() throws Exception {
    	String url = "/accounts";        
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "user");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(
			                		url,
			                		HttpMethod.POST,
			                		entity,
			                		Void.class
			                  );
        
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
//    @Disabled
    public void addAndDeleteBeneficiary_using_superadmin_should_succeed() {
        // perform both add and delete to avoid issues with side effects
        String addUrl = "/accounts/%d/beneficiaries";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("superadmin", "superadmin");
        HttpEntity<String> entity = new HttpEntity<>("David", headers);

        URI newBeneficiaryLocation
                = restTemplate.exchange(
	                		  		addUrl.formatted(1),
	                		  		HttpMethod.POST,
	                		  		entity,
	                		  		Void.class)
                			  .getHeaders()
                			  .getLocation();

        Beneficiary newBeneficiary
                = restTemplate.exchange(
                					newBeneficiaryLocation,
			        		  		HttpMethod.GET,
			        		  		new HttpEntity<Void>(headers),
			        		  		Beneficiary.class
			        		  )
                			  .getBody();
        
        assertThat(newBeneficiary.getName()).isEqualTo("David");
        
        restTemplate.exchange(
							newBeneficiaryLocation,
							HttpMethod.DELETE,
							new HttpEntity<Void>(headers),
							Void.class
				    )
				    .getBody();

        // use exchange method to receive a 404 response
        ResponseEntity<Beneficiary> response =
        		restTemplate.exchange(
    					newBeneficiaryLocation,
        		  		HttpMethod.GET,
        		  		new HttpEntity<Void>(headers),
        		  		Beneficiary.class
        		);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
}
