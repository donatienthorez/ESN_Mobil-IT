package org.esn.mobilit.fragments.Satellite;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.utils.image.ImageLoader;

public class DetailsFragment extends Fragment {

    RSSItem fFeed;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fFeed = (RSSItem) getArguments().getSerializable("feed");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** Inflating the layout country_details_fragment_layout to the view object v */
        View view = inflater.inflate(R.layout.detail_fragment, null);

        /** Getting the textview object of the layout to set the details */
        // Initialize views
        TextView title = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.header);
        WebView desc = (WebView) view.findViewById(R.id.desc);


        /* Getting the bundle object passed from MainActivity */
        Bundle b = getArguments();

        // Set the views
        title.setText(fFeed.getTitle());

        ImageLoader imageLoader = new ImageLoader(getActivity().getApplicationContext());
        imageLoader.displayImage(fFeed.getImage(), imageView);

        desc.loadDataWithBaseURL("" +
                "http://www.androidcentral.com/",
                fFeed.getDescription(),
                "text/html",
                "UTF-8",
                null
        );

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
