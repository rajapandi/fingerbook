package fbUDF;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class ConvertToHashAmounts extends EvalFunc<DataBag> {
	 
	public DataBag exec(Tuple input) throws IOException {
	        if (input == null || input.size() < 2)
	            return null;
	        try{
	        		        	
	        	String hash = (String)input.get(0);
	        	
	        	@SuppressWarnings("unchecked")
				Map<String, Object> fingerbookIdAmounts = (Map<String, Object>) input.get(1);
	        	
	        	DataBag b = BagFactory.getInstance().newDefaultBag();
	        	
	        	for (Map.Entry<String, Object> e : ((Map<String, Object>)fingerbookIdAmounts).entrySet()) {
	        		Long amount = DataByteArrayToLong((DataByteArray) e.getValue());
	        		for(long i = 0; i < amount; i++) {
	        			Tuple hashAmountTuple = TupleFactory.getInstance().newTuple(2);
						hashAmountTuple.set(0, hash);
						hashAmountTuple.set(1, e.getKey());
						b.add(hashAmountTuple);
	        		}

	        	}

	            return b;
	        } catch(Exception e){
	            System.err.println("Failed to process input; error - " + e.getMessage());
	            return null;
	        }
	    }
	
	private Long DataByteArrayToLong(DataByteArray dba) {
		byte[] byteArray = dba.get();
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        return bb.getLong();
	}
}
