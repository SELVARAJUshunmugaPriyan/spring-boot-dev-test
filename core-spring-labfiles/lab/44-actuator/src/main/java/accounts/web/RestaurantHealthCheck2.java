package accounts.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.AbstractHealthIndicator;
import org.springframework.stereotype.Component;

import rewards.internal.restaurant.RestaurantRepository;

@Component
public class RestaurantHealthCheck2 extends AbstractHealthIndicator {

	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		final Long restaurantCount = restaurantRepository.getRestaurantCount();
		if (restaurantCount == 0)
			builder.up()
				   .withDetail("restaurantCount", restaurantCount);
		else
			builder.status("NO_RESTAURANTS");
	}

}