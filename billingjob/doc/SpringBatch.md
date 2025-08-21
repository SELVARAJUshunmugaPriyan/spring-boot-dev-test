===================
   Spring Batch
     Overview
===================
Batch processing is a method of processing large volumes of data simultaneously, instead of processing them individually, in real time (in that case, we could talk about stream processing). This approach is widely used in many industries, including finance, manufacturing, and telecommunications. Batch applications are ephemeral, which means that once they've completed, they end.

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