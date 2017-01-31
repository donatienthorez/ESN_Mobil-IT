package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FeedService {

    @Inject
    CacheService cacheService;

    @Inject
    FeedProvider feedProvider;

    @Inject
    public FeedService(){
        InjectUtil.component().inject(this);}

    public void getFromSite(final String feedType, final NetworkCallback<RSSFeedParser> networkCallback) {
        makeFeedRequest(feedType, new NetworkCallback<RSS>() {
            @Override
            public void onNoConnection() {
                networkCallback.onNoConnection();
            }

            @Override
            public void onSuccess(RSS result) {
                RSSFeedParser rssFeedParser = new RSSFeedParser();
                rssFeedParser.updateItems(result.getRSSChannel().getList());

                cacheService.setFeed(getString(feedType), rssFeedParser.getList());
                networkCallback.onSuccess(rssFeedParser);
            }

            @Override
            public void onNoAvailableData() {
                networkCallback.onNoAvailableData();
            }

            @Override
            public void onFailure(String error) {
                networkCallback.onFailure(error);
            }
        });
    }

    private void makeFeedRequest(final String feedType, NetworkCallback<RSS> callback){
        switch (feedType) {
            case FeedType.NEWS:
                feedProvider.makeNewsRequest(callback);
                break;
            case FeedType.EVENTS:
                feedProvider.makeEventRequest(callback);
                break;
            case FeedType.PARTNERS:
                feedProvider.makePartnersRequest(callback);
                break;
        }
    }

    public String getString(String feedType) {
        switch (feedType) {
            case FeedType.NEWS:
                return ApplicationConstants.CACHE_NEWS;
            case FeedType.EVENTS:
                return ApplicationConstants.CACHE_EVENTS;
            case FeedType.PARTNERS:
                return ApplicationConstants.CACHE_PARTNERS;
        }
        //TODO improve
        throw new RuntimeException("Unsupported FeedService type");
    }
}
