package com.example.cashcard.cashcard.stream;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import org.springframework.messaging.Message;

import com.example.cashcard.cashcard.domain.CashCard;
import com.example.cashcard.cashcard.domain.Transaction;
import com.example.cashcard.cashcard.service.DataSourceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class CashCardApplicationTests {

    @MockitoBean
    private DataSourceService dataSourceService;
    
    @Test
    void basicCashCardSupplier(@Autowired OutputDestination outputDestination)
        throws IOException
    {
        Transaction testTransaction = new Transaction(1L, new CashCard(123L, "sarah1", 1.00));
        given(dataSourceService.getData()).willReturn(testTransaction);

        Message<byte[]> result = outputDestination.receive(5000, "approvalRequest-out-0");

        assertNotNull(result);
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction = objectMapper.readValue(result.getPayload(), Transaction.class);
        assertEquals(transaction.id(), 1L);
        assertEquals(transaction.cashCard(), testTransaction.cashCard());
    }

}
