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

import rewards.internal.account.Account;
import rewards.internal.account.AccountRepository;

import common.money.Percentage;

/**
 * A dummy account repository implementation. Has a single Account "Keith and Keri Donald" with two beneficiaries
 * "Annabelle" (50% allocation) and "Corgan" (50% allocation) associated with credit card "1234123412341234".
 * 
 * Stubs facilitate unit testing. An object needing an AccountRepository can work with this stub and not have to bring
 * in expensive and/or complex dependencies such as a Database. Simple unit tests can then verify object behavior by
 * considering the state of this stub.
 */
public class StubAccountRepository implements AccountRepository {

	private Map<String, Account> accountsByCreditCard = new HashMap<String, Account>();

	public StubAccountRepository() {
		Account account = new Account("123456789", "Keith and Keri Donald");
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));
		accountsByCreditCard.put("1234123412341234", account);
	}

	public Account findByCreditCardNumber(String creditCardNumber) {
		Account account = accountsByCreditCard.get(creditCardNumber);
		if (account == null) {
			throw new ObjectRetrievalFailureException(Account.class, creditCardNumber);
		}
		return account;
	}

	public void updateBeneficiaries(Account account) {
		// nothing to do, everything is in memory
	}

	@Override
	public void flush() {

		
	}

	@Override
	public <S extends Account> S saveAndFlush(S entity) {

		return null;
	}

	@Override
	public <S extends Account> List<S> saveAllAndFlush(Iterable<S> entities) {

		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Account> entities) {

		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {

		
	}

	@Override
	public void deleteAllInBatch() {

		
	}

	@Override
	public Account getOne(Long id) {

		return null;
	}

	@Override
	public Account getById(Long id) {

		return null;
	}

	@Override
	public Account getReferenceById(Long id) {

		return null;
	}

	@Override
	public <S extends Account> List<S> findAll(Example<S> example) {

		return null;
	}

	@Override
	public <S extends Account> List<S> findAll(Example<S> example, Sort sort) {

		return null;
	}

	@Override
	public <S extends Account> List<S> saveAll(Iterable<S> entities) {

		return null;
	}

	@Override
	public List<Account> findAll() {

		return null;
	}

	@Override
	public List<Account> findAllById(Iterable<Long> ids) {

		return null;
	}

	@Override
	public <S extends Account> S save(S entity) {

		return null;
	}

	@Override
	public Optional<Account> findById(Long id) {

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
	public void delete(Account entity) {

		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {

		
	}

	@Override
	public void deleteAll(Iterable<? extends Account> entities) {

		
	}

	@Override
	public void deleteAll() {

		
	}

	@Override
	public List<Account> findAll(Sort sort) {

		return null;
	}

	@Override
	public Page<Account> findAll(Pageable pageable) {

		return null;
	}

	@Override
	public <S extends Account> Optional<S> findOne(Example<S> example) {

		return Optional.empty();
	}

	@Override
	public <S extends Account> Page<S> findAll(Example<S> example, Pageable pageable) {

		return null;
	}

	@Override
	public <S extends Account> long count(Example<S> example) {

		return 0;
	}

	@Override
	public <S extends Account> boolean exists(Example<S> example) {

		return false;
	}

	@Override
	public <S extends Account, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {

		return null;
	}
}