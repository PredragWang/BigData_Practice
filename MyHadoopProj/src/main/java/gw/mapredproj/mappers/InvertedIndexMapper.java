package gw.mapredproj.mappers;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.tartarus.snowball.ext.PorterStemmer;

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
    private PorterStemmer stemmer = new PorterStemmer();
    private Text t = new Text();
    @Override
    public void map(LongWritable key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        String document = value.toString();
        String docTitle = null;
        String docURL = null;
        String docAbstract = null;
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
                            docTitle = reader.getText().trim();
                            if (docTitle != null) {
                                String[] wordsInTitle = docTitle.split(" ");
                                for (String w : wordsInTitle) {
                                    stemmer.setCurrent(w.toLowerCase());
                                    stemmer.stem();
                                    t.set(stemmer.getCurrent());
                                    context.write(t, key);
                                }
                            }
                        } else if (currentElement.equalsIgnoreCase("url")) {
                            docURL = reader.getText().trim();
                        } else if (currentElement.equalsIgnoreCase("abstract")) {
                            docAbstract = reader.getText().trim();
                            if (docAbstract != null) {
                                String[] wordsInAbstract = docAbstract.split(" ");
                                for (String w : wordsInAbstract) {
                                    stemmer.setCurrent(w.toLowerCase());
                                    stemmer.stem();
                                    t.set(stemmer.getCurrent());
                                    context.write(t, key);
                                }
                            }
                        }
                        break;
                }
            }
            reader.close();
        }catch (XMLStreamException xe) {
            xe.printStackTrace();
        }
    }
}
