package test.mockito.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import test.mockito.employee.model.Employee;
import test.mockito.employee.service.EmployeeService;

@RestController
@RequestMapping(value="/test/emp")
public class EmployeeController {

//	private EmployeeService employeeService;
//	
//	public EmployeeController(EmployeeService employeeService)
//	{
//		this.employeeService = employeeService;
//	}
	
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping(value="/postAll")
	@ResponseStatus(HttpStatus.CREATED)
	public String postAll(@RequestBody List<Employee> employees)
	{
		return employeeService.saveEmployee(employees);
	}
	
	@PostMapping(value="/post")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee post(@RequestBody Employee employee)
	{
		return employeeService.saveEmployee(employee);
	}
	
	@GetMapping(value="/getAll")
	@ResponseStatus(HttpStatus.OK)
	public List<Employee> getAll()
	{
		return employeeService.getAllEmployees();
	}
	
	@GetMapping(value="/get/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Employee getWithId(@PathVariable long id)
	{
		return employeeService.getEmployeeById(id).get();
	}
	
	@PutMapping(value="/put/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Employee put(@PathVariable long id, @RequestBody Employee employee)
	{
		return employeeService.updateEmployee(id, employee);
	}
	
	@DeleteMapping(value="/delete/{id}")
	@ResponseStatus(HttpStatus.OK)
	public String delete(@PathVariable long id)
	{
		employeeService.deleteEmployee(id);
		return "Deleted employee with id " + id + " Successfully";
	}
}
