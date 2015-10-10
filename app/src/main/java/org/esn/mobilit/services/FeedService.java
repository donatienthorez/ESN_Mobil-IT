package org.esn.mobilit.services;

import android.content.Context;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

public class FeedService
{
    private static FeedService instance;

    private RSSFeed feedEvents, feedNews, feedPartners;
    private SurvivalGuide survivalguide;

    private FeedService(){
    }

    public static FeedService getInstance(){
        if (instance == null){
            instance = new FeedService();
        }
        return instance;
    }

    public boolean emptyFeeds()
    {
        return feedEvents == null && feedNews == null && feedPartners == null && survivalguide == null;
    }

    public void getRssFeedFromCache()
    {
        feedEvents    = (RSSFeed) Utils.getObjectFromCache(MobilITApplication.getContext(), "feedEvents");
        feedNews      = (RSSFeed) Utils.getObjectFromCache(MobilITApplication.getContext(), "feedNews");
        feedPartners  = (RSSFeed) Utils.getObjectFromCache(MobilITApplication.getContext(), "feedPartners");
        survivalguide = (SurvivalGuide) Utils.getObjectFromCache(MobilITApplication.getContext(), "survivalGuide");
    }

    public RSSFeed getFeedEvents() {
        return feedEvents;
    }

    public void setFeedEvents(RSSFeed feedEvents) {
        this.feedEvents = feedEvents;
    }

    public RSSFeed getFeedNews() {
        return feedNews;
    }

    public void setFeedNews(RSSFeed feedNews) {
        this.feedNews = feedNews;
    }

    public RSSFeed getFeedPartners() {
        return feedPartners;
    }

    public void setFeedPartners(RSSFeed feedPartners) {
        this.feedPartners = feedPartners;
    }

    public SurvivalGuide getSurvivalguide() {
        return survivalguide;
    }

    public void setSurvivalguide(SurvivalGuide survivalguide) {
        this.survivalguide = survivalguide;
    }

}
