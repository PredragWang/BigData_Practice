package gw.utils;

import org.apache.hadoop.mapreduce.Mapper;
import org.tartarus.snowball.ext.PorterStemmer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Guanyu on 3/10/16.
 */
public class GWWikiAbstractHandler extends DefaultHandler {
    private GWWikiArticle doc = new GWWikiArticle();

    private String content = null;

    private PorterStemmer stemmer = new PorterStemmer();
    private Mapper.Context context = null;
    private int id = 0;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        if (qName.equals("doc")) {
            System.out.println("document detected");
            doc.setId(id);
        }
    }

    @Override
    public void endElement(String uri, String localName,
                             String qName)
            throws SAXException {
        if (qName.equals("doc")) {
            doc.processContent(stemmer, context);
            System.out.println("Document " + doc.getTitle());
            id ++;
        }
        else if (qName.equals("title")) {
            doc.setTitle(content);
        }
        else if (qName.equals("url")) {
            doc.setUrl(content);
        }
        else if (qName.equals("abstract")) {
            doc.setArticleAbstract(content);
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        content = String.copyValueOf(ch, start, length).trim();
    }

    public void setContext(Mapper.Context context) {
        this.context = context;
    }
}
