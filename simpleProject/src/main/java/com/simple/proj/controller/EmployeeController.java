package com.simple.proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.simple.proj.model.EmployeeResponse;
import com.simple.proj.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/employees/{id}")
	private ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable int id)
	{
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
	}
	
}
