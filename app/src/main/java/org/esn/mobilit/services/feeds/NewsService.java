package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class NewsService extends RSSFeedService {

    private static NewsService instance;

    public static final String NEWS = "news";

    private NewsService() {
    }

    public static NewsService getInstance() {
        if (instance == null){
            instance = new NewsService();
        }
        return instance;
    }

    @Override
    public void resetService() {
        instance = new NewsService();
    }

    public String getString() {
        return NEWS;
    }

    public void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback) {
        FeedProvider.makeNewsRequest(sectionWebsite, networkCallback);
    }
}
