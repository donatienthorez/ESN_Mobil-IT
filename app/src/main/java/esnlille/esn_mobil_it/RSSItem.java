package esnlille.esn_mobil_it;

import java.io.Serializable;

/**
 * Created by Spider on 21/11/14.
 *
 * Useful links :
 * http://stackoverflow.com/questions/23897215/how-to-automatically-generate-getters-and-setters-in-android-studio
 */
public class RSSItem implements Serializable{

    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String description;
    private String pubDate;
    private String creator;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}
