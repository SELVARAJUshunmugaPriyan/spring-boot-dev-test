package rewards.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.orm.ObjectRetrievalFailureException;

import rewards.Dining;
import rewards.internal.account.Account;
import rewards.internal.restaurant.BenefitAvailabilityPolicy;
import rewards.internal.restaurant.Restaurant;
import rewards.internal.restaurant.RestaurantRepository;

import common.money.Percentage;

/**
 * A dummy restaurant repository implementation. Has a single restaurant "Apple Bees" with a 8% benefit availability
 * percentage that's always available.
 * 
 * Stubs facilitate unit testing. An object needing a RestaurantRepository can work with this stub and not have to bring
 * in expensive and/or complex dependencies such as a Database. Simple unit tests can then verify object behavior by
 * considering the state of this stub.
 */
public class StubRestaurantRepository implements RestaurantRepository {

	private Map<String, Restaurant> restaurantsByMerchantNumber = new HashMap<String, Restaurant>();

	public StubRestaurantRepository() {
		Restaurant restaurant = new Restaurant("1234567890", "Apple Bees");
		restaurant.setBenefitPercentage(Percentage.valueOf("8%"));
		restaurant.setBenefitAvailabilityPolicy(new AlwaysReturnsTrue());
		restaurantsByMerchantNumber.put(restaurant.getNumber(), restaurant);
	}

	/**
	 * @deprecated Use {@link #findByNumber(String)} instead
	 */
	public Restaurant findByMerchantNumber(String merchantNumber) {
		return findByNumber(merchantNumber);
	}

	public Restaurant findByNumber(String merchantNumber) {
		Restaurant restaurant = (Restaurant) restaurantsByMerchantNumber.get(merchantNumber);
		if (restaurant == null) {
			throw new ObjectRetrievalFailureException(Restaurant.class, merchantNumber);
		}
		return restaurant;
	}

	/**
	 * A simple "dummy" benefit availability policy that always returns true. Only useful for testing--a real
	 * availability policy might consider many factors such as the day of week of the dining, or the account's reward
	 * history for the current month.
	 */
	private static class AlwaysReturnsTrue implements BenefitAvailabilityPolicy {
		public boolean isBenefitAvailableFor(Account account, Dining dining) {
			return true;
		}
	}

	@Override
	public void flush() {
		
		
	}

	@Override
	public <S extends Restaurant> S saveAndFlush(S entity) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> List<S> saveAllAndFlush(Iterable<S> entities) {
		
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Restaurant> entities) {
		
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		
		
	}

	@Override
	public void deleteAllInBatch() {
		
		
	}

	@Override
	public Restaurant getOne(Long id) {
		
		return null;
	}

	@Override
	public Restaurant getById(Long id) {
		
		return null;
	}

	@Override
	public Restaurant getReferenceById(Long id) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> List<S> findAll(Example<S> example) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> List<S> findAll(Example<S> example, Sort sort) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> List<S> saveAll(Iterable<S> entities) {
		
		return null;
	}

	@Override
	public List<Restaurant> findAll() {
		
		return null;
	}

	@Override
	public List<Restaurant> findAllById(Iterable<Long> ids) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> S save(S entity) {
		
		return null;
	}

	@Override
	public Optional<Restaurant> findById(Long id) {
		
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long id) {
		
		return false;
	}

	@Override
	public long count() {
		
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		
		
	}

	@Override
	public void delete(Restaurant entity) {
		
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		
		
	}

	@Override
	public void deleteAll(Iterable<? extends Restaurant> entities) {
		
		
	}

	@Override
	public void deleteAll() {
		
		
	}

	@Override
	public List<Restaurant> findAll(Sort sort) {
		
		return null;
	}

	@Override
	public Page<Restaurant> findAll(Pageable pageable) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> Optional<S> findOne(Example<S> example) {
		
		return Optional.empty();
	}

	@Override
	public <S extends Restaurant> Page<S> findAll(Example<S> example, Pageable pageable) {
		
		return null;
	}

	@Override
	public <S extends Restaurant> long count(Example<S> example) {
		
		return 0;
	}

	@Override
	public <S extends Restaurant> boolean exists(Example<S> example) {
		
		return false;
	}

	@Override
	public <S extends Restaurant, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		
		return null;
	}
}