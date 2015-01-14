package org.esn.mobilit.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.HomeActivity;
import org.esn.mobilit.utils.parser.DOMParser;
import org.esn.mobilit.utils.parser.RSSFeed;

/*
 * Links
 * http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
 */
public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
	private RSSFeed feedEvents, feedNews, feedPartners;
    private int count;
    private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

        //Init Intent Manager
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        count = 0;

		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() == null
				&& !conMgr.getActiveNetworkInfo().isConnected()
				&& !conMgr.getActiveNetworkInfo().isAvailable()) {
			// No connectivity - Show alert
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Unable to reach server, \nPlease check your connectivity.")
					.setTitle("ESN Mobil IT")
					.setCancelable(false)
					.setPositiveButton("Exit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();

		} else {
			// Connected - Start parsing
			new AsyncLoadXMLFeedEvents().execute();
            new AsyncLoadXMLFeedNews().execute();
            new AsyncLoadXMLFeedPartners().execute();
		}

	}




	private class AsyncLoadXMLFeedEvents extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
            // Get feed url
            SharedPreferences.Editor spOptionEditor;
            SharedPreferences spOptions = getSharedPreferences("section", 0);
            String base_url = spOptions.getString("SECTION_WEBSITE", null);
            String event_url = base_url + "/events/feed";

			// Obtain feed
            Log.d(TAG, "Debut Parser pour events/feed");
			DOMParser myParser = new DOMParser();
            feedEvents = myParser.parseXml(event_url);
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

            if (count == 3)
                startActivityForResult(intent, 1);
		}

	}

    private class AsyncLoadXMLFeedNews extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Get feed url
            SharedPreferences.Editor spOptionEditor;
            SharedPreferences spOptions = getSharedPreferences("section", 0);
            String base_url = spOptions.getString("SECTION_WEBSITE", null);
            String url = base_url + "/news/feed";

            // Obtain feed
            Log.d(TAG, "Debut Parser pour " + url);
            DOMParser myParser = new DOMParser();
            feedNews = myParser.parseXml(url);
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

            if (count == 3)
                startActivityForResult(intent, 1);
        }

    }

    private class AsyncLoadXMLFeedPartners extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Get feed url
            SharedPreferences.Editor spOptionEditor;
            SharedPreferences spOptions = getSharedPreferences("section", 0);
            String base_url = spOptions.getString("SECTION_WEBSITE", null);
            String event_url = base_url + "/partners/feed";

            // Obtain feed
            Log.d(TAG, "Debut Parser pour partners/feed");
            DOMParser myParser = new DOMParser();
            feedPartners = myParser.parseXml(event_url);
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

            if (count == 3)
                startActivityForResult(intent, 1);
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
