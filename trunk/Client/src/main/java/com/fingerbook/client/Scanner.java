package com.fingerbook.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.Response;
import com.fingerbook.models.UserInfo;
import com.fingerbook.models.Fingerbook.STATE;


public class Scanner {
	//private UserInfo userInfo;
	
	private @Autowired FileHashCalculator fhc; 
	private @Autowired FingerbookClient fiClient;
	private Boolean finishedScan;
	private Boolean finishedPosting;
	private final BlockingQueue<FileInfo> queue;
	private Response response;
	private ExecutorService execFileScanner;
	private ExecutorService execFingerbookPoster;
	private Logger logger; 
	
	public Scanner(String dir, UserInfo userInfo) throws Exception {

		///this.userInfo = userInfo;
		this.finishedScan = false;
		this.finishedPosting = true;
		this.queue = new LinkedBlockingQueue<FileInfo>();
		this.logger = LoggerFactory.getLogger(Scanner.class);
		
		this.fhc = Client.applicationContext.getBean(FileHashCalculator.class);
		this.fiClient = Client.applicationContext.getBean(FingerbookClient.class);
		// Fingerprints fis = scanDirectory(dir, fhc);

		// FileInfo fi = new FileInfo("data.xml",
		// "53870f83687822d7c768e6162cb32cf5e87567da", 1000L);
		// List<fingerbook.domain.Fingerprints> list =
		// fiClient.getGroups("53870f83687822d7c768e6162cb32cf5e87567da");
		// for (fingerbook.domain.Fingerprints fingerprints : list) {
		// for (fingerbook.domain.FileInfo file : fingerprints.getFiles()) {
		// System.out.println(file.getName() + "--------------------"
		// + file.getShaHash());
		// }
		// }
		// // RestClient rc = new RestClient();
		//		
		// File f = rc.getXML("http://www.wergehthin.de/xml/User/");
		// rc.postXML(f, "http://www.wergehthin.de/xml/User/");
		// System.out.println(f.toString());
		// RestTemplate rt = new RestTemplate();
		// String result = rt.getForObject("http://www.wergehthin.de/xml/User/",
		// String.class);
		// System.out.println(result);
	}

	public Response scanDirectory(String scanDir,
			HashMap<String, String> configuration) throws Exception {
		File actual = null;
		
		try {
			actual = new File(scanDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Fingerbook fb = new Fingerbook();
		fb.setState(STATE.START);
		//TODO: ver como se setea el user info cuando se tenga hecho lo de ticket y autenticacion
		//fb.setUserInfo(userInfo)
		if(fiClient == null)
			System.out.println("NULLLLLLLLLL!!!!!!!!!!!!!!!!!!!!!!!!!!");
		Response resp = fiClient.startHashTransaction(null);
		
		if(resp.getErrorCode() != null) {
			return resp;
		}
		Long fid = resp.getRid();
		
		FileScanner fileScanner = new FileScanner(actual, configuration.get("recursive"), this);
		FingerbookPoster fingerbookPoster = new FingerbookPoster(fid, this);
		
		execFileScanner = Executors.newSingleThreadExecutor();
		execFingerbookPoster = Executors.newSingleThreadExecutor(); 
		
		execFileScanner.execute(fileScanner);
		execFingerbookPoster.execute(fingerbookPoster);
		
		execFingerbookPoster.awaitTermination(60, TimeUnit.SECONDS);
		
		if(!finishedPosting) {
			execFingerbookPoster.shutdownNow();
			
			if(response.getErrorCode() != null) {
				queue.clear();
				execFileScanner.shutdownNow();
				return response;
			}
			
			if(!queue.isEmpty()) {
				// if the queue is not empty and there is no errorcode in the response the FileScanner 
				// continued scanning while the poster exited due to a timeout 
				logger.error("Posting not finished due to Time out.");
				queue.clear();
				return null;
			}
		}
		
		// It can't happen that the FileScanner thread didn't finish and the FingerbookPoster did because
		// it's a requirement for the FingerbookPoster to end that the FileScanner also ended
		
		fb = new Fingerbook();
		fb.setFingerbookId(fid);
		fb.setState(STATE.FINISH);
		
		return fiClient.postHashes(fb);	
		
//		List<File> fileList = new ArrayList<File>();
//		
//		addFiles(actual, (ArrayList<File>) fileList, configuration.get("recursive"));
//
//		List<FileInfo> files = new ArrayList<FileInfo>();
//		
//		for (File f : fileList) {
//			files.add(new FileInfo(f.getName(), fhc.getFileHash(f), f.length()));
//		}
//
//		Fingerprints data = new Fingerprints();
//		data.setFiles(files);
//
//		Fingerbook fb = new Fingerbook();
//		fb.setUserInfo(this.userInfo);
//		fb.setFingerPrints(data);
//		fb.setStamp(new GregorianCalendar().getTimeInMillis());
//		Response resp = fiClient.postHashes(fb);
		
		// Serializer serializer = new Persister();
		// File result = new File("data.xml");
		// serializer.write(data, result);
//		return this.response;
	}

//	private void addFiles(File actual, List<File> fileList, String recursive) {
//		for (File f : actual.listFiles()) {
//			if (f.isFile())
//				fileList.add(f);
//			else if (recursive.equals("true") && f.isDirectory())
//				addFiles(f, (ArrayList<File>) fileList, "true");
//		}		
//	}
	
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


	private class FileScanner implements Runnable {
		private File actual;
		private String recursive;
		private Scanner scanner;
		
		public FileScanner(File actual, String recursive, Scanner scanner) {
			this.actual = actual;
			this.recursive = recursive;
			
		}
		
		@Override
		public void run() {
			addFiles(actual);
			scanner.setFinishedScan(true);
		}
		
		private void addFiles(File actual) {
			for (File f : actual.listFiles()) {
				if (f.isFile())
					try {
						scanner.getQueue().put(new FileInfo(f.getName(), fhc.getFileHash(f), f.length()));
					} catch (InterruptedException e) {
						  Thread.currentThread().interrupt();
						  scanner.setFinishedScan(true);
					}
				else if (recursive.equals("true") && f.isDirectory())
					addFiles(f);
			}		
		}
	}
	
	private class FingerbookPoster implements Runnable {
		private Long fid;
		private Scanner scanner;
		
		public FingerbookPoster(Long fid, Scanner scanner) {
			this.fid = fid;
		}
		
		@Override
		public void run() {
			int count = 0;
			List<FileInfo> files = new ArrayList<FileInfo>();
			
			
			while(true) {
				
				if((scanner.getQueue().isEmpty() && scanner.getFinishedScan() == true)) {
					scanner.setCanStartScan(true);
					break;
				}
				
				FileInfo fi = null;
//				try {
					fi = scanner.getQueue().poll(); //TODO: revisar esto porque hace busy waiting, ver como se puede arreglar teniendo en cuenta
													// cuando cae en el take() bloqueante y el FileScanner termino
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//					scanner.setCanStartScan(true);
//				}
				
				if(fi != null) {
					files.add(fi);
					count++;
					if(count == 10 || (scanner.queue.isEmpty() && files.size() > 0)) {
						Fingerbook fb = new Fingerbook();
						fb.setFingerbookId(this.fid);
						fb.setState(STATE.CONTENT);
						Fingerprints fps = new Fingerprints();
						fps.setFiles(files);
						fb.setFingerPrints(fps);
						scanner.setResponse(scanner.fiClient.postHashes(fb));	
						if(scanner.getResponse().getErrorCode() != null) {
							break;
						}
						count = 0;
					}
				}
				
			
			}
		}
	}


}
