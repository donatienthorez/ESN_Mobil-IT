package org.esn.mobilit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.esn.mobilit.image.FileCache;
import org.esn.mobilit.parser.RSSFeed;


public class DetailFragment extends Fragment {
	private int fPos;
	RSSFeed fFeed;
    FileCache fileCache;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.detail_fragment, container, false);

		// Initialize views
		TextView title = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.header);
		WebView desc = (WebView) view.findViewById(R.id.desc);

		// Set the views
		title.setText(fFeed.getItem(fPos).getTitle());
		desc.loadDataWithBaseURL("http://www.androidcentral.com/", fFeed
				.getItem(fPos).getDescription(), "text/html", "UTF-8", null);

        // Set imageview
        //ImageLoader il = new ImageLoader(this.getActivity().getApplicationContext());
        //il.DisplayImage(fFeed.getItem(fPos).getImage(), imageView);

        // Enable the vertical fading edge (by default it is disabled)
        ScrollView sv = (ScrollView) view.findViewById(R.id.sv);
        sv.setVerticalFadingEdgeEnabled(true);

        // Set webview properties
        WebSettings ws = desc.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setLightTouchEnabled(false);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptEnabled(true);


		return view;
	}
}