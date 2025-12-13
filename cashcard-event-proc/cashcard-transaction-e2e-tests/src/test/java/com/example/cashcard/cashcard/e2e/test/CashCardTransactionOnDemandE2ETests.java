package com.example.cashcard.cashcard.e2e.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.example.cashcard.cashcard.controller.CashCardController;
import com.example.cashcard.cashcard.domain.CashCard;
import com.example.cashcard.cashcard.domain.Transaction;
import com.example.cashcard.cashcard.enricher.CashCardTransactionEnricher;
import com.example.cashcard.cashcard.ondemand.CashCardTransactionOnDemand;
import com.example.cashcard.cashcard.sink.CashCardTransactionSink;

@EmbeddedKafka
@SpringBootTest(
    classes = CashCardTransactionOnDemandE2ETests.OnDemandTestConfig.class,
    properties = {
        "spring.cloud.function.definition=enrichTransaction;cashCardTransactionFileSink",
        "spring.cloud.stream.bindings.approvalRequest-out-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-in-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-out-0.destination=enriched-transactions",
        "spring.cloud.stream.bindings.cashCardTransactionFileSink-in-0.destination=enriched-transactions"
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CashCardTransactionOnDemandE2ETests
{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void cashCardTransactionOnDemandEndToEnd() throws IOException
    {
        Path path = Paths.get(CashCardTransactionSink.CSV_FILE_PATH);

        if (Files.exists(path)) {
            Files.delete(path);
        }

        Transaction transaction = new Transaction(1122334455L, new CashCard(6677889900L, "kumar2", 820.0));
        this.restTemplate.postForEntity("http://localhost:" + port + "/publish/txn", transaction, Transaction.class);

        Awaitility.await().until(() -> Files.exists(path));
        List<String> lines = Files.readAllLines(path);
        String csvLine = lines.get(0);
        
        assertTrue(csvLine.contains("1122334455"));
        assertTrue(csvLine.contains("6677889900"));
        assertTrue(csvLine.contains("kumar2"));
        assertTrue(csvLine.contains("820.0"));
    }

    @EnableAutoConfiguration
    @Import({
        CashCardController.class,
        CashCardTransactionEnricher.class,
        CashCardTransactionOnDemand.class,
        CashCardTransactionSink.class
    })
    public static class OnDemandTestConfig
    {

    }
}