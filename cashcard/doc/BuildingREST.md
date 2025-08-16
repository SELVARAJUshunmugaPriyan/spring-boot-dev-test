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