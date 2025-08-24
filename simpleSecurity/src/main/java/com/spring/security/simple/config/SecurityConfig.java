package com.spring.security.simple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Bean
	UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder)
	{
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		
		manager.createUser(User.builder()
							   .username("user")
							   .password(passwordEncoder.encode("pass"))
							   .roles("USER")
							   .build());
		
		manager.createUser(User.builder()
							   .username("gfg")
							   .password(passwordEncoder.encode("pass"))
							   .roles("USER", "ADMIN")
							   .build());
		
		return manager;
	}
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/basic").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/admin").hasAnyRole("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
					.loginPage("/login")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.permitAll()
			);
		
		return http.build();
	}
}