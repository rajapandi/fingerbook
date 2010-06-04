package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

import com.fingerbook.client.gui.Messages;
import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.Response;
import com.fingerbook.models.Fingerbook.STATE;

public class FileScanner implements Runnable {
	private File actual;
	private String recursive;
	private BlockingQueue<FileInfo> queue;
	private FileHashCalculator fhc;
	private Long fid;
	private FingerbookClient fiClient;
	private boolean scanHasEnded = false;
	private ExecutorService execFingerbookPoster;
	private Integer consumerAmount;
	private Integer timeout;
	private Logger logger; 
	
	public FileScanner(File actual, String recursive, BlockingQueue<FileInfo> queue, Long fid, FingerbookClient fiClient, 
			Integer consumerAmount, Integer timeout) {
		this.actual = actual;
		this.recursive = recursive;
		this.queue = queue;
		this.fhc = Client.applicationContext.getBean(FileHashCalculator.class);
		this.fid = fid;
		this.fiClient = fiClient;
		this.consumerAmount = consumerAmount;
		this.timeout = timeout;
		this.logger = LoggerFactory.getLogger(FileScanner.class);

	}
	
	@Override
	public void run() {
		synchronized (this) {
			this.scanHasEnded = false;			
		}

		FingerbookPoster fingerbookPoster = new FingerbookPoster(fid, this.queue,this.fiClient);
		execFingerbookPoster = Executors.newFixedThreadPool(this.consumerAmount); 
		
		Future<?> consumers =  execFingerbookPoster.submit(fingerbookPoster);
		
		try { 
			addFiles(actual);
		} catch (InterruptedException e) {
			  Thread.currentThread().interrupt();
			  consumers.cancel(true);
			  return;
			  //scanner.setFinishedScan(true);
		}
		synchronized (this) {
			this.scanHasEnded = true;			
		}
		
		try {
	        consumers.get(timeout, TimeUnit.SECONDS);
	    } catch (TimeoutException e) {
	        logger.warn(Messages.getString("TimeoutException.1"));
	    } catch (ExecutionException e) {
	        logger.error(e.getMessage());
	        throw new ResponseException(Messages.getString("ResponseException.1"), null);
	    } catch (InterruptedException e) {
	    	logger.error(e.getMessage());
		} catch (RuntimeException e) {
	        logger.warn(e.getMessage());
	        throw e;
	    } finally {
	    	 consumers.cancel(true); 
	    }
	    
	}
	
	private void addFiles(File actual) throws InterruptedException {
		for (File f : actual.listFiles()) {
			if (f.isFile())
					queue.put(new FileInfo(f.getAbsolutePath(), f.getName(), fhc.getFileHash(f), f.length()));
			else if (recursive.equals("true") && f.isDirectory())
				addFiles(f);
		}		
	}
	
	
	private class FingerbookPoster implements Runnable {
		private Long fid;
		private BlockingQueue<FileInfo> queue;
		private FingerbookClient fiClient;
//		private volatile boolean scanHasEnded = false;
		
		public FingerbookPoster(Long fid, BlockingQueue<FileInfo> queue, FingerbookClient fiClient) {
			this.fid = fid;
			this.queue = queue;
			this.fiClient = fiClient;
		}
		
		@Override
		public void run(){
			int count = 0;
			List<FileInfo> files = new ArrayList<FileInfo>();
			Fingerbook fb = null;

			try {
				while(!Thread.currentThread().isInterrupted()) {
					if((queue.isEmpty() && scanHasEnded == true)) {
						//scanner.setCanStartScan(true);
						break;
					}
					
					FileInfo fi = queue.take(); 
					if(fi != null) {
						files.add(fi);
						count++;
						if(count == 2 || (queue.isEmpty() && files.size() > 0)) {
							fb = new Fingerbook();
							fb.setFingerbookId(this.fid);
							fb.setState(STATE.CONTENT);
							Fingerprints fps = new Fingerprints();
							fps.setFiles(files);
							fb.setFingerPrints(fps); 
							Response resp = null;
							try {
								 resp = fiClient.postHashes(fb); 
							} catch (RestClientException ex){
								System.out.println("EXCEPTION!!!");
								throw new ResponseException(Messages.getString("ResponseException.1"), null);
							}
							if(resp != null && resp.getErrorCode() != null) {
								throw new ResponseException(Messages.getString("ResponseException.1"), resp);
							}
							count = 0;
							files.clear();
						}
					}	
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				//scanner.setCanStartScan(true);
			}

		}
	}
}
