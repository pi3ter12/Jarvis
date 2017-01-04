package elearning.wiacekp.pl.jarvis.rssreader;

import java.util.ArrayList;

/**
 * Created by Piotrek on 2016-04-03.
 */
public class RSSDownloadedItem {
    private int id;
    private ArrayList<RSSItem> rssItems = new ArrayList<>();
    private String link;
    private String title="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RSSDownloadedItem(int id, ArrayList<RSSItem> rssItems, String link) {
        this.id = id;
        this.rssItems = rssItems;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<RSSItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(ArrayList<RSSItem> rssItems) {
        this.rssItems = rssItems;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
