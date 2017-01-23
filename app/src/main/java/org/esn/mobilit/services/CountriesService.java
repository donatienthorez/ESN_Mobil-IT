package org.esn.mobilit.services;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.network.providers.CountryProvider;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CountriesService implements CachableInterface {
    @Inject
    CacheService cacheService;
    @Inject
    Utils utils;

    @Inject
    public CountriesService(){
    }

    public void getCountries(final NetworkCallback<List<Country>> callback) {
        if (utils.isConnected()) {
            Thread thread = (new Thread() {
                @Override
                public void run() {
                    CountryProvider.makeCountriesRequest(new NetworkCallback<List<Country>>() {
                        @Override
                        public void onSuccess(List<Country> result) {
                            cacheService.save(getString(), result);
                            callback.onSuccess(result);
                        }

                        @Override
                        public void onNoAvailableData() {
                            callback.onNoAvailableData();
                        }

                        @Override
                        public void onFailure(String error) {
                            callback.onFailure(error);
                        }
                    });
                }
            });
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();
        } else {
            List<Country> cachedCountries = (List<Country>) cacheService.get(getString());

            if (cachedCountries == null || cachedCountries.size() == 0) {
                callback.onFailure("No network");
            } else {
                callback.onSuccess(cachedCountries);
            }
        }
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_COUNTRIES;
    }
}

