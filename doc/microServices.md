# 1  What are micro-services?
    SOA ---> Combn. of independent small services
           > Loosely Coupled (idenpendently deployable and multi-port configurable)
           > Complex coordination and increased maintenance cost 

           > Multiple STS projects connected with load balancer, API gateway and service discovery
# 2  What is monolithic?
        ---> Architecture style 
           > Entire application runs on single port
           > Single deployment and tightly coupled
           > Small modifications affects the whole application
# 3  What is Spring Boot?
    F/w ---> builds Restful APIs, web applications
           > Auto-configures embedded servers like Tomcat, embedded databases like h2
           > Reduces the broilerplate codes
           > Internally handles dependencies
           > stand-alone application with less configuration
# 4  Rest v. Soap
                Rest            |           SoAP
    Architectural style         |       Protocol
diff. data format (Json, XML)   |   only XML
Requires less b/w               |   high b/w
more preferred                  |   less preferred

# 5  Controller v. RestController
            Controller          |      RestController
    Used to receive only the    |  Used to handle the request and
request from UI and returns VIEW|  response from UI
    @ResponseBody is used for   |   It is combn. of @ResponseBody
sending response                |  and @Controller
# 6  ResponseEntity
    > It is a class provided by Spring f/w
    > Represents whole HTTP response
    > It includes status code, header and body
# 7  @SpringBootApplication
    > Comb. of @Configuration, @EnableAutoConfiguration and @ComponentScan
    > @Configuration - source of bean defn. for Spring IoC container
    > @ComponentScan - scan for beans in package or subpackages
    > @EnableAutoConfiguration - auto-configures beans in classpath and configures them to run the method
# 8  @Qualifier
    > distinguish between one or more bean definition
# 9  @Bean
    > bean producing method. It is usually present inside @Configuration class
# 10 @Primary
    > default bean definition
# 11 @Entity
    > Maps the fields of the class with database columns
# 12 @Required
    > @Required annotation ensures that a specific property must be injected with a value.
    > If the property is not injected, Spring throws a BeanInitializationException.
    > The @Required annotation is used in conjunction with setter methods.
    > Always use the @Value annotation or other dependency injection mechanisms to provide values for @Required properties.
# 13 @Value
    > It is used to read spring env var and sys var
    > It assigns default values to var and mthd args
# 14 @RequestParam
    > It is used to extract query parameters from the URI as key & value pairs using '?' and '&' notations.
# 15 @PathVariable
    > It is used to extract data right from the URL
# 16 @Transient
    > De-links entity class variable from database columns
# 17 @Transactional
    > Managing the state of database based on condition, if successful it is stored in db. Otherwise, it returns or rolls back
# 18 What is Put and Patch mapping?
    > Put - overrides existing entity. If fields in new entity are missing, it will override them as default null values.
    > Patch - it transmits partial data that will be updated without changing the data
# 19 How to change server and port?
    > To change the embedded server, we need to exclude default embedded tomcat server and add the dependency of wished server (like jetty, undertow).

    <!--			<exclusions>-->
<!--				<exclusion>-->
<!--					<groupId>org.springframework.boot</groupId>-->
<!--					<artifactId>spring-boot-starter-tomcat</artifactId>-->
<!--				</exclusion>-->
<!--			</exclusions>-->

<!--		<dependency>-->
<!--			<groupId>org.springframework.boot</groupId>-->
<!--			<artifactId>spring-boot-starter-undertow</artifactId>--> <!-- Or spring-boot-starter-jetty -->
<!--			<version>4.0.0-M1</version>-->
<!--		</dependency>-->

    > Port number could be changed with server.port propertiees
    server.port=[ANY_PORT_NUMBER]
# 20 What is Swagger?
    > ...helps build, document and test Restful Web services.
    > ...allows user to describle your entire api including all endpoints and operation on each endpoint.
    > ...to understand capabilities of API without direct access to the source code.
    > ...use dependency in swagger
    > ...activate swagger we need to use {swagger dependency }-
    > by using the endpoint swagger-ui.html we can see the swagger-ui webpage of applicationin chrome.
    E.x.: http://localhost:8080/swagger-ui.html

# 21 What is Rest Template and How it works?
    > Rest template communicates between two APIs. It acts both as server and client.
    > To Autowire the rest template, need to create @Bean method in configuration class.
    > exchange() converts URL into response (url,HttpMethods.GET,null,String.class)

# 22 Cors Policy error:
    > CORS (cross-origin resource sharing).
    > It's a protocol that defines some rules for sharing resources from a different origin/port.
    > Since the client and server are separable applications, they're usually hosted on different domains.
    > Own client requesting data from the server might have different origins/port.
    @Cross origin -> use this in Class OR method LEVEL
# 23 What is spring initializer?
    > It is a web-app to generate Spring Boot project structure with maven or gradle project tool management without any applcation code.
# 24 Dependency injection
    > design pattern to implement Inversion of Control for resolving dependencies at the run-time
    > injecting dependency object to dependent object at run-time
    > Based-on types : constructor
                     - setter
                     - field or property
# 25 Scope of bean
    > Bean scope (@Scope) defines bean creation and presentation throughout the application lifecycle.
    > It defines the visibility and lifecycle of the bean created in the spring container.
    > Scopes - Singleton : A single bean is used throughout the application.
             - Prototype : A new instance is created and injected upon each request for bean
             - Request : A new instance for every HTTP request
             - Session : A new instance for every session
             - Application/GLOBAL : A new instance for every web application runtime.
# 26 Lifecycle of bean
    > Spring IoC manages the lifecycle of bean.
    > At start, container creates a bean instance with dependencies injected. The bean is destroyed when the container is closed.

        container -> bean instantiated -> dependency injected -> init() -> destroy() -> closed
    
    > Annotations: @PostConstruct and @PreDestroy
# 27 API Gateway
    > mediator between client applications and backend services
    > Software layer that acts as a single endpoint for various APIs
    > Adv : 
            - Load balancing - ensures the application is available by balancing the load sent to them
            - micro-services security - An API gateway puts a barrier in front of an application's backend
            - API authentication - Protection against hacking
            - rate limiting - Limits the number of API requests from a client 
    > DisAdv : 
            - Response time - Increased response time as request passes through API gateway
            - reliability - Single-point-of-Failure
            - complexity - Routing logic can make communication with mmicroServices more complex
# 28 Entity Relationship
    > Entity relationship between database entities.
    > Types : one-to-one, one-to-many, many-to-one, many-to-many
    > Tables are joined through foreign key
    > JoinColumn represents foreign key column and cascade property represents the actions on owning entity that should be auto-applied on associated entities
# 29 Joins
    > Combines tables and present them in 'select'
    > Types - (INNER) JOIN - returns matching records on both tables
            - LEFT (OUTER) JOIN - Returns all records from left and matching records from right table
            - RIGHT (OUTER) JOIN - Returns all records from right and matching records from left table
            - FULL (OUTER) JOIN - Returns all records when there is a match in either left ot right table
# 30 Spring Profile
    > Spring boot profile segments parts of application configuration and can be selectively activated using 'spring.profiles.active' in application.properties
    > profiles can be dev, test, prod, stage, etc.
    > Also @Components and @Configuration classes can be marked with @Profile to selectively activate
# 31 What is circuit breaker
    > It is a design pattern to keep microservices running when a service fails
    > states in circuit breaker: open (problem), closed (no problem) and half-open (intermediate).
    > Threshold is fixed after which circuit breaker goes open and sends 500 error
    > It is included with resilience4j and spring actuator is used to monitor health
# 32 Web Security / spring security
    > it is a f/w to build secure web applications
    > Authentication - check credentials are valid
    > Authorization - role based access privilege
# 33 What type of authentication is used in your project
    > JWT - JSON Web Token is used to statefully authenticate HTTP requests.
    > It consists of header (algorithm), payload (data) and signature (secret security code)
    > It is a serialized approach to transfer data
    > Response is sent is JWT is valid
# 34 Static keyword
    > Creation of memory in static memory
    > Static members are called without instantiation
    > Static block is executed only once before main function
    > Static methods cannot be overridden
# 35 Final, Finally, Finalize
    > Final : Non-access modifiers makes the properties of a class immutable
              Final variables cannot be reassigned, Final methods cannot be overridden and Final class cannot be inherited
    > Finally : It is used try-catch block and executes irrespective of exception handling
    > Finalize : Protected method of Object class can be overriden to perform clean up activities before garbage collection
# 36 String and ways of declaring a string
    > An immutable sequence of characters within doubles quotes
    > Ways to declare : String literal (String pool memory) & using new keyword (heap memory)
    > All the String values are created in the string pool memory. Even when a string is concatenated it is created as a new string without affecting the original string. It is called string immutability
# 37    StringBuilder           |           StringBuffer
    > It is async (not thread)  | > It is sync i.e., thread safe 
    2 threads can call the      | 2 threads cannot call the methods
    methods simultaneousily     | simultaneousily
    > more efficient            | > less efficient
    > >Java1.5                  | > >Java1.0
# 38 How to reverse a String?
    > Using String Buffer and Builder 's inbuilt reverse()
# 39 Thread
    > It represents a separate flow of execution within a program. It allows parallel execution.
    > Extending 'Thread' class or Implementing 'Runnable' creates Thread
    > Within synchronised block, wait() causes temporary pause in execution waiting for notify() signal.
    > Generally, sleep() causes temporary pause in execution for specific time period
# 40 OOPS
    > Data abstraction : hiding internal implementation and showing the external functionality through interface or abstract class
    > Encapsulation : hiding (variables) and binding (methods) the data together into a unit. To hide we use private keyword along with getter and setter methods
    > Composition : One object has relationship with another object (loosely coupled) with class fields
    > Inheritance : a class gets all the properties of another class through extends keyword. Type : single-level, multi-level, hierarchial, multiple and hybrid.
    > Polymorphism : programming style to perform different functionalities under same name.
    Types : Method overriding (dynamic binding) child class has same function signature as parent; Method overloading (static binding) function calls varies by parameters.
    > Constructor : Special function with class name as method name and no return type used to initialize instance variables. Types: default constructor (without parameter), parameterized constructor.
    > this & super : 'this' is used to point out class level variables. this() -> same class constructor. 'Super' is used to access immediate parent class properties.
    > Access specifier : controls visibility
        - Private - same class
        - (Default) - same package
        - protected - same package and outside package through child class
        - public - access from anywhere
# 41                Collection           |          Collections
It is a f/w interface extends Iterable   | It is a utility class extends Object
store and manipulate the data            | provides set of methods to perform Collection
has abstract methods >java8 (stat & def) | has only static methods
sub-interfaces like List, Set, etc       | Some methods like sort(), max(), etc.
# 42        List            |           Set             |       Map
extends Collection interface|extnds Collection interface|does not extend coll interface
Ordered coll of objects     |group the unique elements  |key and value pairs
allows Duplicate elements   |doesn't allow duplicates   |doesn't allow duplicate keys
multiple null values        |Only one null value        |Only one null key value
follows insertion order     |doesn't follow insertion   |doesn't follow insertion
Impls - AList, LList, vector|HashSet, TreeSet, Lk HshSet|HashMap, TreeMap, Lk HshMp
# 44 Array | ArrayList
stores the similar data-type collection | implements List interface stores and manipulates
fixed size | dynamic sizeable, default size is 10
Array.length | ArrayList.size()
primitive and Objects | Objects
# 45 What is hashing technique or how hashmap works internally?
Hashing takes the object and convert it to integer based on hash code formula i.e., hash & (n - 1) or hash % n -> means no of buckets. It is used to quickly identify the specific value in the array. Hashing is used to break up a huge amount of data into small tables. It reduces search time and file space. Hash bucket is storing one or more objects which follow Lkd List. Hash collision happens when two objects have same hash value.
# 46 Concurrent Modification
It is the process of modifying objects while another thread is running over them. Normally, it throws exception which could be avoided with iterator, ConcurrentHashMap and CopyOnWriteArrayList.
# 47 Fail fast and fail safe
    > Fail fast aborts the operation on exception. No copy or clone is created. HashMap and ArrayList fail fast.
    > Fail safe tries to prolong the execution before quitting. Copy is created. ConcurrentHashMap and CopyOnWriteArrayList fail safe.
# 48 Functional Interface
    > It is also known as the SAM Interface (Single Abstract Method). It contains only one Abstract method. They are implemented in two ways: inheritance concepts of parents and child relationship, and lambda expressions. 4 pre-defined Functional interfaces: Function, Consumer, Supplier and Predicate.
    > Lambda expression : anonymous functions
    > Method reference : Refer existing methods
# 49 Stream API
    > Java8 feature. Stream interface operates over sequence of objects with various methods which are pipelined structure and can be intermediate or terminal.
    > functions: filter, map, count, distinct, limit, anyMatch, summingInt, sorted, max, min, findFirst, findAny, flatmap
# 50 Marker Interface
    > It does not contain any method or constants but provides run time information to compiler and JVM. It is also known as tagging interface. Types: Serializable, Cloneable and Remote.
# 51 Collectors
    > It is a final class and terminal operation in stream. It collects the stream into collection. e.g.: toList(), toSet(), toCollection(), toUnmodifiableList()
# 52 Terminal | Intermediate
It returns non-stream/another stream values | It returns stream values
Can't be chained together | Can be chained together to form a pipeline structure
Operations can have maximum of one terminal operation | N-number of intermediate operations
# 53 Optional class
    > Java8 features, a final class
    > deals with null-pointer exception
    > Holds non-null values
    > methods: of(), ofNullable(), empty(), isPresent(), orElse()
# 54 Exception
    > Unexpected event the interrupts flow of the program
    > Exception handling with try / catch / finally
    > They are divided into checked and unchecked
    > java.lang.Throwable is inherited by Excpetion and Error
    > throw : raises a new exception / throws : declares a method can throw exception
    > Custom Exception : Exception based on custom requirements
    > Global Exception : A separate class for handling exceptions instead of try-catch blocks everywhere. @RestControllerAdvice (@ControllerAdvice + @ResponseBody) and @ExceptionHandler handles the exception.
# 55 GitHub
    > It is a code hosting platform for version control and collaboration. It lets you and others work together on projects from anywhere.
    > git branch [BRNCH_NAME]/git checkout -b [BRNCH_NAME]
# 56 Junit
    > JUnit is a Java unit testing framework
    > write and execute automated tests
    > Regression testing (test updates don't affect existing functionality)
    > Adv.: Open-source Disadv.: No group testing
    > @Before, @Test, @After, @BeforeAll, @AfterAll, @BeforeClass, @AfterClass
    > assertEquals(), assertNotEquals(), assertNull(), assertNotNull(), assertSame(), assertNotSame(), assertTrue(), assertFalse(), assertArrayEquals()
# 57 Log4j
    > Java based logging f/w. Components: 
    > Logger: Logger is responsible for capturing logging information
    > Appender: publishing logging information in console or files
    > Layout: styling and formatting the logging information
    > methods: trace(), warn(), error(), info() and debug()