package fingerbook.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.*;

import fingerbook.domain.ErrorResponse;
import fingerbook.domain.Fingerbook;
import fingerbook.service.FingerbookServices;

@Controller
public class FingerbooksController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    FingerbookServices fingerbookService = new FingerbookServices();
    
    @RequestMapping(value="/fingerbooks/{fingerbookId}", method=RequestMethod.GET)
    @ResponseBody
    public Fingerbook getFingerbook(@PathVariable("fingerbookId") Long fingerbookId, Model model) {    	
    	Fingerbook fingerbook = fingerbookService.getFingerbook(fingerbookId);
    	model.addAttribute("fingerbook", fingerbook);
    	
    	logger.info("Returning fingerbook with id: " + fingerbookId);
    	
    	return fingerbook;
    }
    
    @RequestMapping("/*")
    @ResponseBody
	public ErrorResponse catchAll() {
    	//TODO: write error codes and descs correctly
    	ErrorResponse error = new ErrorResponse(new Integer(1), "Fingerbooks: Missing arguments");
    	
    	logger.info("Fingerbooks: Missing arguments. Returning error response");
    	
    	return error;
	}
    
    /*
    @RequestMapping
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Returning setFingerprint view");

        return new ModelAndView("setFingerprint.jsp");
    }*/
}
