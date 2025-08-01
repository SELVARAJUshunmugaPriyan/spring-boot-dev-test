package com.extra.proj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeDetails {
	
	private int id, age, employeeId;
	private String name, email, city, state;
	
}
