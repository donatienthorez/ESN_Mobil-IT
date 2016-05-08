package org.esn.mobilit.fragments.Guide;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.renderers.GuideRenderer;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GuideFragment extends Fragment {

    TextView guideContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_guide, container, false);
        guideContent = (TextView) myInflatedView.findViewById(R.id.guideContent);

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

        return myInflatedView;
    }

    public void setGuide(Guide guide) {
        GuideRenderer sgr = new GuideRenderer();
        String survivalContent = sgr.renderSurvivalGuide(guide);
        if (guide.isActivated() && guide.isCreated()) {
            guideContent.setText(Html.fromHtml(survivalContent), TextView.BufferType.SPANNABLE);
        } else {
            guideContent.setText(R.string.info_message_guide_not_displayable, TextView.BufferType.SPANNABLE);
        }
    }
}
