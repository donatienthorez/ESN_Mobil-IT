package org.esn.mobilit.services;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.network.providers.CountryProvider;

import java.util.List;

import retrofit.RetrofitError;

public class CountriesService {

    private static CountriesService instance;

    private CountriesService(){
        instance = new CountriesService();
    }

    public static CountriesService getInstance(){
        if (instance == null){
            instance = new CountriesService();
        }
        return instance;
    }

    public static void getCountries(final NetworkCallback<List<Country>> callback) {
        if (CacheService.getObjectFromCache("countries") == null) {
            CountryProvider.makeCountriesRequest(new NetworkCallback<List<Country>>() {
                @Override
                public void onSuccess(List<Country> result) {
                    CacheService.saveObjectToCache("countries", result);
                    callback.onSuccess(result);
                }

                @Override
                public void onFailure(RetrofitError error) {
                    callback.onFailure(error);
                }
            });
        } else {
            callback.onSuccess((List<Country>) CacheService.getObjectFromCache("countries"));
        }
    }
}
