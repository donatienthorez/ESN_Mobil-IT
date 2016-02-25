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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.RSS.RSSItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    RSSItem fFeed;

    @Bind(R.id.title) TextView title;
    @Bind(R.id.header) ImageView imageView;
    @Bind(R.id.desc) WebView desc;
    @Bind(R.id.sv) ScrollView sv;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fFeed = (RSSItem) getArguments().getSerializable("feed");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.detail_fragment, null);

        // Load Butterknife
        ButterKnife.bind(this, view);

        title.setText(fFeed.getTitle());

        Glide.with(MobilITApplication.getContext())
                .load(fFeed.getImage())
                .placeholder(R.drawable.default_list_item)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(imageView);

        desc.loadDataWithBaseURL("" +
                "http://www.androidcentral.com/",
                fFeed.getDescription(),
                "text/html",
                "UTF-8",
                null
        );

        sv.setVerticalFadingEdgeEnabled(true);

        WebSettings ws = desc.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setLightTouchEnabled(false);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptEnabled(true);

        return view;
    }
}
