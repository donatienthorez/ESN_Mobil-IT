package org.esn.mobilit.tasks.feed;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.DOMParser;
import org.esn.mobilit.utils.parser.RSSFeed;

public class AsyncLoadXMLFeedNews extends AsyncTask<Void, Void, Void> {

    SplashActivity activity;
    Context context;
    Resources resources;

    FeedService feedService;
    RSSFeed news;

    public AsyncLoadXMLFeedNews(SplashActivity activity) {
        this.activity = activity;
        this.context = activity.getContext();
        this.resources = activity.getResources();
        this.feedService = activity.getFeedService();
        this.news = new RSSFeed();
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Get feed url
        String url = Utils.getDefaults(activity.getContext(), "SECTION_WEBSITE") + ApplicationConstants.NEWS_PATH + ApplicationConstants.FEED_PATH;

        DOMParser myParser = new DOMParser();
        this.news = myParser.parseXml(url);
        feedService.setFeedNews(this.news);
        Utils.saveObjectToCache(activity.getContext(), "feedNews", this.news);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        activity.getTextView().setText(resources.getString(R.string.load_news_end, this.news.getItemCount()));
        activity.getTextView().setText(resources.getString(R.string.load_partners_start));

        activity.incrementCount();
        activity.launchHomeActivity();
    }

}
