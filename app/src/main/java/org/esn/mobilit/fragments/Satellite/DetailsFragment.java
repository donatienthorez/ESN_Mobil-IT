
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
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    @ForApplication
    @Inject
    Context context;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.header)
    ImageView imageView;
    @Bind(R.id.desc)
    WebView webView;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    protected RSSItem rssItem;

    public DetailsFragment setFeed(RSSItem rssItem){
        this.rssItem = rssItem;
        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_feeds, container, false);

        InjectUtil.component().inject(this);
        ButterKnife.bind(this, view);

        //// FIXME: 06/02/2017 use string and get rssitem from an array inside appstate
        if (savedInstanceState != null) {
            rssItem = (RSSItem) savedInstanceState.getSerializable("rssItem");
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
        outState.putSerializable("rssItem", rssItem);
    }
}
