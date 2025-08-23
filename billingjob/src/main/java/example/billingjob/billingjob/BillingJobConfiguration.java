package example.billingjob.billingjob;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
import org.springframework.beans.factory.annotation.Value;
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
    @StepScope
    public FlatFileItemReader<BillingData> billingDataFileReader(
        @Value("#{jobParameters['input.file']}") String inputFile
    ) {
        return new FlatFileItemReaderBuilder<BillingData>()
                .name("billingDataFileReader")
                .resource(new FileSystemResource(inputFile))
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
    @StepScope
    public JdbcCursorItemReader<BillingData> billingDataTableReader(
        DataSource dataSource,
        @Value("#{jobParameters['data.year']}") Integer year,
        @Value("#{jobParameters['data.month']}") Integer month
    ) {
            String sql = String.format("SELECT * FROM billing_data " +
                "WHERE data_year=%d AND data_month=%d", year, month);
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
    @StepScope
    public FlatFileItemWriter<ReportingData> billingDataFileWriter(
        @Value("#{jobParameters['output.file']}") String outputFile
    ) {
        return new FlatFileItemWriterBuilder<ReportingData>()
                .resource(new FileSystemResource(outputFile))
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
