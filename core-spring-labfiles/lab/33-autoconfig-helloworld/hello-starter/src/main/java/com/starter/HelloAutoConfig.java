package com.starter;

import com.lib.HelloService;
import com.lib.TypicalHelloService;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

// TO-DO-24: Note that "hello-starter" has its own configuration class,
//          in which "TypicalHelloService" bean is configured.
//@Configuration

// ---------------------------------------------

// TO-DO-33: Add @ConditionalOnClass(HelloService.class) to the class
// - This will make sure this auto-configuration class is used only
//   when the "HelloService.class" is in the classpath
@ConditionalOnClass(HelloService.class)
@AutoConfiguration
public class HelloAutoConfig {

    // TO-DO-34: Add @ConditionalOnMissingBean(HelloService.class) to the method
    // - This will make sure this "HelloService" bean will be auto-configured
    //   only when there is no "HelloService" bean already in the application
    //   context
    @Bean
	@ConditionalOnMissingBean(HelloService.class)
    HelloService helloService() {
        return new TypicalHelloService();
    }

}

