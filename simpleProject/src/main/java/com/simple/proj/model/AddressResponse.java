package com.simple.proj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddressResponse {

	private int id;
	private String city;
	private String state;
//	private String employeeDetails;
	
}