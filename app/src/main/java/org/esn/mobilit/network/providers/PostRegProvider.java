package org.esn.mobilit.network.providers;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.utils.ApplicationConstants;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public class PostRegProvider {

    public static final String TAG = "PostRegProvider";

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(ApplicationConstants.API_ENDPOINT)
            .build();

    private interface PostRegServiceInterface{
        @FormUrlEncoded
        @POST(ApplicationConstants.API_REGIDS)
        void registerId(
                @Field("token") String token,
                @Field("regId") String regId,
                @Field("section") String section,
                Callback<Response> callback
        );
    }

    public static void makeRequest(String codeSection, String regId, final org.esn.mobilit.utils.callbacks.Callback<Response> callback) {
        PostRegServiceInterface postRegService = restAdapter.create(PostRegServiceInterface.class);

        postRegService.registerId(
                ApplicationConstants.MOBILIT_TOKEN,
                regId,
                codeSection,
                new Callback<Response>() {
                    @Override
                    public void success(Response s, Response response) {
                        callback.onSuccess(s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Crashlytics.logException(error);
                        callback.onFailure(error.getMessage());
                    }
                }
        );
    }
}
