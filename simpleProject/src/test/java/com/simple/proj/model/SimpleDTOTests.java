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
		
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	public void checkEmpIdNotNullTest()
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
	public void checkPhoneNumberNotEmptyTest()
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
	public void checkNameNotBlankTest()
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
	public void checkAgeBetweenMinMaxTest()
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
	public void checkEmailIdValidAndNotBlankTest()
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
	
	
	
}
