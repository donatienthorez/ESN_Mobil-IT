package org.esn.mobilit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.esn.mobilit.parser.RSSFeed;

public class DetailActivity extends Activity {
    private static final String TAG = DetailActivity.class.getSimpleName();
	RSSFeed feed;
	TextView title,link;
	WebView desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		// Enable the vertical fading edge (by default it is disabled)
		ScrollView sv = (ScrollView) findViewById(R.id.sv);
		sv.setVerticalFadingEdgeEnabled(true);

		// Get the feed object and the position from the Intent
		feed = (RSSFeed) getIntent().getExtras().get("feed");
		int pos = getIntent().getExtras().getInt("pos");

		// Initialize the views
		title = (TextView) findViewById(R.id.title);
        link = (TextView) findViewById(R.id.linkDetail);
 		desc = (WebView) findViewById(R.id.desc);

		// set webview properties
		WebSettings ws = desc.getSettings();
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws.getPluginState();
		ws.setPluginState(PluginState.ON);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);

		// Set the views
		title.setText(feed.getItem(pos).getTitle());
        link.setText(feed.getItem(pos).getLink());
		desc.loadDataWithBaseURL(null, feed
				.getItem(pos).getDescription(), "text/html", "UTF-8", null);

        Log.d(TAG,feed
                .getItem(pos).getDescription());
	}

}
