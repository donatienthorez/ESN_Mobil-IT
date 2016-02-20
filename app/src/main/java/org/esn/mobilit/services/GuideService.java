package org.esn.mobilit.services;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.GuideProvider;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import retrofit.RetrofitError;

public class GuideService {
    public static void getGuide(Section section, final NetworkCallback<Guide> callback) {
        GuideProvider.makeGuideRequest(section, new NetworkCallback<Guide>() {
            @Override
            public void onSuccess(Guide guide) {
                FeedService.getInstance().setGuide(guide);
                CacheService.saveObjectToCache("survivalGuide", guide);
                callback.onSuccess(guide);
            }

            @Override
            public void onFailure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}
