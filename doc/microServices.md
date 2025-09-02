# 1  What are micro-services?
    SOA ---> Combn. of independent small services
           > Loosely Coupled (idenpendent deployment and multi port configuration)
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