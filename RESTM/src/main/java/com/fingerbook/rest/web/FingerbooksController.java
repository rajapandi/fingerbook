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
    @RequestMapping(value="/new", method=RequestMethod.POST)
    @ResponseBody
    public Response fileHashExists(@RequestBody Fingerbook fingerbook) {    
    	
    	logger.info("Received fingerbook xml with state: " + fingerbook.getState());
    	PersistentFingerbook pf = new PersistentFingerbook(fingerbook); 
    	
    	if(fingerbook.getState() == Fingerbook.STATE.START) {
    		return startTransaction(pf);
    	} else if(fingerbook.getState() == Fingerbook.STATE.CONTENT) {
    		return addContent(fingerbook, pf);
    	} else if(fingerbook.getState() == Fingerbook.STATE.FINISH) {
    		return finishTransaction(fingerbook, pf);
    	} else {
    		String msg = "Invalid fingerbook state";
    		logger.info("Returning error Response object: " + msg);
    		return new Response(new Integer(4), msg);
    	}
    }

	private Response startTransaction(PersistentFingerbook pf) {
    	logger.info("Starting new transaction");
    	// Save to HBASE. Get id to continue with transaction
		Long rid = pf.saveMe();   	
		if(rid >= 0) {
			Response response = new Response(null, "Transaction succesfully started", rid);   		
    		logger.info("Returning Response object with rid: " + rid);
    		return response;
		} else {
			String msg = "Transaction: There was an unexpected error: Transaction aborted.";
			Response response = new Response(new Integer(3), msg);   		
    		logger.info("Returning error Response object: " + msg);
    		return response;
		}
	}
	
    private Response addContent(Fingerbook fingerbook, PersistentFingerbook pf) {
     	logger.info("Adding content to fingerbook id: " + fingerbook.getFingerbookId());
//     	Long fbId = pf.saveMe();
     	Long fbId = PersistentFingerbook.saveFingerprints(fingerbook);
     	if(fbId >= 0) {
	    	fingerbook.setFingerbookId(fbId);
	    	String msg = "Fingerbook succesfully added to fingerbook id: " +  fingerbook.getFingerbookId();
	    	Response response = new Response(null, msg);
	    	logger.info("Returning response object: " + msg);
			return response;
     	} else {
     		String msg = "Fingerbook could not be added to fingerbook id: " + fingerbook.getFingerbookId();
			Response response = new Response(new Integer(5), msg);   		
    		logger.info("Returning error Response object: " + msg);
    		return response;
     	}
	}
    
	private Response finishTransaction(Fingerbook fingerbook, PersistentFingerbook pf) {
    	logger.info("Finishing transaction");
    	//TODO Call PersistentFingerbook.commitSave()
//    	Long fbId = pf.saveMe();  	
    	Long fbId = PersistentFingerbook.commitSave(fingerbook);
      	if(fbId >= 0) {
	    	fingerbook.setFingerbookId(fbId);
	    	String msg = "Transaction succefully finished with fingerbook id: " +  fingerbook.getFingerbookId();
	    	Response response = new Response(null, msg);
	    	if(true) {  //TODO return ticket only when not previously using one
//	    		String ticket = fingerbookService.generateTicket();
	    		String ticket = PersistentFingerbook.createTicket(fbId);
	    		response.setTicket(ticket);
	    	}
	    	logger.info("Returning response object: " + msg);
			return response;
     	} else {
     		String msg = "Transaction with fingerbook id: " + fingerbook.getFingerbookId() + 
     					" could not be finished";
			Response response = new Response(new Integer(5), msg);   		
    		logger.info("Returning error Response object: " + msg);
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