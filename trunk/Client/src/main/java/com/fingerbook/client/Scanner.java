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
	private final BlockingQueue<FileInfo> queue;
	private Response response;
	private ExecutorService execFileScanner;
	private Logger logger; 
	Future<?> producer = null;
	private final int QUEUE_SIZE = 1000;
	
	public Scanner(String dir, UserInfo userInfo) throws Exception {
		this.queue = new LinkedBlockingQueue<FileInfo>(QUEUE_SIZE);
		this.logger = LoggerFactory.getLogger(Client.class);
		this.fiClient = Client.applicationContext.getBean(FingerbookClient.class);
	}

	public Response scanDirectory(Map<String, String> configuration) throws Exception {
		File actual = null;
		Integer connectionTimeout = 600; //TODO: ver si habria que setear el timeout en algun lado o no deberia haber
		Boolean error = false; 
		Boolean timeout = false;
		
		try {
			actual = new File(configuration.get("scanDir"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String ticket = configuration.get("ticket");
		Fingerbook fb;

		Response resp = fiClient.startHashTransaction(ticket);

		if(resp.getErrorCode() != null) {
			return resp;
		}
		Long fid = resp.getRid();
//		Response resp = null;
//		Long fid = 1L;
		
		
		
		FileScanner fileScanner = new FileScanner(actual, configuration.get("recursive"), this.queue, fid, this.fiClient, 
				1, connectionTimeout);
		
		execFileScanner = Executors.newSingleThreadExecutor();
		
		producer =  execFileScanner.submit(fileScanner);
		
		try {
	        producer.get(connectionTimeout, TimeUnit.SECONDS);
	    } catch (TimeoutException e) {
	    	timeout = true;
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
	    fb = new Fingerbook();
		fb.setFingerbookId(fid);
		
		if(timeout) {
			fb.setState(STATE.TIMEOUT_ERROR);
			resp = fiClient.postHashes(fb);
		} else if(!error) {
			fb.setState(STATE.FINISH);
			resp = fiClient.postHashes(fb);
	    } 
	    return resp;
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
	
	public synchronized void stopScanning() {
		//TODO: ver si habria que mandar un paquete al servidor cuando se cancela el escaneo
		if(producer != null) {
			this.producer.cancel(true);
		}
	}
	


}
