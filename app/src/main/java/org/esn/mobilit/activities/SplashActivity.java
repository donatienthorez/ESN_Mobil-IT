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
import org.esn.mobilit.tasks.feed.AsyncLoadXMLFeedEvents;
import org.esn.mobilit.tasks.feed.AsyncLoadXMLFeedNews;
import org.esn.mobilit.tasks.feed.AsyncLoadXMLFeedPartners;
import org.esn.mobilit.tasks.feed.AsyncLoadXMLSurvivalGuide;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Links
 * http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
 */
public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private int count, count_limit;
    private Intent intent;
    private TextView textView;
    private ProgressBar progressBar;
    private Context context;
    private String pushMsg;

    private FeedService feedService;

    public FeedService getFeedService() {
        return feedService;
    }

    public Context getContext() {
        return context;
    }

    public TextView getTextView() {
        return textView;
    }

    public void incrementCount()
    {
        this.count++;
    }

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
        this.context = getApplicationContext();
        count_limit = 5;
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        count = 0;

        this.feedService = new FeedService(context);


        //Init Elements
        TextView version = ((TextView) findViewById(R.id.version));
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("v " + pInfo.versionName);
        } catch(PackageManager.NameNotFoundException e){
            version.setText("v 1.0.0");
        }

        textView = ((TextView)findViewById (R.id.textView));
        progressBar = ((ProgressBar)findViewById (R.id.progressBar));

        if (!Utils.isConnected(this)){
            // No connectivity - Load cache
            textView.setText(getResources().getString(R.string.tryingcache));
            feedService.getRssFeedFromCache();

            if(feedService.emptyFeeds()){
                Log.d(TAG, "EMPTY CACHE");
                textView.setText(getResources().getString(R.string.emptycache));
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                // Load from cache
                Bundle bundle = new Bundle();
                bundle.putSerializable("feedEvents", feedService.getFeedEvents());
                bundle.putSerializable("feedNews", feedService.getFeedNews());
                bundle.putSerializable("feedPartners", feedService.getFeedPartners());
                bundle.putSerializable("survivalGuide", feedService.getSurvivalguide());
                intent.putExtras(bundle);
                count = count_limit;
                launchHomeActivity();
            }

            final Button buttonretry = (Button) findViewById(R.id.retry);
            buttonretry.setVisibility(View.VISIBLE);
            buttonretry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                    finish();
                }
            });

            final Button buttonsection = (Button) findViewById(R.id.changesection);
            buttonsection.setVisibility(View.VISIBLE);
            buttonsection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    setResult(ApplicationConstants.RESULT_FIRST_LAUNCH,returnIntent);
                    finish();
                }
            });

        }else{
            // Push for GCM
            if (Utils.getDefaults(context, REG_ID) != null) {
                // No need to push GCM
                count_limit--;
            }
            else {
                // Registering RegID
                registerUser();
            }

            // Connected - Start parsing
            textView.setText(R.string.load_survival_start);
            new AsyncLoadXMLSurvivalGuide(this).execute();
            new AsyncLoadXMLFeedEvents(this).execute();
            new AsyncLoadXMLFeedNews(this).execute();
            new AsyncLoadXMLFeedPartners(this).execute();
        }
	}

    public void onResume(){
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            pushMsg = getIntent().getExtras().getString("title");
        }
    }

    public void launchHomeActivity(){
        if (count == count_limit) {

            int total = 0;
            total += feedService.getFeedEvents().getItemCount();
            total += feedService.getFeedNews().getItemCount();
            total += feedService.getFeedPartners().getItemCount();
            total += feedService.getSurvivalguide().getCategories().size();

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
            Intent returnIntent = new Intent();
            if (resultCode == RESULT_CANCELED){
                setResult(ApplicationConstants.RESULT_CLOSE_ALL, returnIntent);
                finish();
            } else if (resultCode == ApplicationConstants.RESULT_FIRST_LAUNCH){
                setResult(ApplicationConstants.RESULT_FIRST_LAUNCH, returnIntent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // GCM
    public void registerUser() {
        if (checkPlayServices()) {
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
        }
        return true;
    }
}
