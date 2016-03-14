package gw.test;

import gw.utils.GWWikiAbstractHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Guanyu on 3/14/16.
 */
public class XMLParserTest {
    public static void main(String[] args) {
        if (args.length == 0) return;
        File f = new File(args[0]);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GWWikiAbstractHandler handler = new GWWikiAbstractHandler();
            saxParser.parse(f, handler);
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
