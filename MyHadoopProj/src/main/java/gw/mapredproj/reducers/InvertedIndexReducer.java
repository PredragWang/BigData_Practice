package gw.mapredproj.reducers;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
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
        extends Reducer<Text, LongWritable, Text, Text> {
    private HashMap<Long, Integer> docCount = new HashMap<Long, Integer>();

    @Override
    public void reduce(Text key, Iterable<LongWritable> values,
                       Context context) throws IOException, InterruptedException {


        docCount.clear();
        Integer count = null;
        for (LongWritable docID : values) {
            count = docCount.get(docID.get());
            if (count == null) {
                docCount.put(docID.get(), 1);
            }
            else {
                docCount.put(docID.get(), count+1);
            }
        }
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Long, Integer> entry : docCount.entrySet()) {
            if (output.length() > 0) output.append("/");
            output.append(entry.getKey() + "," + entry.getValue());
        }
        context.write(key, new Text(output.toString()));
    }
}
