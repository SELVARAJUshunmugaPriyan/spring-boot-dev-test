package com.simple.proj.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FizzBuzzTests {

	@ParameterizedTest
	@ValueSource(ints = {3, 6, 9, 12, 33, 36, 39, 42})
	public void shouldConvertMultipleOf3sInFizzTest(int input)
	{
		var sut = new FizzBuzzer();
		assertEquals("Fizz", sut.convert(input));
	}
	
	@ParameterizedTest
	@MethodSource("inputAndResults")
	public void shouldConvert(int input, String expectedResults)
	{
		var sut = new FizzBuzzer();
		assertEquals(expectedResults, sut.convert(input));
	}
	
	private static Stream<Arguments> inputAndResults()
	{
		return Stream.of(
					Arguments.of(-1, "N/A"),
					Arguments.of(0, "N/A"),
					Arguments.of(1001, "N/A"),
					Arguments.of(1, "1"),
					Arguments.of(2, "2"),
					Arguments.of(3, "Fizz"),
					Arguments.of(4, "4"),
					Arguments.of(5, "Buzz"),
					Arguments.of(6, "Fizz"),
					Arguments.of(10, "Buzz"),
					Arguments.of(15, "FizzBuzz"),
					Arguments.of(30, "FizzBuzz"),
					Arguments.of(33, "Fizz"),
					Arguments.of(45, "FizzBuzz")
				);
	}
}
