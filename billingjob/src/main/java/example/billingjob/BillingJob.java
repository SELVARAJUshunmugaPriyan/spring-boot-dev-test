package example.billingjob;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.repository.JobRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BillingJob implements Job {

    private JobRepository jobRepository;

    public BillingJob(JobRepository jobRepository)
    {
        this.jobRepository = jobRepository;
    }
    
    @Override
    public String getName()
    {
        return "BillingJob";
    }

    @Override
    public void execute(JobExecution jobExecution)
    {
        log.info("processing billing information");
        jobExecution.setStatus(BatchStatus.COMPLETED);
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
        this.jobRepository.update(jobExecution);
    }

}
