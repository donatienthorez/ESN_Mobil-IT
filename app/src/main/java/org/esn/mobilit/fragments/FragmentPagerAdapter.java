package org.esn.mobilit.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.fragments.Satellite.ListFragment;
import org.esn.mobilit.fragments.Survival.SurvivalFragment;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

import java.util.ArrayList;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    int count;
    Section section;
    ArrayList<String> tabs;
    FeedService feedService;
    RSSFeed feedEvents, feedNews, feedPartners;
    SurvivalGuide survivalGuide;

    public FragmentPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count   = count;
        this.tabs    = new ArrayList<String>();
        this.section = (Section) Utils.getObjectFromCache(MobilITApplication.getContext(), "section");
        this.feedService = FeedService.getInstance();
        this.feedEvents = feedService.getFeedEvents();
        this.feedNews = feedService.getFeedNews();
        this.feedPartners = feedService.getFeedPartners();
        this.survivalGuide = feedService.getSurvivalguide();

    }

    public void setTabsList(){
        if (feedEvents.getItemCount() > 0 ) tabs.add("Events");
        if (feedNews.getItemCount() > 0 ) tabs.add("News");
        if (feedPartners.getItemCount() > 0 ) tabs.add("Partners");
        if (survivalGuide.getCategories().size() > 0 ) tabs.add("SurvivalGuide");
        if (section != null) tabs.add("About");
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
                else if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
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
                else if (section != null && tabs.contains("About")){
                    tabs.remove("About");
                    return about;
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