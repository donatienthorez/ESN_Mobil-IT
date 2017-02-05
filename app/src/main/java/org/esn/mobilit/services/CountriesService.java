package org.esn.mobilit.services;

import org.esn.mobilit.models.Country;
import org.esn.mobilit.network.providers.CountryProvider;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CountriesService {
    @Inject
    Utils utils;
    @Inject
    AppState appState;

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
                        public void onNoConnection(List<Country> countries) {
                        }

                        @Override
                        public void onSuccess(List<Country> result) {
                            appState.setCountryList(result);
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
            List<Country> cachedCountries = appState.getCountryList();

            if (cachedCountries == null || cachedCountries.size() == 0) {
                callback.onFailure("No network");
            } else {
                callback.onSuccess(cachedCountries);
            }
        }
    }
}

