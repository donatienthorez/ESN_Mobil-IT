package org.esn.mobilit.network.providers;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public class GuideProvider {
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://mobilit.esnlille.fr/api/android/v1")
            .build();

    private interface GuideProviderInterface{
        @GET("/guides/{section}")
        void getGuide(@Path("section") String section, Callback<Guide> callback);
    }

    public static void makeGuideRequest(Section section, final NetworkCallback<Guide> callback) {
        GuideProviderInterface guideService = restAdapter.create(GuideProviderInterface.class);
        guideService.getGuide(section.getCode_section(), new Callback<Guide>() {
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
