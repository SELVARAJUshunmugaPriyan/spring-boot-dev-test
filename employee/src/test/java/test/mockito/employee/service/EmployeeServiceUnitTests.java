package test.mockito.employee.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import test.mockito.employee.model.Employee;
import test.mockito.employee.repository.EmployeeRepository;
import test.mockito.employee.service.impl.EmployeeServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceUnitTests {

	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeServiceImpl employeeService;
	
	private Employee employee;
	
	@BeforeEach
	public void setUp()
	{		
		employee = Employee.builder()
						   .id(1L)
						   .firstName("John")
						   .lastName("Cena")
						   .email("john@gmail.com")
						   .build();
	}
	
	@Test
	@Order(1)
	public void saveEmployeeTest()
	{
		// precondition
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
		given(employeeRepository.save(employee)).willReturn(employee);
		
		// action
		Employee savedEmployee = employeeService.saveEmployee(employee);
		
		// verify the output
		System.out.println(employee);
		assertThat(savedEmployee).isNotNull();
		verify(employeeRepository, times(1)).findByEmail(anyString());
		verify(employeeRepository, times(1)).save(any(Employee.class));
	}
	
	@Test
	@Order(2)
	public void getEmployeeTest()
	{
		// precondition
		given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
		
		// action
		Employee existingEmployee = employeeService.getEmployeeById(employee.getId()).get();
		
		// verify the output
		System.out.println(existingEmployee);
		assertThat(existingEmployee).isNotNull();
		verify(employeeRepository, times(1)).findById(anyLong());
	}
	
	@Test
	@Order(3)
	public void getListOfEmployees()
	{
		Employee employeeTemp = Employee.builder()
									 	.id(2L)
									 	.firstName("Sam")
									 	.lastName("Curran")
									 	.email("sam.curran@gmail.com")
									 	.build();
		// precondition
		given(employeeRepository.findAll()).willReturn(List.of(employee, employeeTemp));
		
		// action
		List<Employee> employees = employeeService.getAllEmployees();
		
		// verify the output
		System.out.println(employees);
		assertThat(employees.size()).isEqualTo(2);
		verify(employeeRepository, times(1)).findAll();
	}
	
	@Test
	@Order(4)
	public void updateEmployee()
	{
		// precondition
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		employee.setEmail("max@gmail.com");
		employee.setFirstName("Max");
		given(employeeRepository.save(employee)).willReturn(employee);
		
		// action
		Employee updatedEmployee = employeeService.updateEmployee(employee.getId(), employee);
		
		// verify the output
		System.out.println(updatedEmployee);
		assertThat(updatedEmployee.getEmail()).isEqualTo("max@gmail.com");
		assertThat(updatedEmployee.getFirstName()).isEqualTo("Max");
		verify(employeeRepository, times(1)).findById(anyLong());
		verify(employeeRepository, times(1)).save(any(Employee.class));
	}
	
	@Test
	@Order(5)
	public void deleteEmployee()
	{
		// precondition
		willDoNothing().given(employeeRepository).deleteById(employee.getId());
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		
		// action
		employeeService.deleteEmployee(employee.getId());
		
		// verify the output
		verify(employeeRepository, times(1)).deleteById(employee.getId());
	}

}
