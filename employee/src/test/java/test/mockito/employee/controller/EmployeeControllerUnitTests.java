package test.mockito.employee.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import test.mockito.employee.model.Employee;
import test.mockito.employee.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerUnitTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private EmployeeService employeeService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Employee employee;
	
	@BeforeEach
	void setUp()
	{
		employee = Employee.builder()
						   .id(1L)
						   .firstName("John")
						   .lastName("Cena")
						   .email("john@gmail.com")
						   .build();
	}
	
	// Post Controller
	@Test
	@Order(1)
	public void saveEmployeeTest() throws Exception
	{
		// precondition
		given(employeeService.saveEmployee(employee)).willReturn(employee);
		
		// action
		ResultActions response = mockMvc.perform(post("/test/emp/post")
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(employee)));
		
		// verify
		response.andDo(print())
			    .andExpect(status().isCreated())
			    .andExpect(jsonPath("$.firstName", 
					   is(employee.getFirstName())))
			    .andExpect(jsonPath("$.lastName",
					   is(employee.getLastName())))
			    .andExpect(jsonPath("$.email", 
					   is(employee.getEmail())));
	}
	
	// Get Controller
	@Test
	@Order(2)
	public void getEmployeeTest() throws Exception
	{
		// precondition
		given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
		
		// action
		ResultActions response = mockMvc.perform(get("/test/emp/get/{id}", employee.getId()));
		
		// verify
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	// Get Controller
	@Test
	@Order(3)
	public void getListOfEmployeeTest() throws Exception
	{
		// precondition
		Employee employeeTmp = Employee.builder()
									   .id(2)
									   .firstName("Sam")
									   .lastName("Curran")
									   .email("sam@gmail.com")
									   .build();
		List<Employee> employees = List.of(employee, employeeTmp);
		given(employeeService.getAllEmployees()).willReturn(employees);
		
		// action
		ResultActions response = mockMvc.perform(get("/test/emp/getAll"));
		
		// verify
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(employees.size())));
	}
	
	// Put Controller
	@Test
	@Order(4)
	public void putEmployeeTest() throws Exception
	{
		// precondition
		given(employeeService.updateEmployee(employee.getId(), employee)).willReturn(employee);
		
		// action
		ResultActions response = mockMvc.perform(put("/test/emp/put/{id}", employee.getId())
													.contentType(MediaType.APPLICATION_JSON)
													.content(objectMapper
															.writeValueAsString(employee)));
		
		// verify
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	// Put Controller
	@Test
	@Order(5)
	public void deleteEmployeeTest() throws Exception
	{
		// precondition
		willDoNothing().given(employeeService).deleteEmployee(employee.getId());
		
		// action
		ResultActions response = mockMvc.perform(delete("/test/emp/delete/{id}",
														employee.getId()));
		
		// verify
		response.andExpect(status().isOk())
				.andDo(print());
	}
		
}
