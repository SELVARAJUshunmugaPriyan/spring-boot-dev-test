
import java.util.function.Consumer;

@SuppressWarnings({"unused", "FieldMayBeFinal", "UseSpecificCatch",
                   "StringBufferMayBeStringBuilder", "empty-statement",
                   "CallToPrintStackTrace"})
public class Solutions {

    private static final Object key = new Object();
    private static int number = 1;

    public static void main(String[] args) {
        B10 b10 = new Solutions.B10();

        // 7. @Transactional    // Rollback changes upon exception/error
        //    @Transactional(readOnly=true) // readOnly improves performance
        //     ↑ this annotation is provided in import org.springframework.transaction
        //                                                  .annotation.Transactional;

        // 8. @Transient
        //    private type var; // not persisted in DB

        // 15. @Value("${spring.application.name}") is used to get Value property in run-time

        // Tasks:
        /*
         * 1. OTP
         * 2. Excel file -> DB -> PostMan
         * 3. Send mail with file attachment from front-end upload
         */
        // string12BuilderBuffer();
        //thread13();
    }

    // 1. Api Gateway
    class ApiGateway1 {
        // Explanation: API gateway is the collective endpoint manager of
        // all backend services. User access backend services which could
        // be blocked for direct connections through API gateway only. It
        // uses Service discovery like Eureka to register services / apps
        // automatically

        // Step 0: Add dependencies
        // implementation 'org.springframework.boot:spring-boot-starter-webflux'
        // implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webflux'

        // Step 1: Application.yml in api-gateway
        /*
         *  spring:
         *      main:
         *          web-application-type: reactive
         *      cloud:
                    gateway:
                        server:
                            webflux:
                                discovery:
                                    locator:
                                        enabled: true
                                        lower-case-service-id: true
                                # routes:
                                #   - id: service-a
                                #     uri: lb://service-a
                                #     # uri: http://localhost:1234
                                #     predicates:
                                #       - Path=/service-a/**
                                #     filters:
                                #       - StripPrefix=1

                                #   - id: service-b
                                #     uri: lb://service-b
                                #     # uri: http://localhost:1235
                                #     predicates:
                                #       - Path=/service-b/**
                                #     filters:
                                #       - StripPrefix=1

                                #   - id: service-c
                                #     uri: lb://service-c
                                #     # uri: http://localhost:1236
                                #     predicates:
                                #       - Path=/service-c/**
                                #     filters:
                                #       - StripPrefix=1 # This filter is important
         */

         // NOTE @PathVariable("placeholder") must be used with placeholder in services
    }

    // 2. Circuit breakers/actuators
    class CircuitBreak2 {
        // Explanation: It allows microservices keep functioning even one of the service fails.
        // Step 0: Add dependencies
        // implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j';

        // Step 1: configure application.yml
        /*
         * spring:
         *  cloud:
                gateway:
                    server:
                        webflux:
                            routes:
                                - id: service-a
                                  uri: lb://service-a
                                  predicates:
                                    - Path=/service-a/**
                                  filters:
                                    # - StripPrefix=1 # It is important to comment stripPrefix
                                                        out
                                    - name: CircuitBreaker
                                      args:
                                        name: userServiceCircuitBreaker
                                        fallbackUri: forward:/test
            management:
                endpoints:
                    web:
                        exposure:
                            include: '*'

                resilience4j:
                    circuitbreaker:
                        instances:
                            userServiceCircuitBreaker:
                                registerHealthIndicator: true
                                slidingWindowSize: 5
                                minimumNumberOfCalls: 3
                                failureRateThreshold: 50
                                waitDurationInOpenState: 1s # this increases or decreases the
                                                              waiting time before circuit
                                                              breaking
                                permittedNumberOfCallsInHalfOpenState: 2
         */
    }

    // 3. Spring profiles
    class Profile3 {
        // Exaplanation: Application properties can be segmented into different files can be
        // selectively activated by spring.profiles.active in default application.yml. The
        // files could by application-dev/prod/test.yml. e.g.:
        // spring:
        //   profiles:
        //     active: dev/prod/test
    }

    // CHECKME How to enable connections only through api gateway not
    // directly to backend servers

    // 4. Singleton class
    class A4 {
        A4 a4 = null;
        private A4() {}
        public A4 getA4() {
            if (a4 == null)
                a4 = new A4();
            return a4;
        }
    }

    // 5. Entity relationship
    class Entity5Relationship {
        // Explanation: Entity relationships refer to how different data entities are connected
        // to each other within a relational database. These relationships are managed by 
        // Spring Data JPA and Hibernate, which map the object-oriented relationships in your 
        // Java code to the underlying database schema.
        /*
         * One-to-One (@OneToOne): This relationship exists when one entity instance is 
         * associated with exactly one instance of another entity. For example, a User 
         * entity might have a one-to-one relationship with a UserProfile entity, where each
         * user has one profile and each profile belongs to one user.
         * 
         * One-to-Many / Many-to-One (@OneToMany, @ManyToOne): This is a common relationship
         * where one entity instance can be associated with multiple instances of another 
         * entity, while each of those multiple instances is associated with only one instance 
         * of the first entity. For example, a Department can have many Employees, but each 
         * Employee belongs to only one Department.
         * 
         * @OneToMany(mappedBy = "manager", ...): mappedBy = "manager" tells JPA that the manager
         * field in the Employee entity is the owner of this relationship (i.e., the foreign key 
         * is defined on the manager side).
         * 
         * Many-to-Many (@ManyToMany): In this relationship, multiple instances of one entity 
         * can be associated with multiple instances of another entity. For example, a Book 
         * can have multiple Authors, and an Author can write multiple Books.
         * 
         * @JoinColumn(name = "manager_id"): This specifies the foreign key column in the 
         * Employee table that will store the ID of the manager.
         * 
         * NOTE: @Column(name="managerId", insertable=false, updatable=false)
         *       private Integer managerId;
         *          <- insertable, updatable is necessary to duplicate exception
         * 
         * @ManyToOne(fetch=FetchType.LAZY)
            @JoinColumn(name="managerId")
            @SuppressWarnings("unused")
            private ApiGatewayEntity manager;

            @OneToMany(mappedBy="manager", cascade=CascadeType.ALL, orphanRemoval=true)
            private Set<ApiGatewayEntity> directReports = new HashSet<>();
         */
    }

    // 6. Eureka
    class Eureka6 {
    // Explanation: Eureka is a service discovery and registeration
    // automation tool in micro-service architecture. It registers services
    // based on spring.application.name and registered clients can discover
    // other registered clients through this lookup name wihtout having to
    // hard code the ip address. In effect, it performs backend dns lookup.
    // discoveryClient is used to discover services programmatically.

    // Steps to configure Eureka server and client:

    // Step 0: Add dependencies
    // implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
    // ...to Eureka server
    // implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    // ...to Eureka client

    // Step 1: Eureka server application.yml :

    /*
     *  spring:
            application:
                name: eureka-server

        server:
            port: 8761

        eureka:
            client:
                register-with-eureka: false
                fetch-registry: false
     */

     // Step 2: Add @EnableEurekaServer to EurekaServerApplication.class
    //  (main springboot class)

     // Step 3: Eureka client application.yml :
     /* spring:
            application:
                name: service-b # It is important not to have underscore in
                application name

        eureka:
            client:
                serviceUrl:
                    defaultZone: http://localhost:8761/eureka/
       */
     // Step 4: Define bean in ServiceApplication.class (main SBoot class)

     /*
      * @Bean
	  * @LoadBalanced // Load balance looks up service name against service
                      // address in eureka server
	  * RestTemplate restTemplate()
      */

      // Step 5: Autowire DiscoveryClient to get information on services and instances.

      // discoveryClient.getServices()
      // discoveryClient.getInstances(serviceId)

      // Step 6: Autowire restTemplate to fetch services
      // restTemplate.getForObject("http://serviceId/", ResponseType.class)
    }

    // 9. Swagger
    class Swagger {
        // Explanation: OpenAPI documentation tool.
        // Step 0 : add dependency
        /*
         *  <dependency>
                <groupId>org.springdoc</groupId>
                <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                <version>3.0.0-M1</version>
            </dependency>
         */
        // Fix duplicate bean error by disabling one bean definition by
        // @EnableAutoConfiguration(exclude = {ValidationAutoConfiguration.class})
    }

    // 10. static class
    static class B10 { }

    // 11. final class
    final class C11 { }
    // class SubC11 extends C11 { } // Cannot extend

    // 12. String builder and String buffer
    private static void string12BuilderBuffer() {
        // 12. StringBuilder/Buffer
        // BufferVsBuilder.class
        Consumer<Void> bufferVsBuilder = (x) -> {
            StringBuffer sb1 = new StringBuffer("Buffer");
            StringBuilder sb2 = new StringBuilder("Builder");

            Runnable task = () -> {
                for (int i = 0; i < 1000; i++) {
                    sb1.append('x');
                    sb2.append('b');
                }
            };

            Thread thread1 = new Thread(task);
            Thread thread2 = new Thread(task);

            thread1.start();
            thread2.start();

            try {
                thread1.join();
                thread2.join();
            } catch (Exception e) {
                ;
            }

            System.out.println("String buffer length: " + sb1.length() +
                    "\nString builder length: " + sb2.length());
        };
        Consumer<Void> stringBufferVsStringBuilder = (x) -> {
            long start, end;
            StringBuffer s1 = new StringBuffer("x");
            start = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                s1.append("x");
            }

            end = System.nanoTime();
            System.out.println("StringBuffer time: " + (end - start));

            StringBuilder s2 = new StringBuilder("x");
            start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                s2.append("x");
            }

            end = System.nanoTime();
            System.out.println("StringBuilder time: " + (end - start));
        };

        //bufferVsBuilder(null);
        stringBufferVsStringBuilder.accept(null);
    }

    // 13. thread
    private static void thread13()
        throws InterruptedException {
        // 13. Thread
        // MyThreadWait.class
        Consumer<Void> myThreadWait = (x) -> {
            Thread t1 = new Thread(() -> {
                synchronized (key) {
                    while (number <= 9) {
                        if (number % 2 == 1) {
                            System.out.println("Thread 1 number: " + number++);
                            try {
                                key.notify();
                                if (number < 10)
                                    key.wait();
                            } catch (Exception e) {
                                ;
                            }
                        }
                    }
                }
            });

            Thread t2 = new Thread(() -> {
                synchronized (key) {
                    while (number <= 10) {
                        if (number % 2 == 0) {
                            System.out.println("Thread 2 number: " + number++);
                            try {
                                key.notify();
                                if (number < 11)
                                    key.wait();
                            } catch (Exception e) {
                                ;
                            }
                        }
                    }
                }
            });

            t1.start();
            t2.start();
        };
        Consumer<Void> useMyThread = (x) -> {
            Thread t1 = new MyThread();

             MyRunnable mr = new MyRunnable();
             Thread t2 = new Thread(mr);

            System.out.println("main starts");

            t1.start();
            //  t1.join();
            t2.start();
            //  t2.join();

            Thread t3 = new Thread(() -> {
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Thread 1 " + i);
                }
            });

            t3.start();
            // t3.join();

            System.out.println("main ends");
        };
        Consumer<Void> usemmThread = (var x) -> {
            System.out.println("main starts");

            Thread t1 = new Thread(() -> {
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Thread 1 " + i);
                    // try {
                    //     Thread.sleep(1000);
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                }
            });

            Thread t2 = new Thread(() -> {
                for (int i = 1; i <= 3; i++) {
                    System.err.println("Thread 2 " + i);
                    // try {
                    //     Thread.sleep(1000);
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                }
            });

            t1.start();
            t2.start();

            System.out.println("main   ends");
        };

        // myThreadWait(null);
        // useMyThread.accept(null);
        usemmThread.accept(null);
    }

    // 16. Changing embedded server
    class Embedded16Server {
        // Explanation: To change an embedded server
        // Step 0 : Modify dependencies
        /*
         *  <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-webflux</artifactId>
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

            implementation('org.springframework.boot:spring-boot-starter-web') {
                exclude group: 'org.springframework.boot', module: 'spring-boot-starter-tomcat'
            }
            implementation 'org.springframework.boot:spring-boot-starter-undertow'
         */
    }

    // 17. Spring Security/JWT
    class Spring17SecurityJWT {
        // Explanation : Spring Security is used to secure Web Applications with defence,
        // authentication and authorization. Using JWT, it statefully authenticates stateless
        // Http requests.

        // Step 0 : add dependencies :
        // implementation 'org.springframework.boot:spring-boot-starter-security';
        // implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server';
        // implementation 'org.springframework.security:spring-security-oauth2-jose';

        // Step 1 : add configuration with securityFilterChain bean and add @EnableWebSecurity
        //              to main application class
        // @Configuration
        // public class ApiGatewayConfiguration {
            // @Bean
            // public SecurityFilterChain filterChain(HttpSecurity http)
            //     throws Exception {
            /*
             *          http
             *              .authorizeHttpRequests((authorize) -> authorize
             *                                                              .anyRequests()
             *                                                              .authenticated()
             *              )
             *              .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer
             *                                      .withDefaults()))
             *              .formLogin(Customizer.withDefaults())
             *              .sessionManagement((session) -> session
             *                  .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
             *
             *          return http.build();
             */
            // }
            // @Bean
            // UserDetailsService userDetailsService() {
            /*
             *      UserDetailService user = User
             *                              .withUserName("admin")
             *                              .password(passwordEncoder().encode("weden19%"))
             *                              .roles("USER")
             *                              .build();
             *
             *      return new InMemoryUserDetailsManager(user);
             */
            // }

            // @Bean
            // PasswordEncoder passwordEncoder() {
            /*
             *      return new BCryptPasswordEncoder();
             */
            // }

            // @Bean
            // public JwtDecoder jwtDecoder() throws Exception {
            //     KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            //     kpg.initialize(2048);
            //     KeyPair kp = kpg.generateKeyPair();
            //     RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
            //     RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();

            //     JWSSigner signer = new RSASSASigner(privateKey);
            //     JWTClaimsSet claims = new JWTClaimsSet.Builder()
            //             .subject("Shunmuga Priyan Selvaraju")
            //             .issuer("dev-issuer")
            //             .audience("test-audience")
            //             .issueTime(new Date())
            //             .expirationTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
            //             .claim("role", "developer")
            //             .build();

            //     SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).build(), claims);
            //     jwt.sign(signer);

            //     System.out.println("JWT:");
            //     System.out.println(jwt.serialize());

            //     return NimbusJwtDecoder.withPublicKey(publicKey).build();
            //      return new SupplierJwtDecoder(() -> JwtDecoders.fromIssuerLocation(issuerUri).build());
            // }
        // }
    
        //  Step 2 : Add configuration to Application.yml
        //  spring:
        //      security:
        //          oauth2:
        //              resourceserver:
        //                  jwt:
        //                      # issuer-uri: http://localhost:9000
        //                      # audience: cashcard-client
        //                      public-key-location: classpath:id_rsa
    }

    // 18. this/super
    class D18 {
        int i;
        public D18() {
            this(0);
        }
        public D18(int i) {
            this.i = i;
        }
    }
    class E18 extends D18 {
        public E18() {
            super();
            super.i = 0;
        }
    }
}

class MyThread extends Thread
{
    @Override
    public void run() {
        System.out.println("Thread executes");
    }
}

class MyRunnable implements Runnable
{

    @Override
    public void run()
    {
        System.out.println("Runnable executes");
    }

}