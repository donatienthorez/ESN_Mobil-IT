package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class PartnersService implements FeedServiceInterface{
    private RSSFeedParser partners;
    private static PartnersService instance;

    public static final String PARTNERS = "partners";

    private PartnersService() {
    }

    public static PartnersService getInstance() {
        if (instance == null){
            instance = new PartnersService();
        }
        return instance;
    }

    @Override
    public RSSFeedParser getFeed() {
        return this.partners;
    }

    @Override
    public RSSFeedParser getFromCache() {
        return (RSSFeedParser) CacheService.getObjectFromCache(PARTNERS);
    }

    @Override
    public void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback) {
        FeedProvider.makePartnersRequest(sectionWebsite, networkCallback);
    }

    @Override
    public void setFeed(RSSFeedParser partners) {
        this.partners = partners;
    }

    @Override
    public void setFeedToCache(RSSFeedParser partners) {
        CacheService.saveObjectToCache(PARTNERS, partners);
    }
}
