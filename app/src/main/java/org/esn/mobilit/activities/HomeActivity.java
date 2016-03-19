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
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)   protected DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)     protected RelativeLayout drawerRelativeLayout;
    @Bind(R.id.toolbar)         protected Toolbar toolbar;
    @Bind(R.id.navigation_view) protected NavigationView navigationView;
    private ArrayList<Fragment> fragmentsList;
    private int currentFragmentId;

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

        ButterKnife.bind(this);
        setFragmentList();
        doDrawerMenuAction(R.id.drawer_item_events);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() != currentFragmentId) {
                    doDrawerMenuAction(menuItem.getItemId());
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
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
            doDrawerMenuAction(0);
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

    private void doDrawerMenuAction(int menuItemId) {
        switch (menuItemId) {
            case R.id.drawer_item_events:
                loadFragment(fragmentsList.get(0), menuItemId);
                break;
            case R.id.drawer_item_news:
                loadFragment(fragmentsList.get(1), menuItemId);
                break;
            case R.id.drawer_item_partners:
                loadFragment(fragmentsList.get(2), menuItemId);
                break;
            case R.id.drawer_item_guide:
                loadFragment(fragmentsList.get(3), menuItemId);
                break;
            case R.id.drawer_item_about:
                loadFragment(fragmentsList.get(4), menuItemId);
                break;
            case R.id.drawer_item_reset:
                PreferencesService.resetSection();
                Intent intent = new Intent(this, FirstLaunchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    public void loadFragment(Fragment fragment, int currentFragmentId) {
        this.currentFragmentId = currentFragmentId;

        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        navigationView.setCheckedItem(currentFragmentId);
    }

    public void setFragmentList()
    {
        fragmentsList = new ArrayList<>();
        fragmentsList.add((new FeedListFragment()).setService(EventsService.getInstance()));
        fragmentsList.add((new FeedListFragment()).setService(NewsService.getInstance()));
        fragmentsList.add((new FeedListFragment()).setService(PartnersService.getInstance()));
        fragmentsList.add(new GuideFragment());
        fragmentsList.add(new AboutFragment());
    }
}