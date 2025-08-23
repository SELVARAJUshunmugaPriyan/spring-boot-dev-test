package com.extra.proj.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.extra.proj.model.Address;
import com.extra.proj.model.AddressResponse;

@Configuration
public class AddressConfig {
	
	PropertyMap<Address, AddressResponse> propertyMap = 
			new PropertyMap<Address, AddressResponse>() {
		
		@Override
		protected void configure() {
			map().setEmployeeDetails(source.getEmployee().toString());
		}
		
	};

	@Bean
	ModelMapper modelMapperBean()
	{
		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.addMappings(propertyMap);
		
		return modelMapper;                                                  
	}
}
