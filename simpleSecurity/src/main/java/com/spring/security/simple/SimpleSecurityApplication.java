package com.spring.security.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FilterType;

import com.spring.security.simple.config.SecurityConfig;

//@SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
	excludeFilters={@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,value=SecurityConfig.class)}
)
public class SimpleSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSecurityApplication.class, args);
	}

}
