package com.example.cashcard.cashcard.e2e.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.cashcard.cashcard.enricher.CashCardTransactionEnricher;
import com.example.cashcard.cashcard.sink.CashCardTransactionSink;
import com.example.cashcard.cashcard.stream.CashCardTransactionStream;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
    classes = CashCardTransactionStreamE2EContainerTests.StreamTestConfig.class,
    properties = {
        "spring.cloud.function.definition=approvalRequest;enrichTransaction;cashCardTransactionFileSink",
        "spring.cloud.stream.bindings.approvalRequest-out-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-in-0.destination=approval-requests",
        "spring.cloud.stream.bindings.enrichTransaction-out-0.destination=enriched-transactions",
        "spring.cloud.stream.bindings.cashCardTransactionFileSink-in-0.destination=enriched-transactions"
    }
)
class CashCardTransactionStreamE2EContainerTests
{
    @Test
    void cashCardTransactionStreamEndToEnd() throws IOException
    {
        Path path = Paths.get(CashCardTransactionSink.CSV_FILE_PATH);

        if (Files.exists(path)) {
            Files.delete(path);
        }

        Awaitility.await().until(() -> Files.exists(path));
        List<String> lines = Files.readAllLines(path);
        String csvLine = lines.get(0);

        assertTrue(csvLine.contains("sarah1"));
        assertTrue(csvLine.contains("123 Main Street"));
        assertTrue(csvLine.contains("APPROVED"));
    }

    @EnableAutoConfiguration
    @Configuration
    @Import({CashCardTransactionStream.class, CashCardTransactionEnricher.class,
        CashCardTransactionSink.class
    })
    public static class StreamTestConfig {
        @SuppressWarnings("resource")
        @Bean
        @ServiceConnection
        KafkaContainer KafkaContainer() {
            WaitAllStrategy fiveMinutes = new WaitAllStrategy(WaitAllStrategy.Mode.WITH_OUTER_TIMEOUT)
                .withStartupTimeout(Duration.ofMinutes(5));
            return new KafkaContainer(DockerImageName.parse("apache/kafka:latest"))
                .waitingFor(Wait.forListeningPort())
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(fiveMinutes);
        }
    }
}