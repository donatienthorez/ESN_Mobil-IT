package org.esn.mobilit.utils.parser;

import android.content.Context;

import com.bumptech.glide.Glide;

import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RSSFeedParser {

	private List<RSSItem> itemlist;

	@ForApplication
	@Inject
	Context context;

	public RSSFeedParser() {
		itemlist = new ArrayList<>();
		InjectUtil.component().inject(this);
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
		if (title == null) {
			return null;
		}
		title = title.replaceAll("\\s+","");
		for(RSSItem item : this.getList()) {
			String itemTitle = item.getTitle().replaceAll("\\s+","");
			if (itemTitle.equalsIgnoreCase(title)) {
				return item;
			}
		}
		return null;
	}

	/**
	 *
	 * @param link Link of the node
	 * @return int position of the item in the list, -1 if not present
	 */
	public int getIndex(List<RSSItem> list, String link) {
		int i = 0;
		for(RSSItem item : list){
			if (item.getLink().equalsIgnoreCase(link)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	public RSSFeedParser updateItems(List<RSSItem> serverItems) {
		if (serverItems.size() == 0) {
			return this;
		}

		for (RSSItem serverItem : serverItems) {
			moveImage(serverItem);
		}

		this.itemlist = serverItems;

		return this;
	}

	public void moveImage(RSSItem rssItem){
		DOMParser.moveImage(rssItem);

		Glide.with(context)
				.load(rssItem.getImage())
				.preload(150, 250);
	}
}
