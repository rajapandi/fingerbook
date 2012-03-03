package com.fingerbook.rest.service;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.transfer.FingerbookFeed;
import com.fingerbook.models.transfer.FingerbookList;
import com.fingerbook.models.transfer.FingerprintsFeed;
import com.fingerbook.models.transfer.SimilaritiesFeed;
import com.fingerbook.persistencehbase.PersistentFingerbook;
import com.fingerbook.rest.web.FingerbooksController;


public class FingerbookServices {
	protected final Log logger = LogFactory.getLog(getClass());

	public Vector<Fingerbook> getFingerbooksWithHash(String hash) {	
		if(hash != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByHash(hash);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public FingerbookList getFingerbookListByHash(String hash, int limit, int offset) {	
		if(hash != null) {
			FingerbookList fingerbookList = PersistentFingerbook.getFingerbookListStampTagsByHashPag(hash, limit, offset);
			return fingerbookList;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByTicket(String ticket) {	
		if(ticket != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByTicket(ticket);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByTicket(String ticket, int limit, int offset) {	
		if(ticket != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbooksByTicketPag(ticket, limit, offset);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public FingerbookList getFingerbookListByTicket(String ticket, int limit, int offset) {	
		if(ticket != null) {
			FingerbookList fingerbookList = PersistentFingerbook.getFingerbookListByTicketPag(ticket, limit, offset);
			return fingerbookList;
		} else {
			return null;
		}
	}
	
	public FingerbookList getFingerbookListByUser(String user, int limit, int offset) {	
		if(user != null) {
			FingerbookList fingerbookList = PersistentFingerbook.getFingerbookListByUserPag(user, limit, offset);
			return fingerbookList;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByUser(String user, int limit, int offset) {	
		if(user != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbooksByUserPag(user, limit, offset);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public Vector<Fingerbook> getFingerbooksByUser(String user) {	
		if(user != null) {
			Vector<Fingerbook> fingerbooks = PersistentFingerbook.getFingerbookByUser(user);
			return fingerbooks;
		} else {
			return null;
		}
	}
	
	public FingerprintsFeed getFingerprintsFeedById(Long id, int limit, int offset) {	
		if(id != null) {
			FingerprintsFeed fingerprintsFeed = PersistentFingerbook.getFingerprintsFeedByFingerBookPag(id, limit, offset);
			return fingerprintsFeed;
		} else {
			return null;
		}
	}
	
	public SimilaritiesFeed getSimilaritiesFeedById(Long id, int limit, int offset) {	
		if(id != null) {
			SimilaritiesFeed similaritiesFeed = PersistentFingerbook.getSimilaritiesFeed(id, limit, offset);
			return similaritiesFeed;
		} else {
			return null;
		}
	}
	
	public Fingerbook getFingerbookById(Long id) {	
		if(id != null) {
			
//			Fingerbook fingerbook = new Fingerbook();
//			fingerbook.setFingerbookId(id);
//			PersistentFingerbook pf = new PersistentFingerbook(fingerbook);
//			fingerbook = pf.loadMe(true);
			Fingerbook fingerbook = PersistentFingerbook.loadMe(id, true);
			
			return fingerbook;
		} else {
			return null;
		}
	}
	
	public Fingerbook getFingerbookById(Long id, boolean loadFingerprints) {	
		if(id != null) {
			
//			Fingerbook fingerbook = new Fingerbook();
//			fingerbook.setFingerbookId(id);
//			PersistentFingerbook pf = new PersistentFingerbook(fingerbook);
//			fingerbook = pf.loadMe(loadFingerprints);
			
			Fingerbook fingerbook = PersistentFingerbook.loadMe(id, loadFingerprints);
			
			return fingerbook;
		} else {
			return null;
		}
	}
	
	public FingerbookFeed getFingerbookFeedById(Long id, boolean loadFingerprints) {
		
		FingerbookFeed fingerbookFeed = new FingerbookFeed();
		if(id != null) {
			
			Fingerbook fingerbook = PersistentFingerbook.loadMe(id, loadFingerprints);
			
			fingerbookFeed.setFingerbook(fingerbook);
			fingerbookFeed.setTotalresults(1);
			
		} else {
			fingerbookFeed.setTotalresults(0);
		}
		
		return fingerbookFeed;
	}
	
	public FingerbookFeed getFingerbookFeedById(Long id, int limit, int offset ) {
		
		FingerbookFeed fingerbookFeed = new FingerbookFeed();
		
		fingerbookFeed.setLimit(1);
		fingerbookFeed.setOffset(0);
		
		if(id != null) {
			
			Fingerbook fingerbook = PersistentFingerbook.loadMe(id, false);
			
			if(fingerbook != null) {
				
				FingerprintsFeed fingerprintsFeed = PersistentFingerbook.getFingerprintsFeedByFingerBookPag(id, limit, offset);
				
				if(fingerprintsFeed != null) {
					
					fingerbook.setFingerPrints(fingerprintsFeed.getFingerPrints());
					
					fingerprintsFeed.setFingerPrints(null);
					fingerbookFeed.setFingerprintsFeed(fingerprintsFeed);
					
				}
			}
			
			
			
			fingerbookFeed.setFingerbook(fingerbook);
			fingerbookFeed.setTotalresults(1);
			
		} else {
			fingerbookFeed.setTotalresults(0);
		}
		
		return fingerbookFeed;
	}
	
	public FingerbookFeed getFingerbookFeedById(Long id, int limitFp, int offsetFp, int limitSimil, int offsetSimil ) {
		
		FingerbookFeed fingerbookFeed = new FingerbookFeed();
		
		fingerbookFeed.setLimit(1);
		fingerbookFeed.setOffset(0);
		
		if(id != null) {
			
			Fingerbook fingerbook = PersistentFingerbook.loadMe(id, false);
			
			if(fingerbook != null) {
				
				FingerprintsFeed fingerprintsFeed = PersistentFingerbook.getFingerprintsFeedByFingerBookPag(id, limitFp, offsetFp);
				
				if(fingerprintsFeed != null) {
					
					fingerbook.setFingerPrints(fingerprintsFeed.getFingerPrints());
					
					fingerprintsFeed.setFingerPrints(null);
					fingerbookFeed.setFingerprintsFeed(fingerprintsFeed);
					
				}
			}
			
			SimilaritiesFeed similaritiesFeed = PersistentFingerbook.getSimilaritiesFeed(id, 0, limitSimil, offsetSimil);
			fingerbookFeed.setSimilaritiesFeed(similaritiesFeed);
			
			
			fingerbookFeed.setFingerbook(fingerbook);
			fingerbookFeed.setTotalresults(1);
			
		} else {
			fingerbookFeed.setTotalresults(0);
		}
		
		return fingerbookFeed;
	}
	
	public Fingerbook getFingerbookById(Long id, int size, int offset ) {	
		if(id != null) {
			
//			Fingerbook fingerbook = new Fingerbook();
//			fingerbook.setFingerbookId(id);
//			PersistentFingerbook pf = new PersistentFingerbook(fingerbook);
//			fingerbook = pf.loadMe(true, size, offset);
			
			Fingerbook fingerbook = PersistentFingerbook.loadMe(id, true, size, offset);
			
			return fingerbook;
		} else {
			return null;
		}
	}
	
	public Integer updateFingerbook(Fingerbook fingerbook) {    
    	return PersistentFingerbook.updateTagsComment(fingerbook);
    }

	public List<Fingerprints> getFingerprintsWithHash(String hash) {
		
		
	// TODO Robot service
		return null;
	}

	public String generateTicket() {
		
		// TODO: Use hash(something)
		/*
		try {
			Random rand = new Random();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			String output = null;
			byte[] buffer = new byte[8192];
			Hex h = new Hex(CharEncoding.ISO_8859_1);
			
			buffer = rand.nextDouble();
			md.update(buffer, 0, read);
			
			byte[] md5sum = md.digest();
			//BigInteger bigInt = new BigInteger(1, md5sum);
			//output = bigInt.toString(16);
			output = new String(h.encodeHex(md5sum, true));	
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/
		
		return "FSGFGS32432fDSG";
	}

	public boolean isValidTicket(String ticket) {
		try {
			return PersistentFingerbook.isValidTicket(ticket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/*public Fingerbook getFingerbook(Fingerprints fingerprints) {
		Fingerbook fingerbook = new Fingerbook();
		fingerbook.setFingerPrints(fingerprints);
		return fingerbook;
	}	*/
	
	public Response validateOwner(Fingerbook fingerbook, String authenticationMethod) {
		
		Response ans = null;
		UserInfo userInfo = null;
		
		Long fingerbookId = fingerbook.getFingerbookId();
		userInfo = fingerbook.getUserInfo();
		
		if(fingerbookId == null) {
			String msg = "Fingerbook could not be accessed. Invalid fingerbookId.";
			Response response = new Response(new Integer(1), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
		}
		
		if(authenticationMethod.equals(FingerbooksController.AUTH_METHOD_ADMIN)) {
			return null;
		}
		else if(authenticationMethod.equals(FingerbooksController.AUTH_METHOD_SEMI_AUTHENTICATED)) {
			String ticket = null;
			if(userInfo != null) {
				ticket = userInfo.getTicket();
			}
			// If a semi-authentication is used, a ticket is expected
			if(ticket == null) {
				String msg = "Operation cancelled: Semi-authentication is used, but no ticket was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(10), msg);
			}
			else {
				boolean isValidOwner = PersistentFingerbook.validateOwnerTicket(ticket, fingerbookId);
				if(!isValidOwner) {
					String msg = "Operation cancelled: Semi-authentication is used, fingerbookId: " + fingerbookId + " not owned by ticket: " + ticket;
					logger.info(authenticationMethod + ": Returning error Response object: " + msg);
					return new Response(new Integer(12), msg);
				}
			}
			
		} else if(authenticationMethod.equals(FingerbooksController.AUTH_METHOD_AUTHENTICATED)) {
			String user = null;
			if(userInfo != null) {
				user = userInfo.getUser();
			}
			// If authenticated is used, a username is expected
			if(user == null) {
				String msg = "Operation cancelled: Authentication is used, but no user name was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(11), msg);
			}
			else {
				boolean isValidOwner = PersistentFingerbook.validateOwnerUser(user, fingerbookId);
				if(!isValidOwner) {
					String msg = "Operation cancelled: Authentication is used, fingerbookId: " + fingerbookId + " not owned by user: " + user;
					logger.info(authenticationMethod + ": Returning error Response object: " + msg);
					return new Response(new Integer(13), msg);
				}
			}
		} else {
			String msg = "Operation cancelled: Anonymous is used, fingerbookId: " + fingerbookId + " not owned by Anonymous";
			logger.info(authenticationMethod + ": Returning error Response object: " + msg);
			return new Response(new Integer(14), msg);
		}

		return ans;
	}
	
	public Response validateOwner(Long fingerbookId, String user, String authenticationMethod) {
		
		Response ans = null;
//		UserInfo userInfo = null;
		
//		Long fingerbookId = fingerbook.getFingerbookId();
//		userInfo = fingerbook.getUserInfo();
		
		if(fingerbookId == null) {
			String msg = "Fingerbook could not be accessed. Invalid fingerbookId.";
			Response response = new Response(new Integer(1), msg);   		
    		logger.info(authenticationMethod + ": Returning error Response object: " + msg);
    		return response;
		}
		
		if(authenticationMethod.equals(FingerbooksController.AUTH_METHOD_SEMI_AUTHENTICATED)) {
			// If a semi-authentication is used, a ticket is expected
			if(user == null) {
				String msg = "Operation cancelled: Semi-authentication is used, but no ticket was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(10), msg);
			}
			else {
				boolean isValidOwner = validateOwnerTicket(fingerbookId, user);
				if(!isValidOwner) {
					String msg = "Operation cancelled: Semi-authentication is used, fingerbookId: " + fingerbookId + " not owned by ticket: " + user;
					logger.info(authenticationMethod + ": Returning error Response object: " + msg);
					return new Response(new Integer(12), msg);
				}
			}
			
		} else if(authenticationMethod.equals(FingerbooksController.AUTH_METHOD_AUTHENTICATED)) {
			// If authenticated is used, a username is expected
			if(user == null) {
				String msg = "Operation cancelled: Authentication is used, but no user name was received";
				logger.info(authenticationMethod + ": Returning error Response object: " + msg);
				return new Response(new Integer(11), msg);
			}
			else {
				boolean isValidOwner = validateOwnerUser(fingerbookId, user);
				if(!isValidOwner) {
					String msg = "Operation cancelled: Authentication is used, fingerbookId: " + fingerbookId + " not owned by user: " + user;
					logger.info(authenticationMethod + ": Returning error Response object: " + msg);
					return new Response(new Integer(13), msg);
				}
			}
		} else {
			String msg = "Operation cancelled: Anonymous is used, fingerbookId: " + fingerbookId + " not owned by Anonymous";
			logger.info(authenticationMethod + ": Returning error Response object: " + msg);
			return new Response(new Integer(14), msg);
		}

		return ans;
	}
	
	public boolean validateOwnerUser(Long fingerbookId, String user) {
		
		boolean isValidOwner = false;
//		Response ans = null;
		if(fingerbookId == null) {
			String msg = "Fingerbook could not be accessed. Invalid fingerbookId.";
//			Response response = new Response(new Integer(1), msg);   		
//    		logger.info("Returning error Response object: " + msg);
//    		return response;
			logger.info("Invalid owner user: " + msg);
			return false;
		}
		
		// If authenticated is used, a username is expected
		if(user == null) {
			String msg = "Operation cancelled: Authentication is used, but no user name was received";
//			logger.info("Returning error Response object: " + msg);
//			return new Response(new Integer(11), msg);
			logger.info("Invalid owner user: " + msg);
			return false;
		}
		else {
			isValidOwner = PersistentFingerbook.validateOwnerUser(user, fingerbookId);
			if(!isValidOwner) {
				String msg = "Operation cancelled: Authentication is used, fingerbookId: " + fingerbookId + " not owned by user: " + user;
//				logger.info("Returning error Response object: " + msg);
//				return new Response(new Integer(13), msg);
				logger.info("Invalid owner user: " + msg);
				return false;
			}
		}

		return isValidOwner;
	}

	public boolean validateOwnerTicket(Long fingerbookId, String ticket) {
		
		boolean isValidOwner = false;
//		Response ans = null;
		if(fingerbookId == null) {
			String msg = "Fingerbook could not be accessed. Invalid fingerbookId.";
//			Response response = new Response(new Integer(1), msg);   		
//			logger.info("Returning error Response object: " + msg);
//			return response;
			logger.info("Invalid owner user: " + msg);
			return false;
		}
		
		// If a semi-authentication is used, a ticket is expected
		if(ticket == null) {
			String msg = "Operation cancelled: Semi-authentication is used, but no ticket was received";
//			logger.info("Returning error Response object: " + msg);
//			return new Response(new Integer(10), msg);
			logger.info("Invalid owner user: " + msg);
			return false;
		}
		else {
			isValidOwner = PersistentFingerbook.validateOwnerTicket(ticket, fingerbookId);
			if(!isValidOwner) {
				String msg = "Operation cancelled: Semi-authentication is used, fingerbookId: " + fingerbookId + " not owned by ticket: " + ticket;
//				logger.info("Returning error Response object: " + msg);
//				return new Response(new Integer(12), msg);
				logger.info("Invalid owner user: " + msg);
				return false;
			}
		}
	
		return isValidOwner;
	}
	
	public boolean validateAuthUser(Fingerbook fingerbook) {
		
		String paramUser = null;
		UserInfo userInfo = null;
		
		if(fingerbook != null) {
			userInfo = fingerbook.getUserInfo();
			if(userInfo != null) {
				paramUser = userInfo.getUser();
			}
		}
		
		return validateAuthUser(paramUser);
		
	}
	
	public boolean validateAuthUser(String paramUser) {
		
		String authUser = null;
		
//		String authenticatedUser = request.getUserPrincipal().getName();
//		String authenticatedUserPasswword = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		
//		authUser = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		authUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		System.out.println("---------- getPrincipal(): " + authUser);
//		System.out.println("---------- getName(): " + authenticatedUser);
		
		return validateAuthUser(authUser, paramUser);

	}
	
	public boolean validateAuthUser(String authUser, String paramUser) {
		
		if(authUser == null || authUser.isEmpty()) {
			String msg = "User could not be validated with authenticated user. Invalid authUser.";
			logger.info("Invalid authUser: " + msg);
			return false;
		}
		if(paramUser == null) {
			String msg = "User could not be validated with authenticated user. Invalid paramUser.";
			logger.info("Invalid paramUser: " + msg);
			return false;
		}
		
		if(authUser.equals(paramUser) ) {
			return true;
		}
		else {
			String msg = "User doesn't match with authenticated user.";
			logger.info("Invalid paramUser: " + msg);
			return false;
		}

	}
}
