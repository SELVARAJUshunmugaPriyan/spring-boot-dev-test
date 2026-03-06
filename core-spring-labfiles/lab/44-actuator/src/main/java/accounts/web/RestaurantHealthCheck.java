package accounts.web;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import rewards.internal.restaurant.RestaurantRepository;

/**
 * TO-DO-16a: Create custom health indicator
 * - Make this class implement HealthIndicator interface
 * - Make this class a component
 * - Inject RestaurantRepository through constructor injection
 * - health() method should return DOWN if the repository is empty
 *   (no restaurants) or UP otherwise. (Note that RestaurantRepository
 *   has a method that returns number of restaurants.)
 */
@Component
public class RestaurantHealthCheck implements HealthIndicator {

	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Override
	public @Nullable Health health() {
		final Long restaurantCount = restaurantRepository.getRestaurantCount();
		return restaurantCount == 0 
			   ? Health.status("NO_RESTAURANTS").build() 
			   : Health.up()
			   		   .withDetail("restaurantCount", restaurantCount)
			   		   .build();
	}

}


/**
 * TO-DO-25 (Optional): Experiment with HealthIndicator above
 * - Change "spring.sql.init.data-locations" property in the
 *   "application.properties" file back to use "data-no-restaurants.sql"
 * - Include the restaurant count as extra detail when DOWN state.
 * - Instead of returning DOWN when there are no restaurants,
 *   define and use a custom status called NO_RESTAURANTS.
 * - When there are no restaurants in the DB, what top-level status
 *   is returned for the "application" health group?
 * - Set "management.endpoint.health.group.application.status.order" property
 *   in the "application.properties" file so that NO_RESTAURANTS
 *   gets displayed as top-level status for the "application" health group.
 * - Restart the application and verify the result.
 */