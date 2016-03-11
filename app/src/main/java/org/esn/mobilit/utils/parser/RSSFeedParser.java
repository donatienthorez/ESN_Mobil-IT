package org.esn.mobilit.utils.parser;

import org.esn.mobilit.models.RSS.RSSItem;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class RSSFeedParser implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<RSSItem> itemlist;

	public RSSFeedParser() {
		itemlist = new Vector<RSSItem>(0);
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

    public boolean isEmpty(){
        return itemlist.size() == 0;
    }

	public int getItemCount() {
		return getList().size();
	}

    public int getPositionFromTitle(String title){
        for(RSSItem item : this.getList()){
            if (item.getTitle().equalsIgnoreCase(title))
				return getList().indexOf(item);
        }
        return -1;
    }
}
