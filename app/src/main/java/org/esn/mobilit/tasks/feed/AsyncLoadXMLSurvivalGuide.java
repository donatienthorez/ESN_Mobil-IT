package org.esn.mobilit.tasks.feed;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;

public class AsyncLoadXMLSurvivalGuide extends AsyncTask<Void, Void, Void> {

    SplashActivity activity;
    Context context;
    Resources resources;

    FeedService feedService;
    SurvivalGuide survivalGuide;

    public AsyncLoadXMLSurvivalGuide(SplashActivity activity) {
        this.activity = activity;
        this.context = activity.getContext();
        this.resources = activity.getResources();
        this.feedService = activity.getFeedService();
        this.survivalGuide = new SurvivalGuide();
    }

    @Override
    protected Void doInBackground(Void... params) {

        String url = ApplicationConstants.SURVIVAL_WEBSERVICE_URL + "/getCategories.php?section=" + Utils.getDefaults(context, "CODE_SECTION");

        this.survivalGuide = JSONfunctions.getSurvivalGuide(url);

        feedService.setSurvivalguide(survivalGuide);
        Utils.saveObjectToCache(context, "survivalGuide", survivalGuide);
        return null;
    }

    protected void onPostExecute(Void args) {
        activity.getTextView().setText(resources.getString(R.string.load_survival_end, survivalGuide.getCategories().size()));
        activity.getTextView().setText(resources.getString(R.string.load_events_start));

        activity.incrementCount();
        activity.launchHomeActivity();
    }
}