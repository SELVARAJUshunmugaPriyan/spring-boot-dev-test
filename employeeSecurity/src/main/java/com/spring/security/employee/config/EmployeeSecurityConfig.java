package com.spring.security.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true,jsr250Enabled=true)
public class EmployeeSecurityConfig {
	
	private static final boolean securityDebug = false;
	
	@Bean
	UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder)
	{
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		
		manager.createUser(User.withUsername("user")
							   .password(bCryptPasswordEncoder.encode("userPass"))
							   .roles("USER")
							   .build());
		
		manager.createUser(User.withUsername("admin")
							   .password(bCryptPasswordEncoder.encode("admnPass"))
							   .roles("USER", "ADMIN")
							   .build());
		
		return manager;
	}
	
	@SuppressWarnings("removal")
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder
			bCryptPasswordEncoder, UserDetailsService userDetailsService)
		throws Exception
	{
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				   .userDetailsService(userDetailsService)
				   .passwordEncoder(bCryptPasswordEncoder)
				   .and()
				   .build();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> 
					authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.DELETE)
																					.hasRole("ADMIN")
						.requestMatchers("/admin/**").hasAnyRole("ADMIN")
						.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/login/**").permitAll()
						.anyRequest().authenticated())
			.httpBasic(Customizer.withDefaults())
			.sessionManagement(httpSecuritySessionManagementConfigurer ->
		httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
	
	@Bean
	WebSecurityCustomizer webSecurityCustomizer()
	{
		return web -> web.debug(securityDebug).ignoring().requestMatchers("/css/**", "/js/**",
				"/img/**", "/lib/**", "/favicon.ico");
	}
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}