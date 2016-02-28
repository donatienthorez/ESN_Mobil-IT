package org.esn.mobilit.services;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;

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
        setDefaults("code_section", null);
        PreferencesService.setDefaults("code_country", null);
        setDefaults("section_website", null);
        setDefaults("regId", null);
    }
}
