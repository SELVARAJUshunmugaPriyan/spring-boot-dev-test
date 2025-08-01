package com.extra.proj.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.proj.model.AddressResponse;
import com.extra.proj.model.EmployeeDetails;
import com.extra.proj.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	ModelMapper mapper;
	
	public AddressResponse getAddressByEmployeeId(int id)
	{		
		AddressResponse addressResponse = new AddressResponse(); 
		
		mapper.map(addressRepository.findByEmployeeId(id).get(), addressResponse);
		
		return addressResponse;
	}
	
	public EmployeeDetails getEmployeeDetailsByEmployeeId(int id)
	{
		return addressRepository.findEmployeeDetailsByEmployeeId(id).orElse(null);
	}
}
