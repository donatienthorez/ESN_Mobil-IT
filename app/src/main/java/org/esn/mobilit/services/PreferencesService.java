package org.esn.mobilit.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesService {

    @Inject
    CacheService cacheService;
    @Inject
    EventsService eventsService;
    @Inject
    NewsService newsService;
    @Inject
    GuideService guideService;

    Context context;

    @Inject
    public PreferencesService(@ForApplication Context context) {
        this.context = context;
    }

    public void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void resetSection(){
        guideService.resetService();
        eventsService.resetService();
        newsService.resetService();
        PartnersService.getInstance().resetService();
        setDefaults(ApplicationConstants.CACHE_DEFAULT_MENU, null);
        setDefaults(ApplicationConstants.PREFERENCES_CODE_SECTION, null);
        setDefaults(ApplicationConstants.PREFERENCES_REG_ID, null);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_EVENTS);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_PARTNERS);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_NEWS);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_GUIDE);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_DEFAULT_MENU);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_COUNTRY);
        cacheService.deleteObjectFromCache(ApplicationConstants.CACHE_SECTION);

    }
}
