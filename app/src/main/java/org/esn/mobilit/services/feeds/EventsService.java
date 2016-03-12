package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class EventsService extends RSSFeedService {

    private static EventsService instance;

    private EventsService(){
    }

    public static EventsService getInstance(){
        if (instance == null){
            instance = new EventsService();
        }
        return instance;
    }

    @Override
    public void resetService() {
        instance = new EventsService();
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_EVENTS;
    }

    @Override
    public void getFromSite(String sectionWebsite, NetworkCallback<RSSFeedParser> networkCallback) {
        FeedProvider.makeEventRequest(sectionWebsite, getCallback(networkCallback));
    }

}
