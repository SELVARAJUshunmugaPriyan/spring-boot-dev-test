package com.extra.proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.extra.proj.model.AddressResponse;
import com.extra.proj.service.AddressService;

@RestController
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@GetMapping("/address/{employeeId}")
	public ResponseEntity<AddressResponse> getAddressByEmployeeId(@PathVariable int employeeId)
	{
		return ResponseEntity.status(HttpStatus.OK)
							 .body(addressService.getAddressByEmployeeId(employeeId));
	}
}
