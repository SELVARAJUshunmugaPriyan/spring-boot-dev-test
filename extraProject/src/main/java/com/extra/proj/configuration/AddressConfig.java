package com.extra.proj.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressConfig {

	@Bean
	ModelMapper modelMapperBean()
	{
		return new ModelMapper();                                                  
	}
}
