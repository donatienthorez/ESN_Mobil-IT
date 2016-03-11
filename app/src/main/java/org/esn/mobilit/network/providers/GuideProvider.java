package org.esn.mobilit.network.providers;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GuideProvider {
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://mobilit.esnlille.fr/api/android/v1")
            .build();

    private interface GuideProviderInterface{
        @GET("/guides/{section}")
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
                callback.onFailure(error.getMessage());
            }
        });
    }
}
