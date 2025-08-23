package example.cashcard.cashcard;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
	Iterable<CashCard> findByOwner(String owner);

	@SuppressWarnings("null")
	@Query("select * from cash_card cc where cc.owner = :#{authentication.name}")
        @Override
	Iterable<CashCard> findAll();
}
