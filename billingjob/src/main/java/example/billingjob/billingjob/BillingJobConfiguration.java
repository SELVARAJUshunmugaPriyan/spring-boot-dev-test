package example.billingjob.billingjob;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingJobConfiguration {
    
    @Bean
    public Job billingJob() {
        return new BillingJob();
    }

}
