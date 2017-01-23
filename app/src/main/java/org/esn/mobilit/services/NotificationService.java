package org.esn.mobilit.services;

import org.esn.mobilit.models.Notification;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationService implements CachableInterface {

    @Inject
    CacheService cacheService;

    @Inject
    public NotificationService() {
    }

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_NOTIFICATIONS;
    }

    public ArrayList<Notification> getFromCache() {
        return  (ArrayList<Notification>) cacheService.get(getString());
    }

    public void addToCache(Notification notification) {
        ArrayList<Notification> cacheNotifications = getFromCache();

        if (cacheNotifications == null) {
            cacheNotifications = new ArrayList<>();
        }

        cacheNotifications.add(0, notification);
        setToCache(cacheNotifications);
    }

    public void setToCache(ArrayList<Notification> notifications) {
        cacheService.save(getString(), notifications);
    }
}
