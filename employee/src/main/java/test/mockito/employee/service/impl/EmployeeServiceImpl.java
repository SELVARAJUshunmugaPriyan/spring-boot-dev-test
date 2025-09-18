package test.mockito.employee.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.mockito.employee.exception.UserDoesNotExistException;
import test.mockito.employee.exception.UserExistsException;
import test.mockito.employee.model.Employee;
import test.mockito.employee.repository.EmployeeRepository;
import test.mockito.employee.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
//	private EmployeeRepository employeeRepository;
//	
//	public EmployeeRepositoryImpl(EmployeeRepository employeeRepository)
//	{
//		this.employeeRepository = employeeRepository;
//	}
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public String saveEmployee(List<Employee> employees)
	{
		String errMsg = "";
		
		for (Employee employee : employees)
			if (employeeRepository.findByEmail(employee.getEmail()).isPresent())
				errMsg += employee + "\n";
			else
				employeeRepository.save(employee);
		
		if (!errMsg.isEmpty())
			throw new UserExistsException("Following employees are registered already : \n" + 
										   errMsg);
		
		return "Posted " + employees.size() + " employees Successfully";
	}
	
	@Override
	public Employee saveEmployee(Employee employee)
	{
		Optional<Employee> savedEmployee = employeeRepository.findByEmail(
				employee.getEmail());
		
		if (savedEmployee.isPresent())
			throw new UserExistsException("Employee already exists with email : " + 
										   employee.getEmail());
		
		return employeeRepository.save(employee);
	}
	
	@Override
	public List<Employee> getAllEmployees()
	{
		return employeeRepository.findAll();
	}
	
	@Override
	public Optional<Employee> getEmployeeById(long id)
	{
		return Optional.of(employeeRepository.findById(id).orElseThrow(() -> 
							new UserDoesNotExistException("Employee does not exist with "
									+ "id : " + id)));
	}

	@Override
	public Employee updateEmployee(long id, Employee employee)
	{
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> 
										new UserDoesNotExistException("Employee does not exist"
												+ " with id : " + id));
		

		existingEmployee.setFirstName(employee.getFirstName());
		existingEmployee.setLastName(employee.getLastName());
		existingEmployee.setEmail(employee.getEmail());
		
		return employeeRepository.save(existingEmployee);
	}
	
	@Override
	public void deleteEmployee(long id)
	{
		if(employeeRepository.findById(id).isEmpty())
			throw new UserDoesNotExistException("Employee does not exist with id : " + id);
		
		employeeRepository.deleteById(id);
	}
}
