package cn.com.aetc.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "GREEN_NEWS".
 */
public class GreenNews {

    private Long id;
    private String date;
    private String newslist;

    public GreenNews() {
    }

    public GreenNews(Long id) {
        this.id = id;
    }

    public GreenNews(Long id, String date, String newslist) {
        this.id = id;
        this.date = date;
        this.newslist = newslist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewslist() {
        return newslist;
    }

    public void setNewslist(String newslist) {
        this.newslist = newslist;
    }

}
