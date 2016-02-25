package org.esn.mobilit.services.feeds;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.LauncherService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.LoadingCallback;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class FeedService
{
    private static FeedService instance;

    private Guide guide;

    private FeedService(){
    }

    public static FeedService getInstance(){
        if (instance == null){
            instance = new FeedService();
        }
        return instance;
    }

    public boolean emptyFeeds(ArrayList<FeedServiceInterface> list) {
        for (FeedServiceInterface f : list) {
            if (f.getFeed() != null) {
                return false;
            }
        }
        return guide == null;
    }

    public int getTotalItems(ArrayList<FeedServiceInterface> list){
        int total = 0;
        for (FeedServiceInterface feedType : list) {
            if (feedType.getFeed() != null) {
                total += feedType.getFeed().getItemCount();
            }
        }
        total += (guide != null && guide.getNodes() != null) ? guide.getNodes().size() : 0;

        return total;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public void loadFeeds(final ArrayList<FeedServiceInterface> arrayList, Section section, final LoadingCallback<RSSFeedParser> callback) {

        for (final FeedServiceInterface feedService : arrayList) {
            getFeed(feedService, section, new NetworkCallback<RSSFeedParser>() {
                @Override
                public void onSuccess(RSSFeedParser result) {
                    callback.onProgress(R.string.emptycache, result);
                    LauncherService.getInstance().incrementCount();
                    if (LauncherService.getInstance().launchHomeActivity()) {
                        if (FeedService.getInstance().getTotalItems(arrayList) > 0) {
                            callback.onSuccess(result);
                        } else {
                            callback.onNoAvailableData();
                        }
                    }
                }

                @Override
                public void onNoAvailableData() {
                    callback.onProgress(R.string.emptycache, null);
                    LauncherService.getInstance().incrementCount();
                    if (LauncherService.getInstance().launchHomeActivity()) {
                        callback.onSuccess(null);
                    }
                }

                @Override
                public void onFailure(RetrofitError error) {
                    callback.onProgress(R.string.emptycache, null);
                    LauncherService.getInstance().incrementCount();
                    if (LauncherService.getInstance().launchHomeActivity()) {
                        callback.onSuccess(null);
                    }
                }
            });
        }
    }

    public void getFeed(FeedServiceInterface feedService, Section section, final NetworkCallback<RSSFeedParser> callback)
    {
        RSSFeedParser feed = feedService.getFeed();

        if (feed != null && feed.getItemCount() > 0) {
            callback.onSuccess(feed);
            return;
        }

        if (Utils.isConnected()){
            feedService.getFromSite(section.getWebsite(), FeedService.getInstance().getCallback(feedService, callback));
        } else {
            feed = feedService.getFromCache();

            if (feed != null) {
                callback.onSuccess(feed);
            } else {
                callback.onNoAvailableData();
            }
        }
    }

    private NetworkCallback<RSS> getCallback(final FeedServiceInterface feedService, final NetworkCallback<RSSFeedParser> callback) {
        return new NetworkCallback<RSS>() {
            @Override
            public void onSuccess(RSS feed) {
                feed.getRSSChannel().moveImage();
                RSSFeedParser rssFeedParser = new RSSFeedParser(feed.getRSSChannel().getList());
                feedService.setFeed(rssFeedParser);
                feedService.setFeedToCache(rssFeedParser);
                callback.onSuccess(rssFeedParser);
            }

            @Override
            public void onNoAvailableData() {
                callback.onNoAvailableData();
            }

            @Override
            public void onFailure(RetrofitError error) {
                callback.onFailure(error);
            }
        };
    }
}
