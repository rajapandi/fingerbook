package com.fingerbook.rest.web;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerbook.models.Auth;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.Fingerbook.STATE;
import com.fingerbook.models.transfer.FingerbookList;
import com.fingerbook.models.transfer.FingerprintsFeed;
import com.fingerbook.models.Response;
import com.fingerbook.persistencehbase.PersistentFingerbook;
import com.fingerbook.rest.service.FingerbookServices;
import com.fingerbook.rest.domain.repository.UserRepository;

@Controller
@RequestMapping("/fingerbooks")
public class FingerbooksController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    public static final String anonymous = "Anonymous";
    public static final String semiAuthenticated = "Semi-Authenticated";
    public static final String authenticated = "Authenticated";
    
    // DI
    private FingerbookServices fingerbookService;
    @Autowired
    private UserRepository userRepository;
    
    /** 
     * Returns de Fingerbook with the given fingerbookId when method is GET
     * @param fingerbookId
     * @param model
     * @return A Fingerbook
     */
//    @RequestMapping(value="/{hash}", method=RequestMethod.GET)
//    @ResponseBody
//    public Vector<Fingerbook> fileHashExists(@PathVariable("hash") String hash, Model model) {    
//    	Vector<Fingerbook> fingerbooks = fingerbookService.getFingerbooksWithHash(hash);
//    	model.addAttribute("fingerbooks", fingerbooks);
//    	
//    	logger.info("Returning fingerbooks which contain hash: " + hash);
//    	
//    	return fingerbooks;
//    }
    
    @RequestMapping(value="/hash/{hash}", method=RequestMethod.GET)
    @ResponseBody
    public Vector<Fingerbook> fileHashExists(@PathVariable("hash") String hash, Model model) {    
    	Vector<Fingerbook> fingerbooks = fingerbookService.getFingerbooksWithHash(hash);
    	model.addAttribute("fingerbooks", fingerbooks);
    	
    	logger.info("Returning fingerbooks which contain hash: " + hash);
    	
    	return fingerbooks;
    }
    
    @RequestMapping(value="/ticket/{ticket}", method=RequestMethod.GET)
    @ResponseBody
    public Vector<Fingerbook> fingerbooksByTicket(@PathVariable("ticket") String ticket, Model model) {    
    	Vector<Fingerbook> fingerbooks = fingerbookService.getFingerbooksByTicket(ticket);
    	model.addAttribute("fingerbooks", fingerbooks);
    	
    	logger.info("Returning fingerbooks for ticket: " + ticket);
    	
    	return fingerbooks;
    }
    
    
    @RequestMapping(value="/hash/{hash}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public FingerbookList fingerbooksByHash(@PathVariable("hash") String hash, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {    
    	FingerbookList fingerbookList = fingerbookService.getFingerbookListByHash(hash, limit, offset);
    	model.addAttribute("fingerbookList", fingerbookList);
    	
    	logger.info("Returning fingerbookList for hash: " + hash);
    	
    	return fingerbookList;
    }
    
    @RequestMapping(value="/semiauthenticated/ticket/{ticket}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public FingerbookList fingerbooksByTicket(@PathVariable("ticket") String ticket, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {    
    	FingerbookList fingerbookList = fingerbookService.getFingerbookListByTicket(ticket, limit, offset);
    	model.addAttribute("fingerbookList", fingerbookList);
    	
    	logger.info("Returning fingerbookList for ticket: " + ticket);
    	
    	return fingerbookList;
    }
    
    @RequestMapping(value="/authenticated/user/{user}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public FingerbookList fingerbooksByUser(@PathVariable("user") String user, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {
    	
    	FingerbookList fingerbookList = null;
    	Response ans = null;
    	if(!fingerbookService.validateAuthUser(user)) {
    		
    		String msg = "Operation cancelled: Authentication is used, authenticated user not allowed to access user's: " + user + " fingerbooks";
			logger.info(authenticated + ": Returning error Response object: " + msg);
			ans =  new Response(new Integer(13), msg);
			fingerbookList = new FingerbookList();
    		fingerbookList.setResponse(ans);
    		return fingerbookList;
    	}
    	
    	fingerbookList = fingerbookService.getFingerbookListByUser(user, limit, offset);
    	model.addAttribute("fingerbookList", fingerbookList);
    	
    	logger.info("Returning fingerbookList for user: " + user);
    	
    	return fingerbookList;
    }
    
    @RequestMapping(value="/user/{user}", method=RequestMethod.GET)
    @ResponseBody
    public Vector<Fingerbook> fingerbooksByUser(@PathVariable("user") String user, Model model) {    
    	Vector<Fingerbook> fingerbooks = fingerbookService.getFingerbooksByUser(user);
    	model.addAttribute("fingerbooks", fingerbooks);
    	
    	logger.info("Returning fingerbooks for user: " + user);
    	
    	return fingerbooks;
    }
    
    @RequestMapping(value="/fingerbook/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Fingerbook fingerbookById(@PathVariable("id") Long id, Model model) {    
//    	Fingerbook fingerbook = fingerbookService.getFingerbookById(id);
    	Fingerbook fingerbook = fingerbookService.getFingerbookById(id, false);
    	model.addAttribute("fingerbooks", fingerbook);
    	
    	logger.info("Returning fingerbook for id: " + id);
    	
    	return fingerbook;
    }
    
    @RequestMapping(value="/authenticated/fingerbook/{id}/user/{user}", method=RequestMethod.GET)
    @ResponseBody
    public Fingerbook fingerbookByIdUser(@PathVariable("user") String user, @PathVariable("id") Long id, Model model) {
    	Fingerbook fingerbook = null;
    	Response ans = null;
    	
    	if(!fingerbookService.validateAuthUser(user)) {
    		
    		String msg = "Operation cancelled: Authentication is used, authenticated user not allowed to access user's: " + user + " fingerbooks";
			logger.info(authenticated + ": Returning error Response object: " + msg);
			ans =  new Response(new Integer(13), msg);
			fingerbook = new Fingerbook();
    		fingerbook.setFingerbookId(-1L);
    	}
    	else if((ans = fingerbookService.validateOwner(id, user, authenticated)) != null) {
    		fingerbook = new Fingerbook();
    		fingerbook.setFingerbookId(-1L);
    	}
    	else {
    		fingerbook = fingerbookService.getFingerbookById(id, false);
    	}
    	
    	model.addAttribute("fingerbooks", fingerbook);
    	logger.info("Returning fingerbook for id: " + id);
    	
    	return fingerbook;
    }
    
    @RequestMapping(value="/semiauthenticated/fingerbook/{id}/ticket/{ticket}", method=RequestMethod.GET)
    @ResponseBody
    public Fingerbook fingerbookByIdTicket(@PathVariable("ticket") String ticket, @PathVariable("id") Long id, Model model) {
    	
    	Fingerbook fingerbook = null;
    	Response ans;
    	if((ans = fingerbookService.validateOwner(id, ticket, semiAuthenticated)) != null) {
    		fingerbook = new Fingerbook();
    		fingerbook.setFingerbookId(-1L);
    	}
    	else {
    		fingerbook = fingerbookService.getFingerbookById(id, false);
    	}
    	
    	model.addAttribute("fingerbooks", fingerbook);
    	
    	logger.info("Returning fingerbook for id: " + id);
    	
    	return fingerbook;
    }
    
    @RequestMapping(value="/anonymous/fingerbook/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Fingerbook fingerbookByIdAnon(@PathVariable("id") Long id, Model model) {
    	Fingerbook fingerbook = null;
    	
    	fingerbook = new Fingerbook();
		fingerbook.setFingerbookId(-1L);
		
		logger.info("Anonymous not able to load fingerbook for id: " + id);
    	
    	return fingerbook;
    }
    
//    @RequestMapping(value="/fingerbook/{id}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
//    @ResponseBody
//    public Fingerbook fingerbookById(@PathVariable("id") Long id, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {    
//    	Fingerbook fingerbook = fingerbookService.getFingerbookById(id, limit, offset);
//    	
//    	model.addAttribute("fingerbooks", fingerbook);
//    	
//    	logger.info("Returning fingerbook for id: " + id);
//    	
//    	return fingerbook;
//    }
    
//    @RequestMapping(value="/fingerprints/{id}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
//    @ResponseBody
//    public FingerprintsFeed fingerprintsById(@PathVariable("id") Long id, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {    
//    	FingerprintsFeed fingerprintsFeed = fingerbookService.getFingerprintsFeedById(id, limit, offset);
//    	model.addAttribute("fingerprintsFeed", fingerprintsFeed);
//    	
//    	logger.info("Returning fingerprintsFeed for id: " + id);
//    	
//    	return fingerprintsFeed;
//    }
    
    @RequestMapping(value="/authenticated/fingerprints/{id}/user/{user}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public FingerprintsFeed fingerprintsByIdAuth(@PathVariable("user") String user,@PathVariable("id") Long id, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {
    	
    	FingerprintsFeed fingerprintsFeed = null;
    	Response ans = null;
    	
    	if(fingerbookService.validateAuthUser(user)) {
    		
    		ans = fingerbookService.validateOwner(id, user, authenticated);
    	}
    	else {
    		String msg = "Operation cancelled: Authentication is used, authenticated user not allowed to access user's: " + user + " fingerbooks";
			logger.info(authenticated + ": Returning error Response object: " + msg);
			ans =  new Response(new Integer(13), msg);
    	}
    	
    	if(ans != null) {
    		fingerprintsFeed = new FingerprintsFeed();
    		fingerprintsFeed.setResponse(ans);
			return fingerprintsFeed;
    	}
    	
    	fingerprintsFeed = fingerbookService.getFingerprintsFeedById(id, limit, offset);
    	model.addAttribute("fingerprintsFeed", fingerprintsFeed);
    	
    	logger.info("Returning fingerprintsFeed for id: " + id);
    	
    	return fingerprintsFeed;
    }
    
    @RequestMapping(value="/semiauthenticated/fingerprints/{id}/ticket/{ticket}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public FingerprintsFeed fingerprintsByIdSemiAuth(@PathVariable("ticket") String ticket,@PathVariable("id") Long id, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {
    	
    	FingerprintsFeed fingerprintsFeed = null;
    	Response ans;
    	if((ans = fingerbookService.validateOwner(id, ticket, semiAuthenticated)) != null) {
    		fingerprintsFeed = new FingerprintsFeed();
    		fingerprintsFeed.setResponse(ans);
			return fingerprintsFeed;
    	}
    	
    	fingerprintsFeed = fingerbookService.getFingerprintsFeedById(id, limit, offset);
    	model.addAttribute("fingerprintsFeed", fingerprintsFeed);
    	
    	logger.info("Returning fingerprintsFeed for id: " + id);
    	
    	return fingerprintsFeed;
    	
    }
    
    @RequestMapping(value="/anonymous/fingerprints/{id}/limit/{limit}/offset/{offset}", method=RequestMethod.GET)
    @ResponseBody
    public FingerprintsFeed fingerprintsByIdAnonymous(@PathVariable("id") Long id, @PathVariable("limit") int limit, @PathVariable("offset") int offset, Model model) {    
    	FingerprintsFeed fingerprintsFeed = fingerbookService.getFingerprintsFeedById(id, limit, offset);
    	model.addAttribute("fingerprintsFeed", fingerprintsFeed);
    	
    	logger.info("Returning fingerprintsFeed for id: " + id);
    	
    	return fingerprintsFeed;
    }
    
//    @RequestMapping(value="/update", method=RequestMethod.POST)
//    @ResponseBody
//    public Integer updateFingerbook(@RequestBody Fingerbook fingerbook) {    
//    	return PersistentFingerbook.updateTagsComment(fingerbook);
//    }
    
    @RequestMapping(value="/anonymous/update", method=RequestMethod.POST)
    @ResponseBody
    public Response anonymousUpdate(@RequestBody Fingerbook fingerbook) {    
    	return genericUpdate(fingerbook, anonymous);
    }
    
    @RequestMapping(value="/semiauthenticated/update", method=RequestMethod.POST)
    @ResponseBody
    public Response semiauthenticatedUpdate(@RequestBody Fingerbook fingerbook) {    
    	return genericUpdate(fingerbook, semiAuthenticated);
    }
    
    @RequestMapping(value="/authenticated/update", method=RequestMethod.POST)
    @ResponseBody
    public Response authenticatedUpdate(@RequestBody Fingerbook fingerbook) {    
    	
    	Response ans = null;
    	if(!fingerbookService.validateAuthUser(fingerbook)) {
    		
    		String msg = "Operation cancelled: Authentication is used, authenticated user not allowed to update fingerbook's user";
			logger.info(authenticated + ": Returning error Response object: " + msg);
			ans =  new Response(new Integer(13), msg);
			return ans;
    	}
    	
    	return genericUpdate(fingerbook, authenticated);
    }
    
    public Response genericUpdate(Fingerbook fingerbook, String authenticationMethod) {    
    	
    	Integer ret = -1;
    	STATE state = Fingerbook.STATE.UPDATE;
    	fingerbook.setState(state);
    	
    	Response ans;
//    	if((ans = isValidRequestUpdate(fingerbook, authenticationMethod)) != null) {
//			return ans;
//    	}
    	if((ans = fingerbookService.validateOwner(fingerbook, authenticationMethod)) != null) {
			return ans;
    	}
		
    	ret = fingerbookService.updateFingerbook(fingerbook);
    	
    	if(ret.equals(new Integer(0))) {
    		ans = new Response(null, "Fingerbook succesfully updated", fingerbook.getFingerbookId());
    		String msg = "Fingerbook succesfully updated fingerbook id: " +  fingerbook.getFingerbookId();
	    	logger.info(authenticationMethod + ": Returning response object: " + msg);
    	}
    	else {
	    	String msg = "Error updating fingerbook id: " + fingerbook.getFingerbookId() + ". Error: " + ret;
	    	ans = new Response(new Integer(2), msg);   		
			logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    	}
		
		return ans;

    }
    
    @RequestMapping(value="/anonymous/put", method=RequestMethod.POST)
    @ResponseBody
    public Response anonymousPUT(@RequestBody Fingerbook fingerbook) {    
    	return genericPUT(fingerbook, anonymous);
    }
    
    @RequestMapping(value="/semiauthenticated/put", method=RequestMethod.POST)
    @ResponseBody
    public Response semiauthenticatedPUT(@RequestBody Fingerbook fingerbook) {    
    	return genericPUT(fingerbook, semiAuthenticated);
    }
    
    @RequestMapping(value="/authenticated/put", method=RequestMethod.POST)
    @ResponseBody
    public Response authenticatedPUT(@RequestBody Fingerbook fingerbook) {    
    	
    	
    	return genericPUT(fingerbook, authenticated);
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
    public Response genericPUT(Fingerbook fingerbook, String authenticationMethod) {    
    	
    	STATE state = fingerbook.getState();
    	logger.info(authenticationMethod + ": Received fingerbook xml with state: " + state);
    	
    	Response ans;
    	if((ans = isValidRequest(fingerbook, authenticationMethod, state)) != null) {
			return ans;
    	}
		
    	// If resuming
    	if(state == Fingerbook.STATE.RESUME) {
    		return resumeTransaction(fingerbook, authenticationMethod);
    	}
    	
    	// If we are not resuming
//    	PersistentFingerbook pf = new PersistentFingerbook(fingerbook); 
    	
    	if(state == Fingerbook.STATE.START) {
//    		return startTransaction(fingerbook, pf, authenticationMethod);
    		return startTransaction(fingerbook, authenticationMethod);
    	} else if(state == Fingerbook.STATE.CONTENT) {
//    		return addContent(fingerbook, pf, authenticationMethod);
    		return addContent(fingerbook, authenticationMethod);
    	} else if(state == Fingerbook.STATE.FINISH) {
//    		return finishTransaction(fingerbook, pf, authenticationMethod);
    		return finishTransaction(fingerbook, authenticationMethod);
    	} else {
    		String msg = "Invalid fingerbook state";
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return new Response(new Integer(4), msg);
    	}
    }

	private Response resumeTransaction(Fingerbook fingerbook, String authenticationMethod) {
		
		logger.info(authenticationMethod + ": Resuming fingerbook with Transaction ID: " + fingerbook.getTransactionId());
		
		// TODO: Call function that receives transaction id + fingerbook and returnes fbId
		
		Response response = null;
		boolean isValid = false;
//		long fbId = -1;
//		String ticket = null;
		
		if(fingerbook != null) {
//			fbId = PersistentFingerbook.getFingerbookIdByTransactionId(fingerbook.getTransactionId());
			
			isValid = PersistentFingerbook.validateResume(fingerbook, authMethodIdByName(authenticationMethod));
			
			if(isValid) {
				response = new Response(null, "Transaction succesfully started", fingerbook.getFingerbookId());
				response.setTransactionId(fingerbook.getTransactionId());
				response.setTicket(fingerbook.getUserInfo().getTicket());
			}
		}
		
		// TODO: Implement. Should send a response with a fbId, transactionId and ticket
		return response;
	}

	private Response startTransaction(Fingerbook fingerbook, String authenticationMethod) {
    	logger.info(authenticationMethod + ": Starting new transaction");
    	
    	int authMethod = authMethodIdByName(authenticationMethod);
    	
    	
    	// Save to HBASE. Get id to continue with transaction
		Long fbId = PersistentFingerbook.saveMe(fingerbook, authMethod);   	
		// Get Transaction ID
		String transactionId = PersistentFingerbook.createTransactionId(fbId);
		
		if(fbId >= 0) {
			Response response = new Response(null, "Transaction succesfully started", fbId);  
			
			String ticket;
			// If using semi-authenticated, provided ticket must be used
			if(authenticationMethod.equals(semiAuthenticated)) {
				ticket = fingerbook.getUserInfo().getTicket();
				logger.info("Using ticket: " + ticket + " from semi authenticated user");
			} else {
				ticket = PersistentFingerbook.createTicket(fbId);
			}
			response.setTicket(ticket);
			response.setTransactionId(transactionId);
    		
    		logger.info(authenticationMethod + ": Returning Response object with fbId: " + fbId + "; ticket: " + ticket
    				+ "; transactionId: " + transactionId);
    		return response;
		} else {
			String msg = "Transaction: There was an unexpected error: Transaction start aborted.";
			Response response = new Response(new Integer(3), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
		}
	}
	
    private Response addContent(Fingerbook fingerbook, String authenticationMethod) {
     	logger.info(authenticationMethod + ": Adding content to fingerbook id: " + fingerbook.getFingerbookId());
     	// Add new content to fingerbook fbId
     	// TODO: What about the transactionId?
     	Long fbId = PersistentFingerbook.saveFingerprints(fingerbook);
     	if(fbId >= 0) {
	    	fingerbook.setFingerbookId(fbId);
	    	String msg = "Fingerbook succesfully added to fingerbook id: " +  fingerbook.getFingerbookId();
	    	Response response = new Response(null, msg);
	    	response.setTransactionId(fingerbook.getTransactionId());
	    	logger.info(authenticationMethod + ": Returning response object: " + msg);
			return response;
     	} else {
     		String msg = "Fingerbook could not be added to fingerbook id: " + fingerbook.getFingerbookId() + " Error: " + fbId;
			Response response = new Response(new Integer(5), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
     	}
	}
    
	private Response finishTransaction(Fingerbook fingerbook, String authenticationMethod) {
    	logger.info(authenticationMethod + ": Finishing transaction");
    	// TODO Call PersistentFingerbook.commitSave()
    	// TODO: What about transactionId?
    	Long fbId = PersistentFingerbook.commitSave(fingerbook);
      	if(fbId >= 0) {
	    	fingerbook.setFingerbookId(fbId);
	    	String msg = "Transaction succefully finished with fingerbook id: " +  fingerbook.getFingerbookId();
	    	Response response = new Response(null, msg);
	    	response.setTransactionId(fingerbook.getTransactionId());
	    	logger.info(authenticationMethod + ": Returning response object: " + msg);
			return response;
     	} else {
     		String msg = "Transaction with fingerbook id: " + fingerbook.getFingerbookId() + 
     					" could not be finished";
			Response response = new Response(new Integer(2), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
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

	private Response isValidRequest(Fingerbook fingerbook, String authenticationMethod, STATE state) {
		
		Response ans = null;
		
		// A transactionID is needed to resume
		if(state == Fingerbook.STATE.RESUME && fingerbook.getTransactionId() == null) {
			String msg = "Resume cancelled, no transaction ID was received";
			logger.info(authenticationMethod + ": Returning error Response object: " + msg);
			return new Response(new Integer(7), msg);
		}
		
		// If an anonymous authentication is used, no ticket should be received
		/*if(authenticationMethod.equals(this.anonymous)) {
			
			// If an anonymous authentication is used, no ticket should be received
			if(fingerbook.getUserInfo().getTicket() != null) {
				String msg = "Operation cancelled: Anonymous authentication is used, but a ticket was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(8), msg);
				
			// If an anonymous authentication is used, no user name should be received		
			} else if(authenticationMethod.equals(this.anonymous) && fingerbook.getUserInfo().getUser() != null) {
				String msg = "Operation cancelled: Anonymous authentication is used, but a user name was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(9), msg);
			
			
		} else */
		if(authenticationMethod.equals(semiAuthenticated)) {
			// If a semi-authentication is used, a ticket is expected
			if(fingerbook.getUserInfo().getTicket() == null) {
				String msg = "Operation cancelled: Semi-authentication is used, but no ticket was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(10), msg);
			} 
			
		} else if(authenticationMethod.equals(authenticated)) {
			// If authenticated is used, a username is expected
			if(fingerbook.getUserInfo().getUser() == null) {
				String msg = "Operation cancelled: Authentication is used, but no user name was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(11), msg);
			}
		}

		return ans;
	}
	
	private Response isValidRequestUpdate(Fingerbook fingerbook, String authenticationMethod) {
		
		Response ans = null;
		UserInfo userInfo = null;
		
		Long fingerbookId = fingerbook.getFingerbookId();
		userInfo = fingerbook.getUserInfo();
		
		if(fingerbookId == null) {
			String msg = "Fingerbook could not be updated. Invalid fingerbookId.";
			Response response = new Response(new Integer(1), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
		}
		
		if(authenticationMethod.equals(semiAuthenticated)) {
			String ticket = null;
			if(userInfo != null) {
				ticket = userInfo.getTicket();
			}
			// If a semi-authentication is used, a ticket is expected
			if(ticket == null) {
				String msg = "Update cancelled: Semi-authentication is used, but no ticket was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(10), msg);
			}
			else {
				boolean isValidOwner = PersistentFingerbook.validateOwnerTicket(ticket, fingerbookId);
				if(!isValidOwner) {
					String msg = "Update cancelled: Semi-authentication is used, fingerbookId: " + fingerbookId + " can't be updated by ticket: " + ticket;
					logger.info(authenticationMethod + ": Returning error Response object: " + msg);
					return new Response(new Integer(12), msg);
				}
			}
			
		} else if(authenticationMethod.equals(authenticated)) {
			String user = null;
			if(userInfo != null) {
				user = userInfo.getUser();
			}
			// If authenticated is used, a username is expected
			if(user == null) {
				String msg = "Update cancelled: Authentication is used, but no user name was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(11), msg);
			}
			else {
				boolean isValidOwner = PersistentFingerbook.validateOwnerUser(user, fingerbookId);
				if(!isValidOwner) {
					String msg = "Update cancelled: Authentication is used, fingerbookId: " + fingerbookId + " can't be updated by user: " + user;
					logger.info(authenticationMethod + ": Returning error Response object: " + msg);
					return new Response(new Integer(13), msg);
				}
			}
		} else {
			String msg = "Update cancelled: Anonymous is used, fingerbookId: " + fingerbookId + " can't be updated by Anonymous";
			logger.info(authenticationMethod + ": Returning error Response object: " + msg);
			return new Response(new Integer(14), msg);
		}

		return ans;
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
	
	public static int authMethodIdByName(String authenticationMethod) {
		
		int authId = 0;
		
		if(authenticationMethod.equals(authenticated)) {
			authId = Auth.AUTH_AUTHENTICATED;
    	}
    	else if(authenticationMethod.equals(semiAuthenticated)) {
    		authId = Auth.AUTH_SEMI_AUTHENTICATED;
    	}
    	else {
    		authId = Auth.AUTH_ANONYMOUS;
    	}
		
		return authId;
	}
    
    
}