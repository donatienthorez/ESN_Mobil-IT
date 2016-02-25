package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class NewsService implements FeedServiceInterface{
    private RSSFeedParser news;
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
    public RSSFeedParser getFeed() {
        return news;
    }

    @Override
    public RSSFeedParser getFromCache() {
        return (RSSFeedParser) CacheService.getObjectFromCache(NEWS);
    }

    @Override
    public void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback) {
        FeedProvider.makeNewsRequest(sectionWebsite, networkCallback);
    }

    @Override
    public void setFeed(RSSFeedParser news) {
        this.news = news;
    }

    @Override
    public void setFeedToCache(RSSFeedParser news) {
        CacheService.saveObjectToCache(NEWS, news);
    }
}
