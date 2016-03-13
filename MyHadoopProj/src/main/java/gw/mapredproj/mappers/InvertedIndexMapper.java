package gw.mapredproj.mappers;

import gw.utils.GWWikiAbstractHandler;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Guanyu on 3/9/16.
 */
public class InvertedIndexMapper
    extends Mapper<Object, Text, Text, Text> {
    @Override
    public void map(Object key, Text value, Mapper.Context context)
            throws IOException, InterruptedException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GWWikiAbstractHandler handler = new GWWikiAbstractHandler();
            handler.setContext(context);
            saxParser.parse(new InputSource(new StringReader(value.toString())), handler);
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
