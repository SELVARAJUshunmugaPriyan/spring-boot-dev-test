===================
   Spring Batch
     Overview
===================
Batch processing is a method of processing large volumes of data simultaneously, instead of processing them individually, in real time (in that case, we could talk about stream processing). This approach is widely used in many industries, including finance, manufacturing, and telecommunications. Batch applications are ephemeral, which means that once they've completed, they end.

Use-cases:
> Reading data from large number of flat files (massive upload/download)
> Reading data from a large database table
> Reading from a large number of messaging broker queues

Challenges...
> Handling large amounts of data efficiently
> Tolerance to human errors and hardware deficiencies
> Scalability

Spring batch provides transaction management, job processing status, statistics, fault-tolerance and advanced scalability features...
    enable high-performance batch jobs through multi-threaded processing and data-partitioning techniques.
Its use-cases include loading a file into a database (simple), and moving data between databases and transforming it (complex).
                                         1--------------
                                        /| Item Reader |
                                       / ---------------
----------------   ------1   *------1 /  0..1--------------
| Job Launcher |-->| Job |-->| Step |----| Item Processor |
----------------   -------   -------- \  ------------------
                                       \ 1--------------
                                        \| Item Writer |
                                         ---------------

A <Job> is an entity that encapsulates an entire batch process, that runs from start to finish which has one or more steps. A <Step> is a unit of work that can be a simple task (such as copying a file or creating an archive), or an item-oriented task (such as exporting records from a relational database table to a file), in which case, it would have an <ItemReader>, an <ItemProcessor> (optional) and an <ItemWriter>. A <Job> needs to be launched with a <JobLauncher>, and can be launched with a set of <JobParameter>s. Execution metadata about the currently running <Job> is stored in a <JobRepository>.

Batch relational Model (Tables)

    Job_Instance : All information relevant to a job definition
    (job name and identification key).

    Job_Execution : All information relevant to the execution of a job (start time, end time, and status).

    Job_Execution_Context : Execution context of a job is a set of key/value pairs of runtime information that typically represents the state that must be retrieved after a failure.

    Step_Execution : All information about execution of a step (start time, end time, item read count, and item write count).

    Step_Execution_Context : Execution context of a step.

    Job_Execution_Params : Runtime parameters.

Layers of batch application...
> The Application layer contains the batch job and custom code written by the developers of the batch application.
    
> The Batch Core layer contains the core runtime classes provided by Spring Batch that are necessary to create and control batch jobs. It includes implementations for Job and Step, as well as common services like JobLauncher and JobRepository.
    
> The Batch Infrastructure layer contains common item readers and writers provided by Spring Batch, plus base services such as the repeat and retry mechanisms, which are used both by application developers and the core framework itself.

    --------------     --------------
    | Applcation | --> | Batch Core |
    --------------     --------------
                |       |
                v       v
        ------------------------
        | Batch Infrastructure | 
        ------------------------
===================
       Job
===================
Job interface is provided by spring-batch-core dependency. Fundamentally, it requires that implementations specify the Job name (getName()) and what the Job is supposed to do (execute()).

The execute method gives a reference to a JobExecution object. The JobExecution represents the actual execution of the Job at runtime. It contains a number of runtime details (start time, end time, execution status). This runtime information is stored by Spring Batch in a metadata repository. execute method isn't expected to throw any exception. Runtime exceptions should be handled by implementations, and added in the JobExecution object. Clients should inspect the JobExecution status to determine success or failure.

JobRepository stores metadata about jobs and steps which could be a persistent store, or an in-memory store. A persistent store has the advantage of providing metadata even after a Job is finished, which could be used for post analysis or to restart a Job in the case of a failure. Spring Batch provides a JDBC implementation of the JobRepository. JobRepository creates JobExecution object when a Job is first launched.



JobLauncher interface obtain a valid JobExecution from the JobRepository and execute the Job.

E.g.:

    public interface JobLauncher {
        JobExecution run(Job job, JobParameters jobParameters)
            throws
                JobExecutionAlreadyRunningException,
                JobRestartException,
                JobInstanceAlreadyCompleteException,
                JobParametersInvalidException;
    }

 ----------------              -------
 | Job Launcher |-----(2)----->| Job |
 ------o---------   Launches   -------
        \                       /
Creates (1)                   (3) Reports to
Job       \                  /
Execution -v----------------v-
          |  Job Repository  |
          --------------------

Batch jobs can be initiated from - web container or command line.

@SpringBootTest enables testing features from Spring Boot, which comprises loading the Spring application context and preparing the test context.

OutputCaptureExtension (provided by Spring Boot) captures any output that is written to the standard output and the error output.
===================
   Job Instance
===================
In Spring Batch, a Job is the generic definition of a batch process which must be parametrized to create actual JobInstance(s). JobParameter(s) distinguishes JobInstance(s). Each JobInstance can have multiple JobExecution(s) if it fails.

    Job 1---*> JobInstance(s) + JobParameters 1---*> JobExecution(s)
===================
   Testing Your
       Job
=================== 
Testing the job from end to end is known as black-box testing.
Random job parameters generation:
>   JobLauncherTestUtils.getUniqueJobParameters
>   JobLauncherTestUtils.getUniqueJobParametersBuilder
Launching an entire job from end to end:
>   JobLauncherTestUtils.launchJob
Launching an individual step:
>   JobLauncherTestUtils.launchStep
JobRepositoryTestUtils is used to create or delete JobExecution(s) as needed during tests. It clears the batch metadata from the database before or after each test run.
ExecutionContextTestUtils provides static methods to access attributes from the execution context of JobExecution and StepExecution. MetaDataInstanceFactory creates metadata entities, such as JobInstance and JobExecution, with the constraints defined by the batch domain model.
JobScopeTestExecutionListener and StepScopeTestExecutionListener listeners are used to test components (like item readers, item writers, etc) that are scoped with the custom Spring scopes provided by Spring Batch which are JobScope and StepScope.
Test pollution occurs when tests leave artifacts behind that might impact other or future tests.
===================
   Understanding
       Steps
===================
A Step is a domain object that encapsulates an independent, sequential phase (or a unit of work) of a batch Job. It is represented by the Step interface.The execute method is designed to throw a JobInterruptedException if the job should be interrupted at that particular step.
                                 Step
                                   ^
                              implements
        ---------------------------+-----------------------------
        |                    |                  |               |
-------------------  ------------------  --------------  ----------------
| PartitionedStep |  |  TaskLet-Step  |  |  FlowStep  |  |    JobStep   |
| (proc. i/p data |  |  (Simple Step) |  | (Logically |  |  (individual |
|  set in partit- |  ------------------  |   grouped  |  |    job exec) |
|      ions)      |          |           |    Steps)  |  ----------------
-------------------         Uses         --------------
                             |
                             v
                        -----------
                        | Tasklet |
                        ----------- 
                             ^
                             |
                         implements
                             |
            ___________________________________
            |                                 |
            v                                 V
    -----------------                -------------------
    | ChunkOriented |                |  SystemCommand  |
    |   Tasklet     |                |     Tasklet     |
    -----------------                -------------------

Tasklet is designed to execute one iteration of business logic with return value of <RepeatStatus.FINISHED/CONTINUABLE>. <StepContribution> provides counts of step execution and <ChunkContext> provides execution context.
===================
       Using
       Steps
===================
Job Execution Flow - SimpleJob (sequential flow) or FlowJob (Conditional branching and parallel execution)
    @Bean
    public Job myJob(JobRepository jobRepository, Step step1, Step step2) {
    return new JobBuilder("job", jobRepository)
        .start(step1)
        .next(step2)
        .build();
    }

    If step1 fails, the job is terminated, and step2 won't be executed.

    @Bean
    public Step taskletStep(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager transactionManager) {
    return new StepBuilder("step1", jobRepository)
        .tasklet(tasklet, transactionManager)
        .build();
    }

    @Bean
    public Step partitionedtStep(JobRepository jobRepository, Partitioner partitioner) {
    return new StepBuilder("step1", jobRepository)
        .partitioner("worker", partitioner)
        .build();
    }
===================
    Reading and
   Writing Data
===================
Challenges:
> Input file could be big and doesn't fit in memory.
> R/W process is terminated abruptly.
Spring Batch solves them using efficient and fault-tolerant chunk-oriented processing model. It is to process the datasource in chunks of a configurable size.

    public class Chunk<T> implements Iterable<T> {
        private List<T> items;
        // methods to add and get items
    }

Each chunk of items (a line in a flat file, a record in a database table) is read and written within the scope of a transaction which means they are either committed together or rolled back togethe.

If the transaction is committed, Spring Batch records, as part of the transaction, the execution progress (read count, write count, etc) in its metadata repository and uses that information to restart where it left off in case of a failure.

If an error occurs while processing a chunk of items, then the transaction will be rolled back by the framework. The number of items to include in a chunk is called the commit-interval, which is the configurable size of a chunk that should be processed within the scope of a single transaction.

    @FunctionalInterface
    public interface ItemReader<T> {
        @Nullable => can return null values
        T read() throws Exception;
    }

    Reader can also return Strings or byte arrays from the file. Here, T denotes DomainType like Entity. e.g.: JdbcCursorItemReader, FlatFileItemReader

    @FunctionalInterface
    public interface ItemWriter<T> {
        void write(Chunk<? extends T> chunk) throws Exception;
    }

    Unlike reading items which is done one item at a time, writing items is done in chunks. The reason behind it is because bulk writes are typically more efficient than single writes. e.g.: JdbcBatchItemWriter,  FlatFileItemWriter

    @Bean
    public Step ingestFile(
              JobRepository jobRepository,
              PlatformTransactionManager transactionManager,
              ItemReader<T> reader,
              ItemWriter<U> writer) {
        return new StepBuilder("stepName", jobRepository)
                .<T, U>chunk([size], transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }
===================
    Processing
       Data
===================
Input/Output systems aren't always a perfect match.
    
    @FunctionalInterface
    public interface ItemProcessor<I, O> {
        @Nullable
        O process(@NonNull I item) throws Exception;
    }

Types of processing:
# Transforming Data
# Enriching Data
# Validating Data
# Filtering Data
===================
   Batch Scoped
    Components    
===================
Spring Batch provides two custom Spring bean scopes: job scope (@JobScope) and step scope (@StepScope). Batch-scoped beans are not created at application startup like singleton beans. Rather, they are created at runtime when the job or step is executed. A job-scoped bean will only be instantiated when the job is started. Similarly, a step-scoped bean won't be instantiated until the step is started. Since this type of bean is lazily instantiated at runtime, any runtime job parameter or execution context attribute can be resolved.
Job parameters and execution context values can be resolved by using the Spring Expression Language (SpEL). It specifies which job parameter or context value should be bound in the bean definition method late at runtime.
The @Value("#{jobParameters['input.file']}") notation instructs Spring Framework to resolve the value lazily from the jobParameters object.
===================
       State 
     management
        and 
   restartability    
===================
State Management: inter-step restartability (refers to resuming a job from the last failed step) and inner-step restartability (resuming a failed step where it left off from within the step itself)
Restartability is a included feature in Spring Batch.

Types of error -
    
    -   Transient error may not occur on subsequent runs (like a failed call to a flaky web service). soln.: Efficient retry policy.
    -   Non-transient will recur constantly (like an incorrect input data). soln.: Tolerate the bad input by skipping it for later analysis or reprocessing.
===================
   Handling non-
  transient error
    with skips    
===================
The skip feature in Spring Batch is specific to chunk-oriented steps. This feature covers all of the phases of the chunk-oriented processing model: when errors occur while reading, processing, or writing items. (Since chunk-writing involves bulk writing we cannot skip individual lines)

Creating a fault-tolerant step is done by calling the <faultTolerant()> method on the <StepBuilder>. The <skip(Exception.class)> on the <StepBuilder> method defines which exception should cause the current item to be skipped. <skipLimit()> method is used to define the maximum number of items to skip.

To validate skipped items,
    
    public interface SkipListener<T, S> extends StepListener {
        default void onSkipInRead(Throwable t) { } <- Read refers to type of step
        default void onSkipInWrite(S item, Throwable t) { } <- Write refers to type of step
        default void onSkipInProcess(T item, Throwable t) { } <- Process refers to type of step
    }

... in FaultTolerantStepBuilder.listener(SkipListener) .

To set skip policy:

    @FunctionalInterface
    public interface SkipPolicy {

        boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException;

    }

... in FaultTolerantStepBuilder.skipPolicy(SkipPolicy) .
===================
     Handling
  transient error
   with retries    
===================
The retry feature in Spring Batch is based on the Spring Retry library which is available in most projects of Spring portfolio.
Similar to the skip feature, the retry feature is designed for chunk-oriented steps, specifically for the processing and writing phases. The reading phase is not retryable. The <retry(Exception.class)> on the <StepBuilder> method defines which exception should cause the current item to be re-tried following <faultTolerant()>. <retryLimit()> method is used to define the maximum number of items to retry. <listener(RetryListenerImpl)> includes the listener in the <StepBuilder>. 

    public interface RetryListener {

    default <T, E extends Throwable> void onSuccess(  --> executes on successful retry
            RetryContext context,
            RetryCallback<T, E> callback,
                    T result) {
            }

    default <T, E extends Throwable> void onError( --> executes on Error during retry
            RetryContext context,
            RetryCallback<T, E> callback,
                Throwable throwable) {
            }
    }

The RetryPolicy interface is part of Spring Retry and is defined as follows:

    public interface RetryPolicy extends Serializable {
        boolean canRetry(RetryContext context);
        void registerThrowable(RetryContext context, Throwable throwable);
    }

... in FaultTolerantStepBuilder.retryPolicy(SkipPolicy) .

Spring Retry already provides several ready-to-use implementations of this interface, including MaxAttemptsRetryPolicy and CircuitBreakerRetryPolicy.