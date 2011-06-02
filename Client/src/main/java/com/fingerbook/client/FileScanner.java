package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.comparator.PathFileComparator;
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
	private List<File> actual;
	private BlockingQueue<FileInfo> queue;
	private FileHashCalculator fhc;
	private Long fid;
	private String recursive;
	private FingerbookClient fiClient;
	private boolean scanHasEnded = false;
	private ExecutorService execFingerbookPoster;
	private Integer consumerAmount;
	private Integer timeout;
	private Logger logger; 
	private final int FILE_INFO_AMOUNT = 3;
	private boolean resume = false;
	private String transactionId;
	
	private static boolean search = true;
	
	
	public FileScanner(List<File> actual, String recursive, BlockingQueue<FileInfo> queue, Long fid, FingerbookClient fiClient, 
			Integer consumerAmount, Integer timeout, boolean resume, String transactionId, String userName, String password) {
		this.actual = actual;
		this.queue = queue;
		this.recursive = recursive;
		this.fhc = Client.applicationContext.getBean(FileHashCalculator.class);
		this.fid = fid;
		this.fiClient = fiClient;
		this.consumerAmount = consumerAmount;
		this.timeout = timeout;
		this.logger = LoggerFactory.getLogger(FileScanner.class);
		this.resume = resume;
		this.transactionId = transactionId;
	}
	
	@Override
	public void run() {
		synchronized (this) {
			this.scanHasEnded = false;			
		}

		// TODO: aca hay que diferenciar cuando es resume de cuando no.
		if(resume);
		
		FingerbookPoster fingerbookPoster = new FingerbookPoster(fid, this.queue,this.fiClient);
		execFingerbookPoster = Executors.newFixedThreadPool(this.consumerAmount); 
		
		Future<?> consumers =  execFingerbookPoster.submit(fingerbookPoster);

		try {
			addFiles();
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
	
	private List<File> sanitizePaths(List<File> files) {
		int size;
		int i, j;
		List<File> resp = new ArrayList<File>();
		List<File> respAux = new ArrayList<File>();
		File fi, fj;

		/* Remove invalid paths */
		for (File f:files) {
			if (f.exists()) {
				/* Need two copies because cannot modify 'files' while iterating over it */
				resp.add(f);
				respAux.add(f);
			}
		}
		/* If a path or file is child from another path, then remove it */
		for (i=0, size=respAux.size(); i < size-1; i++) {
			fi = respAux.get(i);
			for (j=i+1; j < size; j++) {
				fj = files.get(j);
				if (fi.getAbsolutePath().startsWith(fj.getAbsolutePath())) {
					if (fj.isDirectory() && (recursive.equals("true") || (fi.getParent() != null && fi.getParent().equals(fj.getAbsolutePath()) && fi.isFile())))
						resp.remove(fi);
					else if (fi.isFile() && fj.isFile() && fi.getAbsolutePath().equals(fj.getAbsolutePath()))
						resp.remove(fi);
				}
				else if (fj.getAbsolutePath().startsWith(fi.getAbsolutePath()))
					if (fi.isDirectory() && (recursive.equals("true") || (fj.getParent() != null && fj.getParent().equals(fi.getAbsolutePath()) && fj.isFile())))
						resp.remove(fj);
					else if (fi.isFile() && fj.isFile() && fi.getAbsolutePath().equals(fj.getAbsolutePath()))
						resp.remove(fj);
				}
			}
		return resp;
	}
	
	private void addFiles() throws InterruptedException {
		actual = sanitizePaths(actual);

		if (resume)
			doAddFiles(actual, Client.fMan.init(), true);
		else
			doAddFiles(actual, null, true);
	}
	
	@SuppressWarnings({ "unchecked" })
	private void doAddFiles(List<File> actual, File resume, boolean first) throws InterruptedException {
		String lastPath = new String("/////"); // Forbidden file name

		if (resume == null)
			search = false;
		else
			lastPath = new String(resume.getAbsolutePath());
				
		//TODO: Pensar si hace falta! (tecnicamente el orden de los archivos varia segun alocacion fisica)
		Collections.sort(actual, PathFileComparator.PATH_COMPARATOR);
		
		for (File f : actual) {
			/* If access to file is denied, then continue with next file */
			if (!f.canRead())
				continue;

			if (search && f.isDirectory() && lastPath.startsWith(f.getAbsolutePath())) {
				if (lastPath.equals(f.getAbsolutePath()))
					/* Resume directory reached, start queueing files */
					search = false;
			}
			/* Search tree until resume path is found */
			else if(search)
				continue;

			if (!search && f.isFile()) {
				System.out.println("FILE: " + f.getAbsolutePath());
				queue.put(new FileInfo(f.getAbsolutePath(), fhc.getFileHash(f)));
			}
			else if (f.isDirectory() && (recursive.equals("true") || first)) {
				logger.debug(f.getAbsolutePath());

				if (search)
					doAddFiles(Arrays.asList(f.listFiles()), resume, false);
				else
					doAddFiles(Arrays.asList(f.listFiles()), null, false);
			}
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
						if(count == FILE_INFO_AMOUNT || (queue.isEmpty() && files.size() > 0)) {
							fb = new Fingerbook();
							fb.setFingerbookId(this.fid);
							fb.setState(STATE.CONTENT);
							Fingerprints fps = new Fingerprints();
							fps.setFiles(files);
							fb.setFingerPrints(fps); 
							fb.setTransactionId(transactionId);
							Response resp = null;
							try {
								 resp = fiClient.postHashes(fb); 
							} catch (RestClientException ex){
								throw new ResponseException(Messages.getString("ResponseException.1"), null);
							}
							if(resp != null && resp.getErrorCode() != null) {
								throw new ResponseException(Messages.getString("ResponseException.1"), resp);
							}
							else if(recursive.equals("true")) {
								List<FileInfo> fs = fb.getFingerPrints().getFiles();
								if(fs != null && fs.size() > 0) {
									String path = fs.get(fs.size()-1).getPath();
									try {
										Client.fMan.save(path.substring(0, path.lastIndexOf("/")));
									} catch(Exception e) {}
								}
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
