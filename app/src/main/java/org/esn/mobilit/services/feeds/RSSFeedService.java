package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.List;

public abstract class RSSFeedService implements Cachable, Launchable<RSSFeedParser> {

    private RSSFeedParser feed;

    public abstract String getString();
    public abstract void getFromSite(NetworkCallback<RSSFeedParser> networkCallback);
    public abstract void resetService();

    public RSSFeedService(){
        this.feed = new RSSFeedParser();
    }

    public RSSFeedParser getFeed() {
        if (feed == null) {
            feed = new RSSFeedParser();
        }
        return feed;
    }

    public void setFeed(RSSFeedParser feed) {
        this.feed = feed;
    }

    private void setFeedToCache(RSSFeedParser feed) {
        CacheService.saveObjectToCache(this.getString(), feed);
    }

    public RSSFeedParser getFromCache() {
        return (RSSFeedParser) CacheService.getObjectFromCache(this.getString());
    }

    public void doAction(final NetworkCallback<RSSFeedParser> callback)
    {
        RSSFeedParser feed = this.getFeed();

        if (feed != null && feed.getItemCount() > 0) {
            callback.onSuccess(feed);
            return;
        }

//        if (Utils.isConnected()){
//            getFromSite(callback);
//        } else {
            feed = this.getFromCache();

//            if (feed != null) {
                setFeed(feed);
                callback.onSuccess(feed);
//            } else {
//                callback.onNoAvailableData();
//            }
//        }
    }

    protected NetworkCallback<RSS> getCallback(final NetworkCallback<RSSFeedParser> callback) {
        return new NetworkCallback<RSS>() {
            @Override
            public void onSuccess(RSS feed) {
                RSSFeedParser rssFeedParser = (getFromCache() != null)
                        ? getFromCache()
                        : new RSSFeedParser();

                rssFeedParser.addItems(feed);

                setFeed(rssFeedParser);
                setFeedToCache(rssFeedParser);
                callback.onSuccess(rssFeedParser);
            }

            @Override
            public void onNoAvailableData() {
                callback.onNoAvailableData();
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        };
    }


}

