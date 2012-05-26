

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fingerbook.models.FileInfo;
import com.fingerbook.models.Fingerbook;
import com.fingerbook.models.Fingerbook.STATE;
import com.fingerbook.models.Fingerprints;
import com.fingerbook.models.UserInfo;

public class FBGenerator {

	static GeneratorParams params = new GeneratorParams();
	static private UserInfo ui; 
	static private String userName;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(getParams(args) == 0) {
			System.out.println("Invalid parameter");
		}

		generateHashes();
		
		for (String s: args) {
            System.out.println(s);
        }
	}
	
	private static int getParams(String[] args) {
		
		String split[] = null;
	
		for (String arg : args) {
			split = arg.split("=");
			String aux = split[0];
			if (split.length == 2) {
				if (aux.equalsIgnoreCase("topLimit")) {
					params.setTopLimit(new Integer(split[1]));
				} else if (aux.equalsIgnoreCase("amountOfFingerbook")) {
					params.setAmountOfFingerbook(new Integer(split[1]));
				} else if (aux.equalsIgnoreCase("amountOfFingerPrintsLowLimit")) {
						params.setAmountOfFingerPrintsLowLimit(new Integer(split[1]));
				} else if (aux.equalsIgnoreCase("amountOfFingerPrintsTopLimit")) {
						params.setAmountOfFingerPrintsTopLimit(new Integer(split[1]));
				} else if (aux.equalsIgnoreCase("user")) {
					params.setUserName(split[1]);
				} else {
					return 0;
				}
			}
		}
		
		return 1;
	}

	private static void generateHashes() {
		
		int amountOfFingerbooks = params.getAmountOfFingerbook();
		
		int i=0;
		for(i=0; i < amountOfFingerbooks; i++) {
			
			try {		
			
				// Create "FileInfo"s	
				List<FileInfo> files = createFileInfos();

				
				Fingerprints fp = new Fingerprints();
				// Set fingerprint id equal to fingerbook id
				fp.setFingerprintsId(new Long(i));
				fp.setFiles(files);
				
				Fingerbook fb = new Fingerbook();
				ui = new UserInfo();
				ui.setUser(params.getUserName());
				ui.setTicket(null);
				
				fb.setState(STATE.CONTENT);
				fb.setUserInfo(ui);
				// Set tags to null
				fb.setTags(null);
				fb.setComment("generated_by_script");
				// Set fingerprint id equal to fingerbook id
				fb.setFingerbookId(new Long(i));
				fb.setStamp(new Date().getTime());
				fb.setFingerPrints(fp);
			
				System.out.println(fb.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static List<FileInfo> createFileInfos() throws Exception {
		
		int i = params.getAmountOfFingerPrintsLowLimit();
		int top = params.getAmountOfFingerPrintsTopLimit();
		int amountFingerprints = generateRandom(i, top);
		List<FileInfo> files = new ArrayList<FileInfo>();
		String hash;
		
		int j=0;
		for (j=0; j < amountFingerprints; j++) {
			// Generate random number that will be hashed
			int random = generateRandom(1, params.getTopLimit());			
											
			// Hash randomly generated number
			hash = HashNumber.encode(random);
							
			FileInfo file = new FileInfo("generated_by_script", hash); 
			files.add(file);
		}	
		
		return files;
	}

	private static int generateRandom(int lowerLimit, int topLimit) {
		int START = lowerLimit;
	    int END = topLimit;
	    Random random = new Random();
	   return showRandomInteger(START, END, random);
	}
	  
	private static int showRandomInteger(int aStart, int aEnd, Random aRandom){
		if ( aStart > aEnd ) { 
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		//get the range, casting to long to avoid overflow problems
		long range = (long)aEnd - (long)aStart + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long)(range * aRandom.nextDouble());
		int randomNumber =  (int)(fraction + aStart);    
		
		return randomNumber;
	}

}
