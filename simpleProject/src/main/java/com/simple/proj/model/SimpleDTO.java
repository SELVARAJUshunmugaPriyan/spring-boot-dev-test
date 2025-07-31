package com.simple.proj.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SimpleDTO {

	@NotNull(message="Enter a valid Employee Id")
	private Long empId;
	
	@NotEmpty(message="Must not be Empty and NULL")
	private String phoneNumber;

	@NotBlank(message="Name must not be left blank")
	private String name;
	
	@NotNull(message="Age must not be null")
	@Min(value=1,message="Minimum age is 1")
	@Max(value=100,message="Maximum age is 100")
	private Integer age;
	
	@Email(message="Please enter a valid mail id")
	@NotBlank(message="Email must not be left blank")
	private String emailId;
	
//	@Pattern(regexp="^[0-9]{5}$")
}
