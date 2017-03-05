package org.esn.mobilit.services.navigation;

public enum NavigationUriType {

    FEED_LIST("feeds/{feedType}"),
    FEED_DETAILS("feed/{feedType}/{id}"),
    NOTIFICATIONS("notifications"),
    ABOUT("about"),
    GUIDE("guide");

    String type;
    int parametersNumber;

    NavigationUriType(String type) {
        this.type = type;
    }

    NavigationUriType(String type, int parametersNumber) {
        this.type = type;
        this.parametersNumber = parametersNumber;
    }
}
