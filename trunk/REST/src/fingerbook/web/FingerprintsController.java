package fingerbook.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.*;

@Controller
public class FingerprintsController {

    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping("/*")
	public String catchAll() {
		return "setFingerprint";
	}
    
    /*
    @RequestMapping
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Returning setFingerprint view");

        return new ModelAndView("setFingerprint.jsp");
    }*/
}
