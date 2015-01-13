package org.esn.mobilit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.esn.mobilit.R;
import org.esn.mobilit.utils.CustomListAdapter;
import org.esn.mobilit.utils.parser.RSSFeed;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Spider on 06/01/15.
 */
public class ListFragment extends android.support.v4.app.ListFragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    private RSSFeed feed;
    private CustomListAdapter adapter;

    public void setFeed(RSSFeed feed){
        this.feed = feed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList list = new ArrayList();

        for (int i = 0; i < feed.getItemCount(); i++) {
            HashMap hashMap = new HashMap();
            hashMap.put("title", feed.getItem(i).getTitle());
            hashMap.put("date", feed.getItem(i).getDate());
            hashMap.put("thumb", R.drawable.ic_launcher);
            //imageLoader.DisplayImage(feed.getItem(i).getImage(), iv);
            list.add(hashMap);
        }

        String[] from = new String[]{"title", "date", "thumb"};
        int[] to = new int[]{R.id.title, R.id.date, R.id.thumb};
        this.setListAdapter(new SimpleAdapter(getActivity(), list, R.layout.list_item, from, to));
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }
}
