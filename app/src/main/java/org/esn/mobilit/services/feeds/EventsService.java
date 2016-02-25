package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class EventsService implements FeedServiceInterface {

    private RSSFeedParser events;
    private static EventsService instance;

    public static final String EVENTS = "events";

    private EventsService(){
    }

    public static EventsService getInstance(){
        if (instance == null){
            instance = new EventsService();
        }
        return instance;
    }

    @Override
    public RSSFeedParser getFeed() {
        return events;
    }

    @Override
    public RSSFeedParser getFromCache() {
        return (RSSFeedParser) CacheService.getObjectFromCache(EVENTS);
    }

    @Override
    public void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback) {
        FeedProvider.makeEventRequest(sectionWebsite, networkCallback);
    }

    @Override
    public void setFeed(RSSFeedParser events) {
        this.events = events;
    }

    @Override
    public void setFeedToCache(RSSFeedParser events) {
        CacheService.saveObjectToCache(EVENTS, events);
    }
}
