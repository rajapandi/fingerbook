package fbUDF.BagToMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

public class BagToMap extends EvalFunc<Map<String, Object>>{
	
		 
		public Map<String, Object> exec(Tuple input) throws IOException {
			if (input == null)
	            return null;
	        
			Map<String, Object> fingerbookIdPercentage = null;
			
			try{
				fingerbookIdPercentage = new HashMap<String, Object>();
				
				DataBag b = (DataBag) input.get(1);

				for (Tuple tuple : b) {
					fingerbookIdPercentage.put((String)tuple.get(1), tuple.get(2));
				}
				
				System.err.println("Map:" + fingerbookIdPercentage);
	        } catch(Exception e){
	            System.err.println("Failed to process input; error - " + e.getMessage());
	            return null;
	        }
	        
	        return fingerbookIdPercentage;
		}
}
