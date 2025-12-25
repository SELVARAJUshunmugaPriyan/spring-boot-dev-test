package rewards;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbcBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcBootApplication.class, args);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate)
	{
		final String QUERY = "SELECT COUNT(*) FROM t_account";
		
		return args -> System.out.println("Hello, there are "
				+ jdbcTemplate.queryForObject(QUERY, Long.class)
				+ " accounts");
	}

}
