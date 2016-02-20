package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.Guide;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class FeedService
{
    private static FeedService instance;

    private RSSFeedParser feedEvents, feedNews, feedPartners;
    private Guide guide;
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
        return feedEvents == null && feedNews == null && feedPartners == null && guide == null;
    }

    public void getFeedsFromCache()
    {
        feedEvents    = (RSSFeedParser) CacheService.getObjectFromCache("feedEvents");
        feedNews      = (RSSFeedParser) CacheService.getObjectFromCache("feedNews");
        feedPartners  = (RSSFeedParser) CacheService.getObjectFromCache("feedPartners");
        guide = (Guide) CacheService.getObjectFromCache("survivalGuide");
    }

    public int getTotalItems(){
        int total = 0;
        total += feedEvents != null? feedEvents.getItemCount() : 0;
        total += feedNews != null ? feedNews.getItemCount() : 0;
        total += feedPartners != null ? feedPartners.getItemCount() : 0;
        total += guide != null ? guide.getNodes().size() : 0;

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

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

}
