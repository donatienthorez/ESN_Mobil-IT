
package org.esn.mobilit.fragments.Satellite;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.R;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    @ForApplication
    @Inject
    Context context;
    @Inject
    AppState appState;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.header)
    ImageView imageView;
    @Bind(R.id.desc)
    WebView webView;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private FeedType feedType;
    private int position;
    private RSSItem rssItem;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_feeds, container, false);

        InjectUtil.component().inject(this);
        ButterKnife.bind(this, view);

        this.feedType = (FeedType) getArguments().get("feedType");
        if (feedType == null) {
            //TODO manage that case
        }

        //TODO checks for position
        this.position = getArguments().getInt("position");


        if (savedInstanceState != null) {
            //// FIXME: 07/02/2017 Add this in a application constants
            rssItem = (RSSItem) savedInstanceState.getSerializable("rssItem");
            position = savedInstanceState.getInt("position");
            feedType = (FeedType) savedInstanceState.getSerializable("feedType");
        } else {
            rssItem = appState.getFeed(feedType).get(position);
        }

        if (rssItem != null) {
            title.setText(rssItem.getTitle());

            Glide.with(context)
                    .load(rssItem.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .into(imageView);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<html><head><link href=\"detailsFragment.css\" type=\"text/css\" rel=\"stylesheet\"/></head><body>");
            stringBuilder.append(rssItem.getDescription());
            stringBuilder.append("</body></HTML>");

            webView.loadDataWithBaseURL("file:///android_asset/", stringBuilder.toString(), "text/html", "utf-8", null);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setScrollContainer(false);
            WebSettings webSettings = webView.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setJavaScriptEnabled(true);
        }

        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //// FIXME: 07/02/2017 Add this in a application constants
        outState.putSerializable("rssItem", rssItem);
        outState.putSerializable("feedType", feedType);
        outState.putSerializable("position", position);
    }
}
