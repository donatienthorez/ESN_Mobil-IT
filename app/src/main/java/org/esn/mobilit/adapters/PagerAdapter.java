package org.esn.mobilit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Satellite.ListFragment;
import org.esn.mobilit.fragments.Survival.GuideFragment;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.feeds.RSSFeedService;

import java.util.ArrayList;

public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    ArrayList<Fragment> fragmentsList;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentsList = new ArrayList<Fragment>();
        this.setTabsList();
    }

    public void setTabsList()
    {
        addToFragmentsList(EventsService.getInstance());
        addToFragmentsList(NewsService.getInstance());
        addToFragmentsList(PartnersService.getInstance());
        fragmentsList.add(new GuideFragment());
        fragmentsList.add(new AboutFragment());
    }

    public void addToFragmentsList(RSSFeedService rssFeedService)
    {
        ListFragment listFragment = new ListFragment();
        listFragment.setService(rssFeedService);
        fragmentsList.add(listFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
