package example.billingjob.billingjob;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BillingJobFail implements Job {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public String getName() {
        return "BillingJobFail";
    }

    @Override
    public void execute(JobExecution execution) {
        try {
            throw new Exception("Unable to process billing information");
        } catch (Exception exception) {
            execution.addFailureException(exception);
            execution.setStatus(BatchStatus.ABANDONED);
            execution.setExitStatus(ExitStatus.FAILED
                .addExitDescription(exception.getMessage()));
        } finally {
            this.jobRepository.update(execution);
        }
    }
    
}
