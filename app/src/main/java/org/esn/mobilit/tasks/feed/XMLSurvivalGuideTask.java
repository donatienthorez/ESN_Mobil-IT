package org.esn.mobilit.tasks.feed;

import android.os.AsyncTask;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;

public class XMLSurvivalGuideTask extends AsyncTask<Void, Void, Callback> {

    Callback<SurvivalGuide> callback;
    SurvivalGuide survivalGuide;

    public XMLSurvivalGuideTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Callback doInBackground(Void... params) {

        String url = ApplicationConstants.SURVIVAL_WEBSERVICE_URL
                + "/getCategories.php?section="
                + PreferencesService.getDefaults("code_section");

        this.survivalGuide = JSONfunctions.getSurvivalGuide(url);

        FeedService.getInstance().setSurvivalguide(survivalGuide);
        CacheService.saveObjectToCache("survivalGuide", survivalGuide);

        return callback;
    }

    @Override
    protected void onPostExecute(Callback callback) {
        super.onPostExecute(callback);
        this.callback.onSuccess(this.survivalGuide);
    }
}