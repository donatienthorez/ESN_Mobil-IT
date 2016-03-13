package org.esn.mobilit.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.feeds.RSSFeedService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ArrayList<Fragment> fragmentsList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Section section = (Section) CacheService.
                getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        if (section == null || TextUtils.isEmpty(section.getWebsite())) {
            Intent intent = new Intent(this, FirstLaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        this.setFragmentList();
        String[] mPlanetTitles = new String[]{
                getString(R.string.title_events),
                getString(R.string.title_news),
                getString(R.string.title_partners),
                getString(R.string.title_survivalguide),
                getString(R.string.title_about)
        };

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        registerRegId();

        Glide.with(MobilITApplication.getContext())
                .load(section.getLogo_url())
                .downloadOnly(150, 250);

        // Manage GCM notifications, to be refactored
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            GCMService.getInstance().setPushMsg(getIntent().getExtras().getString("title"));
            pushReceived();
        }
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    public void pushReceived() {
        RSSFeedParser feedsEvents = EventsService.getInstance().getFeed();
        RSSFeedParser feedsPartners = PartnersService.getInstance().getFeed();
        RSSFeedParser feedsNews = NewsService.getInstance().getFeed();
        RSSFeedParser currentFeed = null;

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

    /**
     * Call the GCMService to register the regId
     */
    private void registerRegId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GCMService.getInstance().register();
            }
        });
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = fragmentsList.get(position);
        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setFragmentList()
    {
        fragmentsList = new ArrayList<Fragment>();
        addToFragmentsList(EventsService.getInstance());
        addToFragmentsList(NewsService.getInstance());
        addToFragmentsList(PartnersService.getInstance());
        fragmentsList.add(new GuideFragment());
        fragmentsList.add(new AboutFragment());
    }

    public void addToFragmentsList(RSSFeedService rssFeedService)
    {
        FeedListFragment listFragment = new FeedListFragment();
        listFragment.setService(rssFeedService);
        fragmentsList.add(listFragment);
    }
}