package org.esn.mobilit.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.esn.mobilit.R;
import org.esn.mobilit.adapters.PagerAdapter;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;

    private FeedService feedService;
    private ActionBar actionBar;
    private RSSFeedParser currentFeed;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedService = FeedService.getInstance();
        PagerAdapter pagerAdapter = new PagerAdapter(
                getSupportFragmentManager()
        );

        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        initTabs();

        if (!GCMService.getInstance().getPushMsg().equals("")) {
            pushReceived();
        }
    }

    private void initTabs(){
        RSSFeedParser feedsEvents = EventsService.getInstance().getFeed();
        RSSFeedParser feedsPartners = PartnersService.getInstance().getFeed();
        RSSFeedParser feedsNews = NewsService.getInstance().getFeed();

        setRSSFeedTab(R.string.title_events, feedsEvents);
        setRSSFeedTab(R.string.title_news, feedsPartners);
        setRSSFeedTab(R.string.title_partners, feedsNews);

        if (feedService.getGuide() != null && feedService.getGuide().getNodes() != null && feedService.getGuide().getNodes().size() > 0){
            addTab(R.string.title_survivalguide);
        }

        if (CacheService.getObjectFromCache("section") != null){
            addTab(R.string.title_about);
        }
    }

    private void setRSSFeedTab(int stringId, RSSFeedParser rssFeedParser){
        if (rssFeedParser != null && rssFeedParser.getItemCount() > 0) {
            addTab(stringId);
        }
    }

    private void addTab(int stringId){
        ActionBar.Tab tabEvent = actionBar.newTab();
        tabEvent.setText(getResources().getString(stringId));
        tabEvent.setTabListener(this);
        actionBar.addTab(tabEvent);
    }

    public void pushReceived() {
        RSSFeedParser feedsEvents = EventsService.getInstance().getFeed();
        RSSFeedParser feedsPartners = PartnersService.getInstance().getFeed();
        RSSFeedParser feedsNews = NewsService.getInstance().getFeed();

        List<RSSFeedParser> rssFeedParsers = new ArrayList<RSSFeedParser>();
        rssFeedParsers.add(feedsEvents);
        rssFeedParsers.add(feedsPartners);
        rssFeedParsers.add(feedsNews);
        int i = 0, position;
        do {
            position = rssFeedParsers.get(i).getPositionFromTitle(GCMService.getInstance().getPushMsg());
            if (position > 0) {
                currentFeed = rssFeedParsers.get(i);
            }
            i++;
        } while (currentFeed != null || i == rssFeedParsers.size()-1);

        if (currentFeed != null) {
            Intent intent = new Intent(this, DetailActivity.class);

            Bundle b = new Bundle();
            b.putSerializable("feed", currentFeed);
            b.putInt("pos", position);
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
    public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

    }
}