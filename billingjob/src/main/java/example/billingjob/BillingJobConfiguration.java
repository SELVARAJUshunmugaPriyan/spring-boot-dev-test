package example.billingjob;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.NonNull;

@Configuration
public class BillingJobConfiguration {

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
    public FlatFileItemReader<BillingData> billingDataFileReader()
    {
        return new FlatFileItemReaderBuilder<BillingData>()
                    .name("billingDataFileReader")
                    .resource(new FileSystemResource("staging/billing-2023-01.csv"))
                    .delimited()
                    .names("dataYear", "dataMonth", "accountId", "phoneNumber",
                        "dataUsage", "callDuration", "smsCount"
                    )
                    .targetType(BillingData.class)
                    .build();
    }

    @Bean
    public Job job(@NonNull JobRepository jobRepository, @NonNull Step step1,
        @NonNull Step step2
    )
    {
        return new JobBuilder("BillingJob", jobRepository)
                .start(step1)
                .next(step2)
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

}
