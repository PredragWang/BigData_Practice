package gw.mapredproj.mappers;

import gw.utils.GWWordProcessing;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 * Created by Guanyu on 3/9/16.
 */
public class InvertedIndexMapper
        extends Mapper<LongWritable, Text, Text, LongWritable> {
    private Text t = new Text();
    private GWWordProcessing wp = new GWWordProcessing();
    @Override
    public void map(LongWritable key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        String document = value.toString();
        String docTitle = "";
        String docURL = "";
        String docAbstract = "";
        String currentElement = "";
        try {
            XMLStreamReader reader =
                    XMLInputFactory.newInstance().createXMLStreamReader(new
                            ByteArrayInputStream(document.getBytes()));

            while (reader.hasNext()) {
                int code = reader.next();
                switch (code) {
                    case XMLStreamConstants.START_ELEMENT:
                        currentElement = reader.getLocalName();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (currentElement.equalsIgnoreCase("title")) {
                            docTitle += reader.getText().trim();

                        } else if (currentElement.equalsIgnoreCase("url")) {
                            docURL += reader.getText().trim();
                        } else if (currentElement.equalsIgnoreCase("abstract")) {
                            docAbstract += reader.getText().trim();
                        }
                        break;
                }
            }
            reader.close();
        }catch (XMLStreamException xe) {
            xe.printStackTrace();
        }

        if (docTitle.length() > 0) {
            String[] wordsInTitle = docTitle.split(" ");
            for (String w : wordsInTitle) {
                //t.set(w);
                //context.write(t, key);
                if ((w = wp.processWord(w)) != null) {
                    t.set(w);
                    context.write(t, key);
                }
            }
        }
        if (docAbstract.length() > 0) {
            String[] wordsInAbstract = docAbstract.split(" ");
            for (String w : wordsInAbstract) {
                //t.set(w);
                //context.write(t, key);
                if ((w = wp.processWord(w)) != null) {
                    t.set(w);
                    context.write(t, key);
                }
            }
        }
    }
}
