package org.esn.mobilit.services;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.GuideProvider;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import retrofit.RetrofitError;

public class GuideService implements Cachable, Launchable<Guide> {
    private static GuideService instance;
    public static final String GUIDE = "guide";

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
        return GUIDE;
    }

    @Override
    public void doAction(final NetworkCallback<Guide> callback) {
        final Section section = (Section) CacheService.getObjectFromCache("section");

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
            Guide g = (Guide) CacheService.getObjectFromCache(getString());

            if (g != null) {
                callback.onSuccess(g);
            } else {
                callback.onNoAvailableData();
            }
        }
    }
}
