package com.simple.proj.model;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class SimpleDTOTests {

	private Validator validator;
	private SimpleDTO dto;
	private Set<ConstraintViolation<SimpleDTO>> violations;
	
	@BeforeEach
	void setup()
	{
		dto = new SimpleDTO();
		
		dto.setEmpId(1L);
		dto.setName("Name");
		dto.setPhoneNumber("-");
		dto.setAge(50);
		dto.setEmailId("abc.def@example.com");
		dto.setPostalCode("00000");
		
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	void checkEmpIdNotNullTest()
	{
		dto.setEmpId(null);
		
		violations = validator.validate(dto);
		
		Assertions.assertFalse(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
		
		dto.setEmpId(1L);
		
		violations = validator.validate(dto);
		
		Assertions.assertTrue(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
	}
	
	@Test
	void checkPhoneNumberNotEmptyTest()
	{
		dto.setPhoneNumber(null);
		
		violations = validator.validate(dto);
		Assertions.assertFalse(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
		
		dto.setPhoneNumber(new String());
		
		violations = validator.validate(dto);
		Assertions.assertFalse(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
		
		dto.setPhoneNumber("-");
		
		violations = validator.validate(dto);
		Assertions.assertTrue(violations.isEmpty());
	}
	
	@Test
	void checkNameNotBlankTest()
	{
		dto.setName(null);
		
		violations = validator.validate(dto);
		Assertions.assertFalse(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
		
		dto.setName(new String());
		
		violations = validator.validate(dto);
		Assertions.assertFalse(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
		
		dto.setName(" ");
		
		violations = validator.validate(dto);
		Assertions.assertFalse(violations.isEmpty());
		violations.forEach(x -> System.out.println(x.getMessage()));
		
		dto.setName("Name");
		
		violations = validator.validate(dto);
		Assertions.assertTrue(violations.isEmpty());
	}
	
	@Test
	void checkAgeBetweenMinMaxTest()
	{
		dto.setAge(0);
		
		violations = validator.validate(dto);
		
		dto.setAge(101);
		
		violations.addAll(validator.validate(dto));
		
		dto.setAge(null);
		
		violations.addAll(validator.validate(dto));
		
		dto.setAge(50);
		
		violations.addAll(validator.validate(dto));
		
		Assertions.assertTrue(violations.size() == 3);
		violations.forEach(x -> System.out.println(x.getMessage()));
	}
	
	@Test
	void checkEmailIdValidAndNotBlankTest()
	{
		// 1
		dto.setEmailId(null);
		violations = validator.validate(dto);
		
		// 2
		dto.setEmailId(new String());
		violations.addAll(validator.validate(dto));
		
		// 3
		dto.setEmailId(" ");
		violations.addAll(validator.validate(dto));
		
		// 4
		dto.setEmailId("@");
		violations.addAll(validator.validate(dto));
		
		// 5
		dto.setEmailId("@.");
		violations.addAll(validator.validate(dto));
		
		// 6
		dto.setEmailId(".");
		violations.addAll(validator.validate(dto));
		
		// 7
		dto.setEmailId("@e.");
		violations.addAll(validator.validate(dto));
		
		// 8
		dto.setEmailId("@.com");
		violations.addAll(validator.validate(dto));
		
		// 9
		dto.setEmailId("user@.com");
		violations.addAll(validator.validate(dto));
		
		// 10
		dto.setEmailId("user@");
		violations.addAll(validator.validate(dto));
		
		// 11
		dto.setEmailId("user@.");
		violations.addAll(validator.validate(dto));
		
		// 12
		dto.setEmailId("user.");
		violations.addAll(validator.validate(dto));
		
		// 13
		dto.setEmailId("user.com");
		violations.addAll(validator.validate(dto));
		
		// 14
		dto.setEmailId("user@gmail.c");
		violations.addAll(validator.validate(dto));
		
		// 15
		dto.setEmailId("u@g.com");
		violations.addAll(validator.validate(dto));
		
		Assertions.assertTrue(violations.size() == 14);
		violations.forEach(x -> System.out.println(x.getMessage()));
	}
	
	@Test
	void checkPostalCodeNotBlankAndIsValid()
	{
		// 1
		dto.setPostalCode(null);
		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("1 " + x.getMessage()));
		
		// 2
		dto.setPostalCode(new String()); // NotEmpty is unnecessary it is managed within @email
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("2 " + x.getMessage()));
		
		// 3
		dto.setPostalCode(" "); // NotBlank is unnecessary it is managed within @email
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("3 " + x.getMessage()));
		
		// 4
		dto.setPostalCode("aaaaa");
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("4 " + x.getMessage()));
		
		// 5
		dto.setPostalCode("3.333"); // No error since it is not regex pattern and does not confuse
									// it with . (any character) 
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("5 " + x.getMessage()));
		
		// 6
		dto.setPostalCode("3333");
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("6 " + x.getMessage()));
		
		// 7
		dto.setPostalCode("333333");
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("7 " + x.getMessage()));
		
		// 8
		dto.setPostalCode("33333");
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("8 " + x.getMessage()));
		
		Assertions.assertTrue(violations.size() == 7);
		violations.forEach(x -> System.out.println(x.getMessage()));
	}
	
	@Test
	void checkAddressLengthBounded()
	{
		// 1
		dto.setAddress(null); // NotNull is not checked by @Size
		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("1 " + x.getMessage()));
		
		// 2
		dto.setAddress(new String());
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("2 " + x.getMessage()));
		
		// 3
		dto.setAddress("0".repeat(101));
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("3 " + x.getMessage()));
		
		// 4
		dto.setAddress("0".repeat(10));
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("4 " + x.getMessage()));
		
		// 5
		dto.setAddress("0".repeat(100));
		violations.addAll(validator.validate(dto));
//		violations = validator.validate(dto);
//		violations.forEach(x -> System.out.println("5 " + x.getMessage()));
		
		Assertions.assertTrue(violations.size() == 3);
		violations.forEach(x -> System.out.println(x.getMessage()));
	}
	
}
