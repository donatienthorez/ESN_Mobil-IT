package org.esn.mobilit.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesService {

    @Inject
    CacheService cacheService;
    @Inject
    GuideService guideService;

    @ForApplication
    @Inject
    Context context;

    @Inject
    public PreferencesService() {
        InjectUtil.component().inject(this);
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
        setDefaults(ApplicationConstants.CACHE_DEFAULT_MENU, null);
        setDefaults(ApplicationConstants.PREFERENCES_CODE_SECTION, null);
        setDefaults(ApplicationConstants.PREFERENCES_REG_ID, null);
        cacheService.delete(ApplicationConstants.CACHE_EVENTS);
        cacheService.delete(ApplicationConstants.CACHE_PARTNERS);
        cacheService.delete(ApplicationConstants.CACHE_NEWS);
        cacheService.delete(ApplicationConstants.CACHE_GUIDE);
        cacheService.delete(ApplicationConstants.CACHE_DEFAULT_MENU);
        cacheService.delete(ApplicationConstants.CACHE_COUNTRY);
        cacheService.delete(ApplicationConstants.CACHE_SECTION);

    }
}
