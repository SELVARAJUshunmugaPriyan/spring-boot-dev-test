package example.billingjob;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("null")
public class BillingJob implements Job {

    @Autowired
    private JobRepository jobRepository;
    
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
