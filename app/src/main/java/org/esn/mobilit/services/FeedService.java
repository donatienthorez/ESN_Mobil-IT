package org.esn.mobilit.services;

import android.content.Context;

import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

public class FeedService
{
    private RSSFeed feedEvents, feedNews, feedPartners;
    private SurvivalGuide survivalguide;

    private Context context;

    public FeedService(Context context)
    {
        this.context = context;
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

    public boolean emptyFeeds()
    {
        return feedEvents == null && feedNews == null && feedPartners == null && survivalguide == null;
    }

    public void getRssFeedFromCache()
    {
        feedEvents    = (RSSFeed) Utils.getObjectFromCache(this.context, "feedEvents");
        feedNews      = (RSSFeed) Utils.getObjectFromCache(this.context, "feedNews");
        feedPartners  = (RSSFeed) Utils.getObjectFromCache(this.context, "feedPartners");
        survivalguide = (SurvivalGuide) Utils.getObjectFromCache(this.context, "survivalGuide");
    }
}
