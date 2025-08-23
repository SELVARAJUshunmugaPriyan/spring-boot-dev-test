package com.simple.proj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeResponse {

	private int id, age;
	private String name, email;
	private AddressResponse addressResponse;
	
}