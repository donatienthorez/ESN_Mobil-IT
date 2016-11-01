package org.esn.mobilit.utils.helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;

import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.ApplicationConstants;

public class HomeFragmentOrganizer extends FragmentOrganizer{

    public HomeFragmentOrganizer(FragmentManager fragmentManager, NavigationView navigationView, int defaultMenu) {
        super(fragmentManager, navigationView, defaultMenu);
    }

    @Override
    public boolean handleBackNavigation() {
        Fragment fragment = getOpenFragment();

        if (fragment instanceof FeedListFragment){
            return false;
        } else {
            fragmentManager.popBackStack();
            return true;
        }
    }
}
