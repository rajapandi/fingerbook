package fbUDF;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;

public class ConverToInt extends EvalFunc<Long> {
    
	public Long exec(Tuple input) throws IOException {
        if (input == null || input.size() > 1)
            return null;
        try{
        	DataByteArray dba = (DataByteArray)input.get(0);
        	           
            byte[] byteArray = dba.get();
            ByteBuffer bb = ByteBuffer.wrap(byteArray);
            long l = bb.getLong();
            
            return l;
            
        } catch(Exception e){
            System.err.println("Failed to process input; error - " + e.getMessage());
            return null;
        }
    }

}
