package org.esn.mobilit.network.providers;

import com.bumptech.glide.util.Util;
import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

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

    public void makeEventRequest(final NetworkCallback<RSS> callback) {
        if (appState.hasValidSection()) {
            FeedProviderInterface feedProvider = createBuilder(appState.getSectionWebsite());
            feedProvider.getEvents(createCallback(callback));
        }
        //TODO else log exception
    }

    public void makeNewsRequest(final NetworkCallback<RSS> callback) {
        if (appState.hasValidSection()) {
            FeedProviderInterface feedProvider = createBuilder(appState.getSectionWebsite());
            feedProvider.getNews(createCallback(callback));
        }
        //TODO else log exception
    }

    public void makePartnersRequest(final NetworkCallback<RSS> callback) {
        if (appState.hasValidSection()) {
            FeedProviderInterface feedProvider = createBuilder(appState.getSectionWebsite());
            feedProvider.getPartners(createCallback(callback));
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

    private static Callback<RSS> createCallback(final NetworkCallback<RSS> callback) {
        return new Callback<RSS>() {
            @Override
            public void success(RSS result, Response response) {
                if (result.getListSize() == 0) {
                    callback.onNoAvailableData();
                } else {
                    callback.onSuccess(result);
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
