package fbUDF;
import java.io.IOException;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class ColumnMerger extends EvalFunc<Tuple> {
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() < 2)
            return null;
        try{
        	
        	List<Object> inputs = input.getAll();
        	
        	String hash = (String)inputs.get(0);
        	DataBag b = BagFactory.getInstance().newDefaultBag();
        	
        	//removing the rowkey which is the hash
        	inputs.remove(0);
        	int fid = 1;
        	for (Object o : inputs) {
				if(o != null) {
					Integer amount = (Integer) o;
					for(int i = 0; i < amount; i++) {
						Tuple hashAmountTuple = TupleFactory.getInstance().newTuple(2);
						hashAmountTuple.set(0, hash);
						hashAmountTuple.set(1, fid);
						b.add(hashAmountTuple);
					}
				}
				fid++;
			}
        	
            Tuple output = TupleFactory.getInstance().newTuple(1);
            output.set(0, b);

            return output;
        } catch(Exception e){
            System.err.println("Failed to process input; error - " + e.getMessage());
            return null;
        }
    }
    
//    public Schema outputSchema(Schema input) {
//        try{
//            Schema tupleSchema = new Schema();
//            tupleSchema.add(input.getField(1));
//            tupleSchema.add(input.getField(0));
//            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),tupleSchema, DataType.TUPLE));
//        }catch (Exception e){
//                return null;
//        }
//    }
}
