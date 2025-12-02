package config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import rewards.RewardNetwork;
import rewards.internal.RewardNetworkImpl;
import rewards.internal.account.AccountRepository;
import rewards.internal.account.JdbcAccountRepository;
import rewards.internal.restaurant.JdbcRestaurantRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.JdbcRewardRepository;
import rewards.internal.reward.RewardRepository;

@Configuration
public class RewardsConfig {

	private JdbcTemplate jdbcTemplate;
	
	public RewardsConfig(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Bean
	RewardNetwork rewardNetwork(){
		return new RewardNetworkImpl(
			accountRepository(), 
			restaurantRepository(), 
			rewardRepository());
	}
	
	@Bean
	AccountRepository accountRepository(){
		JdbcAccountRepository repository = new JdbcAccountRepository(jdbcTemplate);
		return repository;
	}
	
	@Bean
	RestaurantRepository restaurantRepository(){
		JdbcRestaurantRepository repository = new JdbcRestaurantRepository(jdbcTemplate);
		return repository;
	}
	
	@Bean
	RewardRepository rewardRepository(){
		JdbcRewardRepository repository = new JdbcRewardRepository(jdbcTemplate);
		return repository;
	}
	
}
