package com.simple.proj.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.simple.proj.client.AddressClient;
import com.simple.proj.model.AddressResponse;
import com.simple.proj.model.Employee;
import com.simple.proj.model.EmployeeResponse;
import com.simple.proj.respository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	AddressClient addressClient;
	
	public EmployeeResponse getEmployeeById(int id)
	{
		Optional<Employee> employee = employeeRepository.findById(id);
		EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
		
		ResponseEntity<AddressResponse> addressResponse = 
				addressClient.getAddressByEmployeeId(id);
		
		employeeResponse.setAddressResponse(addressResponse.getBody());
		
		return employeeResponse;
	}
}
