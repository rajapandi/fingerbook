package com.fingerbook.rest.web;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.persistencehbase.PersistentFingerbook;
import com.fingerbook.rest.service.FingerbookServices;

@Controller
@RequestMapping("/fingerbooks")
public class FingerbooksController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    // DI
    private FingerbookServices fingerbookService;
    
    /** 
     * Returns de Fingerbook with the given fingerbookId when method is GET
     * @param fingerbookId
     * @param model
     * @return A Fingerbook
     */
    @RequestMapping(value="/{hash}", method=RequestMethod.GET)
    @ResponseBody
    public Vector<Fingerbook> fileHashExists(@PathVariable("hash") String hash, Model model) {    
    	Vector<Fingerbook> fingerbooks = fingerbookService.getFingerbooksWithHash(hash);
    	model.addAttribute("fingerbooks", fingerbooks);
    	
    	logger.info("Returning fingerbooks which contain hash: " + hash);
    	
    	return fingerbooks;
    }
    
    @RequestMapping(value="/anonymous/put", method=RequestMethod.POST)
    @ResponseBody
    public Response anonymousPUT(@RequestBody Fingerbook fingerbook) {    
    	return genericPUT(fingerbook, "Anonymous");
    }
    
    @RequestMapping(value="/semiauthenticated/put", method=RequestMethod.POST)
    @ResponseBody
    public Response semiauthenticatedPUT(@RequestBody Fingerbook fingerbook) {    
    	return genericPUT(fingerbook, "Semi-Authenticated");
    }
    
    @RequestMapping(value="/authenticated/put", method=RequestMethod.POST)
    @ResponseBody
    public Response authenticatedPUT(@RequestBody Fingerbook fingerbook) {    
    	return genericPUT(fingerbook, "Authenticated");
    }
    
    /**
     * Receives a new Fingerbook with a given state
     * @return 
     * 		If state = START 
     * 			A Response XML object where the rid field is the assigned
     * 			group id  if the transaction was succesfully started and
     * 			added to HBASE, a Response with an error code and description if not
     * 		else if state =  CONTENT
     * 			A Response XML object if the content was succesfully
     * 			added to HBASE, a Response with an error code and description if not
     * 		else if state = FINISH
     * 			A Response XML object if the transaction with group id equal to rid
     * 			was succesfully finished, a Response with an error code and 
     * 			description if not
     */
    public Response genericPUT(Fingerbook fingerbook, String authenticationMethod) {    
    	
    	logger.info(authenticationMethod + ": Received fingerbook xml with state: " + fingerbook.getState());
    	PersistentFingerbook pf = new PersistentFingerbook(fingerbook); 
    	
    	if(fingerbook.getState() == Fingerbook.STATE.START) {
    		return startTransaction(pf, authenticationMethod);
    	} else if(fingerbook.getState() == Fingerbook.STATE.CONTENT) {
    		return addContent(fingerbook, pf, authenticationMethod);
    	} else if(fingerbook.getState() == Fingerbook.STATE.FINISH) {
    		return finishTransaction(fingerbook, pf, authenticationMethod);
    	} else {
    		String msg = "Invalid fingerbook state";
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return new Response(new Integer(4), msg);
    	}
    }

	private Response startTransaction(PersistentFingerbook pf, String authenticationMethod) {
    	logger.info(authenticationMethod + ": Starting new transaction");
    	
    	// Save to HBASE. Get id to continue with transaction
		Long fbId = pf.saveMe();   	
		// Get Transaction ID
		String transactionId = pf.createTransactionId(fbId);
		
		if(fbId >= 0) {
			Response response = new Response(null, "Transaction succesfully started", fbId);   
    		String ticket = PersistentFingerbook.createTicket(fbId);
    		response.setTicket(ticket);
    		response.setTransactionId(transactionId);
    		logger.info(authenticationMethod + ": Returning Response object with fbId: " + fbId + "; ticket: " + ticket
    				+ "; transactionId: " + transactionId);
    		return response;
		} else {
			String msg = "Transaction: There was an unexpected error: Transaction start aborted.";
			Response response = new Response(new Integer(3), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
		}
	}
	
    private Response addContent(Fingerbook fingerbook, PersistentFingerbook pf, String authenticationMethod) {
     	logger.info(authenticationMethod + ": Adding content to fingerbook id: " + fingerbook.getFingerbookId());
     	// Add new content to fingerbook fbId
     	// TODO: What about the transactionId?
     	Long fbId = PersistentFingerbook.saveFingerprints(fingerbook);
     	if(fbId >= 0) {
	    	fingerbook.setFingerbookId(fbId);
	    	String msg = "Fingerbook succesfully added to fingerbook id: " +  fingerbook.getFingerbookId();
	    	Response response = new Response(null, msg);
	    	logger.info(authenticationMethod + ": Returning response object: " + msg);
			return response;
     	} else {
     		String msg = "Fingerbook could not be added to fingerbook id: " + fingerbook.getFingerbookId();
			Response response = new Response(new Integer(5), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
     	}
	}
    
	private Response finishTransaction(Fingerbook fingerbook, PersistentFingerbook pf, String authenticationMethod) {
    	logger.info(authenticationMethod + ": Finishing transaction");
    	// TODO Call PersistentFingerbook.commitSave()
    	// TODO: What about transactionId?
    	Long fbId = PersistentFingerbook.commitSave(fingerbook);
      	if(fbId >= 0) {
	    	fingerbook.setFingerbookId(fbId);
	    	String msg = "Transaction succefully finished with fingerbook id: " +  fingerbook.getFingerbookId();
	    	Response response = new Response(null, msg);
	    	logger.info(authenticationMethod + ": Returning response object: " + msg);
			return response;
     	} else {
     		String msg = "Transaction with fingerbook id: " + fingerbook.getFingerbookId() + 
     					" could not be finished";
			Response response = new Response(new Integer(2), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
     	}
	}

	/**
     * Catches erroneous mappings for GET requests
     * @return An ErrorResponse XML object with an error code and description
     */
    @RequestMapping("/*")
    @ResponseBody
	public Response catchAll() {
    	// TODO: Place Error codes and descs in some file
    	Response error = new Response(new Integer(1), "Fingerbooks: Missing arguments");
    	
    	logger.info("Fingerbooks: Missing arguments. Returning error response");
    	
    	return error;
	}

	public FingerbookServices getFingerbookService() {
		return fingerbookService;
	}

	public void setFingerbookService(FingerbookServices fingerbookService) {
		this.fingerbookService = fingerbookService;
	}
    
    /*
    @RequestMapping
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Returning setFingerprint view");

        return new ModelAndView("setFingerprint.jsp");
    }*/
    
    
}