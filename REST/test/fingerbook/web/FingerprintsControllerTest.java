package fingerbook.web;

import org.springframework.web.servlet.ModelAndView;

import junit.framework.TestCase;

public class FingerprintsControllerTest extends TestCase {

    public void testHandleRequestView() throws Exception{		
        FingerprintsController controller = new FingerprintsController();
        ModelAndView modelAndView = controller.handleRequest(null, null);		
        assertEquals("setFingerprint.jsp", modelAndView.getViewName());
    }
}