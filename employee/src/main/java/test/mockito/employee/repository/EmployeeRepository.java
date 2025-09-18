package test.mockito.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import test.mockito.employee.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	Optional<Employee> findByEmail(String email);
}
