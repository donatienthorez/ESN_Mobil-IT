package org.esn.mobilit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Satellite.ListFragment;
import org.esn.mobilit.fragments.Survival.SurvivalFragment;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;

public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    ArrayList<Fragment> fragmentsList;
    FeedService feedService;
    RSSFeedParser feedEvents, feedNews, feedPartners;
    Guide guide;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentsList = new ArrayList<Fragment>();
        this.feedService = FeedService.getInstance();
        this.feedEvents = feedService.getFeedEvents();
        this.feedNews = feedService.getFeedNews();
        this.feedPartners = feedService.getFeedPartners();
        this.guide = feedService.getGuide();
        this.setTabsList();
    }

    public void setTabsList(){
        if (feedEvents != null && feedEvents.getItemCount() > 0) {
            ListFragment events = new ListFragment();
            events.setFeed(feedEvents);
            fragmentsList.add(events);
        }

        if (feedNews != null && feedNews.getItemCount() > 0) {
            ListFragment news = new ListFragment();
            news.setFeed(feedNews);
            fragmentsList.add(news);
        }

        if (feedPartners != null && feedPartners.getItemCount() > 0 ) {
            //Partners
            ListFragment partners = new ListFragment();
            partners.setFeed(feedPartners);
            fragmentsList.add(partners);
        }

        if (guide != null && guide.getNodes().size() > 0 ) {
            //Survival Guide
            SurvivalFragment survival = new SurvivalFragment();

            fragmentsList.add(survival);
        }

        if (CacheService.getObjectFromCache("section") != null) {
            //About Page
            AboutFragment about = new AboutFragment();
            fragmentsList.add(about);
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
