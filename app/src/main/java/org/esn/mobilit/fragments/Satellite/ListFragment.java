package org.esn.mobilit.fragments.Satellite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.activities.FirstLaunchActivity;
import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.adapters.ListAdapter;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeedParser;


public class ListFragment extends android.support.v4.app.ListFragment
{
    private RSSFeedParser feed;
    private Activity currentActivity;

    ListFragmentItemClickListener itemClickListener;

    public void setFeed(RSSFeedParser feed){
        this.feed = feed;
    }

    public interface ListFragmentItemClickListener {
        void onListFragmentItemClick(int position, RSSFeedParser feed);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        LayoutInflater layoutInflater = (LayoutInflater) currentActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        LayoutInflater layoutInflater = LayoutInflater.from(MobilITApplication.getContext());

        // Set an Adapter to the ListView
        ListAdapter adapter = new ListAdapter(feed, layoutInflater);
        this.setListAdapter(adapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        itemClickListener.onListFragmentItemClick(position, feed);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        currentActivity = activity;

        try{
            /** This statement ensures that the hosting activity implements ListFragmentItemClickListener */
            itemClickListener = (ListFragmentItemClickListener) activity;
        }catch(Exception e){
            Toast.makeText(activity.getBaseContext(), "Exception onAttach ListFragment", Toast.LENGTH_SHORT).show();
        }
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
}
