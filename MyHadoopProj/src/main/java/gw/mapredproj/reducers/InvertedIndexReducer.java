package gw.mapredproj.reducers;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by partizan on 3/13/16.
 */
public class InvertedIndexReducer
        extends Reducer<Text, Iterable<IntWritable>, Text, ArrayWritable> {
    private HashMap<Integer, Integer> docCount = new HashMap<Integer, Integer>();
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context) throws IOException, InterruptedException {
        docCount.clear();
        Integer count = null;
        for (IntWritable docID : values) {
            count = docCount.get(docID.get());
            if (count == null) {
                docCount.put(docID.get(), 1);
            }
            else {
                count++;
            }
        }
        ArrayList<String> output = new ArrayList<String>();
        for (Map.Entry<Integer, Integer> entry : docCount.entrySet()) {
            output.add(entry.getKey() + "," + entry.getValue());
        }
        context.write(key, new ArrayWritable((String[]) output.toArray()));
    }
}
