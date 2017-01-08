package org.esn.mobilit.services;

import org.esn.mobilit.models.Notification;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;

import java.util.ArrayList;

public class NotificationService implements CachableInterface {

    @Override
    public String getString() {
        return ApplicationConstants.CACHE_NOTIFICATIONS;
    }

    private static NotificationService instance;

    private NotificationService(){
    }

    public static NotificationService getInstance(){
        if (instance == null){
            instance = new NotificationService();
        }
        return instance;
    }

    public ArrayList<Notification> getFromCache() {
        return  (ArrayList<Notification>) CacheService.getObjectFromCache(getString());
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
        CacheService.saveObjectToCache(getString(), notifications);
    }
}
