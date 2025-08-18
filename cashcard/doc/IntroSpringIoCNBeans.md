===================
    Spring IoC 
     Container
        and
       Beans
===================
Dependency injection (DI) is a specialized form of Inversion of Control (IoC), whereby objects define their dependencies (that is, the other objects they work with) only through constructor arguments/arguments to a factory method/ properties that are set on the object instance after it is constructed or returned from a factory method.  The IoC container then injects those dependencies when it creates the bean. This process is fundamentally the inverse (hence the name, Inversion of Control) of the bean/inverse of bean or the instantiated object itself controlling the instantiation or location of its dependencies by using direct construction of classes or a mechanism such as the _Service-Locator_ pattern.
The <org.springframework.beans> and <org.springframework.context> packages are the basis for Spring Framework’s IoC container. 
   The <BeanFactory provides the configuration framework and basic functionality>. 
   <ApplicationContext adds more enterprise-specific functionality>. It is a sub-interface of BeanFactory. It adds:
   > Easier integration with Spring’s AOP features
   > Message resource handling (for use in internationalization)
   > Event publication
   > Application-layer specific contexts such as the WebApplicationContext for use in web applications.
In Spring, <bean is an object that is instantiated, assembled, and managed by a Spring IoC container>. Beans, and the dependencies among them, are reflected in the configuration metadata used by a container.

Spring IoC : {  (BeanFactory -> Application context ->)
                Bean : { Configuration metadata }
                  \----> other Beans
             }