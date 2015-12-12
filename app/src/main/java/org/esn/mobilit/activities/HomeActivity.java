package org.esn.mobilit.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.esn.mobilit.R;
import org.esn.mobilit.adapters.FragmentPagerAdapter;
import org.esn.mobilit.fragments.Satellite.ListFragment.ListFragmentItemClickListener;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener, ListFragmentItemClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;

    FeedService feedService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change Layout
        setContentView(R.layout.activity_main);

        feedService = FeedService.getInstance();

        //Init FragmentPagerAdapter
        pagerAdapter = new FragmentPagerAdapter(
                getSupportFragmentManager()
        );

        //Init ActionBar
        final ActionBar actionBar = getActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e);
        }

        //Init Pager
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
            Section section = (Section) Utils.getObjectFromCache("section");

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
        RSSFeedParser currentFeed = null;
        if (feedService.getFeedEvents().getPositionFromTitle(pushMsg) > 0) {
            pos = feedService.getFeedEvents().getPositionFromTitle(pushMsg);
            currentFeed = feedService.getFeedEvents();
        }
        if (feedService.getFeedNews().getPositionFromTitle(pushMsg) > 0) {
            pos = feedService.getFeedNews().getPositionFromTitle(pushMsg);
            currentFeed = feedService.getFeedNews();
        }
        if (feedService.getFeedPartners().getPositionFromTitle(pushMsg) > 0) {
            pos = feedService.getFeedPartners().getPositionFromTitle(pushMsg);
            currentFeed = feedService.getFeedPartners();
        }

        if (currentFeed != null && pos > 0) {
            Intent intent = new Intent(this, DetailActivity.class);

            Bundle b = new Bundle();
            b.putSerializable("feed", currentFeed);
            b.putInt("pos", pos);
            intent.putExtras(b);

            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("viewpagerid", viewPager.getId());
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
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