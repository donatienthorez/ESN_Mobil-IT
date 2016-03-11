package org.esn.mobilit.fragments.Satellite;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
    @Bind(R.id.progress) ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fFeed = (RSSItem) getArguments().getSerializable("feed");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail_feeds, null);

        // Load Butterknife
        ButterKnife.bind(this, view);

        title.setText(fFeed.getTitle());

        Glide.with(MobilITApplication.getContext())
                .load(fFeed.getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(imageView);

        desc.loadData(fFeed.getDescription(), "text/html; charset=UTF-8", null);

        sv.setVerticalFadingEdgeEnabled(true);

        WebSettings ws = desc.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setLightTouchEnabled(false);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptEnabled(true);

        return view;
    }
}
