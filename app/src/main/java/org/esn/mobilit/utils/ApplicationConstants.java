package org.esn.mobilit.utils;

import android.graphics.Color;

public interface ApplicationConstants {

    // DESIGN
    int ESNBlueRGB = Color.rgb(34,157,235);

    // Google Project Number
    String GOOGLE_PROJECT_ID = "1079816997";

    // GCM Messages
    String GCM_TITLE = "title";
    String GCM_CONTENT = "content";
    String GCM_TYPE = "type";
    String GCM_RSS_ITEM = "rssItem";
    String GCM_NOTIFICATION = "notification";

    // Path for news event & partners
    String FEED_PATH = "/feed";
    String NEWS_PATH = "/news";
    String PARTNERS_PATH = "/partners";
    String EVENTS_PATH = "/events";

    // Path of Back-Office API
    String API_ENDPOINT = "http://dev.mobilit.esnlille.fr/api/android/v1";
    String API_COUNTRIES = "/countries/";
    String API_GUIDE = "/guides/{section}";
    String API_REGIDS = "/regids/";
    String API_SECTIONS = "/sections/{section}";

    // Preferences and cache
    String PREFERENCES_CODE_SECTION = "codeSection";
    String PREFERENCES_REG_ID = "regId";

    String CACHE_COUNTRY = "country";
    String CACHE_SECTION = "section";

    String CACHE_GUIDE = "guide";
    String CACHE_EVENTS = "events";
    String CACHE_PARTNERS = "partners";
    String CACHE_NEWS = "news";
    String CACHE_NOTIFICATIONS = "notifications";

    String NOTIFICATION_TYPE_EVENTS = "events";
    String NOTIFICATION_TYPE_PARTNERS = "partners";
    String NOTIFICATION_TYPE_NEWS = "news";
    String NOTIFICATION_TYPE_GUIDE = "guide";
    String NOTIFICATION_TYPE_TEXT = "text";
    String NOTIFICATION_TYPE_LINK = "link";

    String CACHE_DEFAULT_MENU = "defaultMenu";

    // Menu
    String MENU_NEWS = "news";
    String MENU_EVENTS = "events";
    String MENU_PARTNERS = "partners";
    String MENU_GUIDE = "guide";
    String MENU_ABOUT = "about";
    String MENU_NOTIFICATIONS = "notifications";

    String CACHE_COUNTRIES = "countries";

    String MOBILIT_TOKEN = "g6g2A52mGPPbKaaHmFjz";

}
