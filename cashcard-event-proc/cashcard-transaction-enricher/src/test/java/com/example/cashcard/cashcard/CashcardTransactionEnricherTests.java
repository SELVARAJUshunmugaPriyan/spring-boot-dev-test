package com.example.cashcard.cashcard;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.cashcard.cashcard.domain.ApprovalStatus;
import com.example.cashcard.cashcard.domain.CardHolderData;
import com.example.cashcard.cashcard.domain.CashCard;
import com.example.cashcard.cashcard.domain.EnrichedTransaction;
import com.example.cashcard.cashcard.domain.Transaction;
import com.example.cashcard.cashcard.service.EnrichmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class CashcardTransactionEnricherTests {

	@MockitoBean
	private EnrichmentService enrichmentService;

	@Test
	void enrichmentServiceShouldAddDataToTransactions(
		@Autowired InputDestination inputDestination,
		@Autowired OutputDestination outputDestination 
	) throws IOException
	{
		Transaction transaction = new Transaction(1L, new CashCard(123L, "sarah1", 1.00));

		EnrichedTransaction enrichedTransaction = new EnrichedTransaction(
			transaction.id(),
			transaction.cashCard(),
			ApprovalStatus.APPROVED,
			new CardHolderData(
				UUID.randomUUID(),
				transaction.cashCard().owner(),
				"123 Main Street"
			)
		);

		given(enrichmentService.enrichTransaction(transaction)).willReturn(enrichedTransaction);

		Message<Transaction> message = MessageBuilder.withPayload(transaction).build();
		inputDestination.send(message, "enrichTransaction-in-0");

		Message<byte[]> result = outputDestination.receive(5000, "enrichTransaction-out-0");
		assertNotNull(result);

		ObjectMapper objectMapper = new ObjectMapper();
		EnrichedTransaction receivedData = objectMapper.readValue(result.getPayload(), EnrichedTransaction.class);
		assertEquals(enrichedTransaction, receivedData);
	}

	@SpringBootApplication
    public static class App {

    }

}
