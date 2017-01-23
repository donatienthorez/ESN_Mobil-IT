package org.esn.mobilit.network.providers;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class GuideProvider {

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(ApplicationConstants.API_ENDPOINT)
            .build();

    private interface GuideProviderInterface{
        @GET(ApplicationConstants.API_GUIDE)
        void getGuide(@Path("section") String section, @Query("token") String token, Callback<Guide> callback);
    }

    public static void makeGuideRequest(Section section, final NetworkCallback<Guide> callback) {
        GuideProviderInterface guideService = restAdapter.create(GuideProviderInterface.class);
        guideService.getGuide(section.getCode_section(), ApplicationConstants.MOBILIT_TOKEN, new Callback<Guide>() {
            @Override
            public void success(Guide result, Response response) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                Crashlytics.logException(error);
                callback.onFailure(error.getMessage());
            }
        });
    }
}
