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



public class TagsMapToBag extends EvalFunc<DataBag>  {

	@Override
	public DataBag exec(Tuple input) throws IOException {
		 if (input == null || input.size() < 2)
	            return null;
	        try{
	        		        	
	        	Long fidLong = DataByteArrayToLong((DataByteArray)input.get(0));
	        	
	        	String fid = fidLong.toString();
	        	@SuppressWarnings("unchecked")
				Map<String, Object> tagsAmount = (Map<String, Object>) input.get(1);
	        	
	        	DataBag b = BagFactory.getInstance().newDefaultBag();
	        
	        	for (Map.Entry<String, Object> e : ((Map<String, Object>)tagsAmount).entrySet()) {
        			Tuple tagTuple = TupleFactory.getInstance().newTuple(2);
        			tagTuple.set(0, fid);
					tagTuple.set(1, e.getKey());
					b.add(tagTuple);
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
