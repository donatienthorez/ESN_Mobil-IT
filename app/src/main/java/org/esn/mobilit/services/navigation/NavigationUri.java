package org.esn.mobilit.services.navigation;

import android.os.Bundle;

import org.esn.mobilit.services.navigation.NavigationUriType;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * NavigationUris available :
 *
 * FEED_LIST         : feed_list/{feedType}
 * FEED_NEWS_DETAILS : feed_details/{feedType}/{id}
 * ABOUT             : about
 * GUIDE             : guide
 */
public class NavigationUri {

    private NavigationUriType navigationUriType;
    private Bundle bundle;

    public NavigationUri() {}
    public NavigationUri(NavigationUriType navigationUriType) {
        this.navigationUriType = navigationUriType;
    }
    public NavigationUri(NavigationUriType navigationUriType, Bundle bundle) {
        this.navigationUriType = navigationUriType;
        this.bundle = bundle;
    }

    public NavigationUriType getNavigationUriType() {
        return navigationUriType;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
