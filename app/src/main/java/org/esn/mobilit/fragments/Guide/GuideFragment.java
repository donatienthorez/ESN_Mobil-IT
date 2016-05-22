package org.esn.mobilit.fragments.Guide;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.renderers.GuideRenderer;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideFragment extends Fragment {

    @Bind(R.id.guideContent) WebView guideContentWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this, view);

        setGuide(GuideService.getInstance().getFromCache());

        GuideService.getInstance().getFromSite(new NetworkCallback<Guide>() {
            @Override
            public void onSuccess(Guide result) {
                setGuide(result);
            }

            @Override
            public void onNoAvailableData() {
            }

            @Override
            public void onFailure(String error) {
            }
        });

        return view;
    }

    public void setGuide(Guide guide) {
        String survivalContent = getText(R.string.info_message_guide_not_displayable).toString();
        if (guide != null && guide.isActivated() && guide.isCreated()) {
            GuideRenderer sgr = new GuideRenderer();
            survivalContent = sgr.renderSurvivalGuide(guide);
        }

        guideContentWebView.loadData(survivalContent, "text/html; charset=UTF-8", null);
        guideContentWebView.setScrollContainer(false);

        // disable scroll on touch
        guideContentWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        WebSettings webSettings = guideContentWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultFontSize(10);
        webSettings.setJavaScriptEnabled(true);
    }
}
