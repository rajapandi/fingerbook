package com.fingerbook.rest.web;

import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

public class FingerbookControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@SuppressWarnings("unused")
	private FingerbooksController controller;
	
	/*
	 * http://blog.jteam.nl/2009/07/01/testing-the-database-layer/
	 * http://stackoverflow.com/questions/2289243/what-class-should-i-inherit-to-create-a-database-integration-test-in-case-of-spri
	 */
	
	public void setFingerbooksController(FingerbooksController controller) {
		this.controller = controller;
	}
}
