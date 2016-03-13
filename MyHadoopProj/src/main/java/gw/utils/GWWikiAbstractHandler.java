package gw.utils;

import org.apache.hadoop.mapreduce.Mapper;
import org.tartarus.snowball.ext.PorterStemmer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guanyu on 3/10/16.
 */
public class GWWikiAbstractHandler extends DefaultHandler {
    private GWWikiArticle doc = new GWWikiArticle();
    private String title = null;
    private String url = null;
    private String articleAbstract = null;
    private PorterStemmer stemmer = new PorterStemmer();
    private Mapper.Context context = null;
    private int id = 0;
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        if (qName.equals("doc")) {
            title = attributes.getValue("title");
            if (title != null && title.startsWith("Wikipedia:")) {
                title = title.substring("Wikipedia:".length());
            }
            url = attributes.getValue("url");
            articleAbstract = attributes.getValue("abstract");
        }
    }
    public void endElement(String uri, String localName,
                             String qName)
            throws SAXException {
        if (qName.equals("doc")) {
            if (title != null && url != null && articleAbstract != null) {
                doc.setId(id++);
                doc.setTitle(title);
                doc.setUrl(url);
                doc.setArticleAbstract(articleAbstract);
                doc.processContent(stemmer, context);
            }
        }
    }
    public void setContext(Mapper.Context context) {
        this.context = context;
    }
}
