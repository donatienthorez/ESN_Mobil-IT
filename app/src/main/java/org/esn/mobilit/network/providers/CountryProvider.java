package org.esn.mobilit.network.providers;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;


public class CountryProvider {

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://mobilit.esnlille.fr/api/android/v1")
            .build();

    private interface CountriesProviderInterface{
        @GET("/countries/")
        void getCountries(@Query("token") String token, Callback<List<Country>> callback);
    }

    public static void makeCountriesRequest(final NetworkCallback<List<Country>> callback) {
        CountriesProviderInterface countriesService = restAdapter.create(CountriesProviderInterface.class);
        countriesService.getCountries(ApplicationConstants.MOBILIT_TOKEN, new Callback<List<Country>>() {
            @Override
            public void success(List<Country> result, Response response) {
                callback.onSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }
}
