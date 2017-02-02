package org.esn.mobilit.services.feeds;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.utils.parser.DOMParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RSSItemListHelper {

    @Inject
    CacheService cacheService;

    @Inject
    public RSSItemListHelper() {
    }

    /**
     * Retrieves in the rssItemList the rssItem with the specified title.
     *
     * Returns null if nothing is found.
     */
    public RSSItem getRSSItemFromTitle(String title, List<RSSItem> rssItemList) {
        if (title == null) {
            return null;
        }
        title = title.replaceAll("\\s+","");
        for(RSSItem item : rssItemList) {
            String itemTitle = item.getTitle().replaceAll("\\s+","");
            if (itemTitle.equalsIgnoreCase(title)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Search on the rssItems given a better image (cf #DOMParser.moveImage) and loads it with Glide.
     */
    public List<RSSItem> moveRSSImages(List<RSSItem> serverItems) {
        for (RSSItem serverItem : serverItems) {
            DOMParser.moveImage(serverItem);

            Glide.with(MobilITApplication.getContext())
                    .load(serverItem.getImage())
                    .preload(150, 250);
        }

        return serverItems;
    }

    public boolean needsAdapterUpdate(FeedType feedtype, ArrayList<RSSItem> rssItemList) {
        ArrayList<RSSItem>  cachedList = cacheService.getFeed(feedtype.getCacheableString());
        if (cachedList == null || rssItemList == null || cachedList.size() != rssItemList.size()) {
            return true;
        }
        for (int i = 0; i < rssItemList.size(); i++) {
            if (!cachedList.get(i).getPubDate().equals(rssItemList.get(i).getPubDate())
                || !cachedList.get(i).getTitle().equals(rssItemList.get(i).getTitle())
                || !cachedList.get(i).getDescription().equals(rssItemList.get(i).getDescription())) {
                return true;
            }
        }
        return false;
    }
}
