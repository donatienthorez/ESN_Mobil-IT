package org.esn.mobilit.fragments.Guide;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.FirstLaunchActivity;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.renderers.GuideRenderer;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GuideFragment extends Fragment {

    TextView guideContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ras_section_settings:
                if (getActivity() != null) {
                    PreferencesService.resetSection();
                    Intent intent = new Intent(getActivity(), FirstLaunchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setGuide(Guide guide) {
        GuideRenderer sgr = new GuideRenderer();
        String survivalContent = sgr.renderSurvivalGuide(guide);
        guideContent.setText(Html.fromHtml(survivalContent), TextView.BufferType.SPANNABLE);
    }
}
