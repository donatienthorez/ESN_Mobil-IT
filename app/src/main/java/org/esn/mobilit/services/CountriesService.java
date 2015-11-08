package org.esn.mobilit.services;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.models.Countries;
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

public class CountriesService {

    private static CountriesService instance;
    private static Countries countries;

    private CountriesService(){
        instance = new CountriesService();
    }

    public static CountriesService getInstance(){
        return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://esnlille.fr")
            .build();

    private interface CountriesServiceInterface{
        @GET("/webservices/esnmobilit/json/{revision}.json")
        void getCountries(@Path("revision") String revision, Callback<Countries> callback);
    }

    public static Countries getCountries(){
        getCountries(null);
        return countries;
    }

    public static void getCountries(final NetworkCallback<Countries> callback) {
        RevisionService.getRevision(new NetworkCallback<String>() {
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
        Date revisionDate = DateConvertor.getDate(RevisionService.getRevision(), "yyyyMMddHHmmss");
        String prefDateString = Utils.getDefaults("revision");
        if (prefDateString!=null) {
            Date prefDate = DateConvertor.getDate(Utils.getDefaults("revision"), "yyyyMMddHHmmss");
            Countries cacheCountries = (Countries) Utils.getObjectFromCache("countries");
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
        CountriesServiceInterface countriesService = restAdapter.create(CountriesServiceInterface.class);
        countriesService.getCountries(RevisionService.getRevision(), new Callback<Countries>() {
            @Override
            public void success(Countries result, Response response) {
                countries = result;
                Utils.saveObjectToCache("countries", countries);
                Utils.setDefaults("revision", countries.getRevision());
                if(callback != null) {
                    callback.onSuccess(countries);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(callback != null) {
                    callback.onFailure(error);
                }
            }
        });
    }

}
