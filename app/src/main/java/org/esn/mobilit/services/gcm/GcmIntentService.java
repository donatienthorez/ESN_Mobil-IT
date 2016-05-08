package org.esn.mobilit.services.gcm;

import android.app.IntentService;
import android.app.Notification;
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
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.feeds.RSSFeedService;
import org.esn.mobilit.services.interfaces.GCMServiceInterface;
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
        Object title = extras.get(ApplicationConstants.GCM_TITLE);
        String titleString = title != null ? String.valueOf(title) : "";
        Object content = extras.get(ApplicationConstants.GCM_CONTENT);
        String contentString = content != null ? String.valueOf(content) : "";
        Object type = extras.get(ApplicationConstants.GCM_TYPE);
        String typeString = type != null ? String.valueOf(type) : "";

        if (!extras.isEmpty()) {
            switch (messageType) {
                case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:
                    downloadFromSite(titleString, contentString, typeString);
                    break;
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void downloadFromSite(final String title, final String content, final String type) {
        RSSFeedService service = null;
        switch (type) {
            case ApplicationConstants.CACHE_NEWS:
                service = NewsService.getInstance();
                break;
            case ApplicationConstants.CACHE_EVENTS:
                service = EventsService.getInstance();
                break;
            case ApplicationConstants.CACHE_PARTNERS:
                service = PartnersService.getInstance();
                break;
            default:
                sendNotification(null, title, content, null);
        }

        if (service != null) {
            service.getFromSite(new NetworkCallback<RSSFeedParser>() {
                @Override
                public void onSuccess(RSSFeedParser result) {
                    RSSItem rssItem = result.getRSSItemFromTitle(content);
                    if (rssItem != null) {
                        sendNotification(
                                rssItem,
                                title,
                                content,
                                type
                        );
                    } else {
                        sendNotification(null, title, content, null);
                    }
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
    }

    private void sendNotification(RSSItem rssItem, String title, String content, String type) {

        Intent resultIntent = new Intent(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (rssItem != null) {
            resultIntent.putExtra(ApplicationConstants.GCM_RSS_ITEM, rssItem);
        }

        if (type != null) {
            resultIntent.putExtra(ApplicationConstants.GCM_TYPE, type);
        }

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_color)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_SOUND);

        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}
