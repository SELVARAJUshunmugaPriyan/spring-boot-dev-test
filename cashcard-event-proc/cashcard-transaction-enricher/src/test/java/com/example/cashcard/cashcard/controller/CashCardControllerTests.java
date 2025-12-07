package com.example.cashcard.cashcard.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;

import com.example.cashcard.cashcard.domain.CashCard;
import com.example.cashcard.cashcard.domain.Transaction;
import com.example.cashcard.cashcard.ondemand.CashCardTransactionOnDemand;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({ TestChannelBinderConfiguration.class, CashCardTransactionOnDemand.class })
public class CashCardControllerTests {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void cashCardStreamBridge() throws IOException
    {
        Transaction transaction = new Transaction(1L, new CashCard(123L, "kumar2", 1.00));
        ResponseEntity<Transaction> response = this.restTemplate.postForEntity(
            "http://localhost:" + port + "/publish/txn",
            transaction, Transaction.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void postShouldSendTransactionAsAMessage(@Autowired OutputDestination outputDestination)
        throws IOException
    {
        Transaction postedTransaction = new Transaction(1L, new CashCard(123L, "kumar2", 1.00));
        ResponseEntity<Transaction> response = this.restTemplate.postForEntity(
            "http://localhost:" + port + "/publish/txn",
            postedTransaction, Transaction.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Message<byte[]> result = outputDestination.receive(5000, "approvalRequest-out-0");
        assertNotNull(result);

        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transactionFromMessage = objectMapper.readValue(result.getPayload(), Transaction.class);

        assertEquals(transactionFromMessage.id(), postedTransaction.id());
    }

    @SpringBootApplication
    public static class App {
        
    }

}
