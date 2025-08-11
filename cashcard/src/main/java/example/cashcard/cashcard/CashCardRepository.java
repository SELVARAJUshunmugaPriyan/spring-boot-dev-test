package example.cashcard.cashcard;

import org.springframework.data.repository.CrudRepository;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    Iterable<CashCard> findByOwner(String owner);

    default Iterable<CashCard> findAll() {
        throw new UnsupportedOperationException("unsupported, please use findByOwner instead");
    }
}
