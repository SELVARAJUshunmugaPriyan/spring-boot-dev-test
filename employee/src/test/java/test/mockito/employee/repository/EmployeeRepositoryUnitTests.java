package test.mockito.employee.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import test.mockito.employee.model.Employee;

@DataJpaTest
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:unit_test_db",
	    "spring.datasource.driver-class-name=org.h2.Driver",
	    "spring.datasource.username=sa",
	    "spring.datasource.password=password",
	    "spring.jpa.hibernate.ddl-auto=create-drop",
	    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRepositoryUnitTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Test
	@DisplayName("Test 1:Save Employee Test")
	@Order(1)
	@Rollback(value=false)
	public void saveEmployeeTest()
	{
		// Action
		Employee employee = Employee.builder()
									.firstName("Sam")
									.lastName("Curran")
									.email("sam@gmail.com")
									.build();
		
		employeeRepository.save(employee);
		
		// Verify
		System.out.println(employee);
		Assertions.assertThat(employee.getId()).isGreaterThan(0);
	}
	
	@Test
	@DisplayName("Test 2:Get Employee Test")
	@Order(2)
	public void getEmployeeTest()
	{
		// Action
		Employee employee = employeeRepository.findById(1L).get();
		
		// Verify
		System.out.println(employee);
		Assertions.assertThat(employee.getId()).isEqualTo(1L);
	}
	
	@Test
	@DisplayName("Test 3:Get List of Employees Test")
	@Order(3)
	public void getListOfEmployeesTest()
	{
		// Action
		List<Employee> employees = employeeRepository.findAll();
		
		// Verify
		System.out.println(employees);
		Assertions.assertThat(employees.size()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("Test 4:Update Employee Test")
	@Order(4)
	@Rollback(value=false)
	public void updateEmployeeTest()
	{
		// Action
		Employee employee = employeeRepository.findById(1L).get();
		employee.setEmail("sam.curran@gmail.com");
		Employee employeeUpdated = employeeRepository.save(employee);
		
		// Verify
		System.out.println(employeeUpdated);
		Assertions.assertThat(employeeUpdated.getEmail()).isEqualTo("sam.curran@gmail.com");
	}
	
	@Test
	@DisplayName("Test 5:Delete Employee Test")
	@Order(5)
	@Rollback(value=false)
	public void deleteEmployeeTest()
	{
		// Action
		employeeRepository.deleteById(1L);
		Optional<Employee> employeeOptional = employeeRepository.findById(1L);
		
		// Verify
		Assertions.assertThat(employeeOptional).isEmpty();
	}
}
