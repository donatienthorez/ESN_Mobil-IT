package org.esn.mobilit.services.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.GcmBroadcastReceiver;
import org.esn.mobilit.R;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.models.Notification;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.NotificationService;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.services.feeds.RSSItemListHelper;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GcmIntentService extends IntentService {
    public static final int notifyID = 9001;

    @Inject
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Inject
    NotificationService notificationService;

    @Inject
    FeedService feedService;

    @Inject
    RSSItemListHelper rssItemListHelper;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (messageType == null) {
            return;
        }

        if (!extras.isEmpty()) {
            switch (messageType) {
                case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:

                    String title = extras.getString(ApplicationConstants.GCM_TITLE);
                    String content = extras.getString(ApplicationConstants.GCM_CONTENT);
                    String type = extras.getString(ApplicationConstants.GCM_TYPE);
                    String link = extras.getString(ApplicationConstants.GCM_LINK);

                    Notification notification = new Notification(title, content, type, link);

                    switch (notification.getType()) {
                        case ApplicationConstants.NOTIFICATION_TYPE_DRUPAL_NEWS:
                        case ApplicationConstants.NOTIFICATION_TYPE_NEWS_OLD:
                            downloadFeed(FeedType.NEWS, notification, notification.getDescription());
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_BO_NEWS:
                            downloadFeed(FeedType.NEWS, notification, notification.getLink());
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_DRUPAL_EVENTS:
                        case ApplicationConstants.NOTIFICATION_TYPE_EVENTS_OLD:
                            downloadFeed(FeedType.EVENTS, notification, notification.getDescription());
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_BO_EVENTS:
                            downloadFeed(FeedType.EVENTS, notification, notification.getLink());
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_DRUPAL_PARTNERS:
                        case ApplicationConstants.NOTIFICATION_TYPE_PARTNERS_OLD:
                            downloadFeed(FeedType.PARTNERS, notification, notification.getDescription());
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_BO_PARTNERS:
                            downloadFeed(FeedType.PARTNERS, notification, notification.getLink());
                            break;
                        default:
                            sendNotification(null, notification);

                    }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void downloadFeed(FeedType feedType, final Notification notification, final String searchItem) {
        feedService.getFromSite(feedType, new NetworkCallback<ArrayList<RSSItem>>() {
            @Override
            public void onNoConnection(ArrayList<RSSItem> arrayList) {
            }

            @Override
            public void onSuccess(ArrayList<RSSItem> result) {
                RSSItem rssItem = rssItemListHelper.getRSSItemFromTitle(searchItem, result);
                sendNotification(rssItem, notification);
            }

            @Override
            public void onNoAvailableData() {
                //TODO manage errors
            }

            @Override
            public void onFailure(String error) {
                //TODO manage errors
            }
        });
    }

    private void sendNotification(final RSSItem rssItem, Notification notification) {

        Intent resultIntent = new Intent(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (rssItem != null) {
            notification.setRssItem(rssItem);
        }
        //TODO put a value instead of an object (like ID to retrieve)
        resultIntent.putExtra(ApplicationConstants.GCM_NOTIFICATION, notification);

        notificationService.addToCache(notification);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getDescription())
                .setSmallIcon(R.drawable.ic_launcher_color)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setDefaults(android.app.Notification.DEFAULT_LIGHTS
                        | android.app.Notification.DEFAULT_VIBRATE
                        | android.app.Notification.DEFAULT_SOUND);

        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}
