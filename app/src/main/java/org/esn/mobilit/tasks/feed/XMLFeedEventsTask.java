package org.esn.mobilit.tasks.feed;

import android.os.AsyncTask;
import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.activities.Callback;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.DOMParser;
import org.esn.mobilit.utils.parser.RSSFeed;

public class XMLFeedEventsTask extends AsyncTask<Void, Void, Callback> {

    Callback<RSSFeed> callback;
    RSSFeed events;

    public XMLFeedEventsTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Callback doInBackground(Void... params) {
        // Get feed url
        String url = Utils.getDefaults(MobilITApplication.getContext(), "SECTION_WEBSITE")
                + ApplicationConstants.EVENTS_PATH
                + ApplicationConstants.FEED_PATH;

        // Obtain feed
        DOMParser myParser = new DOMParser();
        this.events = myParser.parseXml(url);
        FeedService.getInstance().setFeedEvents(this.events);
        Utils.saveObjectToCache(MobilITApplication.getContext(), "feedEvents", this.events);

        return callback;
    }

    @Override
    protected void onPostExecute(Callback callback) {
        super.onPostExecute(callback);
        this.callback.onSuccess(this.events);
    }
}
