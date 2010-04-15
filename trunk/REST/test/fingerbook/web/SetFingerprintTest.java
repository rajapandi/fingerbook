package fingerbook.web;

import org.springframework.web.servlet.ModelAndView;

import junit.framework.TestCase;

public class SetFingerprintTest extends TestCase {

    public void testHandleRequestView() throws Exception{		
        SetFingerprint controller = new SetFingerprint();
        ModelAndView modelAndView = controller.handleRequest(null, null);		
        assertEquals("setFingerprint.jsp", modelAndView.getViewName());
    }
}