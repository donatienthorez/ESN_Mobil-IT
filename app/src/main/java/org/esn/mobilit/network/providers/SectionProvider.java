package org.esn.mobilit.network.providers;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

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

public class SectionProvider {

    public static final String TAG = "SectionProvider";

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(ApplicationConstants.API_ENDPOINT)
            .build();

    private interface SectionProviderInterface{
        @GET(ApplicationConstants.API_SECTIONS)
        void getSection(@Path("section") String section, @Query("token") String token, Callback<Section> callback);
    }

    public static void makeRequest(Section section, final NetworkCallback<Section> callback) {
        SectionProviderInterface sectionService = restAdapter.create(SectionProviderInterface.class);
        sectionService.getSection(section.getCode_section(), ApplicationConstants.MOBILIT_TOKEN, new Callback<Section>() {
            @Override
            public void success(Section result, Response response) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error.getMessage());
                Crashlytics.log(Log.ERROR, SectionProvider.TAG, error.getMessage());
            }
        });
    }
}
