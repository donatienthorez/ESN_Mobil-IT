package org.esn.mobilit.services;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.network.providers.CountryProvider;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.List;

public class CountriesService implements CachableInterface {

    private static CountriesService instance;

    private CountriesService(){
    }

    public static CountriesService getInstance(){
        if (instance == null){
            instance = new CountriesService();
        }
        return instance;
    }

    public static void getCountries(final NetworkCallback<List<Country>> callback) {
        if (Utils.isConnected()) {
            Thread thread = (new Thread() {
                @Override
                public void run() {
                    CountryProvider.makeCountriesRequest(new NetworkCallback<List<Country>>() {
                        @Override
                        public void onSuccess(List<Country> result) {
                            CacheService.saveObjectToCache(getInstance().getString(), result);
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
            List<Country> cachedCountries = (List<Country>) CacheService.getObjectFromCache(getInstance().getString());

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

