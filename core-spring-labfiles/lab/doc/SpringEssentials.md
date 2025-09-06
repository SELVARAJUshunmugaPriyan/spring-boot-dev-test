======================
#                    #
#  Spring Framework  #
#                    #
======================
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