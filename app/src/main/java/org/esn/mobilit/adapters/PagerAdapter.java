package org.esn.mobilit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Satellite.ListFragment;
import org.esn.mobilit.fragments.Survival.SurvivalFragment;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.utils.parser.RSSFeedParser;

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
        Guide guide = GuideService.getInstance().getGuide();
        RSSFeedParser feedsEvents = EventsService.getInstance().getFeed();
        RSSFeedParser feedsPartners = PartnersService.getInstance().getFeed();
        RSSFeedParser feedsNews = NewsService.getInstance().getFeed();

        addToFragmentsList(feedsEvents);
        addToFragmentsList(feedsNews);
        addToFragmentsList(feedsPartners);

        if (guide != null && guide.getNodes() != null && guide.getNodes().size() > 0) {
            SurvivalFragment survival = new SurvivalFragment();
            fragmentsList.add(survival);
        }

        AboutFragment about = new AboutFragment();
        fragmentsList.add(about);
    }

    public void addToFragmentsList(RSSFeedParser feed)
    {
        if (feed != null && feed.getItemCount() > 0) {
            ListFragment listFragment = new ListFragment();
            listFragment.setFeed(feed);
            fragmentsList.add(listFragment);
        }
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
