package com.fingerbook.client;

import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fingerbook.client.gui.Messages;
import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.Fingerbook.STATE;


public class Scanner {

	private @Autowired FingerbookClient fiClient;
	private Boolean finishedScan;
	private Boolean finishedPosting;
	private final BlockingQueue<FileInfo> queue;
	private Response response;
	private ExecutorService execFileScanner;
	private Logger logger; 
	
	public Scanner(String dir, UserInfo userInfo) throws Exception {
		this.finishedScan = false;
		this.finishedPosting = true;
		this.queue = new LinkedBlockingQueue<FileInfo>();
		this.logger = LoggerFactory.getLogger(Scanner.class);
		this.fiClient = Client.applicationContext.getBean(FingerbookClient.class);
		
	}

	public Response scanDirectory(Map<String, String> configuration) throws Exception {
		File actual = null;
		Integer timeout = 60;
		Boolean error = false;
		
		try {
			actual = new File(configuration.get("scanDir"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String ticket = configuration.get("ticket");
		UserInfo ui = new UserInfo();
		Fingerbook fb = new Fingerbook();
		if(ticket != null) {
			ui.setTicket(ticket);
			fb.setUserInfo(ui);
		}
		
		
		fb.setState(STATE.START);
		//TODO: ver como se setea el user info cuando se tenga hecho lo de ticket y autenticacion
		//fb.setUserInfo(userInfo)
		
		Response resp = fiClient.startHashTransaction(null);
		
		if(resp.getErrorCode() != null) {
			return resp;
		}
		Long fid = resp.getRid();
//		Response resp = null;
//		Long fid = 1L;
		
		
		
		FileScanner fileScanner = new FileScanner(actual, configuration.get("recursive"), this.queue, fid, this.fiClient, 1, 60);
		
		execFileScanner = Executors.newSingleThreadExecutor();
		
		Future<?> producer =  execFileScanner.submit(fileScanner);
		
		try {
	        producer.get(timeout, TimeUnit.SECONDS);
	    } catch (TimeoutException e) {
	    	logger.warn(Messages.getString("TimeoutException.2"));
	    } catch (ExecutionException e) {
	        error = true;
	    } catch (ResponseException e) {
	        error = true;
	        resp = e.getResponse();
	    } finally {
	        producer.cancel(true);
	        queue.clear();
	    }
		
	    if(!error) {
			fb = new Fingerbook();
			fb.setFingerbookId(fid);
			fb.setState(STATE.FINISH);
			resp = fiClient.postHashes(fb);
	    }
	    
	    return resp;
	}
	
	public synchronized Boolean getFinishedScan() {
		return finishedScan;
	}

	public synchronized void setFinishedScan(Boolean finishedScan) {
		this.finishedScan = finishedScan;
	}

	public synchronized Boolean getCanStartScan() {
		return finishedPosting;
	}

	public synchronized void setCanStartScan(Boolean canStartScan) {
		this.finishedPosting = canStartScan;
	}
	
	public Response getResponse() {
		return response;
	}

	public synchronized void setResponse(Response response) {
		this.response = response;
	}


	public BlockingQueue<FileInfo> getQueue() {
		return queue;
	}
	
	


}
