======================
#                    #
#  Spring Framework  #
#                    #
======================
Types of Spring f/w:
- Spring Security
- Spring WebFlow
- Spring Batch
- Spring Integration

Spring is an Open Source, Lightweight, DI (Dependency Injection) Container and Framework for building Java enterprise applications. Spring will add behavior on top of the business logic. Spring applications do not require a Java EE application server. It has low overhead with relatively small JARs. 

Spring serves as a DI container where objects are auto-discoverable and auto-connected. It is also serves as lifecycle manager.

Features of a enterprise application:
- Containerization, Cloud, MicroService
- JDBC, Transactions, ORM / JPA, NoSQL
- Events, Streaming, Reactive, Messaging, JMS, AMQP, Scheduling
- Security, OAuth2, OpenID Connect
- Monitoring and Observability
======================
#                    #
#  Dependency Inje-  #
#  ction Container   #
#                    #
======================
Key principles: Don't repeat yourself, separation of concerns, convention over configuration, and testability
A typical application consists of several parts working together to carry out a use case
But..
    - How to configure the app to ensure all components are assembled correctly?
    - How to swap out implementation without re-writing the application?
    ..these questions concern POJO dependency
======================
#                    #
#  Spring Framework  #
#       History      #
#                    #
======================
Spring aimed to...
    - Simplify configuration (Dependency injection) (Pre-spring a lot of interfaces and a lot of class extends)
    - Transaction mgmt and JDBC data access
    - Multi environment deployable
Spring excels...
    - Integration with other open source projects. (Hibernate, Quartz, multiple View technologies)
    - Spring projects created for common enterprise domains (Spring Security, Batch, Integration)
    - ...for new domains: Spring Data - NoSQL + JPA, Spring Cloud
    - Spring boot created to further simplify DevEx
Common use-cases...
    - Spring deals with plumbing
    - Web Apps / Messaging / Persistance / Batch or Tasks / Integration or Streaming
======================
#                    #
#  Java Configurat-  #
#        ion         #
#                    #
======================
                        Your Application Classes
                                (POJOs)
                                    |
                                    v
                            ---------------------
                            |       Spring      |
        Configuration --->  |    DI Container   |
          Metadata          |    (Application   | 
                            |       Context)    |
                            ---------------------
                                    | Creates
                                    v
                            ---------------------
                            |  Fully Configured |
                            | application system|
                            ---------------------

// Create applica. cntxt from config
ApplicationContext context = SpringApplication.run(ApplicationConfig.class);

// Get a bean
BeanType bean = context.getBean(BeanType.class); -or-
BeanType bean = (BeanType) context.getBean("beanDefnMthdName"); -or-
BeanType bean = context.getBean("beanDefnMthdName", BeanType.class);

Application context creates bean with less dependencies to more dependencies
Application context is the heart of Spring IoC. It can be created in any environment...
    - standalone app
    - web app
    - test env

If there are multiple config class, use @import. E.g.:

@Configuration
@Import(ApplicationConfig.class) <- Application beans
class InfraConfig {
    ... <- data source and other beans
}

If ApplicationConfig has dependencies from InfraConfig use @Autowired for cross dependency injection.

@Bean
Serv serv(BeanType bean) {
    return new Serv(depServ(bean)); <- this is called tramp data usage of   
                                       intermediates (should be avoided)
}

Scope of bean defines how beans are created using @Scope default intrinsic is @Scope("singleton"). Issues with singleton multiple threads accessing singleton bean (ways to handle: stateless/immutable beans, synchronised state or different scope). Prototype scope creates a new instance of bean when it is called. Other scopes being web socket, refresh, thread, custom (by impl. scope interface), session (per user session) or request (per usr req).
======================
#                    #
#  More Configurat-  #
#        ion         #
#                    #
======================
Hard-coding application properties in the running program is a bad practice. It is best to externalize through separate application.properties file.
Environment bean represents loaded properties from runtime environment.
Properties are derived in this order: JVM sys properties > Sys env properties > Java properties file
@PropertySource("/file/path") contributes additional properties
We use @Value to access the property
Properties can be segmented to achieve unique environment (such as dev, test, prod), implementation (jdbc, jpa) or deploymeny platform (on-premise, cloud) based on profiles (env prop spring.profiles.active). Beans are also included or excluded based on profile membership with @Profile("profile"(active) / "!profile"(inactive)).
SpEL - Spring Expression Language, which is pluggable/extenable by other Spring frameworks
@Value("${application.properties}" -or- "#{systemProperties['user.region']}" (expression) )

Examples of SpEL :
property...
-   "${daily.limit}"
-   "${daily.limit:[DEFAULT_VALUE]}"
expression...
-   "#{environment['daily.limit']}"
-   "#{new Integer(environment['daily.limit']) * 2}"
-   "#{new java.net.URL(environment['home.page']).host}"
-   "#{environment['daily.limit']?:[DEFAULT_VALUE]}" // x ?: y is short for x != null ? x : y
=======================
#                     #
#  Compnent Scanning  #
#                     #
=======================
@Component("componentName") <- denotes bean class identified by class name
@ComponentScan <- scans for components in pkg and sub-pkg
@Qualifier("componentName") <- denotes particular bean definition

@Autowired
public MyBean(Queue ack) {}

@Autowired
public void setBean(Queue ack) {}

@Autowired
private Queue ack; <- Spring finds bean with id matching what is being set "ack"

@Lazy <- Lazy beans created first time used when depedency injected

@PostConstruct <- Method called at startup after all dependencies are injected

@PreDestroy <- Method called at shutdown prior to destroying the bean instance

@PostConstruct & @PreDestroy - defined by JSR-250 part of Java >6 in javax.annotation supported by Spring and by Java EE

@Bean(initMethod="func",destroyMethod="func") <- similar

Predefined stereotype annotations @Component - @Service, @Repository, @Controller, @RestController and @Configuration (they are also called meta-annotations)
=======================
#                     #
#     Inside the      #
#  Spring Container   #
#                     #
=======================
Lifecycle:
Initialization (Beans created & dep injected) -> Usage -> Destruction (Released for garbage collection)

Initialization: 
When a context is created, the initialization phase completes
- Load & Process Bean Definitions
    @Configuration classes are processed & @Component are scanned for
    Bean defns added to a BeanFactory (ApplicationContext extends BeanFactory) with each indexed its id and type
    Spl BeanFactoryPostProcessor (functional interface that transforms BeanFactory) beans invoked which can modify bean defn
- Perform Bean Creation
    - Find/Create its dependencies
        Creation occurs eagerly to fail-fast unless @Lazy annotated
    - Instantiate Beans
    - Perform Setter Injection
    - Bean Post Processors (BPP)
        - @BeforeInit (BPP) (Internal methods)
        - Initializer
        - @AfterInit (BPP) (Internal methods)
    - Bean Ready for Use
BPP may wrap the bean in a proxy (Adds behavior (extra logic) to your bean transparently)
        JDK Proxy       |       CGLib Proxy
    'dynamic proxies'   |   Not built into JDK
 API is built into JDK  | Included in Spring Jars
 Rqrs: Java Interface(s)| Used when interface not available
 All interfaces are pro-| Cannot be applied to final classes or
 xied                   | methods

static @Bean runs before any beans are created
Beans must be created in the right order. It must be created after their dependencies. There are two steps:
    - Evaluate dependencies for each bean
    - Get/create each dependency
    ...To enforce dependency order, we use @DependsOn. Type of @Bean includes types of nested interfaces implemented by the Returned object.
=======================
#                     #
#     Introducing     #
#   Aspect Oriented   #
#     Programming     #
#                     #
=======================
Aspect Oriented Programming (AOP) enables modularization of cross-cutting concerns (a behavior that expands across codebase or multiple classes) whose code is inside a module (a class). Examples of Cross-cutting concerns :
    - Logging and Tracing
    - transaction mgmt
    - security
    - caching
    - error handling (global excep in spring boot)
    - perf monitoring
    - Cust business rules
    ...Failing to modularize cross-cutting concerns leads to problems:
        - Code tangling :   Coupling or mixing of concerns difficult to test
        - Code scattering :   same concern spread across modules
How to use AOP ?
- Impl. mainline application logic
- write aspects to impl. your cross-cutting concerns (or modules)
- weave aspects into appl.
Core concepts of AOP...
- Join point - method call or thrown excep
- Pointcut - selection of one or more join points (AspectJ expression Language)
- Advice - Code to be executed at each selected join point
- Aspect - encapsulating module of pointcuts and advice
- Weaving - combining aspects into main application
AOP proxy - an enhanced class that stands in place of your original with extra behv. woven
@Aspect is marked over @Component to create aspect module it is a spring AspectJ annotation.  Concern -> @Before("execution(void set*(*))") calls the aspects before execution of void set- methods with one argument. JoinPoint parameter provides context about the intercepted point

In aspect @Configuration class, @EnableAspectJAutoProxy <- configures spring to recog and use @Aspect proxy.
@Import can be used to import aspect class into main configuration class.

Spring AOP uses AspectJ's pointcut expression language for selective application of advice.
Common pointcut designator:
    - execution(<method pattern>) {method pattern:
        [modifiers] ReturnType [ClassType] MethodName (Arguments) [throws ExceptionType]
    }
    - execution(<pattern1>) || execution(<pattern2>)

Example expression:
    execution(* rewards.restaurant.*Service.find*(..))

    ..where
        execution -> designator
        * (first) -> ReturnType
        rewards.restuarant -> package
        *Service -> ClassName
        find* -> MethodName
        .. -> arguments
    
    wildcards:
        * -> matches once (return type, package, class, method name, argument)
        .. -> matches zero or more (arguments or packages)

    execution(void send*(rewards.Dining)) // Any method starting with send that takes a single Dining parameter and has a void return type
    execution(* send(*)) // Any method named send that takes a single parameter
    execution(* send(int, ..)) // Any method named send whose first parameter is an int (the ".." signifies 0 or more parameters may follow)
    execution(void example.MessageServiceImpl.*(..)) // Any void method in the MessageServiceImpl class (including any sub-class)
    execution(void example.MessageService.send(*)) // Any void send method taking one argument, in any object implementing MessageService (Works even if implementation changes)
    execution(@javax.annotation.security.RolesAllowed void send*(..)) // Any void method whose starts with "send" that is annotated with the @RolesAllowed annotation
    execution(* rewards.*.restaurant.*.*(..)) // There should be only one directory between rewards and restaurant
    execution(* rewards..restaurant.*.*(..)) // There may be several directories between rewards and restaurant
    execution(* *..restaurant.*.*(..)) // Any sub-package named 'restaurant' (starting .. is invalid *.. is valid)

  # Before advice

    -------                 ----------------            ------------
    |Proxy|                 | BeforeAdvice |            |  Target  |
    -------                 ----------------            ------------
       |                            |                         |
  --->| |                           |                         |
      | |------------------------->| |                        |
      | |<-------------------------| |                        |
      | |--------------------------------------------------->| |
      | |<---------------------------------------------------| |
  <---| |                           |                         |

  /*
  code snippet:
    @Aspect
    @Component
    public class PropertyChangeTracker {
        private Logger logger = Logger.getLogger(getClass());
        // Track calls to all setter methods
        @Before("execution(void set*(*))")
        public void trackChange() {
            logger.info("Property about to change...");
        }
        // Audit all operations in the service package that return a Reward object
        // Here return type of SpEL is matched to return type of method argument of 'reward'
        @AfterReturning(value="execution(* service..*.*(..))", returning="reward")
        public void audit(JoinPoint jp, Reward reward) {
            auditService.logEvent(jp.getSignature()) +
                " returns the following reqard object:" + reward.toString());
        }
        // Send an email every time a Repository class throws an exception of type 
        // DataAccessException
        @AfterThrowing(value="execution(* *..Repository.*(..))", throwing="e")
        public void report(JoinPoint jp, DataAccessException e) {
            mailService.emailFailure("Exception in repository", jp, e);
            throw new RewardsException(e);
        }
        // Track calls to all update methods
        @After("execution(void update*(..))")
        public void trackUpdate() {
            logger.info("An update has been attempted ...");
        }
        // Cache values returned by cacheable services
        @Around("execution(@example.Cacheable * rewards.service..*.*(..))")
        public Object cache(ProceedingJoinPoint point) throws Throwable {
            Object value = cacheStore.get(CacheUtils.toKey(point));
            if (value != null) return value;
            value = point.proceed();
            cacheStore.put(CacheUtils.toKey(point), value);
        }
    }
  */

  # After returning advice

    -------               -----------------------         ------------
    |Proxy|               | AfterReturingAdvice |         |  Target  |
    -------               -----------------------         ------------
       |                            |                         |
  --->| |                           |                         |
      | |--------------------------------------------------->| |
      | |<--------------------------------Returning----------| |
      | |------------------------->| |                        |
      | |<-------------------------| |                        |
  <---| |                           |                         |

  # After throwing advice

    -------            ------------------------            ------------
    |Proxy|            |  AfterThrowingAdvice |            |  Target  |
    -------            ------------------------            ------------
       |                            |                         |
  --->| |                           |                         |
      | |--------------------------------------------------->| |
      | |<...................................-------Excpetion| |
      | |------------------------->| |                        |
      | |<-------------------------| |                        |
  <---| |                           |                         |
  # After advice

    -------                 ----------------            ------------
    |Proxy|                 |  AfterAdvice |            |  Target  |
    -------                 ----------------            ------------
       |                            |                         |
  --->| |                           |                         |
      | |--------------------------------------------------->| |
      | |<------------------------------Throwing or Returning| |
      | |------------------------->| |                        |
      | |<-------------------------| |                        |
  <---| |                           |                         |
  # Around advice

    -------                 ----------------            ------------
    |Proxy|                 |  AfterAdvice |            |  Target  |
    -------                 ----------------            ------------
       |                            |                         |
  --->| |                           |                         |
      | |------------------------->| |                        |
      | |                          | |-----------proceed()-->| |
      | |                          | |<----------------------| |                       
      | |<-------------------------| |                        |
  <---| |                           |                         |

  Limitation of Spring AOP:
  - Can only advise non-private methods
  - Can only apply aspects to Spring beans (proxies are created by post-processor after 
  bean initialization)
  - When using proxies, suppose method a() calls method b() on the same class/interface
    -   advice will never be executed for method b()
=======================
#                     #
#   Testing Spring    #
#    Applications     #
#                     #
=======================
JUnit4 was monolith which basically a JAR file. JUnit5 requires Java8 JRE leverages lambda
JUnit5 has separation of concerns like components: JUnit Platform (for launching testing f/
w), JUnit Jupiter (extension for writing tests) and JUnit Vitage (backward compatability).
JUnit4 -> JUnit5
@Before -> @BeforeEach
@BeforeClass -> @BeforeAll
@After -> @AfterEach
@AfterClass -> @AfterAll
@Ignore -> @Disabled
new annotations:
@DisplayName
@Nested
@ParameterizedTest
Use right annotations from correct package. JUnit5 ignores all JUnit4 annotations.
Integration testing -> Tests the interaction of multiple successfully tested units. Test 
application classes in context of their surrounding infrastructure:
    - Out-of-container testing (no need to run up full App)
    - Server infrastructure may be scaled down
    - Test containers

    Application (Production mode)   / TransferServiceTests (Integration Test)
                                    | Inject by Spring
                                    v
                           TransferServiceImpl
                                    | Inject by Spring
                                    v
                              JpaAccountRepo
                Production DB       /   Test DB
Spring support for testing:
-   ApplicationContext is defined based on TestContext
-   @ContextConfiguration defines the spring configuration to use
-   Packaged as a separate module (spring-test.jar)
-   @ExtendWith in JUnit5 supports multiple extensions extends architecture
-   @SpringJUnitConfig -> @ExtendWith + @ContextConfiguration
=======================
#                     #  
#       JDBC          #
#   Simplification    #
#  with Jdbc Template #
#                     #
=======================
Design pattern of JdbcTemplate outlines the skeleton of an algorithm
- Leaves implementation and low-level resource management details
- Reduces boilerplate codes
Spring provides many templates: JdbcTemplate, JmsTemplate, RestTemplate, RabbitMqTemplate, KafkaTemplate and WebServiceTemplate
Adv. of JdbcTemplate:
- reduces boilerplate codes/common causes of bugs
- handles SQLException
- ThreadSafe
/*
int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CUSTOMER", new 
    RowMapper<Customer>() {
        public Customer mapRow(ResultSet rs, int row) throws SQLException {}
    } );
// Steps performed:
- Acquisition of the connection
- Participation in the transaction
- Execution of the statement
- Processing of the result set
- Maps each row
- Handling exceptions
- Release of the connection
*/
JdbcTemplate template = new JdbcTemplate(dataSource);

In object-oriented programming, the template method is one of the behavioral design patterns which is a method in a superclass, usually an abstract superclass, and defines the skeleton of an operation in terms of a number of high-level steps. These steps are themselves implemented by additional helper methods in the same class as the template method. 
=======================
#                     #  
#       JDBC          #
#     Template        #
#  Basic Usage        #
#                     #
=======================
public class JdbcCustomerRepository implements CustomerRepository {
    private JdbcTemplate jdbcTemplate;
    public jdbcCustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int getCountOfNationalsOver(Nationality nationality, int age) {
        String sql = "select count(*) from PERSON where age > ? and nationality = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, age, nationality.toString());
        // jdbcTemplate.update(INSERT / UPDATE query) // Any non-SELECT SQL is run using update()
    }
}

JdbcTemplate can query for
- Simple types (int, long, String, Date)
- Generic Maps
- Domain Objects
=======================
#                     #  
#  Writing with Res-  #
#  ultSets using Ca-  #
#  llbacks            #
#                     #
=======================
methods -
    queryForMap() // no need specific java object mapping
    queryForList()

ORM (Object Relational Mapping) offers first-level/second-level cache and Object query language. Spring provides RowMapper, RowCallbackHandler and ResultSetExtractor interface.

    jdbcTemplate.queryForObject("select first_name, last_name from PERSON where id=?", (rs, rowNum) -> new Person(rs.getString("first_name"), rs.getString("last_name")), id)

    jdbcTemplate.queryForObject("select first_name, last_name from PERSON where city=?", (ResultSetExtractor<Person>) (rs) -> {
        Person person = null;
        while (rs.next()) {
            if (person == null)
                person = new Person(rs.getString(), rs.getString());
            person.addItem(mapitem(rs));
        }
        return person;
    }, city)
=======================
#                     #  
#  Exception Handli-  #
#  ng                 #
#                     #
=======================
Spring -> Method error code -> meaningful exceptions:

> DataAccessException
> DataAccessResourceException
> CleanupFailureDataAccessException
> OptimisticLockingFailureException
> DataIntegrityViolationException
> BadSqlGrammarException
=======================
#                     #  
#  Why Transactions   #
#  and Java supports  #
#  them               #
#                     #
=======================
APIs to manage transactions: JPA, JDBC and JMS
ACID principle of DB:
> Atomic: Each unit of work is an all-or-nothing operation
> Consistent: Database Integrity constraints are never violated
> Isolated: Isolating transactions from each other
> Durable: Committed changes are permanent
## Running non-Transactionally

+-------------------+--------------------+-----------------------+-------------------+---------------+---------------+
| Reward Network    | Account Repository | Restaurant Repository | Reward Repository | DataSource    | Connection    |
+===================+====================+=======================+===================+===============+===============+
|                   |                    |                       |                   |               |               |
| findByCreditCard(String)                                                                                          |
| ----------------->|                    |                       |                   |               |               |
|                   | getConnection()    |                       |                   |               |               |  1. SELECT
|                   | ------------------>|                       |                   | ------------->| connection 1  |
|                   | <------------------|                       |                   | <-------------|               |
|                   | close()            |                       |                   |               |               |
|                   | ------------------>|                       |                   | ------------->|               |
|                   | <------------------|                       |                   | <-------------|               |
|                   |                    |                       |                   |               |               |
| findByMerchantNumber(String)                                                                                      |
| -------------------------------------->|                       |                   |               |               |
|                   |                    | getConnection()       |                   |               |               |  2. SELECT
|                   |                    | --------------------->|                   | ------------->| connection 2  |
|                   |                    | <---------------------|                   | <-------------|               |
|                   |                    | close()               |                   |               |               |
|                   |                    | --------------------->|                   | ------------->|               |
|                   |                    | <---------------------|                   | <-------------|               |
|                   |                    |                       |                   |               |               |
| updateBeneficiaries(Account)                                                                                      |
| ----------------->|                    |                       |                   |               |               |
|                   | getConnection()    |                       |                   |               |               |  3. UPDATE
|                   | ------------------>|                       |                   | ------------->| connection 3  |
|                   | <------------------|                       |                   | <-------------|               |
|                   | close()            |                       |                   |               |               |
|                   | ------------------>|                       |                   | ------------->|               |
|                   | <------------------|                       |                   | <-------------|               |
|                   |                    |                       |                   |               |               |
| confirmReward(AccountContribution, Dining)                                                                        |
| -------------------------------------------------------------->|                   |               |               |
|                   |                    |                       | getConnection()   |               |               |  4. INSERT
|                   |                    |                       | ----------------->| ------------->| connection 4  |
|                   |                    |                       | <-----------------| <-------------|               |
|                   |                    |                       | close()           |               |               |
|                   |                    |                       | ----------------->| ------------->|               |
|                   |                    |                       | <-----------------| <-------------|               |
|                   |                    |                       |                   |               |               |
+-------------------+--------------------+-----------------------+-------------------+---------------+---------------+
                                                                                                                   [DATABASE]

Without transaction each data operation invokes a separate connection. In non-transactional data operations involving multiple data sources, partial failures can occur leading to inconsistencies across DBs. In transactional data operations, DB connections can be reused to perform multiple data operations more efficiently and consistently.
=======================
#                     #  
#  Spring Transacti-  #
#  on management      #
#                     #
=======================
Transaction demarcation expressed via AOP.
Main component PlatormTransactionManager abstracts implementation details
With Separation-of-Concern, we can easily change the transaction context.
Step: -
    Define PlatormTransactionManager bean

        @Bean
        public DataSource dataSource(@Value(“${db.jndi}” String jndiName) {
            JndiDataSourceLookup lookup = new JndiDataSourceLookup();
            return lookup.getDataSource(jndiName);
        }

    Java Naming and Directory Interface (JNDI) is a Java API that provides naming and directory functionality to Java applications

        @Bean
        public PlatormTransactionManager  transactionManager(DataSource dataSource) 
        {
	        return new DataSourceTransactionManager(datasource);
        }

Add @EnableTransactionManagement to configuration class. It defines a Bean Post-Processor - proxies @Transactional(timeout = X (seconds)) beans

Base interface: PlatormTransactionManager 
Implementations: DataSourceTransactionManager, JmsTransactionManager, JpaTransactionManager, JtaTransactionManager, WebLogicJtaTransactionManager,
WebSphereUoWTransactionManager

@Transactional demarcates data operations in a method defining atomic unit-of-work.

                                             +-----------------------+
                                             | Transaction Manager   |
                                             | (begin, commit, ...)  |
                                             +-----------------------+
                                                  ^          |
                                                  |          |
                                                  |  (Begin) | (Commit/Rollback)
                                                  |          V
+-----------------------+    (Request)      +-----------------------------------------+
| Caller                | ----------------> | (A) Spring TransactionInterceptor       |
| (Uses Proxy Reference)|                   |     (The 'Around' Advice)               |
+-----------------------+                   |-----------------------------------------|
                                            | (B) Delegates Call (Target Execution)   |
                                            |                                         |
                                            | +-------------------------------------+ |
                                            | | (C) Target: TransferServiceImpl     | |
                                            | |     (The actual business logic)     | |
                                            | +-------------------------------------+ |
                                            |                                         |
                                            +-----------------------------------------+
                                            ^                                         ^
                                            |           SPRING PROXY WRAPPER          |
                                            +-----------------------------------------+

Transaction steps : -
    Transaction starts before entering the method
    Commit at the end of the method
    Rollback if RuntimeException (default behavior)
        Can be overridden
        Checked exceptions do not cause Rollback
Transaction context holds underlying JDBC connection, Hibernate sessions and JTA work. JdbcTemplate used within transaction uses provided connection, to access it - DataSourceUtils.getConnection(dataSource)
=======================
#                     #  
#  Configure Transa-  #
#  ction Propagation  #
#                     #
=======================
What should happen if parent transactional method calls another child transactional method
There is 7 level of propagation - REQUIRED / REQUIRES_NEW / MANDATORY
Different execution pattern available : -
Join first transaction
Start new sub transaction
@Transactional(propagation=Propagation.REQUIRED(default)) - join existing transaction
Propagation.REQUIRES_NEW - create separate sub transaction
Propagation.SUPPORTS - uses an existing transaction if available, otherwise runs without one Propagation.MANDATORY - requires an existing transaction, otherwise throws an exception Propagation.NOT_SUPPORTED - runs without a transaction, suspending an existing one Propagation.NEVER - runs without a transaction, but throws an exception if one is active
– Setup Rollback Rules and Testing support
RuntimeException - DataAccessException and HibernateException -> Rollback
@Transactional(rollbackFor = MyCheckedException.class,noRollbackFor={MailException.class, MySpecificRuntimeException.class})
@Test can be combined with @Transactional to always rollback after completing the test
@Transactional class can have @Commit methods to commit particular methods