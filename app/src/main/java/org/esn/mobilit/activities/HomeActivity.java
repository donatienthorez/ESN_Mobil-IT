package org.esn.mobilit.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.RelativeLayout;

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

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ArrayList<Fragment> fragmentsList;
    private RelativeLayout drawerRelativeLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

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
        String[] titles = new String[]{
                getString(R.string.menu_drawer_item_events),
                getString(R.string.menu_drawer_item_news),
                getString(R.string.menu_drawer_item_partners),
                getString(R.string.menu_drawer_item_guide),
                getString(R.string.menu_drawer_item_about)
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                drawerLayout.closeDrawers();
                switch(menuItem.getItemId()) {
                    case R.id.drawer_item_events:
                        selectItem(0);
                        break;
                    case R.id.drawer_item_news:
                        selectItem(1);
                        break;
                    case R.id.drawer_item_partners:
                        selectItem(2);
                        break;
                    case R.id.drawer_item_guide:
                        selectItem(3);
                        break;
                    case R.id.drawer_item_about:
                        selectItem(4);
                        break;
                }

                return true;
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = fragmentsList.get(position);
        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        drawerLayout.closeDrawer(drawerRelativeLayout);
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