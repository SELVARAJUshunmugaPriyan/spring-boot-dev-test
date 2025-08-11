package example.cashcard.cashcard;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    @SuppressWarnings("FieldMayBeFinal")
    private CashCardRepository cashCards;

    public CashCardController(CashCardRepository cashCards) {
        this.cashCards = cashCards;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        return this.cashCards.findById(requestedId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CashCard> createCashCard(@RequestBody CashCard cashCardRequest,//newCashCardRequest, 
        UriComponentsBuilder ucb, @CurrentOwner String owner) {
            CashCard cashCard = new CashCard(cashCardRequest.amount(), owner);
            CashCard savedCashCard = this.cashCards.save(cashCard);
            URI locationOfNewCashCard = ucb
                    .path("cashcards/{id}")
                    .buildAndExpand(savedCashCard.id())
                    .toUri();
            return ResponseEntity.created(locationOfNewCashCard).body(savedCashCard);
    }

    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(/* @CurrentOwner String owner */) {
        // var result = this.cashCards.findByOwner(owner);
        // return ResponseEntity.ok(result);
        return ResponseEntity.ok(this.cashCards.findAll());
    }
}