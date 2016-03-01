package org.esn.mobilit.services;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.GuideProvider;
import org.esn.mobilit.network.providers.SectionProvider;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class AboutService implements Cachable, Launchable<Section> {
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

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_SECTION;
    }

    @Override
    public void doAction(final NetworkCallback<Section> callback) {
        final Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        if (Utils.isConnected()){
            SectionProvider.makeRequest(section, new NetworkCallback<Section>() {
                @Override
                public void onSuccess(Section section) {
                    setSection(section);
                    CacheService.saveObjectToCache(getString(), section);
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
        } else {
            if (section != null) {
                callback.onSuccess(section);
            } else {
                callback.onNoAvailableData();
            }
        }
    }
}
