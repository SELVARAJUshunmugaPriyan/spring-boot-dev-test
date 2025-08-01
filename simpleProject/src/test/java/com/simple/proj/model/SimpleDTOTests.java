package com.simple.proj.model;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class SimpleDTOTests {

	private Validator validator;
	private SimpleDTO dto;
	private Set<ConstraintViolation<SimpleDTO>> violations;
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleDTOTests.class);
	
	static Set<ConstraintViolation<SimpleDTO>> peek(Set<ConstraintViolation<SimpleDTO>> x)
	{
		x.stream()
		 .map(y -> "\"" + y.getMessage() + "\"" +
				   " Failed for imput: (" +
				   String.valueOf(y.getInvalidValue()) + ")")
		 .forEach(logger::info);
		
		return x;
	}
	
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
		dto.setAddress("A".repeat(10));
		
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	void checkEmpIdNotNullTest()
	{
		dto.setEmpId(null);
		violations = peek(validator.validate(dto));
		Assertions.assertFalse(violations.isEmpty());
		
		dto.setEmpId(1L);
		violations = peek(validator.validate(dto));
		Assertions.assertTrue(violations.isEmpty());
	}
	
	@Test
	void checkPhoneNumberNotEmptyTest()
	{
		dto.setPhoneNumber(null);
		violations = peek(validator.validate(dto));
		Assertions.assertFalse(violations.isEmpty());
		
		dto.setPhoneNumber(new String());
		violations = peek(validator.validate(dto));
		Assertions.assertFalse(violations.isEmpty());
		
		dto.setPhoneNumber("-");
		violations = peek(validator.validate(dto));
		Assertions.assertTrue(violations.isEmpty());
	}
	
	@Test
	void checkNameNotBlankTest()
	{
		dto.setName(null);
		
		violations = peek(validator.validate(dto));
		Assertions.assertFalse(violations.isEmpty());
		
		dto.setName(new String());
		
		violations = peek(validator.validate(dto));
		Assertions.assertFalse(violations.isEmpty());
		
		dto.setName(" ");
		
		violations = peek(validator.validate(dto));
		Assertions.assertFalse(violations.isEmpty());
		
		dto.setName("Name");
		
		violations = peek(validator.validate(dto));
		Assertions.assertTrue(violations.isEmpty());
	}
	
	@Test
	void checkAgeBetweenMinMaxTest()
	{
		dto.setAge(0);
		
		violations = peek(validator.validate(dto));
		
		dto.setAge(101);
		
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setAge(null);
		
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setAge(50);
		
		violations.addAll(peek(validator.validate(dto)));
		
		Assertions.assertTrue(violations.size() == 3);
	}
	
	@Test
	void checkEmailIdValidAndNotBlankTest()
	{
		dto.setEmailId(null);
		violations = peek(validator.validate(dto));
		
		dto.setEmailId(new String());
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId(" ");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("@");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("@.");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId(".");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("@e.");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("@.com");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("user@.com");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("user@");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("user@.");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("user.");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("user.com");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("user@gmail.c");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setEmailId("u@g.com");
		violations.addAll(peek(validator.validate(dto)));
		
		Assertions.assertTrue(violations.size() == 14);
	}
	
	@Test
	void checkPostalCodeNotBlankAndIsValid()
	{
		dto.setPostalCode(null);
		violations = peek(validator.validate(dto));

		dto.setPostalCode(new String()); // NotEmpty is unnecessary it is managed within @email
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setPostalCode(" "); // NotBlank is unnecessary it is managed within @email
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setPostalCode("aaaaa");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setPostalCode("3.333"); // No error since it is not regex pattern and does not confuse
									// it with . (any character) 
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setPostalCode("3333");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setPostalCode("333333");
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setPostalCode("33333");
		violations.addAll(peek(validator.validate(dto)));
		
		Assertions.assertTrue(violations.size() == 7);
	}
	
	@Test
	void checkAddressLengthBounded()
	{
		dto.setAddress(null);
		violations = peek(validator.validate(dto));
		
		dto.setAddress(new String());
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setAddress("0".repeat(101));
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setAddress("0".repeat(10));
		violations.addAll(peek(validator.validate(dto)));
		
		dto.setAddress("0".repeat(100));
		violations.addAll(peek(validator.validate(dto)));
		
		Assertions.assertTrue(violations.size() == 3);
	}
	
}
