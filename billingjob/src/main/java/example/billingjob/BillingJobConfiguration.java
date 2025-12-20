package example.billingjob;

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
import org.springframework.lang.NonNull;

@Configuration
public class BillingJobConfiguration {

    @Bean
    public BillingDataProcessor billingDataProcessor()
    {
        return new BillingDataProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<ReportingData> billingDataFileWriter(
        @NonNull @Value("#{jobParameters['output.file']}") String outputFile
    ) {
        return new FlatFileItemWriterBuilder<ReportingData>()
                .resource(new FileSystemResource(outputFile))
                .name("billingDataFileWriter")
                .delimited()
                .names("billingData.dataYear", "billingData.dataMonth", 
                    "billingData.accountId", "billingData.phoneNumber", "billingData.dataUsage",
                    "billingData.callDuration", "billingData.smsCount", "billingTotal")
                .build();

    }

    @SuppressWarnings("null")
    @Bean
    @StepScope
    public JdbcCursorItemReader<BillingData> billingDataTableReader(
        @NonNull DataSource dataSource,
        @NonNull @Value("#{jobParameters['data.year']}") Integer year,
        @NonNull @Value("#{jobParameters['data.month']}") Integer month
    ) {
        final String sql = String.format(
            "select * from BILLING_DATA where data_year = %d and data_month = %d",
            year, month);
        return new JdbcCursorItemReaderBuilder<BillingData>()
                .name("billingDataTableReader")
                .dataSource(dataSource)
                .sql(sql)
                .rowMapper(new DataClassRowMapper<>(BillingData.class))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BillingData> billingDataTableWriter(@NonNull DataSource datasource)
    {
        final String sql = "insert into BILLING_DATA values (:dataYear, :dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, :smsCount)";
        return new JdbcBatchItemWriterBuilder<BillingData>()
                    .dataSource(datasource)
                    .sql(sql)
                    .beanMapped()
                    .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<BillingData> billingDataFileReader(
        @NonNull @Value("#{jobParameters['input.file']}") String inputFile
    ) {
        return new FlatFileItemReaderBuilder<BillingData>()
                    .name("billingDataFileReader")
                    .resource(new FileSystemResource(inputFile))
                    .delimited()
                    .names("dataYear", "dataMonth", "accountId", "phoneNumber",
                        "dataUsage", "callDuration", "smsCount"
                    )
                    .targetType(BillingData.class)
                    .build();
    }

    @Bean
    public Job job(@NonNull JobRepository jobRepository, @NonNull Step step1,
        @NonNull Step step2, @NonNull Step step3
    )
    {
        return new JobBuilder("BillingJob", jobRepository)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1(@NonNull JobRepository jobRepository, @NonNull JdbcTransactionManager transactionManager)
    {
        return new StepBuilder("filePreparation", jobRepository)
                .tasklet(new FilePreparationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step step2(
        @NonNull JobRepository jobRepository, 
        @NonNull JdbcTransactionManager transactionManager,
        @NonNull ItemReader<BillingData> billingDataFileReader,
        @NonNull ItemWriter<BillingData> billingDataTableWriter
    ) {
        return new StepBuilder("fileIngestion", jobRepository)
            .<BillingData, BillingData>chunk(100, transactionManager)
            .reader(billingDataFileReader)
            .writer(billingDataTableWriter)
            .build();
    }

    @Bean
    public Step step3(
        @NonNull JobRepository jobRepository, 
        @NonNull JdbcTransactionManager transactionManager,
        @NonNull ItemReader<BillingData> billingDataTableReader,
        @NonNull ItemProcessor<BillingData, ReportingData> billingDataProcessor,
        @NonNull ItemWriter<ReportingData> billingDataFileWriter
    ) {
        return new StepBuilder("fileIngestion", jobRepository)
            .<BillingData, ReportingData>chunk(100, transactionManager)
            .reader(billingDataTableReader)
            .processor(billingDataProcessor)
            .writer(billingDataFileWriter)
            .build();
    }

}
