package org.esn.mobilit.utils.parser;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.esn.mobilit.utils.Reversed.reversed;

public class RSSFeedParser implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<RSSItem> itemlist;

	public RSSFeedParser() {
		itemlist = new ArrayList<RSSItem>();
	}

    public RSSFeedParser(List<RSSItem> itemlist){
        this.itemlist = itemlist;
    }

	public void addItem(RSSItem rssItem) {
		itemlist.add(0, rssItem);
	}

	public RSSItem getItem(int location) {
		return itemlist.get(location);
	}

    public List<RSSItem> getList(){
		return itemlist;
	}

	public int getItemCount() {
		return getList().size();
	}

	public RSSItem getRSSItemFromTitle(String title) {
		for(RSSItem item : this.getList()) {
			if (item.getTitle().equalsIgnoreCase(title)) {
				return item;
			}
		}
		return null;
	}

	public boolean isInList(String title, String link) {
		for(RSSItem item : this.getList()){
			if (item.getTitle().equalsIgnoreCase(title) && item.getLink().equalsIgnoreCase(link)) {
				return true;
			}
		}
		return false;
	}

	public RSSFeedParser addItems(RSS rss) {
		List<RSSItem> items = rss.getRSSChannel().getList();

		for (RSSItem newItem : reversed(items)) {
			if (!isInList(newItem.getTitle(), newItem.getLink())) {
				getList().add(0, newItem);
				moveImage(newItem);
			}
		}

		return this;
	}

	public void moveImage(RSSItem rssItem){
		DOMParser.moveImage(rssItem);

		Glide.with(MobilITApplication.getContext())
				.load(rssItem.getImage())
				.preload(150, 250);
	}
}
