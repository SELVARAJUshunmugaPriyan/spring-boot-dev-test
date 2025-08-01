package com.simple.proj.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeConfig {

	@Bean
	ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
}
