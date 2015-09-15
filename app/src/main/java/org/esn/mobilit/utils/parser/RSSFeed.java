package org.esn.mobilit.utils.parser;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class RSSFeed implements Serializable {

	private static final long serialVersionUID = 1L;
	private int itemcount = 0;
	private List<RSSItem> itemlist;

	public RSSFeed() {
		itemlist = new Vector<RSSItem>(0);
	}

	public void addItem(RSSItem item) {
		itemlist.add(item);
		itemcount++;
	}

	public RSSItem getItem(int location) {
		return itemlist.get(location);
	}

    public List<RSSItem> getList(){
		return itemlist;
	}
	public int getItemCount() {
		return itemcount;
	}

    public int getPositionFromTitle(String title){
        for(RSSItem item : this.getList()){
            if (item.getTitle().equalsIgnoreCase(title))
				return getList().indexOf(item);
        }
        return -1;
    }

}
