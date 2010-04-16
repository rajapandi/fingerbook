package fingerbook.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fingerbook.domain.ErrorResponse;
import fingerbook.domain.Fingerprints;
import fingerbook.service.FingerbookServices;

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

	public FingerbookServices getFingerbookService() {
		return fingerbookService;
	}

	public void setFingerbookService(FingerbookServices fingerbookService) {
		this.fingerbookService = fingerbookService;
	}
}
