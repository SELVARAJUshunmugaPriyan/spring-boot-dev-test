===================
  BEFORE SECURITY
===================
Neglecting to secure our API's endpoints has at least three consequences:

    The content is public - you can't control who sees the information
    The content is anonymous - you can't know who is asking
    The content is unprotected - bad actors can take advantage of Browser-based vulnerabilities

Attacks if content is public ...
server-side request forgery (SSRF)

Attacks if content is unprotected ...
Cross Site Request Forgery (CSRF), Manipulator-in-the middle (MITM), Cross Site Scripting (XSS)

While a 404 Not Found isn't a bad thing, when left unsecured like this, it can lead to Fingerprinting, Directory Traversal and other enumeration-like vulnerabilities.
===================
  SPRING SECURITY
    (FUNCTIONS)
===================
Spring Security helps with three things:

    Defense: determining if the request is safe to process
    Authentication: determining who is making the request
    Authorization: determining if they have the needed permissions

Secure by Default is a principle that encourages you to ensure that your default settings are secure

Principle of Least Privilege says that you should offer only the information that the end user is privileged to know. This principle that encourages you to think about each piece of data as a privilege to possess and to give end users the lowest privileges possible that let them do their work well.

Spring Boot works to configure your application with the following defaults for a REST API:

1.  Requires authentication for all requests:
____________________________________________
        
    Spring Security applies a Servlet Filter that inspects every request and rejects it if the request is unauthenticated.

__NOTE_ABOUT_SERVLET__
        The Java Servlet API is the module in Java for processing HTTP requests inside of an application. A servlet handles HTTP requests and yields an HTTP response. You can think of a servlet like a "mini-server". In Spring terms, Spring MVC constitutes a single servlet, Spring Security constitutes a set of filters, and Spring Boot ships with an embedded container/IOC container that performs the various dispatches needed to service a single request.
        
        Under servlet terminology, a given HTTP request can pass through multiple dispatches. A dispatch represents a single pass/process an HTTP request makes through a set of filters and its target servlet. Typically, an HTTP request first passes through the REQUEST dispatch, but can also subsequently pass through the ERROR dispatch, the FORWARD dispatch, and others.
        
        Each dispatch can be intercepted by multiple filters on its way to a single servlet. A filter intercepts HTTP requests to handle cross-cutting/different concerns. Typically, filters either enrich the request in some way or they deny the request, preventing it from reaching the servlet.

        All of this means that Spring Security defaults require that every dispatch be authenticated.

        Spring Boot App : { 
            REQUEST dispatch : 
                web request -> 
                    filters (Spring Security) -> 
                        servlet (Spring MVC),
            ERROR dispatch
            .
            .
            .
            FORWARD dispatch 
                          }

    E.g.: For security purposes, even which "URIs are valid" is a privileged information. You can imagine if someone requested index.jsp or /admin. If Spring Security returned a <404> in those cases instead of a <401>, that would mean <404> is a hint to a bad actor that a given endpoint exists! The bad actor can use this hint to enumerate the REST APIs valid URIs, figure out underlying vulnerable technologies, and accelerate their attack.


    ---
2.  Responds with secure headers for all request:
_________________________________________________

    HTTP headers allow a client and server to exchange additional information between each other in an HTTP request and response.

    Solutions to unsecured headers :
    - Cache-Control
        Spring Security applies secure settings for Cache-Control and other headers to mitigate this class (that response could be cached in the browser for later retrieval by a bad actor) of vulnerabilities.
    - Strict Transport Security Header
        MITM attacks are mitigated by HTTPS, and the Strict Transport Security header tells the browser to not send any requests to this REST API over HTTP (autoupgrade to HTTPS).
    - X-Content-Type-Options
        This header tells browsers to not try to guess (& execute) the content type of a response. For example, it is possible for an image to contain scripting content and the browser can be tricked into guessing and executing steal-my-password.jpg as JavaScript. Spring Security addresses this by issuing a secure setting for X-Content-Type-Options by default.

    ---
3.  Requires CSRF mitigation for all requests with side-effects:
_______________________________________________________________

    This request ( <img src="https://mybank.example.org/account/32?transfer=25&toAccount=45" /> ) will be executed by the browser. This works because the browser has no way of knowing whether the URL points to an image until the response comes back. By then, the damage has already been done. Browsers, by default, will send all of mybank.example.org 's cookies and HTTP Basic credentials to it by default as well. 

    Because of that, Spring Security automatically protects these endpoints with side-effects, like POSTs, PUTs, and DELETEs. It does this by sending a special token to the client that it should use on subsequent requests. The token is transmitted in such a way that third parties cannot see it. So when it is returned, Spring Security believes that it is legitimately from the client.

    ---
4.  Allows for HTTP Basic authentication with a default user:
____________________________________________________________

    Spring Security generates a default user called 'user'. Mainly, its password is auto-generated on each startup. The reason for that is so that if you accidentally deploy Spring Security defaults to production, no one can use the default username and password to command your application (Secure By Default example).

    To find out the password, you can look at the Boot startup logs for this string:
            Using generated security password: fc7e0357-7d82-4a9c-bae7-798887f7d3b3
    The UUID in that string is the password. 

    ---
5.  Responds RESTfully to security failures:
___________________________________________

    401 Unauthorized status code when credentials are wrong or missing
    403 Forbidden status code when credentials are good, but the request isn't authorized

        Other status codes : 
        400 Bad request

    ---
6.  Protects against malicious requests with an application firewall
____________________________________________________________________

    Spring Security helps you with this by adding an application firewall that, by default, rejects requests that contain double encoding and several unsafe characters like carriage returns and linefeeds.

    ---
===================
  SPRING SECURITY
    (WORKING)
===================
Spring Security as a framework for authenticating requests. This means confirming:

    - The caller's identity - who made the request, sometimes called the client or the agent
    - The principal's identity - who the request is about, often an end user
    - The request's integrity - proof that the request wasn't modified by an intermediary

you can think of Spring Security's authentication support in three parts, regardless of which authentication scheme it uses:

    - It parses the request material into a credential.
    - It tests that credential.
    - If the credential passes it translates that credential into a principal and authorities.

Authentication <Class> ...has

    Principal ("who")
    Credentials ("proof")
    Authorities ("permissions")

HTTP Basic is an example of a stateless authentication scheme. Since it remembers nothing from previous requests, you need to give it the username and password on every request. Spring Security activates HTTP Basic and Form Login authentication schemes by default.

HttpBasic : { 
    user -<gives password>-> 
        client(app) -<authorization: basic>-> 
            restAPI(data)
            }

Limitations of HttpBasic:
    - It uses a long-term credential that requires the end user to change it.
    - Authorization Bypass (application to only use your username and password for purposes that you want them to)
    - Single Data Exposure (a single HTTP request being intercepted or a single memory dump of a client application could reveal your password)
===================
    OAuth & JWT
===================
User -> Client (app) -<send token>-> Resource (your data)
<Ask for |  ↑
 token>  ↓  | <Give token>
        Authz (OAuth 2.0) 

JSON Web Token (JWT) is an industry standard format for encoding access tokens. Within JWT, Claims are facts that the token is asserting, like what principal the token represents. They are called "claims" because they still need to be verified by the resource server – the JWT "claims" these facts to be true.

JWT : { Headers {
            type -> "typ": "JWT",
       algorithm -> "alg": "RS256"
                }, 
        Claims  {
        audience -> "aud": "https://cashcard.example.org",
      expires on -> "exp": 1689364985,
       issued on -> "iat": 1689361385,
          issuer -> "iss": "https://issuer.example.org",
 permissions set -> "scp": ["cashcard:read", "cashcard:write"],
       principal -> "sub": "sarah1"
                }
        }

Part of the minting process is for the authorization server to sign the JWT. A good signature must be non-repudiation that the contract was signed by you and only you. Then, the resource server verifies that signature. This lets the resource server confirm the integrity of the request in addition to the principal's identity.

How Spring Security processes authentication:

    It parses the request material into a credential. Spring Security looks for 
                        Authorization: Bearer ${JWT}
    ... in headers. 
    
    It tests that credential. Spring Security uses a JwtDecoder instance to query the authorization server for keys, uses those keys to verify the JWTs signature, and validates it's from a trusted issuer and is still within its expiry window.
    
    If the credential passes it translates that credential into a principal and authorities. Spring Security stores the JWT's claims as the principal. It takes the scope claim and parses each individual value into a permission with the pattern    
                                SCOPE_${value}
    ... in "scp" field of JWT claims.

This principal and authorities are then stored by Spring Security inside context and accessible for the remainder of the request.
===================
  Accessing Auth-
   entication in
    Spring MVC
===================
When :
    - to know profile or other details about the principal
    - a primary key or other identifiers about the principal to formulate a query
    - to declare whether a principal has permission to perform a given action
    - to propagate credentials to downstream services

To get principal:

        @GetMapping
        public ResponseEntity<Iterable<CashCard>> findAll(Authentication authentication) { ... }

Authentication#getPrincipal <annotation#methodName (only for documenting purpose)> returns Object . This is helpful when integrating Spring Security with custom user representations. However, using Authentication#getPrincipal directly can cause unwanted casting. To assist with that, Spring Security provides @CurrentSecurityContext.

So, use this instead:

        @GetMapping
        public ResponseEntity<Iterable<CashCard>> findAll(@CurrentSecurityContext(expression = "authentication.principal") Jwt jwt) { ... }

        @GetMapping
        public ResponseEntity<Iterable<CashCard>> findAll(@CurrentSecurityContext(expression = "authentication.name") String owner) { ... }

... to get any attribute (like name) from Authentication

A meta-annotation is an annotation that aliases another annotation.

Custom meta-annotation:

        @Target(ElementType.PARAMETER)
        @Retention(RetentionPolicy.RUNTIME)
        @CurrentSecurityContext(expression="authentication.name")
        public @interface CurrentOwner {
        }

... alias for @CurrentSecurityContext(..name..) (this is SpEL!) (Use it directly, instead of @AuthenticationPrincipal, when principal is a custom class).

    Spring Expression Language (SpEL) supports querying and manipulating an object graph at runtime.

        @GetMapping
        public ResponseEntity<Iterable<CashCard>> findAll(@CurrentOwner String owner) { ... }

-or-

        @GetMapping
        public ResponseEntity<Iterable<CashCard>> findAll(@AuthenticationPrincipal String owner) { ... }

    findAll() works fine but if there are trillions of records in the database. It had to pull all into memory and loop through each one.
===================
  The Big Picture
===================
Spring Security filter chain is a set of security filters that are run in sequence on each request.

                Client
FileterChain       ↑
        -----------------------
        |          ↓          |   SecurityFileterChain
        |       Filter_0      |   -------------------
        |          ↕          |   |Security Filter_0|
        |DelegatingFilterProxy|   |        ↕        |
        |(FileterChainProxy)--|--→|       ...       |
        |          ↕          |   |        ↕        |
        |       Filter_2      |   |Security Filter_N|
        |          ↕          |   -------------------
        |       Servlet       |
        -----------------------

Each filter can short-circuit the request and prevent the other filters and servlets from running. The filter chain is represented by a single bean named SecurityFilterChain. It can hold an arbitrary number of security filters that it will execute on every request.

/*
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .addFilter(webAsyncManagerIntegrationFilter) // infrastructure
        .securityContext(withDefaults()) // infrastructure
        .servletApi(withDefaults()) // infrastructure
        .csrf(withDefaults()) // defense
        .headers(withDefaults()) // defense
        .logout(withDefaults()) // authentication
        .sessionManagement(withDefaults()) // authentication
        .requestCache(withDefaults()) // authentication
        .formLogin(withDefaults()) // authentication
        .httpBasic(withDefaults()) // authentication
        .anonymous(withDefaults()) // authentication
        .exceptionHandling(withDefaults()) // infrastructure
        .authorizeHttpRequests((authorize) -> authorize  // authorization
            .anyRequest().authenticated()
        );

    return http.build();
}
*/

HttpSecurity object simplifies construction of the filter chain.
With spring-security-oauth2-resource-server dependency and the spring.security.oauth2.resourceserver.jwt.issuer-uri property, Spring Boot automatically publishes the default OAuth 2.0 Resource Server bean. Flow of filers:
1.  Infrastructural Filters
2.  Defense Filters:
___________________
    - CsrfFilter - the filter that checks incoming CSRF tokens and issues new ones
    - HeaderFilter - the filter that writes secure headers to the HTTP response
3.  Authentication Filters
__________________________
    - BasicAuthenticationFilter - Handles HTTP Basic Authentication
    - BearerTokenAuthenticationFilter - Handles Bearer Token Authentication (including JWTs)
    - UsernamePasswordAuthenticationFilter - Handles Form Login Authentication
    - AnonymousAuthenticationFilter - Populates the context with a Null Object authentication instance

    How Authentication works :
        /* Note: this is pseudocode!
        if (!requestMatcher.matches(request)) {
            // skip this filter
        } else {
            Authentication token = getAuthenticationRequest(request) <1>
            try {
                Authentication result = authenticationManager.authenticate(token) <2> <3>
                saveToSecurityContextHolder(result);
                fireSuccessEvent(result);
                handleSuccess(result);
            } catch (AuthenticationException ex) {
                handleFailure(token);
            }
        }

    <1> parses the request material into a credential
    <2> tests that credential and returns a principal and authorities
    <3> constructs the principal and authorities
        */

    Authentication is a Spring Security interface that represents both an authentication token (material to be authenticated, like a JWT) and an authentication result (authenticated material).

            Authentication Token	Authentication Result
             principal ("who")	     principal ("who")
             credentials ("proof")	 credentials ("proof")
             authenticated = false	 authenticated = true
	                                 authorities ("permissions")
            
    AuthenticationManager is an interface that tests an authentication token. If the test succeeds, then the AuthenticationManager constructs an authentication result. The AuthenticationManager is composed of several AuthenticationProvider(s), each of which handle a single authentication scheme, like authenticating a JWT.

    The SecurityContext (can hold para-user security information, However it is very rarely exercised in Spring Security) is an object that holds the current Authentication     
4.  Authorization Filters
_________________________
    By default, Spring Security constructs an AuthorizationFilter that requires that all requests be authenticated.

===================
     Accessing
   Authentication
      Anywhere
===================
SecurityContextHolder, a static class, provides access to the current security context, including the authentication details to be used anywhere in the application, like in POJO classes which are not managed by Spring IOC (Since it does not have @Component). It simplifies the handling of security-related information and ensures thread-safe access to the user's security context throughout the application.
===================
  Validate Claims
===================
Spring Security authenticates each JWT by:

    - Validating the signature, and
    - Checking that the current time is between the timestamps in the iat (Issued At) and exp (Expires At) claims.

       AuthenticationManager
    ____________↓______________
    |JwtAuthenticationProvider|
    |  _________↓___________  |
    |  |    JwtDecoder     |  |
    |  |___________________|  |
    |                         |
    |_________________________|

In custom validation, JwtDecoder verifies the signature and validates the claims. Spring Security uses NimbusJwtDecoder by default.
===================
    Processing 
     Failures
===================
Each entry point is how Spring Security provides both error and try-again information to the client.
    - LoginUrlAuthenticationEntryPoint
        Spring Security will redirect you to the login entry point page shown below, or the point at which the user enters your application.
    - BearerTokenAuthenticationEntryPoint
        WWW-Authenticate: Bearer error="invalid_token",error_description="A description of the error", error_uri="[]()"
===================
   Authorization
      Models 
     Overview
===================
Categories of Authorization:
- coarse-grained authorization:
Authorization rules that make a decision on only a single factor (like any request that starts with /admin )
- fine-grained authorization: 
Authorization rules that make a decision based on multiple factors (like method parameters, return values, and the method signature)

Types of Authorization:
- Request-Level Authorization (Authorization at Controller layer)
- Method-Level Authorization (Authorization at Service layer)
- Data-Level Authorization (Authorization at Repository / Database layer)
- Delegated Authorization 
    (Authorization using Authorization server (OAuth 2.0))
===================
     OAuth 2.0 
       Scopes
===================
The scp claim in Jwt is the "scope" claim (common convention resourcename:operation). It is the set of permissions that this token has. Spring Security checks these values against the authorization rules that you declare in your application. The SCOPE_ prefix is there because authorities can come from multiple sources. If it starts with SCOPE_ then it likely came from a JWT. If it starts with ROLE_ it likely came from a database. Also, you can use these prefixes strategically, like saying that this ROLE_ authority implies these n SCOPE_ authorities.
===================
      Adding 
      Request 
   Authorization
===================

Authorization filter has 3 parts :

_request_____________________________  _authority_________________________
                      _pattern_______
.requestMatchers(GET, "/cashcards/**").hasAuthority("SCOPE_cashcard:read")

- request: 
Any part of the HTTP request; practically, anything that can be read from an HttpServletRequest instance
- pattern: 
The matching rule that decides whether the given request material matches
- authority: 
The authorization rule that applies for the request material that matches the pattern

Spring Security-enabled application enables "all requests require authentication". 

                .anyRequest().authenticated()

In authorizeHttpRequest
    \- requestMatchers(String...)
    \- anyRequest()
...rules are always processed from top to bottom, again, just like if-else-if statements.

Endpoints:
    Static Resources
            .requestMatchers("/error", "/css/**", "/js/**", "/images/**").permitAll() (non-authorization filters like defense still apply)
    RESTful Resources
            .requestMatchers(GET, "/cashcards/**").hasAuthority("SCOPE_cashcard:read")
            .requestMatchers("/cashcards/**").hasAuthority("SCOPE_cashcard:write")
    Catch-all rules
            .anyRequest().authenticated()

/*
@Bean
SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/error", "/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers(GET, "/cashcards/**").hasAuthority("SCOPE_cashcard:read")
            .requestMatchers("/cashcards/**").hasAuthority("SCOPE_cashcard:write")
            .anyRequest().denyAll()
        );
    
    return http.build();
}
*/

===================
      Adding 
      Request 
   Authorization
===================

    When System uses a message queue via Spring AMQP instead of the HTTP REST API. You can certainly extract the findAll business logic out of the controller and into a service-layer class, but the authorization rules would be "trapped" in the HTTP layer if you continue to use request-level authorization. Switching to method-level authorization in the service layer would let the authorization rules apply to HTTP, AMQP, or any other usage of that service layer class.

Spring Security does not enable method security by default, since Spring Security supports more than one set of method annotations. Adding @EnableMethodSecurity configures Spring Security to intercept and secure any public Spring-managed class or method annotated with its pre-post annotations.

    @PreAuthorize("rule") : Block this method invocation unless the given rule passes
    @PostAuthorize("rule") : Block this method return unless the given rule passes

    ... others : @PreFilter and @PostFilter

use cases for method security are:

1. Securing the non-HTTP layers of your application
... using @PreAuthorize
2. Performing fine-grained authorization.
... or Insecure Direct Object Reference mitigation. Instead of using
        /*
         if (!card.getOwner().equals(authentication.getName())) {
            throws new AccessDeniedException("denied");
         }
        */
    we can use @PostAuthorize(_SpELexp_:"returnObject.body.owner == authentication.name") . _returnObject_ is a special variable the Spring Security understands to mean the return value of the method.

There is a performance implication with unneccessary object creation and DB access. But, this way we can differentiate <403> (Unauthorized) and <404> (invalid arguments).
===================
      Adding 
       Data 
   Authorization
===================
Data authorization is achieced by adding Spring Security authentication material into Spring Data queries. It is disabled and does not exist in classpath by default. To activate, add _org.springframework.security:spring-security-data_

Data authorization : {
    
    @Query("SELECT * FROM entity e WHERE e.owner = :#{authentication.name}")

    -effectively-
    
    PreparedStatement ps = connection.prepareStatement("SELECT * FROM cash_card cc WHERE cc.owner = ?");
    ps.setValue(1, SecurityContextHolder.getContext().getAuthentication().getName());

}

    This is an example of preventing user data from leaking. There are two main takeaways about this: First, it removes boilerplate code, and second it ensures that services cannot accidentally call findAll and leak user data. Having authorization logic centralized from potentially many services, we've reduced the security "surface area".

It is preferred to use clarible _findByOwner(String owner)_ instead of filterd _findAll_ ; to provide appropriate method names.

/*
    Iterable<CashCard> findByOwner(String owner);
    default Iterable<CashCard> findAll() {
        throw new UnsupportedOperationException("unsupported, please use findByOwner instead");
    }
*/
===================
      Adding 
       Data 
   Authorization
===================
To point your resource server at an authorization server, you need to add  _spring-security-oauth2-resource-server_ dependency, _oauth2ResourceServer_ Lambda DSL (Domain-Specific Language) call to the filter chain definition and Spring Boot property _spring:_
                           _security:_
                             _oauth2:_
                               _resourceserver:_
                                 _jwt:_
                                   _issuer-uri: https://example.org/oauth2_.
_issuer-uri_ property computes an Authorization Server endpoint that serves a set of public keys, effectively replacing the _public-key-location_ property. 
    On startup, Spring Security uses this property to formulate an OIDC (OpenID Connect) Discovery request (_jwks-url_) to the authorization server. The _jwks-url_ (JSON Web Key Set-url) endpoint is the source that the resource server will use to retrieve, cache, and periodically re-retrieve the authorization server’s public keys throughout the life of the application.
What Spring Security does with this value is create the _JwtDecoder_ bean you learned about during the Authentication module. It looks something like this:

/*
@Bean
JwtDecoder jwtDecoder() {
    return new SupplierJwtDecoder(() -> JwtDecoders.fromIssuerLocation(issuerUri).build());
}
*/

_SupplierJwtDecoder_ defers the call to the authorization server until the first request. This makes the decoder more resilient during restarts since then the authorization server doesn’t need to be running.

Once started, to talk to the _resource-server_, you now must ask the authorization server for a token, using any of the grant flows (authorization code or client credentials grant flow) that it supports. JWT does not become a representation of the session, since it is stateless and non-revocable (user logged out).