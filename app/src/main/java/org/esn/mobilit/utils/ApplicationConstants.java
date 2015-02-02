package org.esn.mobilit.utils;

/**
 * Created by Spider on 26/01/15.
 */
public interface ApplicationConstants {

    // Php Application URL to store Reg ID created
    static final String APP_SERVER_URL = "http://esnlille.fr/survivalGuide/includes/rest/insertRegId.php";

    // Google Project Number
    static final String GOOGLE_PROJ_ID = "1079816997";

    // Message Key
    static final String MSG_KEY = "m";
    static final String SBJ_KEY = "sbj";

    // Path for news event & partners
    static final String NEWS_PATH = "/news";
    static final String PARTNERS_PATH = "/partners";
    static final String EVENTS_PATH = "/events";

    // Path for feed
    static final String FEED_PATH = "/feed";

    // Webservices
    // Survival Guide Webservice
    static final String SURVIVAL_WEBSERVICE_URL = "http://esnlille.fr/survivalGuide/json/";

    //Sections webservice
    static final String SECTIONS_WEBSERVICE_URL = "http://www.esnlille.fr/WebServices/Sections/";

    static final int RESULT_CLOSE_ALL = 666;
    static final int RESULT_FIRST_LAUNCH = 57;
    static final int RESULT_SPLASH_ACTIVITY = 420;
}
