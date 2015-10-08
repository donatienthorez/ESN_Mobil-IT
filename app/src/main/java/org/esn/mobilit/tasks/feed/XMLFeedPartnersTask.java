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

public class XMLFeedPartnersTask extends AsyncTask<Void, Void, Void> {

    SplashActivity activity;
    Context context;
    Resources resources;

    FeedService feedService;
    RSSFeed partners;

    public XMLFeedPartnersTask(SplashActivity activity) {
        this.activity = activity;
        this.context = activity.getContext();
        this.resources = activity.getResources();
        this.feedService = activity.getFeedService();
        this.partners = new RSSFeed();
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Get feed url
        String url = Utils.getDefaults(activity.getContext(), "SECTION_WEBSITE") + ApplicationConstants.PARTNERS_PATH + ApplicationConstants.FEED_PATH;

        DOMParser myParser = new DOMParser();
        this.partners = myParser.parseXml(url);
        feedService.setFeedPartners(this.partners);
        Utils.saveObjectToCache(activity.getContext(), "feedPartners", this.partners);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        activity.getTextView().setText(resources.getString(R.string.load_partners_end, this.partners.getItemCount()));

        activity.incrementCount();
        activity.launchHomeActivity();
    }

}
