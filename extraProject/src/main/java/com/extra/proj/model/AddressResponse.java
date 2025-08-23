package com.extra.proj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddressResponse {


	private int id;
	private String city, state;
	private String employeeDetails;
	
}