package org.esn.mobilit.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.DetailActivity;
import org.esn.mobilit.utils.image.ImageLoader;
import org.esn.mobilit.utils.parser.RSSFeed;

/**
 * Created by Spider on 06/01/15.
 */
public class ListFragment extends android.support.v4.app.ListFragment {
    private static final String TAG = ListFragment.class.getSimpleName();
    private RSSFeed feed;
    private CustomListAdapter adapter;
    private int type;
    private Activity currentActivity;
    private ImageLoader imageLoader;
    private LayoutInflater layoutInflater;

    public void setFeed(RSSFeed feed){
        this.feed = feed;
    }
    public void setType(int type){
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Set an Adapter to the ListView
        adapter = new CustomListAdapter(currentActivity);
        this.setListAdapter(adapter);

        /*ArrayList list = new ArrayList();
        layoutInflater = (LayoutInflater) currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(currentActivity.getApplicationContext());

        for (int i = 0; i < feed.getItemCount(); i++) {
            HashMap hashMap = new HashMap();
            hashMap.put("title", feed.getItem(i).getTitle());
            hashMap.put("date", feed.getItem(i).getDate());
            //hashMap.put("thumb", R.drawable.ic_launcher);

            //Image
            View listItem = getView();
            listItem = layoutInflater.inflate(R.layout.list_item, null);
            ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
            //imageLoader.DisplayImage(feed.getItem(i).getImage(), iv);
            hashMap.put("thumb", imageLoader.getBitmap(feed.getItem(i).getImage()));

            //Add hashMap to view
            list.add(hashMap);
        }

        String[] from = new String[]{"title", "date", "thumb"};
        int[] to = new int[]{R.id.title, R.id.date, R.id.thumb};
        this.setListAdapter(new SimpleAdapter(getActivity(), list, R.layout.list_item, from, to));*/

    }

    public void onActivityCreated (Bundle savedInstanceState){
        ColorDrawable c = null;

        switch(type){
            case 0 : c = new ColorDrawable(Color.rgb(112, 185, 24)); break; //ESN GREEN -> EVENTS
            case 1 : c = new ColorDrawable(Color.rgb(222, 0, 126)); break;  //ESN PINK  -> NEWS
            case 2 : c = new ColorDrawable(Color.rgb(235, 103, 0)); break;  //ESN ORANGE-> PARTNERS
            case 3 : c = new ColorDrawable(Color.rgb(33, 155, 243)); break; //ESN BLUE  -> GUIDE
        }
        this.getListView().setDivider(c);

        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();

        // actions to be performed when a list item clicked
        Bundle bundle = new Bundle();
        bundle.putSerializable("feed", feed);
        Intent intent = new Intent(currentActivity,
                DetailActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("pos", position);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.ras_section_settings:

                if (getActivity() == null)
                    Log.d(TAG,"getActivity() is null");
                if (currentActivity == null) {
                    Log.d(TAG, "currentActivity is null");
                }
                else {
                    printPreferences();
                    Log.d(TAG, "RAS SECTION SETTINGS");
                    reset_section();
                    printPreferences();

                    currentActivity.setResult(1);
                    currentActivity.finish();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        currentActivity = activity;
    }

    // PREFERENCES
    public void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        return preferences.getString(key, null);
    }

    public void reset_section(){
        setDefaults("CODE_SECTION", null);
        setDefaults("CODE_COUNTRY", null);
        setDefaults("SECTION_WEBSITE", null);
    }

    public void printPreferences(){
        Log.d(TAG, "CODE_COUNTRY :" + getDefaults("CODE_COUNTRY"));
        Log.d(TAG, "CODE_SECTION :" + getDefaults("CODE_SECTION"));
        Log.d(TAG, "SECTION_WEBSITE :" + getDefaults("SECTION_WEBSITE"));
    }





    class CustomListAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        public ImageLoader imageLoader;

        public CustomListAdapter(Activity activity) {

            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(activity.getApplicationContext());
        }

        @Override
        public int getCount() {
            // Set the total list item count
            return feed.getItemCount();
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
                listItem = layoutInflater.inflate(R.layout.list_item, null);
            }

            // Initialize the views in the layout
            ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
            TextView tvTitle = (TextView) listItem.findViewById(R.id.title);
            TextView tvDate = (TextView) listItem.findViewById(R.id.date);

            // Set the views in the layout
            imageLoader.DisplayImage(feed.getItem(pos).getImage(), iv);
            tvTitle.setText(feed.getItem(pos).getTitle());
            tvDate.setText(feed.getItem(pos).getDate());

            return listItem;
        }
    }

}
