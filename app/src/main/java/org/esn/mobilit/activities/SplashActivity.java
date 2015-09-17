package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.esn.mobilit.R;
import org.esn.mobilit.fragments.HomeActivity;
import org.esn.mobilit.models.Category;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.DOMParser;
import org.esn.mobilit.utils.parser.RSSFeed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Context context;
    private String pushMsg;

    //GCM
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID = "regId";
    GoogleCloudMessaging gcmObj;
    String regId = "";
    RequestParams params = new RequestParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Change layout
		setContentView(R.layout.splash);

        //Init values
        context = getApplicationContext();
        count_limit = 5;
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        count = 0;

        //Init Elements
        TextView version = ((TextView) findViewById(R.id.version));
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("v " + pInfo.versionName);
        }catch(PackageManager.NameNotFoundException e){
            version.setText("v 1.0.0");
        }

        textView = ((TextView)findViewById (R.id.textView));
        progressBar = ((ProgressBar)findViewById (R.id.progressBar));

        if (!Utils.isConnected(this)){
            // No connectivity - Load cache
            textView.setText(getResources().getString(R.string.tryingcache));
            feedEvents    = (RSSFeed) Utils.getObjectFromCache(context, "feedEvents");
            feedNews      = (RSSFeed) Utils.getObjectFromCache(context, "feedNews");
            feedPartners  = (RSSFeed) Utils.getObjectFromCache(context, "feedPartners");
            survivalguide = (SurvivalGuide) Utils.getObjectFromCache(context, "survivalGuide");

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
            if (Utils.getDefaults(context, REG_ID) != null) {
                Log.d(TAG,"No need to push GCM");
                count_limit--;
            }
            else {
                Log.d(TAG,"Registering RegID");
                RegisterUser();
            }

            Log.d(TAG, "count_limit:" + count_limit);

            // Connected - Start parsing
            textView.setText(R.string.load_survival_start);
            new DownloadJSONSurvivalGuide().execute();
            new AsyncLoadXMLFeedEvents().execute();
            new AsyncLoadXMLFeedNews().execute();
            new AsyncLoadXMLFeedPartners().execute();
        }
	}

    public void onResume(){
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            pushMsg = getIntent().getExtras().getString("title");
        }
    }

    public void onPause(){
        super.onPause();
    }

    public void onStop(){
        super.onStop();
    }

    public void launchHomeActivity(){
        if (count == count_limit) {

            int total = 0;
            total += feedEvents.getItemCount();
            total += feedNews.getItemCount();
            total += feedPartners.getItemCount();
            total += survivalguide.getCategories().size();

            if (total > 0) {
                textView.setText(R.string.start_homeactivity);

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);

                if (pushMsg != null) {
                    i.putExtra("pushReceived", true);
                    i.putExtra("pushMsg", pushMsg);
                }else{
                    i.putExtra("pushReceived", false);
                }

                startActivityForResult(i, ApplicationConstants.RESULT_SPLASH_ACTIVITY);
            }
            else {
                textView.setText(R.string.noitems);
                progressBar.setVisibility(View.INVISIBLE);

                final Button button = (Button) findViewById(R.id.retry);
                button.setVisibility(View.VISIBLE);

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                        finish();
                    }
                });

            }
        }
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
            String event_url = Utils.getDefaults(context, "SECTION_WEBSITE") + ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH;

			// Obtain feed
            Log.d(TAG, "Debut Parser pour " + ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH);
			DOMParser myParser = new DOMParser();
            feedEvents = myParser.parseXml(event_url);

            Utils.saveObjectToCache(context, "feedEvents", feedEvents);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

            textView.setText(getResources().getString(R.string.load_events_end, feedEvents.getItemCount()));
            textView.setText(getResources().getString(R.string.load_news_start));

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
            String url = Utils.getDefaults(context, "SECTION_WEBSITE") + ApplicationConstants.NEWS_PATH + ApplicationConstants.FEED_PATH;
            //String url = "http://esnlille.fr/BuddySystem/test.xml";

            // Obtain feed
            Log.d(TAG, "Debut Parser pour " + url);
            DOMParser myParser = new DOMParser();
            feedNews = myParser.parseXml(url);
            Utils.saveObjectToCache(context, "feedNews", feedNews);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            textView.setText(getResources().getString(R.string.load_news_end, feedNews.getItemCount()));
            textView.setText(getResources().getString(R.string.load_partners_start));

            count++;
            launchHomeActivity();
        }

    }

    private class AsyncLoadXMLFeedPartners extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Get feed url
            String url = Utils.getDefaults(context, "SECTION_WEBSITE") + ApplicationConstants.PARTNERS_PATH + ApplicationConstants.FEED_PATH;

            // Obtain feed
            Log.d(TAG, "Debut Parser pour " + url);
            DOMParser myParser = new DOMParser();
            feedPartners = myParser.parseXml(url);
            Utils.saveObjectToCache(context, "feedPartners", feedPartners);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            textView.setText(getResources().getString(R.string.load_partners_end, feedPartners.getItemCount()));

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
            String url = ApplicationConstants.SURVIVAL_WEBSERVICE_URL + "/getCategories.php?section=" + Utils.getDefaults(context, "CODE_SECTION");
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

            Utils.saveObjectToCache(context, "survivalGuide", survivalguide);
            return null;
        }

        protected void onPostExecute(Void args) {
            textView.setText(getResources().getString(R.string.load_survival_end, survivalguide.getCategories().size()));
            textView.setText(getResources().getString(R.string.load_events_start));

            count++;
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
                                .getInstance(context);
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
                    new postRegID().execute();
                } else {
                    Log.d(TAG, "onPostExecute registerInBackground failed");
                }
            }
        }.execute(null, null, null);
    }

    private class postRegID extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ApplicationConstants.APP_SERVER_URL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("regId", regId));
                nameValuePairs.add(new BasicNameValuePair("CODE_SECTION", Utils.getDefaults(context, "CODE_SECTION")));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                Log.d(TAG,"ClientProtocolException" + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG,"IOException" + e.getMessage());
            }

            return null;
        }

        protected void onPostExecute(Void args) {
            Log.d(TAG,"onPostExecute");
            storeRegIdinSharedPref();
            count++;
            if (count == count_limit) {
                launchHomeActivity();
                finish();
            }
        }
    }

    private void storeRegIdinSharedPref() {
        Utils.setDefaults(context, REG_ID, regId);
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
                //        context,
                //        "This device doesn't support Play services, App will not work normally",
                //        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            //Toast.makeText(
            //        context,
            //        "This device supports Play services, App will work normally",
            //        Toast.LENGTH_LONG).show();
        }
        return true;
    }

}
