package org.esn.mobilit.services;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.GuideProvider;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GuideService implements Cachable, Launchable<Guide> {
    private static GuideService instance;

    private Guide guide;

    private GuideService(){
    }

    public static GuideService getInstance() {
        if (instance == null){
            instance = new GuideService();
        }
        return instance;
    }

    public void resetService() {
        instance = new GuideService();
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public Guide getGuide() {
        return guide;
    }

    public String getString() {
        return ApplicationConstants.CACHE_GUIDE;
    }

    @Override
    public void doAction(final NetworkCallback<Guide> callback) {
        final Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        if (Utils.isConnected()){
            GuideProvider.makeGuideRequest(section, new NetworkCallback<Guide>() {
                @Override
                public void onSuccess(Guide guide) {
                    setGuide(guide);
                    CacheService.saveObjectToCache(getString(), guide);
                    callback.onSuccess(guide);
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
            Guide guide = (Guide) CacheService.getObjectFromCache(getString());
            if (guide != null) {
                setGuide(guide);

                callback.onSuccess(guide);
            } else {
                callback.onNoAvailableData();
            }
        }
    }
}
