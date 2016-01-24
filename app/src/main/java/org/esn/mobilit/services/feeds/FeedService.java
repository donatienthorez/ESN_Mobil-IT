package org.esn.mobilit.services.feeds;

import android.util.Log;

import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class FeedService
{
    private static FeedService instance;

    private RSSFeedParser feedEvents, feedNews, feedPartners;
    private SurvivalGuide survivalguide;
    private static final String TAG = "FeedService";


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

    public void getFeedsFromCache()
    {
        feedEvents    = (RSSFeedParser) CacheService.getObjectFromCache("feedEvents");
        feedNews      = (RSSFeedParser) CacheService.getObjectFromCache("feedNews");
        feedPartners  = (RSSFeedParser) CacheService.getObjectFromCache("feedPartners");
        survivalguide = (SurvivalGuide) CacheService.getObjectFromCache("survivalGuide");
    }

    public int getTotalItems(){
        int total = 0;
        total += feedEvents != null? feedEvents.getItemCount() : 0;
        total += feedNews != null ? feedNews.getItemCount() : 0;
        total += feedPartners != null ? feedPartners.getItemCount() : 0;
        total += survivalguide != null ? survivalguide.getCategories().size() : 0;

        return total;
    }

    public RSSFeedParser getFeedEvents() {
        return feedEvents;
    }

    public void setFeedEvents(RSSFeedParser feedEvents) {
        this.feedEvents = feedEvents;
    }

    public RSSFeedParser getFeedNews() {
        return feedNews;
    }

    public void setFeedNews(RSSFeedParser feedNews) {
        this.feedNews = feedNews;
    }

    public RSSFeedParser getFeedPartners() {
        return feedPartners;
    }

    public void setFeedPartners(RSSFeedParser feedPartners) {
        this.feedPartners = feedPartners;
    }

    public SurvivalGuide getSurvivalguide() {
        return survivalguide;
    }

    public void setSurvivalguide(SurvivalGuide survivalguide) {
        this.survivalguide = survivalguide;
    }

}
