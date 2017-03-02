package org.esn.mobilit.services.navigation;

import java.util.ArrayList;

public enum NavigationUriType {

    FEED_LIST("feed_list/{feedType}", 1),
    FEED_DETAILS("feed/{feedType}/{id}", 2),
    NOTIFICATIONS("notifications"),
    ABOUT("about"),
    GUIDE("guide");

    String type;
    int parametersNumber;

    NavigationUriType(String type) {
        this.type = type;
        this.parametersNumber = 0;
    }

    NavigationUriType(String type, int parametersNumber) {
        this.type = type;
        this.parametersNumber = parametersNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParametersNumber() {
        return parametersNumber;
    }

    public void setParametersNumber(int parametersNumber) {
        this.parametersNumber = parametersNumber;
    }

    /**
     * Returns all the navigation uri types that has parameters.
     */
    public static ArrayList<NavigationUriType> getAllNavigationsUriTypesWithParameters() {
        return getAllNavigationsUriTypes(true);
    }

    /**
     * Returns all the navigation uri types that has no parameters.
     */
    public static ArrayList<NavigationUriType> getAllNavigationsUriTypesWithoutParameters() {
        return getAllNavigationsUriTypes(false);
    }

    /**
     * Returns all the navigation uri types that has or not parameters.
     *
     * If true, returns all navigation uri types that has parameters
     * If false, returns all navigation uri types that does not have parameters
     */
    private static ArrayList<NavigationUriType> getAllNavigationsUriTypes(boolean withParameters) {
        ArrayList<NavigationUriType> navigationUriTypes = new ArrayList<>(NavigationUriType.values().length);
        for (NavigationUriType navigationUriType : NavigationUriType.values()) {
            if (withParameters && navigationUriType.getParametersNumber() > 0) {
                navigationUriTypes.add(navigationUriType);
            }
            if (!withParameters && navigationUriType.getParametersNumber() == 0) {
                navigationUriTypes.add(navigationUriType);
            }
        }
        return navigationUriTypes;
    }
}
