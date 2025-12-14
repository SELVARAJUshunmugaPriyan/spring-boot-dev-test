package example.billingjob;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingJobConfiguration {

    private JobRepository jobRepository;

    public BillingJobConfiguration(JobRepository jobRepository)
    {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job job()
    {
        return new BillingJob(jobRepository);
    }
}
