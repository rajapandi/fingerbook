package com.fingerbook.rest.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Response;
import com.fingerbook.rest.service.FingerbookServices;

@Controller
@RequestMapping("/ticketAuthenticate")
public class TicketAuthenticateController {

	// DI
    private FingerbookServices fingerbookService;
    
	protected final Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(method=RequestMethod.POST)
    @ResponseBody
	public Response ticketAuthenticate(@RequestParam("ticket") String ticket, 
			Model model) {	
		Response response;
		
		if(fingerbookService.isValidTicket(ticket)) {
			String role = "[ROLE_SEMIAUTH]";
	    	String msg = "Authentication succesful for ticket: " +  ticket + ", role: "
	    		+ role;
	    	response = new Response(null, msg);
	    	logger.info(msg);
		} else {
			String msg = "Permission denied for ticket: " +  ticket;
	    	response = new Response(new Integer(12), msg);
	    	logger.info(msg);
		}
    	
		return response;
	}
	
	public FingerbookServices getFingerbookService() {
		return fingerbookService;
	}

	public void setFingerbookService(FingerbookServices fingerbookService) {
		this.fingerbookService = fingerbookService;
	}
}
