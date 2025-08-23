package com.spring.security.employee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class BasicConfig {
		
//	@SuppressWarnings({ "removal", "deprecation" })
	protected void configure(HttpSecurity https) throws Exception
	{
//		https.authorizeRequests()
//			 .anyRequest().authenticated()
//			 .and()
//			 .formLogin()
//			 .and()
//			 .httpBasic();
	}
	
}