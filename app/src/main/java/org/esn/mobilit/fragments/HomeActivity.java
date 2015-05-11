package org.esn.mobilit.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ListView;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.Satellite.DetailActivity;
import org.esn.mobilit.fragments.Satellite.ListFragment.ListFragmentItemClickListener;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.image.InternalStorage;
import org.esn.mobilit.utils.parser.RSSFeed;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener, ListFragmentItemClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Context homeActivityContext;

    ViewPager myPager;
    MyFragmentPagerAdapter myAdapter;
    RSSFeed feedEvents, feedNews, feedPartners;
    SurvivalGuide survivalGuide;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change Layout
        //getActionBar().setIcon(R.drawable.ic_launcher);
        setContentView(R.layout.activity_main);

        // Save context
        homeActivityContext = getApplicationContext();

        // Get feed form cache
        feedEvents = (RSSFeed) getObjectFromCache("feedEvents");
        feedNews = (RSSFeed) getObjectFromCache("feedNews");
        feedPartners = (RSSFeed) getObjectFromCache("feedPartners");
        survivalGuide = (SurvivalGuide) getObjectFromCache("survivalGuide");

        //Count numbers of available tabs
        int totalTabs = 0;
        if (feedEvents.getItemCount() > 0) totalTabs++;
        if (feedNews.getItemCount() > 0) totalTabs++;
        if (feedPartners.getItemCount() > 0) totalTabs++;
        if (survivalGuide.getCategories().size() > 0) totalTabs++;

        //Init FragmentPagerAdapter
        MyFragmentPagerAdapter fpa = new MyFragmentPagerAdapter(getSupportFragmentManager(), totalTabs);
        fpa.setFeedEvents(feedEvents);
        fpa.setFeedNews(feedNews);
        fpa.setFeedPartners(feedPartners);
        fpa.setSurvivalGuide(survivalGuide);
        fpa.setTabsList();
        myAdapter = fpa;

        //Init ActionBar
        final ActionBar actionBar = getActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
        }

        //Init Pager
        myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(myAdapter);
        myPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //Add tabs
        if (feedEvents.getItemCount() > 0)
            actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.title_events)).setTabListener(this));
        if (feedNews.getItemCount() > 0)
            actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.title_news)).setTabListener(this));
        if (feedPartners.getItemCount() > 0)
            actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.title_partners)).setTabListener(this));
        if (survivalGuide.getCategories().size() > 0)
            actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.title_survivalguide)).setTabListener(this));

        if (getIntent().getBooleanExtra("pushReceived", false)) pushReceived();
    }

    public void pushReceived() {
        String pushMsg = getIntent().getStringExtra("pushMsg");

        Log.d(TAG, "pushTitle length: " + pushMsg.length());

        int pos = -1;
        RSSFeed currentfeed = null;
        ListView lv = null;
        if (feedEvents.getPositionFromTitle(pushMsg) > 0) {
            pos = feedEvents.getPositionFromTitle(pushMsg);
            currentfeed = feedEvents;
        }
        if (feedNews.getPositionFromTitle(pushMsg) > 0) {
            pos = feedNews.getPositionFromTitle(pushMsg);
            currentfeed = feedNews;
        }
        if (feedPartners.getPositionFromTitle(pushMsg) > 0) {
            pos = feedPartners.getPositionFromTitle(pushMsg);
            currentfeed = feedPartners;
        }

        if (currentfeed != null && pos > 0) {
            Intent intent = new Intent(this, DetailActivity.class);

            Bundle b = new Bundle();
            b.putSerializable("feed", currentfeed);
            b.putInt("pos", pos);
            intent.putExtras(b);

            startActivity(intent);
        }
    }

    // PREFERENCES
    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(key, null);
    }

    public Object getObjectFromCache(String key) {
        Object o = null;
        key = getDefaults("CODE_SECTION") + "_" + key;
        try {
            o = InternalStorage.readObject(homeActivityContext, key);
        } catch (Exception e) {
            Log.d(TAG, "Exception getobject: " + e);
        }
        return o;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("viewpagerid", myPager.getId());
    }

    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
        myPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onListFragmentItemClick(int position, RSSFeed currentfeed) {
        /** Creating an intent object to start the CountryDetailsActivity */
        Intent intent = new Intent(this, DetailActivity.class);

        /** Setting data ( the clicked item's position ) to this intent */
        Bundle b = new Bundle();
        b.putSerializable("feed", currentfeed);
        b.putInt("pos", position);
        intent.putExtras(b);

        /** Starting the activity by passing the implicit intent */
        startActivity(intent);
    }

    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Log.d(TAG, "PAUSE");
    }

    public void onStop(){
        super.onStop();  // Always call the superclass method first
        Log.d(TAG, "STOP");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "DESTROY");
    }
}