package org.esn.mobilit.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.esn.mobilit.fragments.ListTestFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    int count;

    public MyFragmentPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ListTestFragment();
            case 1:
                return new ListTestFragment();
            case 2:
                return new ListTestFragment();
            case 3:
                return new ListTestFragment();
            default:
                return new ListTestFragment();
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
