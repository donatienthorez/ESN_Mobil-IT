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
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.feeds.RSSFeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class GcmIntentService extends IntentService {
    public static final int notifyID = 9001;

    public GcmIntentService() {
        super("GcmIntentService");
    }

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

                    Notification notification = new Notification(title, content, type);
                    NotificationService.getInstance().addToCache(notification);

                    switch (notification.getType()) {
                        case ApplicationConstants.NOTIFICATION_TYPE_NEWS:
                            downloadFeed(NewsService.getInstance(), notification);
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_EVENTS:
                            downloadFeed(EventsService.getInstance(), notification);
                            break;
                        case ApplicationConstants.NOTIFICATION_TYPE_PARTNERS:
                            downloadFeed(PartnersService.getInstance(), notification);
                            break;
                        default:
                            sendNotification(null, notification);

                    }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void downloadFeed(RSSFeedService feedService, final Notification notification) {
        feedService.getFromSite(new NetworkCallback<RSSFeedParser>() {
            @Override
            public void onSuccess(RSSFeedParser result) {
                RSSItem rssItem = result.getRSSItemFromTitle(notification.getDescription());
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

    private void sendNotification(RSSItem rssItem, Notification notification) {

        Intent resultIntent = new Intent(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (rssItem != null) {
            resultIntent.putExtra(ApplicationConstants.GCM_RSS_ITEM, rssItem);
        }
        resultIntent.putExtra(ApplicationConstants.GCM_NOTIFICATION, notification);

        if (notification.getType() != null) {
            resultIntent.putExtra(ApplicationConstants.GCM_TYPE, notification.getType());
        }

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
