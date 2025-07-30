package com.spring.security.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(EmployeeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTest {

	@Autowired
	MockMvc mvc;
	
	@Test
	@Order(1)
	@WithAnonymousUser
	public void whenAnonymousAccessLogin_thenOk() throws Exception
	{
		mvc.perform(get("/login"))
		   .andExpect(status().isOk());
	}
	
	@Test
	@Order(2)
	@WithAnonymousUser
	public void whenAnonymousAccessRestrictedEndpoint_thenIsUnauthorized() throws Exception
	{
		mvc.perform(get("/all"))
		   .andExpect(status().isUnauthorized());
	}
	
	@Test
	@Order(3)
	@WithUserDetails()
	public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception
	{
		mvc.perform(get("/user"))
		   .andExpect(status().isOk());
	}
	
	@Test
	@Order(4)
	@WithUserDetails()
	public void whenUserAccessRestrictedEndpoint_thenOk() throws Exception
	{
		mvc.perform(get("/all"))
		   .andExpect(status().isOk());
	}
	
	@Test
	@Order(5)
	@WithUserDetails()
	public void whenUserAccessAdminSecuredEnpoint_thenForbidden() throws Exception
	{
		mvc.perform(get("/admin"))
		   .andExpect(status().isForbidden());
	}
	
	@Test
	@Order(6)
	@WithUserDetails()
	public void whenUserAccessDeleteSecuredEnpoint_thenForbidden() throws Exception
	{
		mvc.perform(delete("/delete"))
		   .andExpect(status().isForbidden());
	}
	
	@Test
	@Order(7)
	@WithUserDetails(value="admin")
	public void whenAdminAccessUserSecuredEndpoint_thenOk() throws Exception
	{
		mvc.perform(get("/user"))
		   .andExpect(status().isOk());
	}
	
	@Test
	@Order(8)
	@WithUserDetails(value="admin")
	public void whenAdminAccessAdminSecurdEndpoint_thenOk() throws Exception
	{
		mvc.perform(get("/admin"))
		   .andExpect(status().isOk());
	}
	
	@Test
	@Order(9)
	@WithUserDetails(value="admin")
	public void whenAdminAccessDeleteSecuredEnpoint_thenOk() throws Exception
	{
		mvc.perform(delete("/delete").content("{}"))
		   .andExpect(status().isOk());
	}
	
}
