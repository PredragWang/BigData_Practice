package gw.mapredproj;

import gw.mapredproj.mappers.InvertedIndexMapper;
import gw.mapredproj.reducers.InvertedIndexReducer;
import gw.utils.XmlInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;



/**
 * Created by Guanyu on 3/4/16.
 */
public class InvertedIndex {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
        String[] remainingArgs = optionParser.getRemainingArgs();
        if (!(remainingArgs.length != 2 || remainingArgs.length != 4)) {
            System.err.println("Usage:  <in> <out>");
            System.exit(2);
        }
        conf.set("xmlinput.start", "<doc>");
        conf.set("xmlinput.end", "</doc>");
        Job job = Job.getInstance(conf, "Inverted index");
        job.setInputFormatClass(XmlInputFormat.class);
        job.setJarByClass(InvertedIndex.class);
        job.setMapperClass(InvertedIndexMapper.class);
        job.setReducerClass(InvertedIndexReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        FileInputFormat.addInputPath(job, new Path(remainingArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(remainingArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
