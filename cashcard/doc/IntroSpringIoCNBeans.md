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
===================
     Container 
      Overview
===================
The <org.springframework.context.ApplicationContext interface represents the Spring> <IoC_container> and is responsible for instantiating, configuring, and assembling the beans. The container gets its instructions on the components to instantiate, configure, and assemble by reading configuration metadata. The configuration metadata can be represented as annotated component classes, configuration classes with factory methods, or external XML files or Groovy scripts.
ApplicationContext is usually an instance of AnnotationConfigApplicationContext or ClassPathXmlApplicationContext. Application context is implicitly bootstrapped for you based on common setup conventions.

                     
                      Your Business Objects     
                             (POJOs)           
                                |
                                v
                   +------------------------+
  Configuration--->|  The Spring Container  |
    metadata       +------------+-----------+
                                |
                                v
                   +-------------------------+
                   | Fully configured system |
                   |    (Ready for Use)      |
                   +-------------------------+

Configuration metadata tells the Spring container how to instantiate, configure, and assemble the components in the application. There are two ways :-
   > Annotation-based configuration: define beans using annotation-based configuration metadata on your application’s component classes (operating on metadata in the component class itself).

   > Java-based configuration: define beans external to your application classes by using Java-based configuration classes. To use these features, see the @Configuration, @Bean, @Import, and @DependsOn annotations.

- XML as an External Configuration DSL
______________________________________
   beans as <bean/> elements inside a top-level <beans/>
   - id attribute (string) identifies the individual bean definition.
   - class attribute defines the type of the bean
   Configuration metadata can be loaded from external resources from Java CLASSPATH. The linkage between id (of <bean> tag) and ref (within <property> tag enclosed by a <bean> tag) elements expresses the dependency between collaborating objects.
   To load,

      ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
   ...services.xml:
   <beans>
     <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
       <property name="accountDao" ref="accountDao"/>
       <property name="itemDao" ref="itemDao"/>
     </bean>
	</beans>
   ...daos.xml
   <beans>
	   <import resource="services.xml"/> // To import more beans
      <bean id="accountDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
	   </bean>
   </beans>

${…​} resolves JVM system properties at runtime
fully qualified resource locations - file:C:/config/services.xml or classpath:/config/services.xml

...in Groovy
   myService(MyService) {
		nestedBean = { AnotherBean bean ->
			dataSource = dataSource
		}
	}

   // create and configure beans
   ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
      - or -
   ApplicationContext context = new GenericGroovyApplicationContext("services.groovy", "daos.groovy");
      - or -
   GenericApplicationContext context = new GenericApplicationContext();
   new XmlBeanDefinitionReader(context).loadBeanDefinitions("services.xml", "daos.xml");
   context.refresh();

   // retrieve configured instance
   PetStoreService service = context.getBean("petStore", PetStoreService.class);

   // use configured instance
   List<String> userList = service.getUsernameList();

Application code should have no calls to the getBean() and IoC should take care of it.