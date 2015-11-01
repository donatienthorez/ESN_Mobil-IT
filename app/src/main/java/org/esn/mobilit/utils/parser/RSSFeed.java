package org.esn.mobilit.utils.parser;

import org.esn.mobilit.models.RSS.RSSItem;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class RSSFeed implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<RSSItem> itemlist;

	public RSSFeed() {
		itemlist = new Vector<RSSItem>(0);
	}

    public RSSFeed(List<RSSItem> itemlist){
        this.itemlist = itemlist;
    }

	public void addItem(RSSItem item) {
		itemlist.add(item);
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

    public int getPositionFromTitle(String title){
        for(RSSItem item : this.getList()){
            if (item.getTitle().equalsIgnoreCase(title))
				return getList().indexOf(item);
        }
        return -1;
    }

}
