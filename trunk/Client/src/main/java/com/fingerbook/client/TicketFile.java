package com.fingerbook.client;

import java.io.File;

public class TicketFile {
	String ticket = null;
	private static final int ticketLenght = 40;
	
	public TicketFile(File file) throws Exception {
		/* Ticket MUST be 40 characters long */
		if(file.length() != ticketLenght)
			throw new Exception("Invalid Ticket");
		else {
			/* Read ticket from file */
			java.util.Scanner scanner = new java.util.Scanner(file);
			ticket = scanner.next();
		}
	}
	
	public String getTicket() {
		return ticket;
	}
}
