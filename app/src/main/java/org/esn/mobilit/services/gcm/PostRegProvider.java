package org.esn.mobilit.services.gcm;

import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

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

    public static void makeRequest(final NetworkCallback<Response> callback) {
        PostRegServiceInterface postRegService = restAdapter.create(PostRegServiceInterface.class);
        Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);
        postRegService.registerId(
                GCMService.getInstance().getRegId(),
                section.getCode_section(),
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
