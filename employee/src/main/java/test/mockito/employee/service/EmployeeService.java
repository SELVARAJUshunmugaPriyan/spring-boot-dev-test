package test.mockito.employee.service;

import java.util.List;
import java.util.Optional;

import test.mockito.employee.model.Employee;

public interface EmployeeService {
	
	String saveEmployee(List<Employee> employees);
	Employee saveEmployee(Employee employee);
	List<Employee> getAllEmployees();
	Optional<Employee> getEmployeeById(long id);
	Employee updateEmployee(long id, Employee employee);
	void deleteEmployee(long id);
	
}
