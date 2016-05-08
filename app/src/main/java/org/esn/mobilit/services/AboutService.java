package org.esn.mobilit.services;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.SectionProvider;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class AboutService implements CachableInterface {
    private static AboutService instance;

    private Section section;

    private AboutService(){
    }

    public static AboutService getInstance() {
        if (instance == null){
            instance = new AboutService();
        }
        return instance;
    }

    public void resetService() {
        instance = new AboutService();
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public Section getFromCache() {
        return (Section) CacheService.getObjectFromCache(this.getString());
    }

    public void setSectionToCache(Section section) {
        CacheService.saveObjectToCache(getString(), section);
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_SECTION;
    }

    public void getFromSite(final NetworkCallback<Section> callback) {
        final Section section = getFromCache();

        if (!Utils.isConnected()) {
            callback.onFailure(MobilITApplication.getContext().getResources().getString(
                    R.string.info_message_no_network
            ));
            return;
        }

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
