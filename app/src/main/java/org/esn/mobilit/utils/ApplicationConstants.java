package org.esn.mobilit.utils;

import android.graphics.Color;

/**
 * Created by Spider on 26/01/15.
 */
public interface ApplicationConstants {

    // DESIGN
    static final int ESNBlueRGB = Color.rgb(34,157,235);

    // Php Application URL to store Reg ID created
    static final String APP_SERVER_URL = "http://esnlille.fr/survivalGuide/includes/rest/insertRegId.php";
    //static final String APP_SERVER_URL = "http://esnlille.fr/webservices/esnmobilit/rest/insertRegId.php";

    // Google Project Number
    static final String GOOGLE_PROJ_ID = "1079816997";

    // Message Key
    static final String MSG_KEY = "m";
    static final String SBJ_KEY = "sbj";

    // Path for news event & partners
    static final String FEED_PATH = "/feed";
    static final String NEWS_PATH = "/news";
    static final String PARTNERS_PATH = "/partners";
    static final String EVENTS_PATH = "/events";


    // Webservices
    // Survival Guide Webservice
    static final String SURVIVAL_WEBSERVICE_URL = "http://esnlille.fr/survivalGuide/json/";
    static final String APP_WEBSERVICE_URL      = "http://esnlille.fr/webservices/esnmobilit/";
    static final String LOGOINSERTER_URL   = "http://logoinserter.esnlille.fr/";

    static final int RESULT_CLOSE_ALL = 666;
    static final int RESULT_FIRST_LAUNCH = 57;
    static final int RESULT_SPLASH_ACTIVITY = 420;
}
