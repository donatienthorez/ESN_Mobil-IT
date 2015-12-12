package org.esn.mobilit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Satellite.ListFragment;
import org.esn.mobilit.fragments.Survival.SurvivalFragment;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    int count;
    Section section;
    ArrayList<String> tabs;
    FeedService feedService;
    RSSFeedParser feedEvents, feedNews, feedPartners;
    SurvivalGuide survivalGuide;

    public FragmentPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count   = count;
        this.tabs    = new ArrayList<String>();
        this.section = (Section) Utils.getObjectFromCache("section");
        this.feedService = FeedService.getInstance();
        this.feedEvents = feedService.getFeedEvents();
        this.feedNews = feedService.getFeedNews();
        this.feedPartners = feedService.getFeedPartners();
        this.survivalGuide = feedService.getSurvivalguide();

    }

    public void setTabsList(){
        if (feedEvents != null && feedEvents.getItemCount() > 0 ) tabs.add("Events");
        if (feedNews != null && feedNews.getItemCount() > 0 ) tabs.add("News");
        if (feedPartners != null && feedPartners.getItemCount() > 0 ) tabs.add("Partners");
        if (survivalGuide != null && survivalGuide.getCategories().size() > 0 ) tabs.add("SurvivalGuide");
        if (section != null) tabs.add("About");
    }

    @Override
    public Fragment getItem(int position) {
        //Init Fragment
        //Events
        ListFragment events = new ListFragment();
        feedEvents = FeedService.getInstance().getFeedEvents();
        events.setFeed(feedEvents);

        //News
        ListFragment news = new ListFragment();
        news.setFeed(feedNews);

        //Partners
        ListFragment partners = new ListFragment();
        partners.setFeed(feedPartners);

        //Survival Guide
        SurvivalFragment survival = new SurvivalFragment();
        survival.setSurvivalGuide(survivalGuide);

        //About Page
        AboutFragment about = new AboutFragment();

        switch (position) {
            case 0: //Events
                if (feedEvents != null && feedEvents.getItemCount() > 0 && tabs.contains("Events")) {
                    tabs.remove("Events");
                    return events;
                }
                else if (feedNews != null && feedNews.getItemCount() > 0 && tabs.contains("News")) {
                    tabs.remove("News");
                    return news;
                }
                else if (feedPartners != null && feedPartners.getItemCount() > 0 && tabs.contains("Partners")) {
                    tabs.remove("Partners");
                    return partners;
                }
                else if (survivalGuide != null && survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")){
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
                else if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
                }
            case 1: //News
                if (feedNews != null && feedNews.getItemCount() > 0 && tabs.contains("News")) {
                    tabs.remove("News");
                    return news;
                }
                else if (feedPartners != null && feedPartners.getItemCount() > 0 && tabs.contains("Partners")) {
                    tabs.remove("Partners");
                    return partners;
                }
                else if (survivalGuide != null && survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")){
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
                else if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
                }
            case 2: //Partners
                if (feedPartners != null && feedPartners.getItemCount() > 0 && tabs.contains("Partners")) {
                    tabs.remove("Partners");
                    return partners;
                }
                else if (survivalGuide != null && survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")){
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
                else if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
                }
            case 3: //Survival Guide
                if (survivalGuide.getCategories().size() > 0 && tabs.contains("SurvivalGuide")) {
                    tabs.remove("SurvivalGuide");
                    return survival;
                }
                else if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
                }
            case 4: //About
                if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
                }
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
