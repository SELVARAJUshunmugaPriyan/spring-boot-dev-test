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