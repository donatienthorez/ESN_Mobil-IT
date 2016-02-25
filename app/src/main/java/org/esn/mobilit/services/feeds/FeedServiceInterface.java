package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public interface FeedServiceInterface {
    RSSFeedParser getFeed();
    RSSFeedParser getFromCache();
    void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback);
    void setFeed(RSSFeedParser rssFeedParser);
    void setFeedToCache(RSSFeedParser rssFeedParser);
}

