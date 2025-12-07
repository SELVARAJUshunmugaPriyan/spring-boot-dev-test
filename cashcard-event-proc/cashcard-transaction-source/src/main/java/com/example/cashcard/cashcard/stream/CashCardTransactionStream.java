package com.example.cashcard.cashcard.stream;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.cashcard.cashcard.domain.Transaction;
import com.example.cashcard.cashcard.service.DataSourceService;

@Configuration
public class CashCardTransactionStream {
    
    @Bean
    public Supplier<Transaction> approvalRequest(DataSourceService dataSource)
    {
        return () -> {
            return dataSource.getData();
        };
    }

    @Bean
    public DataSourceService dataSourceService()
    {
        return new DataSourceService();
    }

}
