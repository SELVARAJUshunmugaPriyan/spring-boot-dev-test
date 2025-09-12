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
Comman use-cases...
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