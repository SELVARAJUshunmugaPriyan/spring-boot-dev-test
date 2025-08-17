package example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp()
    {
        cashCards = Arrays.array(
            new CashCard(99L, 123.45, "sarah1"),
            new CashCard(100L, 100.00, "sarah1"),
            new CashCard(101L, 150.00,"sarah1")
        );
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        // CashCard cashCard = new CashCard(99L, 123.45);

        assertThat(json.write(cashCards[0])).isStrictlyEqualToJson("single.json");
        assertThat(json.write(cashCards[0])).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCards[0])).extractingJsonPathNumberValue("@.id")
            .isEqualTo(99);
        assertThat(json.write(cashCards[0])).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCards[0])).extractingJsonPathNumberValue("@.amount")
            .isEqualTo(123.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        String expected = """
                                {
                                    "id" : 99,
                                    "amount" : 123.45,
                                    "owner" : "sarah1"
                                }
                          """;

        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 123.45, "sarah1"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected = 
                """
                    [
                        { "id": 99, "amount": 123.45, "owner" : "sarah1" },
                        { "id": 100, "amount": 100.00, "owner" : "sarah1" },
                        { "id": 101, "amount": 150.00, "owner" : "sarah1" }
                    ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
}
