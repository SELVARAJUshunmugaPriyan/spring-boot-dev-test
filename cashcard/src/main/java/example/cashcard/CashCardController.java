package example.cashcard;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    @Autowired
    CashCardRepository cashCardRepository;

    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) throws IOException {
        CashCard cashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        
        if (cashCard != null)
            return ResponseEntity.ok(cashCard);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest,
        UriComponentsBuilder ucb, Principal principal) {
            CashCard savedCashCard = cashCardRepository.save(new CashCard(
                null, newCashCardRequest.amount(), principal.getName()));
            URI locationOfNewCashCard = ucb.path("/cashcards/{id}")
                                           .buildAndExpand(savedCashCard.id())
                                           .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping()
    public ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(
            principal.getName(),
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
            )
        ); 
        return ResponseEntity.ok(page.getContent());
    }
    
    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate,
        Principal principal) {
            CashCard cashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
            if (cashCard != null) {
                CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
                cashCardRepository.save(updatedCashCard);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashCard(@PathVariable Long id, Principal principal) {
        if (cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
            cashCardRepository.deleteById(id); 
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
