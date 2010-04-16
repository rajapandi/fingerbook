package fingerbook.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fingerbook.domain.Fingerbook;

public class FingerbookServices {

	protected final Log logger = LogFactory.getLog(getClass());

	public Fingerbook getFingerbook(Long fingerbookId) {
		// TODO Robot service
		Fingerbook fingerbook = new Fingerbook();
		fingerbook.setFingerbookId(fingerbookId);
		
		return fingerbook;
	}
	
	
}
