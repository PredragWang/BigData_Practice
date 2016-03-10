package gw.utils;

/**
 * Created by Guanyu on 3/10/16.
 */
public class GWWikiArticle {
    private String title;
    private String url;
    private String articleAbstract;

    public GWWikiArticle(String title, String url, String articleAbstract){
        this.title = title;
        this.url = url;
        this.articleAbstract = articleAbstract;
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
}
