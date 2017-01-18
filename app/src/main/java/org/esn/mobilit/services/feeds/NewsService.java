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
public class NewsService extends RSSFeedService {

    @Inject
    public NewsService() {}

    @Inject
    CacheService cacheService;

    @Override
    public void resetService() {
    }

    public String getString() {
        return ApplicationConstants.CACHE_NEWS;
    }

    public void getFromSite(NetworkCallback<RSSFeedParser> networkCallback) {
        Section section = (Section) cacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);
        if (section != null) {
            FeedProvider.makeNewsRequest(section.getWebsite(), getCallback(networkCallback));
        }
    }
}
