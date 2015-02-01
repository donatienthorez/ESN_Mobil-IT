package org.esn.mobilit.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.ListFragment.ListFragmentItemClickListener;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.parser.RSSFeed;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener, ListFragmentItemClickListener {

    ViewPager               myPager;
    MyFragmentPagerAdapter  myAdapter;
    RSSFeed feedEvents, feedNews, feedPartners;
    SurvivalGuide survivalGuide;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("viewpagerid" , myPager.getId() );
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Get feed form the file
        feedEvents = (RSSFeed) getIntent().getExtras().get("feedEvents");
        feedNews = (RSSFeed) getIntent().getExtras().get("feedNews");
        feedPartners = (RSSFeed) getIntent().getExtras().get("feedPartners");
        survivalGuide = (SurvivalGuide) getIntent().getExtras().get("survivalGuide");

        //Init FragmentPagerAdapter
        MyFragmentPagerAdapter fpa = new MyFragmentPagerAdapter(getSupportFragmentManager(),4);
        fpa.setFeedEvents(feedEvents);
        fpa.setFeedNews(feedNews);
        fpa.setFeedPartners(feedPartners);
        fpa.setSurvivalGuide(survivalGuide);
        myAdapter = fpa;

        //Init ActionBar
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

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
        actionBar.addTab(actionBar.newTab().setText("Events").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("News").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Partners").setTabListener(this));

        if (survivalGuide.getFirstlevel().size() > 0)
            actionBar.addTab(actionBar.newTab().setText("Guide").setTabListener(this));
    }

    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft){
        myPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft){
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft){
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
}
