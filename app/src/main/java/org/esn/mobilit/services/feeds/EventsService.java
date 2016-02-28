package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class EventsService extends RSSFeedService {

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
    public void resetService() {
        instance = new EventsService();
    }

    @Override
    public String getString() {
        return EVENTS;
    }

    @Override
    public void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback) {
        FeedProvider.makeEventRequest(sectionWebsite, networkCallback);
    }

}
