package com.simple.proj.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	
	@NotNull(message="Postal code must not be null")
	@Pattern(regexp="^[0-9]{5}$",message="Employee postal code must be a 5-digit number.")
	private String postalCode;
	
	@NotNull(message="Address cannot be null")
	@Size(min=10,max=100,message="Address should have a length between 10 and 100 characters.")
	private String address;
}
