package example.billingjob.billingjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
public class BillingJobConfiguration {
    
    @Bean
    public Job billingJob() {
        return new BillingJob();
    }


    @Bean
    public Job billingJobWithStep(JobRepository jobRepository, Step stepOne) {
        return new JobBuilder("BillingJobWithStep", jobRepository)
                .start(stepOne)
                .build();
    }

    @Bean
    public Step stepOne(JobRepository jobRepository, 
        JdbcTransactionManager transactionManager) {
            return new StepBuilder("filePreparation", jobRepository)
                    .tasklet(new FilePreparationTasklet(), transactionManager)
                    .build();
    }

}
