package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PartnersService extends RSSFeedService {

    private static PartnersService instance;

    @Inject
    public PartnersService() {
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
    public void getFromSite(NetworkCallback<RSSFeedParser> networkCallback) {
        Section section = (Section) cacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);
        if (section != null) {
            FeedProvider.makePartnersRequest(section.getWebsite(), getCallback(networkCallback));
        }
    }
}
