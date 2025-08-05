package com.simple.proj.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.simple.proj.client.AddressClient;
import com.simple.proj.model.AddressRequest;
import com.simple.proj.model.AddressResponse;
import com.simple.proj.model.Employee;
import com.simple.proj.model.EmployeeRequest;
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
	
	PropertyMap<EmployeeRequest, Employee> propertyMapEmployeeRequestEmployee =
			new PropertyMap<>() {
		@Override
		protected void configure()
		{
			map().setAge(source.getAge());
			map().setEmail(source.getEmail());
			map().setName(source.getName());
		}
	};
	
	PropertyMap<EmployeeRequest, AddressRequest> propertyMapEmployeeRequestAddressRequest =
			new PropertyMap<>() {
		@Override
		protected void configure()
		{
			map().setCity(source.getCity());
			map().setState(source.getState());
		}
	};
	
	public EmployeeResponse postEmployee(EmployeeRequest employeeRequest)
	{
		modelMapper.addMappings(propertyMapEmployeeRequestEmployee);
		
		Employee employee = employeeRepository.save(modelMapper.map(employeeRequest, Employee.class));
		
		modelMapper.addMappings(propertyMapEmployeeRequestAddressRequest);
		
		AddressRequest addressRequest = modelMapper.map(employeeRequest, AddressRequest.class);
		
		addressRequest.setEmployeeId(employee.getId());
		
		addressClient.postAddressWithEmployeeId(addressRequest);
		
		return getEmployeeById(employee.getId());
	}
	
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
