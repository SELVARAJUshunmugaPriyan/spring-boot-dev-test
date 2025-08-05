package com.spring.security.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FilterType;

import com.spring.security.employee.config.BasicConfig;
//import com.spring.security.employee.config.EmployeeSecurityConfig;

//@SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters={
		@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,value=BasicConfig.class)
})
public class EmployeeSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeSecurityApplication.class, args);
	}

}
