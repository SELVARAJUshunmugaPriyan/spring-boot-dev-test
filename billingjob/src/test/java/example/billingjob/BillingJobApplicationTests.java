package example.billingjob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBatchTest
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class BillingJobApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@BeforeEach
	public void setup()
	{
		this.jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	void testJobExecution(CapturedOutput output) throws Exception {
		JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParametersBuilder()
				.addString("input.file", "src/main/resources/billing-2023-01.csv")
				.toJobParameters();
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
		assertTrue(Files.exists(Paths.get("staging", "billing-2023-01.csv")));
	}

}
