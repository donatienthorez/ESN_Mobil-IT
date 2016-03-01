package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public abstract class RSSFeedService implements Cachable, Launchable<RSSFeedParser> {

    private RSSFeedParser feed;

    public abstract String getString();
    public abstract void getFromSite(String sectionWebsite, NetworkCallback<RSS> networkCallback);
    public abstract void resetService();

    public RSSFeedParser getFeed() {
        return feed;
    }

    public void setFeed(RSSFeedParser feed) {
        this.feed = feed;
    }

    private void setFeedToCache(RSSFeedParser feed) {
        CacheService.saveObjectToCache(this.getString(), feed);
    }

    private RSSFeedParser getFromCache() {
        String s = this.getString();
        return (RSSFeedParser) CacheService.getObjectFromCache(this.getString());
    }

    public void doAction(final NetworkCallback<RSSFeedParser> callback)
    {
        RSSFeedParser feed = this.getFeed();

        if (feed != null && feed.getItemCount() > 0) {
            callback.onSuccess(feed);
            return;
        }

        if (Utils.isConnected()){
            Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);
            getFromSite(section.getWebsite(), getCallback(callback));
        } else {
            feed = this.getFromCache();

            if (feed != null) {
                setFeed(feed);
                callback.onSuccess(feed);
            } else {
                callback.onNoAvailableData();
            }
        }
    }

    private NetworkCallback<RSS> getCallback(final NetworkCallback<RSSFeedParser> callback) {
        return new NetworkCallback<RSS>() {
            @Override
            public void onSuccess(RSS feed) {
                feed.getRSSChannel().moveImage();
                RSSFeedParser rssFeedParser = new RSSFeedParser(feed.getRSSChannel().getList());
                setFeed(rssFeedParser);
                setFeedToCache(rssFeedParser);

                rssFeedParser = (RSSFeedParser) CacheService.getObjectFromCache(getString());
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

