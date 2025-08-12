package test.mockito.employee;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.testcontainers.junit.jupiter.Container;
import test.mockito.employee.model.Employee;
import test.mockito.employee.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class EmployeeApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	Employee employee;
	
	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.30");
	
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry)
	{
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}
	
	@BeforeAll
	static void beforeAll()
	{
		mySQLContainer.start();
	}
	
	@AfterAll
	static void afterAll()
	{
		mySQLContainer.stop();
	}
	
	@BeforeEach
	public void setup()
	{
		employee = Employee.builder()
						   .id(1L)
						   .firstName("Lakshitha")
						   .lastName("Fernando")
						   .email("lakshitha@gmail.com")
						   .build();
		
		System.out.println(mySQLContainer.getDatabaseName());
		System.out.println(mySQLContainer.getJdbcUrl());
		System.out.println(mySQLContainer.getUsername());
		System.out.println(mySQLContainer.getPassword());
	}
	
	//Save Employee Test
	@Test
	@Order(1)
	public void saveEmployeeTest() throws Exception
	{
		employee.setId(0);
		mockMvc.perform(post("/test/emp/post")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(employee)))
			   .andExpect(status().isCreated())
			   .andDo(print())
			   .andExpect(jsonPath("$.firstName").value("Lakshitha"))
			   .andExpect(jsonPath("$.lastName").value("Fernando"))
			   .andExpect(jsonPath("$.email").value("lakshitha@gmail.com"));
	}
	
	@Test
	@Order(2)
	public void getAllEmployeesTest() throws Exception
	{
		List<Employee> employeesList = new ArrayList<>();
		employeesList.add(employee);
		employeesList.add(Employee.builder().id(0).firstName("Sam").lastName("Curran").email("sam@gmail.com").build());
		employeeRepository.saveAll(employeesList);
		
		employeesList = employeeRepository.findAll();
		
		mockMvc.perform(get("/test/emp/getAll"))
			   .andExpect(status().isOk())
			   .andDo(print())
			   .andExpect(jsonPath("$.size()", is(employeesList.size())));
	}
	
	@Test
	@Order(3)
	public void getEmployeeById() throws Exception
	{
		 mockMvc.perform(get("/test/emp/get/{id}", employee.getId()))
		 		.andExpect(status().isOk())
		 		.andDo(print())
		 		.andExpect(jsonPath("$.firstName").value("Lakshitha"))
		 		.andExpect(jsonPath("$.lastName").value("Fernando"))
		 		.andExpect(jsonPath("$.email").value("lakshitha@gmail.com"));
	}
	
	@Test
    @Order(4)
    public void updateEmployee() throws Exception {

 
       employee.setEmail("lakshitha12345@gmail.com");
       employee.setLastName("Fdo");
       mockMvc.perform(put("/test/emp/put/{id}", employee.getId())
             .contentType(MediaType.APPLICATION_JSON)
             .content(objectMapper.writeValueAsString(employee)))
             .andExpect(status().isOk())
             .andDo(print())
             .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
             .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    @Order(5)
    public void deleteEmployee() throws Exception{
       mockMvc.perform(delete("/test/emp/delete/{id}", employee.getId()))
              .andExpect(status().isOk())
              .andDo(print());
    }

}
