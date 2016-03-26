package org.esn.mobilit.services;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.utils.ApplicationConstants;

public class PreferencesService {
    private static PreferencesService instance;

    private PreferencesService(){
    }

    public static PreferencesService getInstance(){
        if (instance == null){
            instance = new PreferencesService();
        }
        return instance;
    }

    public static void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MobilITApplication.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MobilITApplication.getContext());
        return preferences.getString(key, null);
    }

    public static void resetSection(){
        GuideService.getInstance().resetService();
        EventsService.getInstance().resetService();
        NewsService.getInstance().resetService();
        PartnersService.getInstance().resetService();
        setDefaults(ApplicationConstants.PREFERENCES_CODE_SECTION, null);
        setDefaults(ApplicationConstants.PREFERENCES_REG_ID, null);

    }
}
