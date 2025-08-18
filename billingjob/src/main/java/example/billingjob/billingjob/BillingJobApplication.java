package example.billingjob.billingjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingJobApplication {

	static {
		System.out.println("JVM timeZone: " + java.util.TimeZone.getDefault().getID());
	}
	public static void main(String[] args) {
		SpringApplication.run(BillingJobApplication.class, args);
	}

}
