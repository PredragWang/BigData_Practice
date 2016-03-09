package gw.mapredproj.mappers;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by Guanyu on 3/9/16.
 */
public class InvertedIndexMapper
    extends Mapper<Object, Text, Text, Text> {

    private Text user = new Text();
    private Text friendValue = new Text();
    @Override
    public void map(Object key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {

    }
}
