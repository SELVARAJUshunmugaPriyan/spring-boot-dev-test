package example.billingjob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class BillingJobApplicationTests {

	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	@Test
	void testJobExecution(CapturedOutput output) throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("input.file", "/some/input/file")
				.toJobParameters();
		@SuppressWarnings("null")
		JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);
		assertTrue(output.getOut().contains("processing billing information "
			+ "from file /some/input/file"
		));
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

}
