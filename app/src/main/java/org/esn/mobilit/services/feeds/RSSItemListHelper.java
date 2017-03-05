package org.esn.mobilit.services.feeds;

import android.content.Context;

import com.bumptech.glide.Glide;

import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.utils.inject.ForApplication;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RSSItemListHelper {

    @Inject
    CacheService cacheService;
    @Inject
    AppState appState;

    @Inject
    @ForApplication
    Context context;

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
            moveImage(serverItem);

            Glide.with(context)
                    .load(serverItem.getImage())
                    .preload(150, 250);
        }

        return serverItems;
    }

    public void moveImage(RSSItem item){
        org.jsoup.nodes.Document docHtml = Jsoup.parse(item.getDescription());
        Elements imgEle = docHtml.select("img");
        Elements colorboxLink = docHtml.getElementsByClass("colorbox");

        if(imgEle != null && imgEle.first() != null) {
            item.setImage(imgEle.first().attr("src"));
            String description = item.getDescription().replace(imgEle.first().toString(), "");
            item.setDescription(description);
        }
        /**
         * ColorBoxLink is a image with a better quality but not present all the time
         * if we find it we replace the image by the better one
         */
        if (colorboxLink != null && colorboxLink.first() != null) {
            item.setImage(colorboxLink.first().attr("href"));
        }
    }

    public boolean needsAdapterUpdate(FeedType feedtype, ArrayList<RSSItem> rssItemList) {
        ArrayList<RSSItem>  cachedList = appState.getFeed(feedtype);
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
