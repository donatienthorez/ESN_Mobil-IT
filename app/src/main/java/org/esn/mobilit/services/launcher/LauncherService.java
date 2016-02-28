package org.esn.mobilit.services.launcher;


import org.esn.mobilit.R;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.services.launcher.enums.LauncherStatus;
import org.esn.mobilit.utils.callbacks.LoadingCallback;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.RetrofitError;

public class LauncherService {

    private static LauncherService instance;

    HashMap<String, LauncherStatus> feedsStatus;

    private LauncherService(){
        feedsStatus = new HashMap<String, LauncherStatus>();
    }

    public static LauncherService getInstance(){
        if (instance == null){
            instance = new LauncherService();
        }
        return instance;
    }

    public void reset() {
        instance = new LauncherService();
    }

    public void addStatus(String s, LauncherStatus status) {
        feedsStatus.put(s, status);
    }

    public void launchHomeActivity(LoadingCallback callback){
        boolean downloadedOnce = false;
        for (LauncherStatus status : feedsStatus.values()) {
            switch (status) {
                case PENDING:
                    return;
                case FINISHED:
                    downloadedOnce = true;
                    break;
            }
        }
        if (downloadedOnce) {
            callback.onSuccess();
        } else {
            callback.onFailure();
        }
    }

    public void launchServices(final ArrayList<Launchable> arrayList, final LoadingCallback callback) {
        for (final Launchable feedService : arrayList) {
            addStatus(feedService.getString(), LauncherStatus.PENDING);

            new Thread(new Runnable() {
                public void run() {
                    feedService.doAction(new NetworkCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            callback.onProgress(R.string.load_events_end);
                            addStatus(feedService.getString(), LauncherStatus.FINISHED);
                            launchHomeActivity(callback);
                        }

                        @Override
                        public void onNoAvailableData() {
                            callback.onProgress(R.string.emptycache);
                            addStatus(feedService.getString(), LauncherStatus.FINISHED);
                            launchHomeActivity(callback);
                        }

                        @Override
                        public void onFailure(String error) {
                            callback.onProgress(R.string.emptycache);
                            addStatus(feedService.getString(), LauncherStatus.ERROR);
                            launchHomeActivity(callback);
                        }
                    });
                }
            }).start();
        }
    }
}
