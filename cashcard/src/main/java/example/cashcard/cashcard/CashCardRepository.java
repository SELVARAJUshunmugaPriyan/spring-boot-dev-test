package example.cashcard.cashcard;

// import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    Iterable<CashCard> findByOwner(String owner);

    // @Query("select * from cash_card cc where cc.owner = :#{authentication.name}")
    // Iterable<CashCard> findAll();
    // @Override
    // default Iterable<CashCard> findAll() {
        // SecurityContext context = SecurityContextHolder.getContext();
        // Authentication authentication = context.getAuthentication();
        // String owner = authentication.getName();
        // return findByOwner(owner);
    // }

    default Iterable<CashCard> findAll() {
        throw new UnsupportedOperationException("unsupported, please use findByOwner instead");
    }
}
