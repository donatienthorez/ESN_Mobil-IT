package org.esn.mobilit.services;

import android.content.Context;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
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
public class AboutService implements CachableInterface {

    private Section section;

    Context context;
    @Inject
    CacheService cacheService;
    @Inject
    Utils utils;

    @Inject
    public AboutService(@ForApplication Context context) {
        this.context = context;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public Section getFromCache() {
        return (Section) cacheService.getObjectFromCache(this.getString());
    }

    public void setSectionToCache(Section section) {
        cacheService.saveObjectToCache(getString(), section);
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_SECTION;
    }

    public void getFromSite(final NetworkCallback<Section> callback) {
        final Section section = getFromCache();

        if (!utils.isConnected()) {
            callback.onFailure(context.getResources().getString(
                    R.string.info_message_no_network
            ));
            return;
        }

        if (section != null) {
            SectionProvider.makeRequest(section, new NetworkCallback<Section>() {
                @Override
                public void onSuccess(Section section) {
                    setSection(section);
                    setSectionToCache(section);
                    callback.onSuccess(section);
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
    }
}
