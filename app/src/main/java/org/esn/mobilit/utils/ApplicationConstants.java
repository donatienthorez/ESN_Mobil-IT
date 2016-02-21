package org.esn.mobilit.utils;

import android.graphics.Color;

public interface ApplicationConstants {

    // DESIGN
    int ESNBlueRGB = Color.rgb(34,157,235);

    // Php Application URL to store Reg ID created
    String APP_SERVER_URL = "http://esnlille.fr/survivalGuide/includes/rest/insertRegId.php";

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

    // Logo inserter API
    String LOGOINSERTER_URL   = "http://logoinserter.esnlille.fr/";
}
