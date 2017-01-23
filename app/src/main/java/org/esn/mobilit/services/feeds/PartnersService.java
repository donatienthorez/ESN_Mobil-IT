package org.esn.mobilit.services.feeds;

import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PartnersService extends RSSFeedService {

    @Inject
    FeedProvider feedProvider;

    @Inject
    public PartnersService() {
        InjectUtil.component().inject(this);
    }

    @Override
    public void resetService() {}

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_PARTNERS;
    }

    @Override
    public void getFromSite(NetworkCallback<RSSFeedParser> networkCallback) {
        feedProvider.makePartnersRequest(getCallback(networkCallback));
    }
}
