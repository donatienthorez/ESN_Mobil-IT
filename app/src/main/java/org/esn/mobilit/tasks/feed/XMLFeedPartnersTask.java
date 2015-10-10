package org.esn.mobilit.tasks.feed;

import android.os.AsyncTask;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.activities.Callback;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.DOMParser;
import org.esn.mobilit.utils.parser.RSSFeed;

public class XMLFeedPartnersTask extends AsyncTask<Void, Void, Callback> {

    Callback<RSSFeed> callback;
    RSSFeed partners;

    public XMLFeedPartnersTask(Callback callback)
    {
        this.callback = callback;
    }

    @Override
    protected Callback doInBackground(Void... params) {
        // Get feed url
        String url = Utils.getDefaults(MobilITApplication.getContext(), "SECTION_WEBSITE")
                + ApplicationConstants.PARTNERS_PATH
                + ApplicationConstants.FEED_PATH;

        DOMParser myParser = new DOMParser();
        this.partners = myParser.parseXml(url);
        FeedService.getInstance().setFeedPartners(this.partners);
        Utils.saveObjectToCache(MobilITApplication.getContext(), "feedPartners", this.partners);

        return callback;
    }

    @Override
    protected void onPostExecute(Callback callback) {
        super.onPostExecute(callback);
        this.callback.onSuccess(this.partners);
    }
}
