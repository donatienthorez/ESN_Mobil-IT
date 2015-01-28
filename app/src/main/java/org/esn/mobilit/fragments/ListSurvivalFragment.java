package org.esn.mobilit.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.esn.mobilit.R;
import org.esn.mobilit.models.SurvivalGuide;

/**
 * Created by aymeric on 06/01/15.
 */
public class ListSurvivalFragment extends ListFragment {

    private SurvivalGuide survivalGuide;
    private CustomListAdapter adapter;
    private Activity currentActivity;

    ListFragmentItemClickListener itemClickListener;

    /** An interface for defining the callback method */
    public interface ListFragmentItemClickListener {
        /** This method will be invoked when an item in the ListFragment is clicked */
        void onListFragmentItemClick(int position, SurvivalGuide survivalGuide);
    }

    public void setSurvivalGuide(SurvivalGuide survivalGuide){
        this.survivalGuide = survivalGuide;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set an Adapter to the ListView
        adapter = new CustomListAdapter(currentActivity);
        this.setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //itemClickListener.onListFragmentItemClick(position, survivalGuide);
        Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        currentActivity = activity;
    }

    class CustomListAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public CustomListAdapter(Activity activity) {

            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // Set the total list item count
            return survivalGuide.getFirstlevel().size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Inflate the item layout and set the views
            View listItem = convertView;
            int pos = position;
            if (listItem == null) {
                listItem = layoutInflater.inflate(R.layout.list_item_survival, null);
            }

            // Initialize the views in the layout
            TextView tvTitle = (TextView) listItem.findViewById(R.id.title);

            // Set the views in the layout
            tvTitle.setText(survivalGuide.getFirstlevel().get(pos).getName());

            return listItem;
        }
    }
}
