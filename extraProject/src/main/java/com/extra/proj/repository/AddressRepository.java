package com.extra.proj.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import com.extra.proj.model.Address;


public interface AddressRepository extends JpaRepository<Address, Integer>{

//	@Query(value="SELECT id, city, state FROM Address ea WHERE employee_id = ?1 ")
//	public Optional<Address> findAddressByEmployeeId(int employeeId);
	
	public Optional<Address> findByEmployeeId(int employeeId);
	
	// FIXME User details model
}
