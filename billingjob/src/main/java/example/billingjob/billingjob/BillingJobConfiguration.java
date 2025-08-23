package example.billingjob.billingjob;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
public class BillingJobConfiguration {
    
    @Bean
    public Job billingJob() {
        return new BillingJob();
    }


    @Bean
    public Job billingJobWithStep(JobRepository jobRepository, 
        Step stepOne, Step stepTwo, Step stepThree) {
        return new JobBuilder("BillingJobWithStep", jobRepository)
                .start(stepOne)
                .next(stepTwo)
                .next(stepThree)
                .build();
    }

    @Bean
    public Step stepOne(JobRepository jobRepository, 
        JdbcTransactionManager transactionManager) {
            return new StepBuilder("filePreparation", jobRepository)
                    .tasklet(new FilePreparationTasklet(), transactionManager)
                    .build();
    }

    @Bean
    public Step stepTwo(
        JobRepository jobRepository, JdbcTransactionManager transactionManager,
        ItemReader<BillingData> billingDataFileReader,
        ItemWriter<BillingData> billingDataTableWriter
    ) {
        return new StepBuilder("fileIngestion", jobRepository)
                .<BillingData, BillingData>chunk(100, transactionManager)
                .reader(billingDataFileReader)
                .writer(billingDataTableWriter)
                .build();
    }

    @Bean
    public Step stepThree(JobRepository jobRepository,
        JdbcTransactionManager transactionManager, ItemReader<BillingData> billingDataTableReader,
        ItemProcessor<BillingData, ReportingData> billingDataProcessor, ItemWriter<ReportingData> billingDataFileWriter)
        {
            return new StepBuilder("reportGeneration", jobRepository)
                    .<BillingData, ReportingData>chunk(100, transactionManager)
                    .reader(billingDataTableReader)
                    .processor(billingDataProcessor)
                    .writer(billingDataFileWriter)
                    .build();
        }

    @Bean
    public FlatFileItemReader<BillingData> billingDataFileReader() {
        return new FlatFileItemReaderBuilder<BillingData>()
                .name("billingDataFileReader")
                .resource(new FileSystemResource("staging/billing-2023-01.csv"))
                .delimited()
                .names(
                    "dataYear",
                    "dataMonth",
                    "accountId",
                    "phoneNumber",
                    "dataUsage",
                    "callDuration",
                    "smsCount"
                )
                .targetType(BillingData.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BillingData> billingDataTableWriter(DataSource dataSource) {
        String sql = "INSERT INTO billing_data VALUES (:dataYear, " + 
        ":dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, " +
        ":smsCount)";

        return new JdbcBatchItemWriterBuilder<BillingData>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcCursorItemReader<BillingData>
        billingDataTableReader(DataSource dataSource) {
            String sql = "SELECT * FROM billing_data";
            return new JdbcCursorItemReaderBuilder<BillingData>()
                    .name("billingDataTableReader")
                    .dataSource(dataSource)
                    .sql(sql)
                    .rowMapper(new DataClassRowMapper<>(BillingData.class))
                    .build();
        }

    @Bean
    public BillingDataProcessor billingDataProcessor() {
        return new BillingDataProcessor();
    }

    @Bean
    public FlatFileItemWriter<ReportingData> billingDataFileWriter() {
        return new FlatFileItemWriterBuilder<ReportingData>()
                .resource(new FileSystemResource("staging/billing-report-2023-01.csv"))
                .name("billingDataFileWriter")
                .delimited()
                .names(
                    "BillingData.dataYear",
                    "BillingData.dataMonth",
                    "BillingData.accountId",
                    "BillingData.phoneNumber",
                    "BillingData.dataUsage",
                    "BillingData.callDuration",
                    "BillingData.smsCount",
                    "billingTotal"
                )
                .build();
    }
}
