package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FeedService {

    @Inject
    CacheService appState;

    @Inject
    FeedProvider feedProvider;
    @Inject
    RSSItemListHelper rssItemListHelper;

    @Inject
    public FeedService(){
        InjectUtil.component().inject(this);}

    public void getFromSite(final FeedType feedType, final NetworkCallback<ArrayList<RSSItem>> networkCallback) {
        feedProvider.makeFeedRequest(feedType, new NetworkCallback<ArrayList<RSSItem>>() {
            @Override
            public void onNoConnection(ArrayList<RSSItem> result) {
                networkCallback.onNoConnection(new ArrayList<>(result));
            }

            @Override
            public void onSuccess(ArrayList<RSSItem> result) {
                List<RSSItem> rssItems = rssItemListHelper.moveRSSImages(result);
                ArrayList<RSSItem>  rssItemList = new ArrayList<>(rssItems);

                appState.setFeed(feedType, rssItemList);
                networkCallback.onSuccess(rssItemList);
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
}
