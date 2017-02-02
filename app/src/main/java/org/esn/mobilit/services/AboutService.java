package org.esn.mobilit.services;

import android.content.Context;

import org.esn.mobilit.R;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.SectionProvider;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AboutService {

    @ForApplication
    Context context;
    @Inject
    CacheService cacheService;
    @Inject
    Utils utils;
    @Inject
    AppState appState;

    @Inject
    public AboutService() {
    }

    public void getFromSite(final NetworkCallback<Section> callback) {
        Section section = appState.getSection();

        if (!utils.isConnected()) {
            if (callback != null) {
                callback.onNoConnection(section);
            }
            return;
        }

        if (section != null) {
            SectionProvider.makeRequest(section, new NetworkCallback<Section>() {
                @Override
                public void onNoConnection(Section section) {
                    //FIXME
                }

                @Override
                public void onSuccess(Section section) {
                    appState.setSection(section);
                    if (callback != null) {
                        callback.onSuccess(section);
                    }
                }

                @Override
                public void onNoAvailableData() {
                    if (callback != null) {
                        callback.onNoAvailableData();
                    }
                }

                @Override
                public void onFailure(String error) {
                    if (callback != null) {
                        callback.onFailure(error);
                    }
                }
            });
        }
    }
}
