package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.HomeActivity;
import org.esn.mobilit.models.Category;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.image.InternalStorage;
import org.esn.mobilit.utils.parser.DOMParser;
import org.esn.mobilit.utils.parser.RSSFeed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/*
 * Links
 * http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
 */
public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
	private RSSFeed feedEvents, feedNews, feedPartners;
    private SurvivalGuide survivalguide;
    private int count, count_limit;
    private Intent intent;
    private TextView textView;
    private ProgressBar progressBar;
    private Context splashActivityContext;
    //GCM
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID = "regId";
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";
    RequestParams params = new RequestParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

        //Init values
        splashActivityContext = getApplicationContext();
        applicationContext = getApplicationContext();
        count_limit = 5;
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        count = 0;

        //Init Elements
        textView = ((TextView)findViewById (R.id.textView));
        progressBar = ((ProgressBar)findViewById (R.id.progressBar));

        if (!Utils.isConnected(this)){
            // No connectivity - Load cache
            textView.setText(getResources().getString(R.string.tryingcache));
            feedEvents    = (RSSFeed) getObjectFromCache("feedEvents");
            feedNews      = (RSSFeed) getObjectFromCache("feedNews");
            feedPartners  = (RSSFeed) getObjectFromCache("feedPartners");
            survivalguide = (SurvivalGuide) getObjectFromCache("survivalGuide");

            // Cache is empty
            if (feedEvents == null && feedNews == null && feedPartners == null && survivalguide == null){
                Log.d(TAG, "EMPTY CACHE");
                textView.setText(getResources().getString(R.string.emptycache));
                progressBar.setVisibility(View.INVISIBLE);
            }else{ // Cache is not empty, load homeactivity
                Log.d(TAG, "LOADED FROM CACHE");
                //Add inputs
                Bundle bundle = new Bundle();
                bundle.putSerializable("feedEvents", feedEvents);
                bundle.putSerializable("feedNews", feedNews);
                bundle.putSerializable("feedPartners", feedPartners);
                bundle.putSerializable("survivalGuide", survivalguide);
                intent.putExtras(bundle);
                count = count_limit;
                launchHomeActivity();
            }

            final Button buttonretry = (Button) findViewById(R.id.retry);
            buttonretry.setVisibility(View.VISIBLE);
            buttonretry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(TAG,"SplashActivity restarting");
                    Intent returnIntent = new Intent();
                    setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                    finish();
                }
            });

            final Button buttonsection = (Button) findViewById(R.id.changesection);
            buttonsection.setVisibility(View.VISIBLE);
            buttonsection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(TAG,"SplashActivity restarting");
                    Intent returnIntent = new Intent();
                    setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                    finish();
                }
            });


        }else{
            // Push for GCM
            if (getDefaults(REG_ID) != null)
                count_limit--;
            else
                RegisterUser();

            // Connected - Start parsing
            new DownloadJSONSurvivalGuide().execute();
            new AsyncLoadXMLFeedEvents().execute();
            new AsyncLoadXMLFeedNews().execute();
            new AsyncLoadXMLFeedPartners().execute();
        }
	}

    public void launchHomeActivity(){
        if (count == count_limit) {

            int total = 0;
            total += feedEvents.getItemCount();
            total += feedNews.getItemCount();
            total += feedPartners.getItemCount();
            total += survivalguide.getCategories().size();

            if (total > 0) {
                Log.d(TAG, "TOTAL ITEMS COUNT : " + total);
                startActivityForResult(intent, ApplicationConstants.RESULT_SPLASH_ACTIVITY);
            }
            else {
                textView.setText("There is no contents for this section, please contact g33kteam@ixesn.fr");
                progressBar.setVisibility(View.INVISIBLE);

                final Button button = (Button) findViewById(R.id.retry);
                button.setVisibility(View.VISIBLE);

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG,"SplashActivity restarting");
                        Intent returnIntent = new Intent();
                        setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                        finish();
                    }
                });

            }
        }
    }

    // PREFERENCES
    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(key, null);
    }

    public void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // CACHE
    public void saveObjectToCache(String key, Object o){
        key = getDefaults("CODE_SECTION") + "_" + key;
        try {
            InternalStorage.writeObject(splashActivityContext, key, o);
        }catch (Exception e){
            Log.d(TAG, "Exception saveobject: " + e);
        }
    }

    public Object getObjectFromCache(String key){
        Object o = null;
        key = getDefaults("CODE_SECTION") + "_" + key;
        try {
            o = InternalStorage.readObject(splashActivityContext, key);
        }catch (Exception e){
            Log.d(TAG, "Exception getobject: " + e);
        }
        return o;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");

        if (requestCode == ApplicationConstants.RESULT_SPLASH_ACTIVITY) {
            Log.d(TAG, "RESULT SPLASH");
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "weird");
            }else if (resultCode == RESULT_CANCELED){
                // The user pressed back
                Intent returnIntent = new Intent();
                setResult(ApplicationConstants.RESULT_CLOSE_ALL,returnIntent);
                finish();
            }else if (resultCode == ApplicationConstants.RESULT_FIRST_LAUNCH){
                Intent returnIntent = new Intent();
                setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

	private class AsyncLoadXMLFeedEvents extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
            // Get feed url
            String event_url = getDefaults("SECTION_WEBSITE") + ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH;

			// Obtain feed
            Log.d(TAG, "Debut Parser pour " + ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH);
			DOMParser myParser = new DOMParser();
            feedEvents = myParser.parseXml(event_url);

            saveObjectToCache("feedEvents", feedEvents);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Bundle bundle = new Bundle();
			bundle.putSerializable("feedEvents", feedEvents);

			//Put Extra
            intent.putExtras(bundle);

            count++;

            launchHomeActivity();
		}

	}

    private class AsyncLoadXMLFeedNews extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Get feed url
            String url = getDefaults("SECTION_WEBSITE") + ApplicationConstants.NEWS_PATH + ApplicationConstants.FEED_PATH;
            //String url = "http://esnlille.fr/BuddySystem/test.xml";

            // Obtain feed
            Log.d(TAG, "Debut Parser pour " + url);
            DOMParser myParser = new DOMParser();
            feedNews = myParser.parseXml(url);
            saveObjectToCache("feedNews", feedNews);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedNews", feedNews);

            //Put Extra
            intent.putExtras(bundle);

            count++;

            launchHomeActivity();
        }

    }

    private class AsyncLoadXMLFeedPartners extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Get feed url
            String url = getDefaults("SECTION_WEBSITE") + ApplicationConstants.PARTNERS_PATH + ApplicationConstants.FEED_PATH;

            // Obtain feed
            Log.d(TAG, "Debut Parser pour " + url);
            DOMParser myParser = new DOMParser();
            feedPartners = myParser.parseXml(url);
            saveObjectToCache("feedPartners", feedPartners);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedPartners", feedPartners);

            //Put Extra
            intent.putExtras(bundle);

            count++;

            launchHomeActivity();
        }

    }

    private class DownloadJSONSurvivalGuide extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Create survival guide array
            survivalguide = new SurvivalGuide();

            //categories_list = new ArrayList<Category>();
            String url = ApplicationConstants.SURVIVAL_WEBSERVICE_URL + "getCategories.php?section=" + getDefaults("CODE_SECTION");
            JSONObject jsonobject = JSONfunctions
                    .getJSONfromURL(url);
            try {
                // SURVIVAL GUIDE LEVEL 1
                JSONArray jsonarray_level1 = jsonobject.getJSONArray("categories");
                for (int i = 0; i < jsonarray_level1.length(); i++) {
                    JSONObject jsonobject_level1 = jsonarray_level1.getJSONObject(i);
                    Category category = new Category(
                        jsonobject_level1.optInt("id"),
                        jsonobject_level1.optString("name"),
                        jsonobject_level1.optString("section"),
                        jsonobject_level1.optString("content"),
                        0,
                        jsonobject_level1.optInt("position")
                    );
                    survivalguide.getCategories().add(category);

                    // SURVIVAL GUIDE LEVEL 2
                    JSONArray jsonarray_level2 = jsonobject_level1.getJSONArray("categories");
                    for (int j = 0; j < jsonarray_level2.length(); j++) {
                        JSONObject jsonobject_level2 = jsonarray_level2.getJSONObject(j);
                        Category categorylvl2 = new Category(
                                jsonobject_level2.optInt("id"),
                                jsonobject_level2.optString("name"),
                                jsonobject_level2.optString("section"),
                                jsonobject_level2.optString("content"),
                                1,
                                jsonobject_level2.optInt("position")
                        );
                        survivalguide.getCategories().add(categorylvl2);

                        // SURVIVAL GUIDE LEVEL 3
                        JSONArray jsonarray_level3 = jsonobject_level2.getJSONArray("categories");
                        for (int k = 0; k < jsonarray_level3.length(); k++) {
                            JSONObject jsonobject_level3 = jsonarray_level3.getJSONObject(k);
                            Category categorylvl3 = new Category(
                                    jsonobject_level3.optInt("id"),
                                    jsonobject_level3.optString("name"),
                                    jsonobject_level3.optString("section"),
                                    jsonobject_level3.optString("content"),
                                    2,
                                    jsonobject_level3.optInt("position")
                            );
                            survivalguide.getCategories().add(categorylvl3);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG,"Error" + e.getMessage());
                e.printStackTrace();
            }

            saveObjectToCache("survivalGuide", survivalguide);
            return null;
        }

        protected void onPostExecute(Void args) {
            count++;

            //Add new bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("survivalGuide", survivalguide);

            //Put Extra
            intent.putExtras(bundle);

            launchHomeActivity();
        }
    }


    // GCM
    public void RegisterUser() {
        if (checkPlayServices()) {
            Log.d(TAG,"RegisterUser : check OK");
            registerInBackground();
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    storeRegIdinSharedPref();
                    //Toast.makeText(
                    //        applicationContext,
                    //        "Registered with GCM Server successfully.\n\n"
                    //                + msg, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(
                    //       applicationContext,
                    //       "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                    //               + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    private void storeRegIdinSharedPref() {
        setDefaults(REG_ID, regId);
        setDefaults("FROM_FIRSTLAUNCH",null);
        storeRegIdinServer();
    }

    private void storeRegIdinServer() {
        params.put("regId", regId);
        params.put("CODE_SECTION", getDefaults("CODE_SECTION"));

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ApplicationConstants.APP_SERVER_URL, params,
                new AsyncHttpResponseHandler() {
                    public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] b) {

                        //Toast.makeText(applicationContext,
                        //        "Reg Id shared successfully with Web App ",
                        //        Toast.LENGTH_LONG).show();
                        count++;

                        if (count == count_limit) {
                            //startActivityForResult(intent, ApplicationConstants.RESULT_SPLASH_ACTIVITY);
                            launchHomeActivity();

                            finish();
                        }
                    }

                    public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] b, Throwable throwable) {
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            //    Toast.makeText(applicationContext,
                            //        "Requested resource not found",
                            //        Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            //Toast.makeText(applicationContext,
                            //        "Something went wrong at server end",
                            //        Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            //Toast.makeText(
                            //        applicationContext,
                            //        "Unexpected Error occcured! [Most common Error: Device might "
                            //                + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                            //        Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Toast.makeText(
                //        applicationContext,
                //        "This device doesn't support Play services, App will not work normally",
                //        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            //Toast.makeText(
            //        applicationContext,
            //        "This device supports Play services, App will work normally",
            //        Toast.LENGTH_LONG).show();
        }
        return true;
    }

}
