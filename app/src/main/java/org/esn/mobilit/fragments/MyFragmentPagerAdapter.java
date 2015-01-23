package org.esn.mobilit.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.esn.mobilit.utils.parser.RSSFeed;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    int count;
    RSSFeed feedEvents, feedNews, feedPartners;

    public MyFragmentPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    public void setFeedEvents(RSSFeed feedEvents){
        Log.d(MyFragmentPagerAdapter.class.getSimpleName(), "Events feed count : " + feedEvents.getItemCount());
        this.feedEvents = feedEvents;
    }
    public void setFeedNews(RSSFeed feedNews){
        Log.d(MyFragmentPagerAdapter.class.getSimpleName(), "News feed count : " + feedNews.getItemCount());
        this.feedNews = feedNews;
    }
    public void setFeedPartners(RSSFeed feedPartners){
        Log.d(MyFragmentPagerAdapter.class.getSimpleName(), "Partners feed count : " + feedPartners.getItemCount());
        this.feedPartners = feedPartners;
    }

    @Override
    public Fragment getItem(int position) {
        //Init Fragment
        //Events
        ListFragment events = new ListFragment(); events.setFeed(feedEvents); events.setType(0);


        //News
        ListFragment news = new ListFragment(); news.setFeed(feedNews); events.setType(1);

        //Partners
        ListFragment partners = new ListFragment(); partners.setFeed(feedPartners); events.setType(2);

        switch (position) {
            case 0: //Events
                return events;
            case 1: //News
                return news;
            case 2: //Partners
                return partners;
            case 3: //Survival Guide
                return new ListTestFragment();
            default: //Events
                return events;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
