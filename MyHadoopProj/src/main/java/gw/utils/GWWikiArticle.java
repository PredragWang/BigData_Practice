package gw.utils;

import org.apache.hadoop.mapreduce.Mapper;
import org.tartarus.snowball.ext.PorterStemmer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Guanyu on 3/10/16.
 */
public class GWWikiArticle {
    private String title;
    private String url;
    private String articleAbstract;
    private PorterStemmer stemmer;
    private List<String> wordsList;
    private boolean outputInvertedIndex;
    private Mapper.Context context;

    public GWWikiArticle(String title, String url, String articleAbstract,
                         PorterStemmer stemmer, boolean outputInvertedIndex,
                         Mapper.Context context){
        this.title = title;
        this.url = url;
        this.articleAbstract = articleAbstract;
        this.stemmer = stemmer;
        this.outputInvertedIndex = outputInvertedIndex;
        this.context = context;
        wordsList = new LinkedList<String>();
        if (stemmer != null) {
            processContent();
        }
    }

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

    private void processContent () {
        String[] wordsInTitle = title.split(" ");
        for (String w : wordsInTitle) {
            stemmer.setCurrent(w.toLowerCase());
            stemmer.stem();

            wordsList.add(stemmer.toString());
        }
        String[] wordsInAbstract = articleAbstract.split(" ");
        for (String w : wordsInAbstract) {
            stemmer.setCurrent(w.toLowerCase());
            stemmer.stem();
            wordsList.add(stemmer.toString());
        }
    }

    public List<String> getWordsList() {
        return this.wordsList;
    }
}
