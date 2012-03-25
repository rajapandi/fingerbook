package fbUDF.BagToMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

public class TupleBagToMap extends EvalFunc<Map<String, Object>> {
	
	public Map<String, Object> exec(Tuple input) throws IOException {
		if (input == null)
            return null;
        
		Map<String, Object> tagsMap = null;
		
		try{
			tagsMap = new HashMap<String, Object>();
			
			DataBag b = (DataBag) input.get(0);
			if(b.size() <= 0) {
				return tagsMap; 
			}
			
			b = (DataBag) b.iterator().next().get(0);
			for (Tuple tuple : b) {
				tagsMap.put((String)tuple.get(0), 1);
			}
			
        } catch(Exception e){
            System.err.println("Failed to process input; error - " + e.getMessage());
            return tagsMap;
        }
        
        return tagsMap;
	}
}
