package com.simple.proj.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.simple.proj.model.AddressResponse;

@FeignClient(name="address-service",url="http://localhost:1233",path="/address-service")
public interface AddressClient {

	@GetMapping("/address/{id}")
	public ResponseEntity<AddressResponse> getAddressByEmployeeId(@PathVariable int id);
}
