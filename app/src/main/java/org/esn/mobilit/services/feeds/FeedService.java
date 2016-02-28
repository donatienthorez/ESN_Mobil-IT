package org.esn.mobilit.services.feeds;


import java.util.ArrayList;

public class FeedService
{
    private static FeedService instance;

    private FeedService(){
    }

    public static FeedService getInstance(){
        if (instance == null){
            instance = new FeedService();
        }
        return instance;
    }

    public boolean hasFeeds(ArrayList<RSSFeedService> list) {
        for (RSSFeedService f : list) {
            if (f.getFeed() != null) {
                return false;
            }
        }
        return true;
    }
}
