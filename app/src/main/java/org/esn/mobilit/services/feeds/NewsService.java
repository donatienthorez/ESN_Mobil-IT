package org.esn.mobilit.services.feeds;

import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class NewsService extends RSSFeedService {

    private static NewsService instance;

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
        return ApplicationConstants.CACHE_NEWS;
    }

    public void getFromSite(String sectionWebsite, NetworkCallback<RSSFeedParser> networkCallback) {
        FeedProvider.makeNewsRequest(sectionWebsite, getCallback(networkCallback));
    }
}
