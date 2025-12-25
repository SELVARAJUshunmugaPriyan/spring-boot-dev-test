package rewards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class JdbcBootApplicationTests {

	private static final String QUERY = "SELECT COUNT(*) FROM t_account";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	void testNumberOfAccount() {
		long count = jdbcTemplate.queryForObject(QUERY, Long.class);
		assertEquals(count, 21L);
	}

}
