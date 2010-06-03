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

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.Response;
import com.fingerbook.persistencehbase.PersistentFingerbook;
import com.fingerbook.rest.service.FingerbookServices;

@Controller
@RequestMapping("/fingerprints")
public class FingerprintsController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	 // DI
    private FingerbookServices fingerbookService;
    
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
     * @return A Response XML object with if Fingerprints was succesfully
     * added to HBASE, a Response with and error code and description if not
     */
    @RequestMapping(value="/new", method=RequestMethod.POST)
    @ResponseBody
    public Response fileHashExists(@RequestBody Fingerbook fingerbook) {    
    	
    	logger.info("Received fingerbook xml with: " + fingerbook.toString());
    	logger.info("Sending fingerbook to HBASE");
    	
    	// TODO add to persistence
    	PersistentFingerbook pf = new PersistentFingerbook(fingerbook);    	
    	fingerbook.setFingerbookId(pf.saveMe());
    	
    	//TODO send response
    	Response response = new Response(null, "Fingerprints succesfully added to Fingerbook with ID: " 
    			+ fingerbook.getFingerbookId());
    	
    	//TODO When does it fail?
    	
    	
    	return response;
    }

    /**
     * Catches erroneous mappings for GET requests
     * @return A Response XML object with an error code and description
     */
    @RequestMapping("/*")
    @ResponseBody
	public Response catchAll() {
    	// TODO: Place Error codes and descs in some file
    	Response error = new Response(new Integer(2), "Fingerprints: Missing arguments");
    	
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
