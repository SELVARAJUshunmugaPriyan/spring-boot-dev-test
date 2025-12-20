package example.billingjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.NonNull;

@Configuration
public class BillingJobConfiguration {

    @Bean
    public Job job(@NonNull JobRepository jobRepository,@NonNull Step step1)
    {
        return new JobBuilder("BillingJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(@NonNull JobRepository jobRepository, @NonNull JdbcTransactionManager transactionManager)
    {
        return new StepBuilder("filePreparation", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

}
