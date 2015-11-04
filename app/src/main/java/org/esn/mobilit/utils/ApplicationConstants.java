package org.esn.mobilit.utils;

import android.graphics.Color;

public interface ApplicationConstants {

    // DESIGN
    int ESNBlueRGB = Color.rgb(34,157,235);

    // Php Application URL to store Reg ID created
    String APP_SERVER_URL = "http://esnlille.fr/survivalGuide/includes/rest/insertRegId.php";
    //String APP_SERVER_URL = "http://esnlille.fr/webservices/esnmobilit/rest/insertRegId.php";

    // Google Project Number
    String GOOGLE_PROJ_ID = "1079816997";

    // Message Key
    String MSG_KEY = "m";
    String SBJ_KEY = "sbj";

    // Path for news event & partners
    String FEED_PATH = "/feed";
    String NEWS_PATH = "/news";
    String PARTNERS_PATH = "/partners";
    String EVENTS_PATH = "/events";

    // Webservices
    // Survival Guide Webservice
    String SURVIVAL_WEBSERVICE_URL = "http://esnlille.fr/survivalGuide/json/";
    String APP_WEBSERVICE_URL      = "http://esnlille.fr/webservices/esnmobilit/";
    String LOGOINSERTER_URL   = "http://logoinserter.esnlille.fr/";

    int RESULT_CLOSE_ALL = 666;
    int RESULT_FIRST_LAUNCH = 57;
    int RESULT_SPLASH_ACTIVITY = 420;
}
