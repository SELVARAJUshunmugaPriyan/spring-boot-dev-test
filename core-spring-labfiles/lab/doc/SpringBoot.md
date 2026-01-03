======================
#                    #
#  Spring Boot Fea-  #
#  ture Introducti-  #
#  on                #
#                    #
======================
- Handles most low-level, predictable setup for you
- Everything happens in runtime
Range of non-functional features:
> Embedded server
> metrics
> health checks
> externalized configuration
> containerization
Spring Boot parent POM defines:
    - Defines versions of key dependencies
    -- Uses a dependencyManagement section internally
    -- Through spring-boot-dependencies as a parent
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.5</version>
    </parent>
    - Defines Maven plugins
    - Sets up Java version
spring-boot-starter resolves multiple jars -
spring-boot-*.jar
spring-context-*.jar
spring-beans-*.jar
spring-core-*.jar
spring-aop-*.jar
*-slf4j-*.jar
common starters -
spring-boot-starter-jdbc
spring-boot-starter-test
spring-boot-starter-data-jpa
spring-boot-starter-web
spring-boot-starter-batch
Spring boot automatically creates beans based on application

@SpringBootConfiguration \
@ComponentScan -------------> @SpringBootApplication(scanBasePackages="example.config")
@EnableAutoConfiguration /
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

 When we include      At runtime SpringBoot ch-      And sprin-
                      ecks if                        gBoot con-
                                                     figures
[EMBEDDED_DB_DEP] -> [HSQLDB/DERBY_ON_CLASSPATH] -> [DATASOURCE]
[STARTER_JDBC]    -> [IF_DATASOURCE_CONFIGURED]  -> [JDBCTEMPLATE_CONFIGURED]
                 \-> [IF_DATASOURCE_NOT_CONFIGURED] -> [RUNTIME_ERROR_JDBC_FOUND_WITHOUT_DATASOURCE]

======================
#                    #
#  Packaging and I-  #
#  ntegration Test-  #
#  ing               #
#                    #
======================
A 'fat' jar contains all dependencies
    gradle assemnle -or- mvn package
creates 'fat' jar...
my-app.jar (22M) -> executable jar
my-app.jar.original (5K) -> only compiled classes

spring-boot-maven-plugin extends package goal to create fat jar

@SpringBootTest(classes=Application.class) (alternative to @SpringJUnitConfig)
public class TransferServiceTests {

}

@SpringBootTest handles external configuration which isn't done by @SpringJUnitConfig
.. it searches for @SpringBootConfiguration and creates application context for the test
... only one @SpringBootConfiguration/@SpringBootApplication is allowed

======================
#                    #
#  Spring Boot - G-  #
#  etting Started    #
#                    #
======================
[POM.XML] setups dependencies
[APPLICATION.PROPERTIES] configures general configuration
[APPLICATION_CLASSES] has application launcher
Spring Initializr constructs template of SpringBoot porjects and curates dependecny management. It is accessible via 'New Project' wizard in STS.

pom.xml :-
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.5</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId> --> SpringBoot, spring jdbc and jdbc connection pool
        </dependency>
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId> --> embedded database
        </dependency> --> versions managed by parent
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

application.properties :-
    logging.level.root=ERROR --> set log levels to all modules
    spring.sql.init.schema-locations=classpath:rewards/schema.sql --> DDM file
    spring.sql.init.data-locations=classpath:rewards/data.sql --> DML file
======================
#                    #
#  Spring Boot - A   #
#  Closer Look       #
#                    #
======================
Developers commonly externalize properties to files
- Easily consumable via @PropertySource
- Properties defined in Application.properties -or- Application.yml are available via environment and @Value

Properties file can be placed in src/main/resources, classpath root or config package of the classpath.
SpringBoot classifies profiles based on application-{profile}.yml convention
Application.properties is always loaded by default.

Profiles can be segregated in Application.yml (also possible in Application.properties) where "---" indicates separate logical file.

    Sample Application.yml :-
    ======================

    spring.datasource:
      driver:
      username:

    ---
    spring:
      profiles: local
      datasource:

    ---
    spring:
      profiles: cloud
      datsource:

Precedence order -
> Devtools settings
> @TestPropertySource and @SpringBootTest properties
> CLI arguments
> SPRING_APPLICATION_JSON (inline JSON properties)
> ServletConfig / ServletContext parameters
> JNDI attributes from java:comp/env
> Java System properties
> OS environment variables
> Profile-specific application properties
> Application properties / YAML
> @PropertySource files
> SpringApplication.setDefaultProperties

@ConfigurationProperties is used to automatically set from corresponding properties

    @ConfigurationProperties(prefix = "spring.datasource")
    public class ConnectionSettings
    {
        private String url;
        private String driver;
        private String driverClassName;
        ...

    }

This is enabled by @EnableConfigurationProperties(ConnectionSettings.class), @ConfigurationPropertiesScan or @Component (above ConnectionSettings).
@ConfigurationProperties beans allows relaxed bindings
valid matches (for above class) :

    spring.datasource.driverClassName <- camel case
    spring.datasource.driver-class-name <- kebab case
    spring.datasource.driver_class_name <- snake case
    SPRING_DATASOURCE_DRIVERCLASSNAME <- ENV variable style
======================
#                    #
#  Using Auto Conf-  #
#  iguration         #
#                    #
======================
@SpringBootApplication -or- @EnableAutoConfiguration enable auto-configuration by extensive use of @Configuration or auto-configuration classes with conditions -
- Do classpath include specific classes?
- Are properties set?
- Are beans configured?
@ConditionalOnBean(Bean.class) ..similar to
@ConditionalOnClass, @ConditionalOnMissingClass, @ConditionalOnProperty

    @AutoConfiguration
    ...
    public class DataSourceAutoConfiguration {
        ...
        @Conditional(...)
        @ConditionalOnMissingBean(DataSource.class, ..)
        @Import({EmbeddedDataSourceConfiguration.class}) // Auto-configure DataSource bean only if it 
                                                         // is not already configured.
        protected static class EmbeddedDatabaseConfiguration { ... }
        ...
    }

Pre-written Spring configurations:
- org.springframework.boot.autoconfigure package
- spring-boot-autoconfigure JAR file

@EnableAutoConfiguration processes autoconfiguration:
- @AutoConfiguration annotation specifies a class explicitly for autoconfiguration
- spring-boot-autoconfigure/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
======================
#                    #
#  Override Default  #
#  Configuration     #
#                    #
======================
SpringBoot selects best defaults. e.g. Logback is used by default.
Options to override defaults:
- setting spring boot properties
- explicit bean defintion

    @Bean
    public DataSource dataSource()
    {
        return new EmbeddedDatabaseBuilder()
                        .setName([OUR_OWN_DB])
                        .build();

    }

- disabling auto-configuration

    @EnableAutoConfiguration(exclude = DataSourcceAutoConfiguration.class)
    public class ApplicationConfiguration
    {
        ...
    }

    -or-

    spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration \- > fully qualified name

- changing dependencies or their versions for compliance or company policy

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>
    </dependencies>

The spring.datasource.type property in Spring Boot is used to explicitly specify the fully qualified class name of the JDBC DataSource implementation that should be used for the application's database connection pool.
The CommandLineRunner is a functional interface in Spring Boot used to execute specific code just before the application startup completes and after the application context is fully initialized.  The run() method is invoked automatically by Spring Boot after the application context has been loaded and all beans are created, but before the main SpringApplication.run() method finishes execution.

    @SpringBootApplication
    public class JdbcBootApplication {

        public static void main(String[] args) {
            SpringApplication.run(JdbcBootApplication.class, args);
        }
        
        @Bean
        CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate)
        {
            final String QUERY = "SELECT COUNT(*) FROM t_account";
            
            return args -> System.out.println("Hello, there are "
                    + jdbcTemplate.queryForObject(QUERY, Long.class)
                    + " accounts");
        }

    }