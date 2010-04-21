package com.fingerbook.rest.web;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.*;


import com.fingerbook.models.Fingerbook;
import com.fingerbook.rest.domain.ErrorResponse;
import com.fingerbook.rest.service.FingerbookServices;

@Controller
@RequestMapping("/fingerbooks")
public class FingerbooksController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    // TODO: Do it with IoC
    private FingerbookServices fingerbookService = new FingerbookServices();
    
    /** 
     * Returns de Fingerbook with the given fingerbookId when method is GET
     * @param fingerbookId
     * @param model
     * @return A Fingerbook
     */
    @RequestMapping(value="/{fingerbookId}", method=RequestMethod.GET)
    @ResponseBody
    public Fingerbook getFingerbook(@PathVariable("fingerbookId") Long fingerbookId, Model model) {    
    	//TODO: If I call /fingerbooks/asdsa it fails
    	Fingerbook fingerbook = fingerbookService.getFingerbook(fingerbookId);
    	model.addAttribute("fingerbook", fingerbook);
    	
    	logger.info("Returning fingerbook with id: " + fingerbookId);
    	
    	return fingerbook;
    }
    
    /**
     * Catches erroneous mappings for GET requests
     * @return An ErrorResponse XML object with an error code and description
     */
    @RequestMapping("/*")
    @ResponseBody
	public ErrorResponse catchAll() {
    	// TODO: Place Error codes and descs in some file
    	ErrorResponse error = new ErrorResponse(new Integer(1), "Fingerbooks: Missing arguments");
    	
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
