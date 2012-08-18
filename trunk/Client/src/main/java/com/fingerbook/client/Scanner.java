//TODO: COMENTAR!
package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.fingerbook.models.Fingerbook.STATE;
import com.fingerbook.models.Response;

public class Scanner {
	private @Autowired FingerbookClient fiClient;
	private final BlockingQueue<FileInfo> queue;
	private Response response;
	private ExecutorService execFileScanner;
	private Logger logger; 
	Future<?> producer = null;
	private final int QUEUE_SIZE = 1000;
	
	private String userName;
	private String password;
	
	private String ticket;

	public Scanner() throws Exception {
		this.logger = LoggerFactory.getLogger(Client.class);
		this.queue = new LinkedBlockingQueue<FileInfo>(QUEUE_SIZE);
		this.fiClient = Client.applicationContext.getBean(FingerbookClient.class);
	}

	public Scanner(String userName, String password) {
		this.logger = LoggerFactory.getLogger(Client.class);
		this.queue = new LinkedBlockingQueue<FileInfo>(QUEUE_SIZE);
		this.fiClient = Client.applicationContext.getBean(FingerbookClient.class);
		this.userName = userName;
		this.password = password;
	}

	public Response scanDirectory(Map<String, String> configuration, boolean resume) throws Exception {
		String actual = null;
		String comment = null;
		//TODO: ver si habria que setear el timeout en algun lado o no deberia haber
		Integer connectionTimeout = 600;
		Boolean error = false; 
		Boolean timeout = false;
		Response resp;

		try {
			actual = new String(configuration.get("scanDir"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		ticket = configuration.get("ticket");
		Fingerbook fb;
		String transactionId = null;

		//if(resume)
		//	resp = fiClient.resumeHashTransaction(ticket);
		//else {
			comment = configuration.get("comment");
			if (comment != null)
				comment = comment.trim();
			resp = fiClient.startHashTransaction(ticket,
					getTags(configuration.get("tags")), comment);
		//}
		if (resp == null) {
			logger.error("Error: null response");
			return null;
		}

		if(resp.getErrorCode() != null) {
			return resp;
		}
		
		Long fid = resp.getRid();
		
		// Get ticket
		if (!resp.getTicket().equals(ticket)) {
			ticket = resp.getTicket();
		}
		
		// Get transactionId
		if (resp.getTransactionId() != null) {
			transactionId = resp.getTransactionId();
			configuration.put("transactionId", transactionId);
			/* Make configuration persistent */
			Client.fMan.saveActualParams(configuration);
		} else {
			logger.error("Error: null transaction ID");
			return resp;
		}

		/* Parse dirs paths and add them to a List */
		java.util.Scanner scan = new java.util.Scanner(actual);
		List<File> files = new ArrayList<File>();

		scan.useDelimiter(";");
		while (scan.hasNext())
			files.add(new File(scan.next()));		

		FileScanner fileScanner = new FileScanner(files, configuration.get("recursive"), this.queue, fid, this.fiClient, 
				1, connectionTimeout, resume, transactionId, userName, password);

		execFileScanner = Executors.newSingleThreadExecutor();

		// TODO: Si la lista de archivos que mando esta vacia, se cuelga. ARREGLAR!
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
		fb.setTransactionId(transactionId);

		if(timeout) {
			fb.setState(STATE.TIMEOUT_ERROR);
			resp = fiClient.postHashes(fb);
		} else if(!error) {
			fb.setState(STATE.FINISH);
			resp = fiClient.postHashes(fb);
			if (resp != null && resp.getErrorCode() == null)
				Client.fMan.clean();
		} 
		return resp;
	}

	private Set<String> getTags(String tagsS) {
		if (tagsS == null)
			tagsS = "";
		
		/* Build List from String */
		List<String> tagList = new ArrayList<String>();
		java.util.Scanner scan = new java.util.Scanner(tagsS);
		scan.useDelimiter(",");
		while (scan.hasNext())
			tagList.add(scan.next().trim());
		Set<String> tags = new HashSet<String>(tagList);
		
		return tags;
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

	public String getTicket() {
		return ticket;
	}
}
