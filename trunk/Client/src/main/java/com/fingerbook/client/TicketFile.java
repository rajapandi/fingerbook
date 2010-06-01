package com.fingerbook.client;

import java.io.File;

public class TicketFile {

	String ticket = null;
	private static final int ticketLenght = 41;
	
	public TicketFile(File file) throws Exception {
		if(file.length() != ticketLenght)
			throw new Exception("Invalid Ticket");
		else {
			java.util.Scanner scanner = new java.util.Scanner(file);
			ticket = scanner.next();
		}
	}
	
	public String getTicket() {
		return ticket;
	}

}
