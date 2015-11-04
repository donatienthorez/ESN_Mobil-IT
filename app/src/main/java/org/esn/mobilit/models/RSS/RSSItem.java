package org.esn.mobilit.models.RSS;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="item")
public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Element(name = "title")
	private String title;

	@Element(name = "description")
	private String description;

	@Element(name = "link")
    private String link;

	@Element(name = "pubDate")
	private String pubDate;

	@Element(name = "creator")
	private String creator;

	@Element(name = "guid")
	private String guid;

	@Element(name = "comments", required = false)
	private String comments;

	private String date;
	private String image;


	public RSSItem(){
		this.setTitle(null);
		this.setDescription(null);
		this.setLink(null);
		this.setDate(null);
		this.setImage(null);
		this.setPubDate(null);
	}

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setLink(String link) {
		this.link = link;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(String pubdate) {
		date = pubdate;
	}

	public void setImage(String image) {
		this.image = image;
	}

    public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}

	public String getImage() {
		return image;
	}
}
