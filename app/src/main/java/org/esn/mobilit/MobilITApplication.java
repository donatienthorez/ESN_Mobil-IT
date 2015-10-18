package org.esn.mobilit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.esn.mobilit.models.Countries;
import org.esn.mobilit.models.RevisionList;
import org.esn.mobilit.utils.DateConvertor;
import org.esn.mobilit.utils.Utils;

import java.text.ParseException;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public class MobilITApplication extends Application {

    private static Context context;
    private static String revision;
    private static Countries countries;

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://esnlille.fr")
            .build();

    public interface RevisionService {
        @GET("/webservices/esnmobilit/getRevision.php")
        void revision(Callback<RevisionList> callback);
    }

    public interface CountriesService{
        @GET("/webservices/esnmobilit//json/{revision}.json")
        void getCountries(@Path("revision") String revision, Callback<Countries> callback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public static void getRevision(final NetworkCallback<String> callback) {
        if (revision != null){
            callback.onSuccess(revision);
        } else {
            makeRevisionRequest(callback);
        }
    }

    public static void makeRevisionRequest(final NetworkCallback<String> callback){
        RevisionService service = restAdapter.create(RevisionService.class);
        service.revision(new Callback<RevisionList>() {
            @Override
            public void success(RevisionList revisionList, Response response) {
                revision = revisionList.getRevision(0).getDate();
                callback.onSuccess(revision);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }

    public static void getCountries(final NetworkCallback<Countries> callback) {
        getRevision(new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    initCountries(callback);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(RetrofitError error) {

            }
        });
    }

    public static void initCountries(NetworkCallback<Countries> callback) throws ParseException{
        Date revisionDate = DateConvertor.getDate(revision, "yyyyMMddHHmmss");
        String prefDateString = Utils.getDefaults(MobilITApplication.getContext(), "revision");
        if (prefDateString!=null) {
            Date prefDate = DateConvertor.getDate(Utils.getDefaults(MobilITApplication.getContext(), "revision"), "yyyyMMddHHmmss");
            Countries cacheCountries = (Countries) Utils.getObjectFromCache(getContext(), "countries");
            if (cacheCountries == null || prefDate.compareTo(revisionDate) < 0) {
                makeCountriesRequest(callback);
            } else {
                countries = cacheCountries;
                callback.onSuccess(countries);
            }
        } else {
            makeCountriesRequest(callback);
        }
    }

    public static void makeCountriesRequest(final NetworkCallback<Countries> callback){
        CountriesService countriesService = restAdapter.create(CountriesService.class);
        countriesService.getCountries(revision, new Callback<Countries>() {
            @Override
            public void success(Countries result, Response response) {
                countries = result;
                Utils.saveObjectToCache(getContext(), "countries", countries);
                Utils.setDefaults(getContext(), "revision", countries.getRevision());
                callback.onSuccess(countries);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}

