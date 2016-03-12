package org.esn.mobilit.services.feeds;

import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class PartnersService extends RSSFeedService {

    private static PartnersService instance;

    private PartnersService() {
    }

    public static PartnersService getInstance() {
        if (instance == null){
            instance = new PartnersService();
        }
        return instance;
    }

    @Override
    public void resetService() {
        instance = new PartnersService();
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_PARTNERS;
    }

    @Override
    public void getFromSite(String sectionWebsite, NetworkCallback<RSSFeedParser> networkCallback) {
        FeedProvider.makePartnersRequest(sectionWebsite, getCallback(networkCallback));
    }
}
