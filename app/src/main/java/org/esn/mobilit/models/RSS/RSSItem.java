package org.esn.mobilit.models.RSS;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="item")
public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Element(name = "title", required = true)
	private String title;

	@Element(name = "link", required = false)
	private String link;

	@Element(name = "description", required = false)
	private String description;

    @Element(name = "author", required = false)
	private String author;

    @Element(name = "creator", required = false)
    private String creator;

    @Element(name = "category", required = false)
	private String category;

    @Element(name = "comments", required = false)
	private String comments;

	@Element(name = "enclosure", required = false)
	private String enclosure;

    @Element(name = "guid", required = false)
	private String guid;

    @Element(name = "pubDate", required = false)
	private String pubDate;

    @Element(name = "source", required = false)
	private String source;

	private String date;
	private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description != null ? description : "Empty description";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
