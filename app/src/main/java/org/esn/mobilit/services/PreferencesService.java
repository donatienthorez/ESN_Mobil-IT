package org.esn.mobilit.services;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.esn.mobilit.MobilITApplication;

public class PreferencesService {
    private static PreferencesService instance;

    private PreferencesService(){
        instance = new PreferencesService();
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
        setDefaults("code_section", null);
        PreferencesService.setDefaults("code_country", null);
        setDefaults("section_website", null);
        setDefaults("regId", null);
    }
}
