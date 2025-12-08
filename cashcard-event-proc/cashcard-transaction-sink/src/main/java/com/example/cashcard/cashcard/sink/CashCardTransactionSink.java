package com.example.cashcard.cashcard.sink;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.cashcard.cashcard.domain.EnrichedTransaction;

@Configuration
public class CashCardTransactionSink {
    
    @Bean
    public Consumer<EnrichedTransaction> sinkToConsole()
    {
        return enrichedTransaction -> {

        };
    }

}
