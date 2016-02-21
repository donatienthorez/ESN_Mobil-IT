package org.esn.mobilit.services.gcm;

import android.util.Log;

import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.text.ParseException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

public class PostRegService {

    public static PostRegService instance;

    private PostRegService(){
        instance = new PostRegService();
    }

    public static PostRegService getInstance(){
        return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://mobilit.esnlille.fr/api/android")
            .build();

    private interface PostRegServiceInterface{
        @Multipart
        @POST("/v1/regids/")
        void registerId(
                @Part("regId") String regId,
                @Part("section") String section,
                Callback<Response> callback
        );
    }

    public static void registerId(final NetworkCallback<Response> callback) throws ParseException {
        PostRegServiceInterface postRegService = restAdapter.create(PostRegServiceInterface.class);
        Section section = (Section) CacheService.getObjectFromCache("section");
        postRegService.registerId(
                GCMService.getInstance().getRegId(),
                section.getCode_section(),
                new Callback<Response>() {
                    @Override
                    public void success(Response s, Response response) {
                        callback.onSuccess(s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onFailure(error);
                    }
                }
        );
    }
}
