package org.esn.mobilit.network.providers;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;

@Singleton
public class FeedProvider {

    @Inject
    AppState appState;

    @Inject
    Utils utils;

    @Inject
    CacheService cacheService;

    @Inject
    public FeedProvider() {
        InjectUtil.component().inject(this);
    }

    private interface FeedProviderInterface{
        @GET(ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH)
        void getEvents(Callback<RSS> callback);

        @GET(ApplicationConstants.NEWS_PATH + ApplicationConstants.FEED_PATH)
        void getNews(Callback<RSS> callback);

        @GET(ApplicationConstants.PARTNERS_PATH + ApplicationConstants.FEED_PATH)
        void getPartners(Callback<RSS> callback);
    }


    public void makeFeedRequest(FeedType feedType, final NetworkCallback<ArrayList<RSSItem>> callback) {
        if (!utils.isConnected()) {
            callback.onNoConnection(appState.getFeed(feedType));
            return;
        }

        if (appState.hasValidSection()) {
            FeedProviderInterface feedProvider = createBuilder(appState.getSectionWebsite());
            //FIXME change cacheable strings to feed_type
            switch (feedType.getFeedTypeString()) {
                case ApplicationConstants.FEED_TYPE_NEWS:
                    feedProvider.getNews(createCallback(callback));
                    break;
                case ApplicationConstants.FEED_TYPE_EVENTS:
                    feedProvider.getEvents(createCallback(callback));
                    break;
                case ApplicationConstants.FEED_TYPE_PARTNERS:
                    feedProvider.getPartners(createCallback(callback));
                    break;
            }
        }
        //TODO else log exception
    }

    private static FeedProviderInterface createBuilder(String sectionWebsite) {
        return new RestAdapter.Builder()
                .setEndpoint(sectionWebsite)
                .setConverter(new SimpleXMLConverter())
                .build()
                .create(FeedProviderInterface.class);
    }

    private static Callback<RSS> createCallback(final NetworkCallback<ArrayList<RSSItem>> callback) {
        return new Callback<RSS>() {
            @Override
            public void success(RSS result, Response response) {
                if (result.getListSize() == 0) {
                    callback.onNoAvailableData();
                } else {
                    ArrayList<RSSItem> rssItems = new ArrayList<>();
                    rssItems.addAll(result.getRSSChannel().getList());
                    callback.onSuccess(rssItems);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Crashlytics.logException(error);
                callback.onFailure(error.getMessage());
            }
        };
    }
}
