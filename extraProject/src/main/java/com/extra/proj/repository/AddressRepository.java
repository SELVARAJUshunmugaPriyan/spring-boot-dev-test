package com.extra.proj.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.extra.proj.model.Address;
import com.extra.proj.model.EmployeeDetails;


public interface AddressRepository extends JpaRepository<Address, Integer>{

	@Query(value="SELECT ea.id, e.age, e.id AS employeeId, e.name, "
			+ "e.email, ea.city, ea.state FROM simple_schema.address ea "
			+ "JOIN simple_schema.employee e ON e.id = ea.employee_id "
			+ "WHERE employee_id = ?1 ", nativeQuery=true)
	public Optional<EmployeeDetails> findEmployeeDetailsByEmployeeId(int employeeId);
	
	public Optional<Address> findByEmployeeId(int employeeId);
	
	@Modifying
	@Query(value="INSERT INTO simple_schema.address(city, state, employee_id) "
			+ "VALUES (?1, ?2, ?3)", nativeQuery=true)
	public void saveAddress(String city, String state, int employeeId);
}
