package com.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lib.HelloService;

public class MyOwnHelloService implements HelloService 
{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void greet() {
		logger.info("My Own Hello Service!");
	}	
	
}
