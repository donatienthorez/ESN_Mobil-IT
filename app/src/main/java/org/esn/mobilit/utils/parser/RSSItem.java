package org.esn.mobilit.utils.parser;

import java.io.Serializable;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String _title = null;
	private String _description = null;
    private String _link = null;
	private String _date = null;
	private String _image = null;
    private String _pubDate;

    public String get_pubDate() {
        return _pubDate;
    }

    public void set_pubDate(String _pubDate) {
        this._pubDate = _pubDate;
    }

    void setLink(String link) {this._link = link;}

	void setTitle(String title) {
		_title = title;
	}

	void setDescription(String description) {
		_description = description;
	}

	void setDate(String pubdate) {
		_date = pubdate;
	}

	void setImage(String image) {
		_image = image;
	}

    public String getLink() {return _link;}

	public String getTitle() {
		return _title;
	}

	public String getDescription() {
		return _description;
	}

	public String getDate() {
		return _date;
	}

	public String getImage() {
		return _image;
	}

}
