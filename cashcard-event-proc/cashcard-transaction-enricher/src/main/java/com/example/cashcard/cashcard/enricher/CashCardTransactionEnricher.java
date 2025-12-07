package com.example.cashcard.cashcard.enricher;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.cashcard.cashcard.domain.EnrichedTransaction;
import com.example.cashcard.cashcard.domain.Transaction;
import com.example.cashcard.cashcard.service.EnrichmentService;

@Configuration
public class CashCardTransactionEnricher {
    
    @Bean
    public EnrichmentService enrichmentService()
    {
        return new EnrichmentService();
    }

    @Bean
    public Function<Transaction, EnrichedTransaction> enrichTransaction(EnrichmentService enrichmentService)
    {
        return transaction -> {
            return enrichmentService.enrichTransaction(transaction);
        };
    }

}
