package org.esn.mobilit.services;

import android.content.Context;

import org.esn.mobilit.R;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.GuideProvider;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GuideService implements CachableInterface {

    @Inject
    CacheService cacheService;
    @Inject
    AboutService aboutService;
    @Inject
    Utils utils;

    Context context;

    private Guide guide;

    @Inject
    public GuideService(@ForApplication Context context) {
        this.context = context;
    }

    public void resetService() {
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
        return (Guide) cacheService.get(getString());
    }

    private void setGuideToCache(Guide guide) {
        cacheService.save(getString(), guide);
    }

    public void getFromSite(final NetworkCallback<Guide> callback) {
        final Section section = aboutService.getFromCache();
        if (!utils.isConnected()) {
            callback.onFailure(context.getResources().getString(
                    R.string.info_message_no_network
            ));
        }

        GuideProvider.makeGuideRequest(section, new NetworkCallback<Guide>() {
            @Override
            public void onNoConnection(Guide guide) {
                //FIXME
            }

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
