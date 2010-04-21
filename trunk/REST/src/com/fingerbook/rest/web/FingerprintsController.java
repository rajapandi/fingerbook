package com.fingerbook.rest.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.rest.domain.ErrorResponse;
import com.fingerbook.rest.domain.Fingerprints;
import com.fingerbook.rest.domain.SuccessResponse;
import com.fingerbook.rest.service.FingerbookServices;

@Controller
@RequestMapping("/fingerprints")
public class FingerprintsController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	 // TODO: Do it with IoC
    private FingerbookServices fingerbookService = new FingerbookServices();
    
    @RequestMapping(value="/{hash}", method=RequestMethod.GET)
    @ResponseBody
    public List<Fingerprints> fileHashExists(@PathVariable("hash") String hash, Model model) {    
    	List<Fingerprints> fingerprints = fingerbookService.getFingerprintsWithHash(hash);
    	model.addAttribute("fingerprints", fingerprints);
    	
    	logger.info("Returning fingerprints which contain hash: " + hash);
    	
    	return fingerprints;
    }
    
    /**
     * Receives a new Fingerprints and adds it to HBASE
     * @return A SuccessREsponse XML object with if the Fingerprints was succesfully
     * added to HBASE, an ErrorResponse with and error code and description if not
     */
    @RequestMapping(value="/new", method=RequestMethod.POST)
    @ResponseBody
    public SuccessResponse fileHashExists(@RequestBody Fingerprints fingerprints) {    
    	
    	// TODO: Add it to HBASE. if succesfull return this message
    	SuccessResponse response = new SuccessResponse("Fingerprints succesfully added");
    	logger.info("Received fingerprints xml with: " + fingerprints.toString());
    	
    	return response;
    }

    /**
     * Catches erroneous mappings for GET requests
     * @return An ErrorResponse XML object with an error code and description
     */
    @RequestMapping("/*")
    @ResponseBody
	public ErrorResponse catchAll() {
    	// TODO: Place Error codes and descs in some file
    	ErrorResponse error = new ErrorResponse(new Integer(2), "Fingerprints: Missing arguments");
    	
    	logger.info("Fingerbooks: Missing arguments. Returning error response");
    	
    	return error;
	}
    
    /*
    @ExceptionHandler(Exception.class)
    public void handle() {
        System.out.println("exception");
    }*/

	public FingerbookServices getFingerbookService() {
		return fingerbookService;
	}

	public void setFingerbookService(FingerbookServices fingerbookService) {
		this.fingerbookService = fingerbookService;
	}
}
