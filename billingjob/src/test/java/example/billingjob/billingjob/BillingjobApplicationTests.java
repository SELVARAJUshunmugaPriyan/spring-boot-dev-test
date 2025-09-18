package example.billingjob.billingjob;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBootTest(properties="spring.batch.job.name=BillingJob")
@SpringBatchTest
@ExtendWith(OutputCaptureExtension.class)
class BillingjobApplicationTests {

    @Autowired
    private Job billingJob;

    @Autowired
    private Job billingJobWithStep;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @AfterEach
    public void clearExecution() {
        this.jobRepositoryTestUtils.removeJobExecutions();
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "BILLING_DATA");
    }

    @Test
    void testJobExecution(CapturedOutput output) throws Exception {
        // given
        JobParameters jobParameters = new JobParameters();
        
        // when
        JobExecution jobExecution = this.jobLauncher.run(this.billingJob, jobParameters);

        // then
        Assertions.assertTrue(output.getOut().contains("processing billing information"));
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    void testJobExecutionWithParams(CapturedOutput output) throws Exception {
        // given
        JobParameters jobParameters = 
            // new JobParametersBuilder()
            this.jobLauncherTestUtils.getUniqueJobParametersBuilder()
                    .addString("input.file", "/some/input/file")
                    .addString("file.format", "csv", false)
                    .toJobParameters();
        // when
        JobExecution jobExecution = this.jobLauncher.run(this.billingJob, jobParameters);

        // then
        Assertions.assertTrue(output.getOut().contains("processing billing information " +
            "from file /some/input/file"));
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    void autoTestJobExecution(CapturedOutput output) throws Exception {
        // given
        JobParameters jobParameters = 
            // new JobParametersBuilder()
            this.jobLauncherTestUtils.getUniqueJobParametersBuilder()
                    .addString("input.file", "/some/input/file")
                    .toJobParameters();
            this.jobLauncherTestUtils.setJob(billingJob);
        
        // when
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertTrue(output.getOut().contains("processing billing information " +
            "from file /some/input/file"));
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    void testJobStepExecution() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input.file", "input/billing-2023-01.csv")
                .addString("output.file", "staging/billing-report-2023-01.csv")
                .addJobParameter("data.year", 2023, Integer.class)
                .addJobParameter("data.month", 1, Integer.class)
                .toJobParameters();
        this.jobLauncherTestUtils.setJob(billingJobWithStep);
        
        // when
        JobExecution jobExecution = 
            this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        Assertions.assertTrue(Files.exists(Paths.get("staging", "billing-2023-01.csv")));
        Assertions.assertEquals(1000, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "BILLING_DATA"));
        Path billingReport = Paths.get("staging", "billing-report-2023-01.csv");
        Assertions.assertTrue(Files.exists(billingReport));
        Assertions.assertEquals(781, Files.lines(billingReport).count());
    }
}