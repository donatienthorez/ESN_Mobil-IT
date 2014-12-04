package org.esn.mobilit;

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

import org.esn.mobilit.parser.DOMParser;
import org.esn.mobilit.parser.RSSFeed;

/*
 * Links
 * http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
 */
public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
	private RSSFeed feed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

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
			new AsyncLoadXMLFeed().execute();
            //new DownloadJSONSection().execute();
		}

	}

	private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
            // Get feed url
            SharedPreferences.Editor spOptionEditor;
            SharedPreferences spOptions = getSharedPreferences("section", 0);
            String base_url = spOptions.getString("SECTION_WEBSITE", null);
            String event_url = base_url + "/events/feed";
            Log.d(TAG, "URL:" + event_url);

			// Obtain feed
			DOMParser myParser = new DOMParser();
			feed = myParser.parseXml(event_url);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Bundle bundle = new Bundle();
			bundle.putSerializable("feed", feed);

			// launch List activity
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.putExtras(bundle);

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
