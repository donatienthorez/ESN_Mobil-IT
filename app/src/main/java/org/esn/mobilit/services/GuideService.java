package org.esn.mobilit.services;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.GuideProvider;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GuideService implements Cachable {
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

    public Guide getFromCache() {
        return (Guide) CacheService.getObjectFromCache(getString());
    }

    public void setGuideToCache(Guide guide) {
        CacheService.saveObjectToCache(getString(), guide);
    }

    public void getFromSite(final NetworkCallback<Guide> callback) {
        final Section section = AboutService.getInstance().getFromCache();
        if (!Utils.isConnected()) {
            callback.onFailure(MobilITApplication.getContext().getResources().getString(
                    R.string.info_message_no_network
            ));
        }

        GuideProvider.makeGuideRequest(section, new NetworkCallback<Guide>() {
            @Override
            public void onSuccess(Guide guide) {
                setGuide(guide);
                setGuideToCache(guide);
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
    }
}
