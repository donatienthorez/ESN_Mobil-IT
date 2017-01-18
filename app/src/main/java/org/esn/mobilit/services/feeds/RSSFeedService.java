package org.esn.mobilit.services.feeds;

import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;
import org.simpleframework.xml.util.Cache;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public abstract class RSSFeedService implements CachableInterface {

    public abstract String getString();
    public abstract void getFromSite(NetworkCallback<RSSFeedParser> networkCallback);
    public abstract void resetService();

    @Inject
    CacheService cacheService;

    private void setFeedToCache(RSSFeedParser feed) {
        cacheService.saveObjectToCache(this.getString(), feed);
    }

    public RSSFeedParser getFromCache() {
        RSSFeedParser rssFeedParser = (RSSFeedParser) cacheService.getObjectFromCache(this.getString());
        return rssFeedParser != null ? rssFeedParser : new RSSFeedParser();
    }

    protected NetworkCallback<RSS> getCallback(final NetworkCallback<RSSFeedParser> callback) {
        return new NetworkCallback<RSS>() {
            @Override
            public void onSuccess(RSS clientRss) {
                RSSFeedParser rssFeedParser = (getFromCache() != null)
                        ? getFromCache()
                        : new RSSFeedParser();

                rssFeedParser = rssFeedParser.updateItems(clientRss.getRSSChannel().getList());

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

