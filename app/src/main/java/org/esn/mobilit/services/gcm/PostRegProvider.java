package org.esn.mobilit.services.gcm;

import org.esn.mobilit.utils.ApplicationConstants;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

public class PostRegProvider {

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://mobilit.esnlille.fr/api/android")
            .build();

    private interface PostRegServiceInterface{
        @Multipart
        @POST("/v1/regids")
        void registerId(
                @Part("regId") String regId,
                @Part("section") String section,
                @Query("token") String token,
                Callback<Response> callback
        );
    }

    public static void makeRequest(String codeSection, String regId, final org.esn.mobilit.utils.callbacks.Callback<Response> callback) {
        PostRegServiceInterface postRegService = restAdapter.create(PostRegServiceInterface.class);

        postRegService.registerId(
                regId,
                codeSection,
                ApplicationConstants.MOBILIT_TOKEN,
                new Callback<Response>() {
                    @Override
                    public void success(Response s, Response response) {
                        callback.onSuccess(s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onFailure(error.getMessage());
                    }
                }
        );
    }
}
