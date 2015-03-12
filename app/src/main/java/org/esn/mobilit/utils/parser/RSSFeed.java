package org.esn.mobilit.utils.parser;

import android.util.Log;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class RSSFeed implements Serializable {

	private static final long serialVersionUID = 1L;
	private int _itemcount = 0;
	private List<RSSItem> _itemlist;

	RSSFeed() {
		_itemlist = new Vector<RSSItem>(0);
	}

	void addItem(RSSItem item) {
		_itemlist.add(item);
		_itemcount++;
	}

	public RSSItem getItem(int location) {
		return _itemlist.get(location);
	}

    public List<RSSItem> getList(){ return _itemlist; }
	public int getItemCount() {
		return _itemcount;
	}

    public int getTitleID(String title){
        for(RSSItem item : this.getList()){
            Log.d("getTitleID", title + "|" + item.getTitle());
            if (item.getTitle().equalsIgnoreCase(title)) return getList().indexOf(item);
        }
        return -1;
    }

}
