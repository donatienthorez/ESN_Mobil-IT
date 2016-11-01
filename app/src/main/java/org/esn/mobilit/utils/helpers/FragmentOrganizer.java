package org.esn.mobilit.utils.helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;

import org.esn.mobilit.R;

public abstract class FragmentOrganizer {

    protected FragmentManager fragmentManager;
    protected NavigationView navigationView;
    protected int defaultMenu;

    public FragmentOrganizer(FragmentManager fragmentManager, NavigationView navigationView, int defaultMenu) {
        this.fragmentManager = fragmentManager;
        this.navigationView = navigationView;
        this.defaultMenu = defaultMenu;
    }

    public abstract boolean handleBackNavigation();

    public Fragment loadFragment(String fragmentClass, boolean addToBackStack) {
        Fragment fragment = FragmentFactory.createFragmentFromString(fragmentClass);
        openFragment(fragment, addToBackStack);

        return fragment;
    }

    /**
     * Open the fragment
     *
     * @param fragment Fragment to load.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
    public void openFragment(Fragment fragment, boolean addToBackStack) {
//        if (isFragmentOpen(fragment)) {
//            return;
//        }

        String fragmentTag = createFragmentTag(fragment, true);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragmentTag);
        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag);
        }
        transaction.commit();
    }

    public Fragment getOpenFragment(){
        String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() -1).getName();
        return fragmentManager.findFragmentByTag(tag);
    }

    protected boolean isFragmentOpen(Fragment fragment){
        return isFragmentOpen(fragment, true);
    }

    protected boolean isFragmentOpen(Fragment fragment, boolean useArgs){
        String fragmentTag = createFragmentTag(fragment, useArgs);

        if (fragmentManager.getBackStackEntryCount() != 0)
        {
            String name = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();

            if (!useArgs) {
                name = name.substring(0, name.indexOf("-"));
            }

            return name.equals(fragmentTag);
        }
        return false;
    }

    protected String createFragmentTag(Fragment fragment, boolean addArgs) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fragment.getClass().getSimpleName());
        if(addArgs) {
            stringBuilder.append("-");
            if (fragment.getArguments() != null)
                stringBuilder.append(fragment.getArguments().toString());
        }
        return stringBuilder.toString();
    }

    public void clearBackStack()
    {
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }
}
