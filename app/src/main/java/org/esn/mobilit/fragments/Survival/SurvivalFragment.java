package org.esn.mobilit.fragments.Survival;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import org.esn.mobilit.renderers.GuideRenderer;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.FeedService;

public class SurvivalFragment extends Fragment{
    private static final String TAG = SurvivalFragment.class.getSimpleName();
    private Activity currentActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // -- inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.survival, container,false);

        // Set the Text to try this out
        TextView t = (TextView) myInflatedView.findViewById(R.id.survivalContent);

        GuideRenderer sgr = new GuideRenderer();
        String survivalContent = sgr.renderSurvivalGuide(FeedService.getInstance().getGuide());
        t.setText(Html.fromHtml(survivalContent), TextView.BufferType.SPANNABLE);

        return myInflatedView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        currentActivity = activity;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ras_section_settings:
                if (currentActivity != null) {
                    PreferencesService.resetSection();
                    Intent intent = new Intent(currentActivity, FirstLaunchActivity.class);
                    startActivity(intent);
                    currentActivity.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
