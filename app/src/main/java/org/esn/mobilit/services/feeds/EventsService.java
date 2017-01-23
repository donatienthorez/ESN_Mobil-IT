package org.esn.mobilit.services.feeds;

import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventsService extends RSSFeedService {

    @Inject
    FeedProvider feedProvider;

    @Inject
    public EventsService() {
        InjectUtil.component().inject(this);
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_EVENTS;
    }

    @Override
    public void getFromSite(NetworkCallback<RSSFeedParser> networkCallback) {
        feedProvider.makeEventRequest(getCallback(networkCallback));
    }

    @Override
    public void resetService() {

    }
}
