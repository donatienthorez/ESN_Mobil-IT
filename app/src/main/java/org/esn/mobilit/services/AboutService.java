package org.esn.mobilit.services;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.About;
import org.esn.mobilit.models.Abouts;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.text.ParseException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public class AboutService {

    private static AboutService instance;
    private static About about;

    private AboutService(){
        instance = new AboutService();
    }

    public static AboutService getInstance(){
        return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://logoinserter.esnlille.fr")
            .build();

    private interface AboutServiceInterface{
        @GET("/rest/getPath.php")
        void getAbout(@Query("code_section") String section, Callback<Abouts> callback);
    }

    public static About getAbout(final NetworkCallback<Abouts> callback) {
        try{
            initAbout(callback);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return about;
    }

    public static void initAbout(final NetworkCallback<Abouts> callback) throws ParseException{
        AboutServiceInterface aboutService = restAdapter.create(AboutServiceInterface.class);
        aboutService.getAbout(MobilITApplication.getSectionFromCache().getCode_section(), new Callback<Abouts>() {
            @Override
            public void success(Abouts abouts, Response response) {
                about = abouts.getAbout();
                callback.onSuccess(abouts);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}
