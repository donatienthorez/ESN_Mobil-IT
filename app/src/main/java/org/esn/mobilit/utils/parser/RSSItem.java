package org.esn.mobilit.utils.parser;

import java.io.Serializable;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
    private String link;
	private String date;
	private String image;
    private String pubDate;

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

    void setLink(String link) {
		this.link = link;
	}

	void setTitle(String title) {
		this.title = title;
	}

	void setDescription(String description) {
		this.description = description;
	}

	void setDate(String pubdate) {
		date = pubdate;
	}

	void setImage(String image) {
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
