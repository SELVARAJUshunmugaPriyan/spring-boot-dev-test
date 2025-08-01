package com.extra.proj.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.proj.model.AddressResponse;
import com.extra.proj.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	ModelMapper mapper;
	
	public AddressResponse getAddressByEmployeeId(int id)
	{
		return mapper.map(addressRepository.findByEmployeeId(id).get(),
						  AddressResponse.class);
	}
}
