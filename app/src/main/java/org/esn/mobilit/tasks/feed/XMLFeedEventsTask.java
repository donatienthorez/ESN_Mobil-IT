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

public class XMLFeedEventsTask extends AsyncTask<Void, Void, Void> {

    SplashActivity activity;
    Context context;
    Resources resources;

    FeedService feedService;
    RSSFeed events;

    public XMLFeedEventsTask(SplashActivity activity) {
        this.activity = activity;
        this.context = activity.getContext();
        this.resources = activity.getResources();
        this.feedService = activity.getFeedService();
        this.events = new RSSFeed();
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Get feed url
        String url = Utils.getDefaults(activity.getContext(), "SECTION_WEBSITE") + ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH;

        // Obtain feed
        DOMParser myParser = new DOMParser();
        this.events = myParser.parseXml(url);
        feedService.setFeedEvents(this.events);
        Utils.saveObjectToCache(this.context, "feedEvents", this.events);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        activity.getTextView().setText(this.resources.getString(R.string.load_events_end, events.getItemCount()));
        activity.getTextView().setText(this.resources.getString(R.string.load_news_start));

        activity.incrementCount();
        activity.launchHomeActivity();
    }
}
