package gw.utils;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Guanyu on 3/10/16.
 */
public class GWWikiArticle {
    private int id;
    private String title;
    private String url;
    private String articleAbstract;
    private Text t = new Text();
    private IntWritable iw = new IntWritable();

    public GWWikiArticle() {}
    public GWWikiArticle(int id, String title, String url, String articleAbstract){
        this.id = id;
        this.title = title;
        this.url = url;
        this.articleAbstract = articleAbstract;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public void processContent (PorterStemmer stemmer, Mapper.Context context) {
        iw.set(id);
        try {
            String[] wordsInTitle = title.split(" ");
            for (String w : wordsInTitle) {
                stemmer.setCurrent(w.toLowerCase());
                stemmer.stem();
                if (context != null) {
                    t.set(stemmer.toString());
                    context.write(t, new IntWritable(id));
                }
            }
            String[] wordsInAbstract = articleAbstract.split(" ");
            for (String w : wordsInAbstract) {
                stemmer.setCurrent(w.toLowerCase());
                stemmer.stem();
                if (context != null) {

                }
            }
        }catch (InterruptedException ie) {
            ie.printStackTrace();
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
