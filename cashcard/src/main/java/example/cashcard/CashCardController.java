package example.cashcard;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/{requestedId}")
    ResponseEntity<String> findById(@PathVariable Long requestedId) throws IOException {
        if (requestedId.equals(99L)) {
            CashCard cashCard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(objectMapper.writeValueAsString(cashCard));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
