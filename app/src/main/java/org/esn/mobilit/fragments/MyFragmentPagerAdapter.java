package org.esn.mobilit.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.esn.mobilit.fragments.Satellite.ListFragment;
import org.esn.mobilit.fragments.Survival.SurvivalFragment;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    int count;
    RSSFeed feedEvents, feedNews, feedPartners;
    SurvivalGuide survivalGuide;
    ArrayList<String> tabs;
    Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, int count, Context context) {
        super(fm);

        this.count   = count;
        this.tabs    = new ArrayList<String>();
        this.context = context;
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
    public void setSurvivalGuide(SurvivalGuide survivalGuide){
        Log.d(MyFragmentPagerAdapter.class.getSimpleName(), "SurvivalGuide categories count : " + survivalGuide.getCategories().size());
        this.survivalGuide = survivalGuide;
    }
    public void setTabsList(){
        if (feedEvents.getItemCount() > 0 ) tabs.add("Events");
        if (feedNews.getItemCount() > 0 ) tabs.add("News");
        if (feedPartners.getItemCount() > 0 ) tabs.add("Partners");
        if (survivalGuide.getCategories().size() > 0 ) tabs.add("SurvivalGuide");
        if (Utils.getObjectFromCache(context, "section") != null) tabs.add("About");
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

        //Survival Guide
        SurvivalFragment survival = new SurvivalFragment(); survival.setSurvivalGuide(survivalGuide);

        //About Page
        AboutFragment about = new AboutFragment();

        switch (position) {
            case 0: //Events
                if (feedEvents.getItemCount() > 0 && tabs.contains("Events")) {
                    tabs.remove("Events");
                    return events;
                }
                else if (feedNews.getItemCount() > 0 && tabs.contains("News")) {
                    tabs.remove("News");
                    return news;
                }
                else if (feedPartners.getItemCount() > 0 && tabs.contains("Partners")) {
                    tabs.remove("Partners");
                    return partners;
                }
                else if (survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")){
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
            case 1: //News
                if (feedNews.getItemCount() > 0 && tabs.contains("News")) {
                    tabs.remove("News");
                    return news;
                }
                else if (feedPartners.getItemCount() > 0 && tabs.contains("Partners")) {
                    tabs.remove("Partners");
                    return partners;
                }
                else if (survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")){
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
            case 2: //Partners
                if (feedPartners.getItemCount() > 0 && tabs.contains("Partners")) {
                    tabs.remove("Partners");
                    return partners;
                }
                else if (survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")){
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
            case 3: //Survival Guide
                if (survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")) {
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
            case 4: //About
                    return about;

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
