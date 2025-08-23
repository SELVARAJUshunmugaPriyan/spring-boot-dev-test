package example.billingjob.billingjob;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BillingJob implements Job {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public String getName()
    {
        return "BillingJob";
    }

    @Override
    public void execute(JobExecution execution)
    {
        JobParameters jobParams = execution.getJobParameters();
        String inputFile = jobParams.getString("input.file");
        System.out.println("processing billing information from file " + inputFile);
        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        this.jobRepository.update(execution);
    }
}
