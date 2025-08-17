===================
   Introduction
===================
Steel Thread approach creates a "thread"/work-flow through all the integration points (API "front door", database, and security) of the application. It results in an end-to-end, executable application.
===================
    Spring and 
    Spring Boot
===================
Setting up a Spring application requires a lot of configuration, and developers need to manually configure various components of the framework.
Various tools such as Spring MVC for web application, Spring Data for data access, and Spring Security for authentication and authorization, are provided under Spring.

1. Spring's Inversion of Control Container
__________________________________________
Spring Boot allows you to configure how and when dependencies are provided to your application at runtime. This puts you in control of how your application operates in different scenarios.

For example, you might want to use a different database for local development than for your live, public-facing application.
===================
   API Contracts
      & JSON
===================
Software industry has adopted several patterns ("Contracts") for capturing agreed upon API behavior in documentation and code. E.g.: API contracts, Consumer Driven Contracts and Provider Driven Contracts.

\-Sample(API Contract)->
    Request
        URI: /cashcards/{id}
        HTTP Verb: GET
        Body: None

    Response:
        HTTP Status:
            200 OK if the user is authorized and the Cash Card was successfully retrieved
            401 UNAUTHORIZED if the user is unauthenticated or unauthorized
            404 NOT FOUND if the user is authenticated and authorized but the Cash Card cannot be found
        Response Body Type: JSON
        Example Response Body:
            {
            "id": 99,
            "amount": 123.45
            }

JSON - Javascript Object Notation
XML  - Extensible Markup Language
YAML - Yet Another Markup Language
===================
   Testing First
===================
Test driven development (TDD) is writing tests before implementing the application code, useful for designing the system based on what we want it to do. When the tests pass, it delivers a minimum working implementation (the application code), and a guard against introducing errors in the future (the tests).

                   ▲         +---+          ▲
  More Integration |        / E2E \         |   Slower
                   |       / Tests \        |
                   |      /---------\       |
                   |     / Integrat- \      |
                   |    /  ion Tests  \     |
                   |   /---------------\    |
    More Isolation |  /    Unit Tests   \   |   Faster
                   ▼ +-------------------+  ▼

    _Unit-Tests_: A Unit Test exercises a small “unit” of the system that's isolated from the rest of the system. They should be simple and speedy. You want a high ratio of Unit Tests in your testing pyramid, as they’re key to designing highly cohesive, loosely coupled software.

    _Integration-Tests_: Integration Tests exercise a subset of the system and may exercise groups of units in one test. They are more complicated to write and maintain, and run slower than unit tests.

    _End-to-End_ Tests: An End-to-End Test exercises the system using the same interface that a user would, such as a web browser. While extremely thorough, End-to-End Tests can be very slow and fragile because they use simulated user interactions in potentially complicated UIs. Implement the smallest number of these tests

1. The Red, Green, Refactor Loop
________________________________
    Red: Write a failing test for the desired functionality.
    Green: Implement the simplest thing that can work to make the test pass.
    Refactor: Look for opportunities to simplify, reduce duplication, or otherwise improve the code without changing any behavior—to refactor.
===================
    REST, CRUD, 
     and HTTP
===================
REST: Representational State Transfer. In a RESTful system, data objects are called Resource Representations. The purpose of a RESTful API (Application Programming Interface) is to manage the state of these Resources. Here “state” is “value” and “Resource Representation” is an “object” or "thing". Thus, REST is simply a method to manage the values of things via API.

       +---------+
       | Client  |
       +---------+
            ⇅
     HTTP Methods:
   GET / POST / PUT / DELETE
       Responses:
       JSON / XML
            ⇅
     +----------------+
     |   REST API     |
     |                |
     +----------------+
            ⇅
     Internal Data Access:
     Read / Write / Update
            ⇅
     +----------------+
     |   Database     |
     +----------------+
CRUD stands for “Create, Read, Update, and Delete”.

In HTTP (Hypertext Transfer Protocol (for REST)), a caller sends a Request to a URI. A web server receives the request, and routes it to a request handler. The handler creates a Response, which is then sent back to the caller.

Request

    Method (also called Verb)
    URI (also called Endpoint)
    Body

Response

    Status Code
    Body

Operation	API Endpoint	HTTP Method	    Response Status
Create      /[api]          POST(w. ReqBody)201 (CREATED)
Read	    /[api]/{id}     GET             200 (OK)
Update	    /[api]/{id}     PUT(w. ReqBody) 204 (NO CONTENT)
Delete	    /[api]/{id}     DELETE      	204 (NO CONTENT)
===================
  Repositories & 
    Spring Data
===================
Separation of Concerns principle states that well-designed software should be modular, with each module having distinct and separate concerns.
Controller is an abstraction of a web interface.

A common architectural framework that divides these layers, typically by function or value, is called Layered Architecture. Controller layer is near Client (receives/responds web requests) while Repository layer is near the data store (reads-writes data store).

SQL (Structured Query Language)
JDBC (Java DB Connectivity)
RDBMS (Relational DataBase Management Systems)
ORM (Object-Relational Mapping framework)

CrudRepository is an interface without actual code. Used Spring Data framework used (Spring Data JDBC) takes care of the implementation during the IoC container startup time.
===================
   Implementing 
       POST
===================
In a REST API, an idempotent operation is one that even if it were to be performed several times, the resulting data on the server would be the same as if it had been performed only once. GET, PUT and DELETE are idempotent, whereas POST and PATCH are not.

E.g.:
return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, uriOfCashCard.toASCIIString())
        .build();
===================
    Returning a 
     list with 
        GET
===================
Pagination in Spring (and many other frameworks) is to specify the page length (e.g. 10 items), and the page index (starting with 0).

Benefits to use pagination with ordering:
    Minimize cognitive overhead
    Minimize future errors

Page<Object> page2 = objectRepository.findAll(
    PageRequest.of(
        1,  // page index for the second page - indexing starts at 0
        10, // page size (the last page might have fewer items)
        Sort.by(new Sort.Order(Sort.Direction.DESC, "field"))));


Pagination: Spring can parse out the `page` and `size` parameters if you pass a `Pageable` object to a PagingAndSortingRepository `find…()` method.
Sorting: Spring can parse out the `sort` parameter, consisting of the field name and the direction separated by a comma. This data is part of the `Pageable` object.

Compose a URI for the new endpoint:

    Get the second page
    /cashcards?page=1

    …where a page has length of 3
    /cashcards?page=1&size=3

    …sorted by the current Cash Card balance
    /cashcards?page=1&size=3&sort=amount

    …in descending order (highest balance first)
    /cashcards?page=1&size=3&sort=amount,desc

Java impl.:
@GetMapping
private ResponseEntity<List<Object>> findAll(Pageable pageable) {
   Page<Object> page = cashCardRepository.findAll(
           PageRequest.of(
                   pageable.getPageNumber(),
                   pageable.getPageSize(),
                   pageable.getSortOr(Sort.by(Sort.Direction.DESC, "amount"))));
   return ResponseEntity.ok(page.getContent());
}

@DirtiesContext runs each test method with clean state
===================
   Simple Spring
      Security
===================
Principal - user (person or another program) of the api

Authentication Session (or Auth Session) is created when a user gets authenticated. Sessions are implemented in many ways and a common mechanism is using a Session Token (a string of random characters) which is generated and placed in a Cookie. 
                \- A Cookie is a set of data stored in a web client (such as a browser), and associated with a specific URI.
                \- Cookies are automatically sent to the server with every request.
                \- Cookies can persist for a certain amount of time even if the web page is closed and later re-visited.

Spring Security provides Authorization via Role-Based Access Control (RBAC). This means that a Principal has a number of Roles. Each resource (or operation) specifies which Roles a Principal must have in order to perform actions with proper authorization.

HTTP clients and servers implementing the Same Origin Policy (SOP). This policy states that only scripts which are contained in a web page are allowed to send requests to the origin (URI) of the web page. For example, let’s look at a typical banking web site. If a user is logged into their bank account and visits a malicious web page (in a different browser tab or window), the malicious requests could be sent (with the Auth Cookies) to the banking site.

Cross-Origin Resource Sharing (CORS) is a way that browsers and servers can cooperate to relax the SOP. A server can explicitly allow a list of “allowed origins” of requests coming from an origin outside the server’s. @CrossOrigin used without arguments will allow all origins.

Cross-Site Request Forgery (CSRF) or “Sea-Surf” or Session Riding is actually enabled by Cookies. A CSRF Token is different from an Auth Token because a unique token is generated on each request which prevents outside actor to insert itself into the conversation between the client and the server.

Cross-Site Scripting (XSS) occurs when an attacker is able to trick the victim application into executing arbitrary code. These attacks can be mitigated by properly escaping the special HTML characters when the string is rendered.

The @Configuration annotation tells Spring to use this class to configure Spring and Spring Boot itself. Any Beans specified in this class will now be available to Spring's Auto Configuration engine. Annotating a method returning a <Object> with the @Bean satisfies this expectation.

Controller is returning 403 FORBIDDEN instead of a 500 INTERNAL_SERVER_ERROR because Spring Security is automatically implementing a best practice regarding how errors are handled by Spring Web.

It's important to understand that any information returned from our application might be useful to a bad actor attempting to violate our application's security. For example: knowledge about actions that causes our application to crash -- a 500 INTERNAL_SERVER_ERROR.
===================
   Implementing 
        PUT
===================
PUT means create or replace the complete record
PATCH means update only some fields of the existing record
> Natural Key - supplied by the client to the API
> Surrogate Key - generated by the server

If you need the server to return the URI of the created resource, then you must use POST. Alternatively, when the resource URI is known at creation time, you can use PUT. For e.g.: In an Invoice API, we could write a Create endpoint that accepts requests such as PUT /invoice/1234-567 . The corresponding Read call would use the exact same URI: GET /invoice/1234-567 .

POST creates a sub-resource (child resource) under the request URI. For e.g.: client calls the Create endpoint at POST /api and the created resource contains a generated ID at the end: /api/ID .

PUT creates or replaces (updates) a resource at a specific request URI. For the /invoice example above, the Create endpoint would be PUT /invoice/1234-567, and the URI of the created resource would be the same as the URI sent.

201 - CREATED
204 - NO CONTENT (Update)
200 - OK