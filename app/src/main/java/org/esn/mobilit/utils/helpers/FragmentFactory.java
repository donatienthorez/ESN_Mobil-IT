package org.esn.mobilit.utils.helpers;

import android.app.Fragment;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.fragments.NotificationFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.utils.ApplicationConstants;

public class FragmentFactory {

    public static Fragment createFragmentFromString(String fragmentClass) {
        switch (fragmentClass) {
            case ApplicationConstants.MENU_NEWS :
            case ApplicationConstants.MENU_EVENTS :
            case ApplicationConstants.MENU_PARTNERS :
                return new FeedListFragment();

            case ApplicationConstants.MENU_GUIDE :
                return new GuideFragment();

            case ApplicationConstants.MENU_ABOUT :
                return new AboutFragment();

            case ApplicationConstants.MENU_NOTIFICATIONS:
                return new NotificationFragment();

            case ApplicationConstants.FRAGMENT_FEED_DETAILS:
                return new DetailsFragment();

            case ApplicationConstants.FRAGMENT_GUIDE:
                return new GuideFragment();
        }
        return null;
    }
}
