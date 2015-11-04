package org.esn.mobilit.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ListView;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.adapters.FragmentPagerAdapter;
import org.esn.mobilit.fragments.Satellite.ListFragment.ListFragmentItemClickListener;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.image.InternalStorage;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener, ListFragmentItemClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    ViewPager myPager;
    FragmentPagerAdapter myAdapter;

    FeedService feedService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change Layout
        setContentView(R.layout.activity_main);

        feedService = FeedService.getInstance();
        int totalTabs = feedService.getTotalTabs();

        try{
            Section section = (Section) Utils.getObjectFromCache(getApplicationContext(), "section");
            if (section != null) totalTabs++;
        } catch (NullPointerException e){
            Log.d(TAG, e.toString());
        }

        //Init FragmentPagerAdapter
        FragmentPagerAdapter fpa = new FragmentPagerAdapter(
                getSupportFragmentManager(),
                totalTabs
        );

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
        initTabs();

        if(! GCMService.getInstance().getPushMsg().equals("")) {
            pushReceived();
        }
    }

    private void initTabs(){
        if (feedService.getFeedEvents() != null && feedService.getFeedEvents().getItemCount() > 0) {
            ActionBar.Tab tabEvent = getActionBar().newTab();
            tabEvent.setText(getResources().getString(R.string.title_events));
            tabEvent.setTabListener(this);
            getActionBar().addTab(tabEvent);
        }

        if (feedService.getFeedNews() != null && feedService.getFeedNews().getItemCount() > 0){
            ActionBar.Tab tabNews = getActionBar().newTab();
            tabNews.setText(getResources().getString(R.string.title_news));
            tabNews.setTabListener(this);
            getActionBar().addTab(tabNews);
        }

        if (feedService.getFeedPartners() != null && feedService.getFeedPartners().getItemCount() > 0){
            ActionBar.Tab tabPartners = getActionBar().newTab();
            tabPartners.setText(getResources().getString(R.string.title_partners));
            tabPartners.setTabListener(this);
            getActionBar().addTab(tabPartners);
        }

        if (feedService.getSurvivalguide() != null && feedService.getSurvivalguide().getCategories().size() > 0){
            ActionBar.Tab tabSurvivalGuide = getActionBar().newTab();
            tabSurvivalGuide.setText(getResources().getString(R.string.title_survivalguide));
            tabSurvivalGuide.setTabListener(this);
            getActionBar().addTab(tabSurvivalGuide);
        }

        try{
            Section section = (Section) Utils.getObjectFromCache(getApplicationContext(),"section");

            if (section != null){
                ActionBar.Tab tabAbout = getActionBar().newTab();
                tabAbout.setText(getResources().getString(R.string.title_about));
                tabAbout.setTabListener(this);
                getActionBar().addTab(tabAbout);
            }

        }catch (NullPointerException e){
            Log.d(TAG, e.toString());
        }
    }

    public void pushReceived() {
        String pushMsg = GCMService.getInstance().getPushMsg();

        int pos = -1;
        RSSFeedParser currentfeed = null;
        ListView lv = null;
        if (feedService.getFeedEvents().getPositionFromTitle(pushMsg) > 0) {
            pos = feedService.getFeedEvents().getPositionFromTitle(pushMsg);
            currentfeed = feedService.getFeedEvents();
        }
        if (feedService.getFeedNews().getPositionFromTitle(pushMsg) > 0) {
            pos = feedService.getFeedNews().getPositionFromTitle(pushMsg);
            currentfeed = feedService.getFeedNews();
        }
        if (feedService.getFeedPartners().getPositionFromTitle(pushMsg) > 0) {
            pos = feedService.getFeedPartners().getPositionFromTitle(pushMsg);
            currentfeed = feedService.getFeedPartners();
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
            o = InternalStorage.readObject(MobilITApplication.getContext(), key);
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
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        myPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onListFragmentItemClick(int position, RSSFeedParser currentfeed) {
        /** Creating an intent object to start the CountryDetailsActivity */
        Intent intent = new Intent(this, DetailActivity.class);

        /** Setting data ( the clicked item's position ) to this intent */
        Bundle b = new Bundle();
        b.putSerializable("feed", currentfeed.getItem(position));
        intent.putExtras(b);

        /** Starting the activity by passing the implicit intent */
        startActivity(intent);
    }
}