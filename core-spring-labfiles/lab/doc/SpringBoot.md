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
======================
#                    #
#  Spring Boot - S-  #
#  pring Data JPA    #
#                    #
======================
Only spring-boot-starter-data-jpa artifact is needed to create spring JPA application. It resolves -
> spring-boot-starter
> spring-boot-starter-jdbc
> spring-boot-starter-aop
> spring-data-jpa
> hibernate-core (JPA implementation)
> javax.transaction-api
JPA requires DataSource, EntityManagerFactoryBean and JpaTransactionManager. JPA deals with entities which is a persistent object managed by entity manager. Manager has capabilities for second-level cache.

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabase(Database.HSQL);
        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", "true");
        LocalContainerEntityManagerFactoryBean emfb = 
                new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setPackagesToScan("rewards.internal");
        emfb.setJpaProperties(props);
        emfb.setJpaVendorAdapter(adapter);
        return emfb;
    }

@EnableAutoConfiguration includes @EntityScan

    Application.properties :-
    # Leave blank - Spring Boot will try to select dialect for you
    # Set to 'default' - Hibernate will try to determine it
    spring.jpa.database=default

    # Create tables automatically? Default is:
    #   Embedded database: create-drop
    #   Any other database: none (do nothing)
    # Options: validate | update | create | create-drop
    spring.jpa.hibernate.ddl-auto=update

    # Show SQL being run (nicely formatted)
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    # Any hibernate property 'xxx'
    spring.jpa.properties.hibernate.xxx=???

Spring boot auto-configures jpa via -

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager
            transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public DataSource dataSource() { /* Lookup via JNDI or create locally */ }
======================
#                    #
#  Create Spring D-  #
#  ata Repositories  #
#  for JPA           #
#                    #
======================
[CORE_PROJECT] :  Spring Data
[SUB_PROJECTS] :  JPA / Pivotal Gemfire / MongoDB 
                  Apache Hadoop / Solr / JDBC ext
                  Neo4j / Redis / many more ...
Spring Data depends on data domains like entities through @Table, @Entity and other annotations, and repositories.
Spring Data implements repositories interfaces at run-time by
-   Scanning for marker interfaces extending Spring Data Common Repository<T, K>
-   Auto-generating CRUD methods when using CrudRepository<T, K>
-   Paging, custom queries and sorting supported
-   Variations exist for most Spring Data sub-projects

    SQL Domain class:

    @Entity
    @Table("TABLE")
    public class Domain {

        @Id
        @GeneratedValue(strategy = GenerationType)
        private Long pk;
        private String columns;

    }

    MongoDB Domain class:
    @Document
    public class Domain {}

    Neo4J Domain class:
    @NodeEntity
    public class Domain { @GraphId Long id; }

    Gemfire Domain class:
    @Region
    public class Domain {}

    Base repository:
    public interface Repository<T, ID> { }

    Crud repository:
    public interface CrudRepository<T, ID extends Serializable> 
        extends Repository<T, ID> {
        public long count();
        public <S extends T> S save(S entity);
        public <S extends T> Iterable<S> save(Iterable<S> entities);
        public Optional<T> findById(ID id);
        public Iterable<T> findAll();
        public Iterable<T> findAllById(Iterable<ID> ids);
        public void deleteAll(Iterable<? extends T> entities);
        public void delete(T entity);
        public void deleteById(ID id);
        public void deleteAll();
    }

    Custom repository:
    public interface CustomRepository extends CrudRepository<T, ID> { }

@EnableJpaRepositories(basePackages="com.example") to dictate packages to be scanned for jpa.
======================
#                    #
#  Spring Web Intr-  #
#  oduction          #
#                    #
======================
Features of Spring MVC :-
> Model/View/Controller (MVC) design pattern
> POJO programming removes f/w dependencies 
> Testable components with minimal dependency on f/w
> Spring f/w for configuration, dependency injection and auto-configuration
> Supports Client-Side Rendering (CSR) (REST) and Server-Side Rendering(SSR) (templates)

Types of Spring MVC Applications :-
Web Servlet (Traditional)       Web Reactive (Modern)
-------------------------       ---------------------
+Traditional approach.	        +Newer, more efficient.
+Based on Java EE Servle-       +Non-blocking approach.
t Specification.	
+Includes Servlets, Filt-       +Uses Netty, Undertow,
ers, Listeners, etc..	        or Servlet 3.1+.
                                +Requires knowledge of 
                                Reactive programming.
+Spring Web                     +Spring Webflux starter

Spring boot supports embedded 'fat' JAR as well as traditional WAR packaging in external container or application server.

{LOCALE_RESOLVER] [HANDLER_MAPPING]         {EXCEPTION_RESOLVER]
                  1.^|
                    |v         2.
--request(URL)-->[DISPATCHER]--dispatch-->[HANDLER] {CONTROLLER]
<-response--------[SERVLET]<---domain object----[ADAPTER]
                  /3.|^ ^\-----3.data---->[MESSAGE] {CONTENT_TYPE_MANAGER]
                 /   v|  \----------------[CONVERTER]
              Model [VIEW_RESOLVER]
              /
             v
            [VIEW]
View resolver and views are part of SSR. Message convertor returns java objects used by CSR.
======================
#                    #
#  Create a simple   #
#  RESTful control-  #
#  ler to handle G-  #
#  ET requests       #
#                    #
======================

--request(URL)-->[DISPATCHER]--dispatch--------------->[HANDLER] {CONTROLLER]
<-response--------[SERVLET]<---domain object (data)----[ADAPTER]
                    ^\-----covert (data)----->[MESSAGE] {CONTENT_TYPE_MANAGER]
                    \------XML, JSON----------[CONVERTER]

Message converters: Jackson for JSON/XML, JAXB for XML and GSON for JSON
Controller is a POJO class annotated with @Component picked by @ComponentScan

    Sample :
    ========

    @Controller
    public class AccountController {
        @GetMapping("/accounts/{accountId}")
        public @ResponseBody List<Account> list(Principal user, @RequestParam(required=false, name="userId") Integer userId,
            @PathVariable("accountId") Long id, @RequestHeader("user-agent") String agent) {}
    }

    @Controller -> MVC
    @GetMapping -> Handler Adapter
    @ResponseBody -> REST response and disables view handling
    @RequestParam -> Request parameters
    @PathVariable -> dynamic URI part extraction based on URI templates
    @RequestHeader -> HTTP header

    @RestController
    public class AccountController {
        @GetMapping("/accounts")
        public List<Account> list(Principal user) {}
    }

    @RestController -> @Controller + @ResponseBody

Spring injects handler method arguments. It could be -
> HttpServletRequest
> HttpSession
> Principal
> Locale
======================
#                    #
#  Message Convert-  #
#  ers               #
#                    #
======================

    HTTP request:
    ------------

    GET /store/orders/123
    Host: shop.spring.io
    Accept: application/json, ...   # format accepted by client 
    ...


    HTTP response:
    -------------

    HTTP/1.1 200 OK
    Date: ..
    Content-Length: 756
    Content-Type: application/json  # format sent by the server

    {
        "id": 123,
        "total": 200.00,
        "items": [ ... ]
    }

HttpMessageConverter converts HTTP request/response body, which is automatically configured by Spring using @ResponseBody annotation.
ResponseEntity supports more control over HttpServletResponse through header settings. It supportes 'fluent' api, which is a object-oriented API whose design relies extensively on method chaining. Its goal is to increase code legibility by creating a domain-specific language (DSL). ResponseEntity subclasses HttpEntity with fluent API.

    ResponseEntity<String> response = ResponseEntity.ok()
                                                    .lastModified(order.lastUpdated())
                                                    .contentType(MediaType.TEXT_PLAIN)
                                                    .body("Hello Spring");
======================
#                    #
#  Configure for W-  #
#  AR or JAR deplo-  #
#  yment             #
#                    #
======================
SpringBoot auto-configures -
- DispatcherServlet/Web Container/Servlet Container
- Internal configuration to support controllers
- Default resource locations (images, css, JavaScript)
- Default message converters

For WAR packaging,

    @SpringBootApplication
    public class Application extends SpringBootServletInitializer {
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(Application.class);
        }
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }

Embedded Tomcat JARs can cause conflicts when running WAR inside traditional web container which can be avoided by marking Tomcat dependencies as provided.
SpringBoot developer tools enables non-production auto-restart providing faster restart than cold-restart.
======================
#                    #
#  Spring MVC Quick  #
#  Start             #
#                    #
======================

    server.servlet.session.timeout=5m <- 5 minutes

======================
#                    #
#  RESTful Applica-  #
#  tion with Spring  #
#  Boot              #
#                    #
======================

REST is an architecture which describes best practices to expose web services over HTTP. As an application protocol, HTTP emphasizes both scalability and transportation. REST - REpresentational State Transfer. REST relies on HTTP for simplicity, ubiquity and interoperability. Uses of REST - Mobile Apps, Microservices, SPA and AJAX in web browser.
SPAs (Single-Page Applications) and AJAX (Asynchronous JavaScript and XML) work together in browsers: a SPA loads one main HTML page and uses AJAX (like the fetch API or XMLHttpRequest) for background requests to fetch data (JSON/XML) and update parts of the page dynamically without full reloads, creating a fluid experience.
REST principle -
    limited HTTP methods + accessible resource URIs -> Uniform interface

HATEOAS (Hypermedia as the Engine of Application State) is a core constraint of REST architectural style where responses from a server include hypermedia links (like URLs) that guide the client on what actions they can take next, making the API self-discoverable and decoupling client and server. Instead of clients needing hardcoded knowledge of endpoints, they follow these dynamic links, similar to clicking links on a webpage, allowing APIs to evolve independently. HATEOAS sample response -

    {
        "orderId": 123,
        "status": "Processing",
        "_links": {
            "self": { "href": "/orders/123" },
            "customer": { "href": "/customers/456" },
            "cancel": { "href": "/orders/123/cancel" }
        }
    }

Principles of REST -
> No HttpSession usage
> GETs can be cached on URL
> Recommends clients to keep track of the state
> Scalability
> Looser coupling between client and server

SOAP (Simple Object Access Protocol) defines an XML-based messaging standard; HTTP is the common transport protocol used to send these messages; and WS-Security (Web Services Security) provides standard extensions for security like encryption and digital signatures. WS-Security components (signatures, encryption) are added to the SOAP Header.
Jakarta EE is a set of specifications for building cloud native java applications. JAX-RS - Jakarta RESTful Web Services - is a Jakarta EE specification with implementations such as Jersey, RESTEasy and Apache CXF.
======================
#                    #
#  Implementing HT-  #
#  TP GET and PUT    #
#                    #
======================
Requirements for using Spring MVC to support REST -
> Map requests based on HTTP method (GET, POST, etc.)
> Define response status (404, 204, 201, etc.)
> Message Converters
> Access request and response body data
> Build valid Location URIs (as part of HTTP POST handling)
@GetMapping is a “composed” annotation. @RequestMethod enumerates GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS, TRACE.

    @GetMapping("/store/orders") -or- @RequestMapping(path="/store/orders", method=RequestMethod.GET)

HTTP GET method returns a java object which is automatically converted by message converter based on 'accept' request header. Message converters also converts incoming request body in HTTP PUT or POST methods to java object based on content type. To enable converters, @ResponseBody is used.

    @GetMapping("/store/orders/{id})
    public ResponseEntity<Order> getOrder(@PathVariable long id) {
        Order order = orderService.find(id);

        return ResponseEntity.ok()
                             .lastModified(order.lastUpdated())
                             .body(order);
    }

HTTP status codes :-
> 200 OK
> 201 CREATED
> 30x REDIRECTS
> 400 BAD REQUEST
> 404 NOT FOUND
> 405 HTTP METHOD NOT SUPPORTED
> 406 NOT ACCEPTABLE
> 415 UNSUPPORTED MEDIA TYPE
> 500 INTERNAL SERVER ERROR

@ResponseStatus method annotation is used to specify the status code of methods.

    @PutMapping("/store/orders/{orderId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@PathVariable Integer orderId,
                            @PathVariable Integer itemId,
                            @RequestBody Item item)
    {
        orderService.findOrderById(orderId).updateItem(itemId, item);
    }
======================
#                    #
#  Implementing HT-  #
#  TP POST and DEL-  #
#  ETE               #
#                    #
======================
HTTP POST requirements :-
> Responds only to POST method
> Access data in request
> Return 201 CREATED status
> Generate Location header for newly created resource

    POST /store/orders/123/items                        HTTP/1.1 201 Created
    Host: shop.io                                       Date: ...
    Content-Type: application/json                      Content-Length: 0
                                                ==>>    Location:
    Body:                                                   http://shop.io/store/orders/123/items/abc
    {
        "cost": 50.00,
        "product": SKU9988, ...
    }

Location header is provided by UriComponentBuilder which allows explicit creation of URI but requires hard-coded URLs, and ServletUriComponentsBuilder which is a subclass of UriComponentBuilder provides access to the URL that invoked the current controller method. 

    @PostMapping("/store/orders/{orderId}/items")
    public ResponseEntity<Void> createItem(@PathVariable Long orderId, @RequestBody Item item)
    {
        orderService.findOrderByItem(orderId).addItem(item);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequestUri()
                            .path("/{itemId}") // Adds to end of calling URI
                            .buildAndExpand(itemId) // produces proper URI converting non-supported characters in itemId. 
                                                    // e.g. whitespace is converted to %20
                            .toUri();

        return ResponseEntity.created(location) // places URI in location headers
                             .build();
    }

HTTP DELETE requirements :-
> Responds only to DELETE requests
> Return empty response, status 204

    DELETE /store/orders/123/items/abc                  HTTP/1.1 204 No Content
    Host: shop.spring.io                   ==>>         Date: ...
    Content-Length: 0                                   Content-Length: 0

    @DeleteMapping("/store/orders/{orderId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Integer orderId,
                           @PathVariable Integer itemId)
    {
        orderService.findOrderById(orderId).deleteItem(itemId);
    }
======================
#                    #
#  Utilize RestTem-  #
#  plate to invoke   #
#  RESTful services  #
#                    #
======================
HTTP Method	    RestTemplate Method
-----------     -------------------
DELETE	        delete(String url, Object... urlVariables)
GET	            getForObject(String url, Class<T> responseType, Object... urlVariables)
HEAD	        headForHeaders(String url, Object... urlVariables)
OPTIONS	        optionsForAllow(String url, Object... urlVariables)
POST	        postForLocation(String url, Object request, Object... urlVariables)
	            postForObject(String url, Object request, Class<T> responseType, Object... urlVariables)
PUT	            put(String url, Object request, Object... urlVariables)
PATCH	        patchForObject(String url, Object request, Class<T> responseType, Object... urlVariables)

    public class MyClientClass {
        private RestTemplate template;                          -or-    RestTemplate template = new RestTemplate();

        @Autowired
        public MyClientClass(RestTemplateBuilder builder) {
            this.template = builder.build();
        }

        final String uri = "http://shop.io/store/orders/{orderId}/items";

        Item[] items     = template.getForObject(uri, Item[].class, "1");
        Item item = new Item();
        URI location     = template.postForLocation(uri, item, "1");
        item.setAmount(2);
                           template.put(location, item);
                           template.delete(location);

        ResponseEntity<Order> response =
            template.getForEntity(uri, Order.class, "1");
        assert(response.getStatusCode()).equals(HttpStatus.OK);
        long modified = response.getHeaders().getLastModified();
        Order order = response.getBody();

        RequestEntity<Item> request    =
            RequestEntity.post(new URI(itemUrl))
                         .getHeaders().add(HttpHeaders.AUTHORIZATION, "Basic " + getBase64EncodedLoginData())
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(newItem);
        ResponseEntity<Void> response  =
            template.exchange(request, Void.class);     // exchange() is a generic method
        assert(response.getStatusCode()).equals(HttpStatus.CREATED);
    }

RestTemplate won't be further developed and not deprecated. Modern use-cases such as streaming, needs reactive non-blocking API calls which is performed by WebClient. Instead of returning raw objects, WebClient returns Mono<T> (for a single object) or Flux<T> (for a list/stream of objects). These are "Publishers" that you subscribe to.
======================
#                    #
#  Spring Boot Tes-  #
#  ting              #
#                    #
======================
SpringBoot testing f/w is built on top of Spring testing f/w.

Integration testing > @SpringBootTest
Slice testing       > Web-layer
                      ---------
                      @WebMvcTest, @WebFluxTest

                      Data-layer
                      ----------
                      @DataJpaTest, @DataJdbcTest, @JdbcTest, @DataMongoTest, @DataRedisTest

                      Specific-component
                      ------------------
                      @MockBean

Testing Dependencies with spring-boot-starter-test

    JUnit: JUnit 5 is default version (from Spring Boot 2.2)
    Spring Test & Spring Boot Test: Testing annotations
    AssertJ: A fluent assertion library
    Hamcrest: A library of matcher
    Mockito: A Java mocking framework
    JSONassert: An assertion library for JSON
    JsonPath: XPath for JSON

Integration Testing with @SpringBootTest

    Automatically searches for @SpringBootConfiguration
        An alternative to @ContextConfiguration for creating application context for testing
        Use @SpringBootTest for integration testing and use @ContextConfiguration for slice testing

    Provides support for different webEnvironment modes
        RANDOM_PORT, DEFINED_PORT, MOCK, NONE

    The embedded server gets started by the testing framework
        When RANDOM_PORT, DEFINED_PORT are used
        Integration testing can be done as part of CI/CD pipeline

    Auto-configures a TestRestTemplate

Integration Testing with TestRestTemplate

    Convenient alternative of RestTemplate suitable for integration tests
        Takes a relative path (instead of an absolute path)
        Fault tolerant: it does not throw an exception when an error response such as 404 is received from the server application
        Configured to ignore cookies and redirects

    If you need customizations
        Use RestTemplateBuilder
        Examples: add custom message converters, setting connection or read timeouts

    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    public class AccountClientBootTests {
        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        public void addAndDeleteBeneficiary() {

            String addUrl = "/accounts/{accountId}/beneficiaries";
            URI newBeneficiaryLocation = restTemplate.postForLocation(addUrl, "David", 1);
            Beneficiary newBeneficiary = restTemplate.getForObject(newBeneficiaryLocation, Beneficiary.class);
            assertThat(newBeneficiary.getName()).isEqualTo("David");

            restTemplate.delete(newBeneficiaryLocation);

            ResponseEntity<Beneficiary> response =
                    restTemplate.getForEntity(newBeneficiaryLocation, Beneficiary.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
======================
#                    #
#  Perform MockMVC   #
#  testing           #
#                    #
======================

    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @SpringBootTest(webEnvironment = WebEnvironment.MOCK)
    @AutoConfigureMockMvc
    public final class AccountControllerTests {

        @Autowired
        MockMvc mockMvc;

        @Test
        public void testBasicGet() {
            mockMvc.perform(put("/accounts/{accountId}", "12345678") <-- MockHttpServletRequestBuilder (get, put, post or delete)
                            .accept("application/json")
                            .content("{ ... }")
                            .contentType("application/json")
                    )
                   .andExpect(status().isOk());
        }
    }

Method	        Description
param	        Add a request parameter – such as param("myParam", 123)
requestAttr	    Add an object as a request attribute. Also, sessionAttr does the same for session scoped objects
header	        Add a header variable to the request. Also see headers, which adds multiple headers
content	        Request body
contentType	    Set content type (Mime type) for body of the request
accept	        Set the requested type (Mime type) for the expected response
locale	        Set the local for making requests

perform() returns ResultActions object which can be chained (fluid syntax) with andExpect() with static argumnets like content() and jsonPath() from MockMvcResultMatchers. These arguments methods provides specific assertions using Hamcrest.

Method	    Matcher Returned	    Description
content	    ContentResultMatchers	Assertions relating to the HTTP response body
header	    HeaderResultMatchers	Assertions on the HTTP headers
status	    StatusResultMatchers	Assertions on the HTTP status
xpath		                        Search returned XML using Xpath expression
jsonPath		                    Search returned JSON using JsonPath

Other fluid methods that can be chained with perform() are print(), andDo() and andReturn().
======================
#                    #
#  Perform Slice T-  #
#  esting            #
#                    #
======================
Slice testing refers to isolated testing within a slice of the application
- Web slice
- Repository slice
- caching slice
and dependencies need to be mocked.

@Mock
    From Mockito framework
    Use it when Spring context is not needed

@MockBean
    From Spring Boot Framework
    Use it when Spring context is needed
    Creates a new mock bean when it is not present in the Spring context or replaces a bean with a mock bean when it is present

    @WebMvcTest(AccountController.class) // WebMvcTest = MockMvc, @MockitoBean
    public class AccountControllerBootTests {
        @Autowired
        private MockMvc mockMvc;
        @MockitoBean
        private AccountManager accountManager;
        @Test
        public void accountDetails() throws Exception {

            // arrange
            given(accountManager.getAccount(anyLong()))
                    .willReturn(new Account("1234567890", "John Doe"));

            // act and assert
            mockMvc.perform(get("/accounts/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value("John Doe"))
                .andExpect(jsonPath("number").value("1234567890"));

            // verify
            verify(accountManager).getAccount(anyLong());
        }
    }

Repository Slice Testing with @DataJpaTest

    Can be used when a test focuses only on JPA components
        Loads @Repository beans, excludes all other @Components

    Auto-configures TestEntityManager
        Alternative to EntityManager for use in JPA tests
        Provides a subset of EntityManager methods
            Just those useful for tests
            Helper methods for common testing tasks such as persistFlushFind(), persistAndFlush()

    Uses an embedded in-memory database
        Replaces any explicit or auto-configured DataSource
        The @AutoConfigureTestDatabase annotation can be used to override these settings