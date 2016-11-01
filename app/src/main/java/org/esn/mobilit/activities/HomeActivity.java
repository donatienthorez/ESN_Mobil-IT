package org.esn.mobilit.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.models.Notification;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.gcm.RegIdService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.helpers.FragmentOrganizer;
import org.esn.mobilit.utils.helpers.HomeFragmentOrganizer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)   protected DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)     protected RelativeLayout drawerRelativeLayout;
    @Bind(R.id.toolbar)         protected Toolbar toolbar;
    @Bind(R.id.navigation_view) protected NavigationView navigationView;
    private int currentFragmentId;
    private Section section;
    private FragmentOrganizer fragmentOrganizer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                loadFragment(ApplicationConstants.MENU_NOTIFICATIONS, this.currentFragmentId, true);
                break;
        }
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        section = (Section) getIntent().getSerializableExtra("section");

        if (section == null) {
            section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);
        }

        if (section == null || TextUtils.isEmpty(section.getWebsite())) {
            Intent intent = new Intent(this, FirstLaunchActivity.class);
            startActivity(intent);
        } else {
            Object defaultMenu = CacheService.getObjectFromCache(ApplicationConstants.CACHE_DEFAULT_MENU);
            fragmentOrganizer = new HomeFragmentOrganizer(getFragmentManager(), navigationView, defaultMenu != null ? (int) defaultMenu : R.id.drawer_item_news);
            ButterKnife.bind(this);

            registerRegId();
            buildMenu();
            manageNotificationRedirection();
            //TODO
//            updateSection();

        }
    }

    /**
     * Build the left drawer menu.
     */
    private void buildMenu() {
        setSupportActionBar(toolbar);
        Object defaultMenu = CacheService.getObjectFromCache(ApplicationConstants.CACHE_DEFAULT_MENU);
        executeDrawerMenuAction(defaultMenu != null ? (int) defaultMenu : R.id.drawer_item_news);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                executeDrawerMenuAction(menuItem.getItemId());
                drawerLayout.closeDrawers();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Glide.with(MobilITApplication.getContext())
                .load(section.getLogo_url())
                .downloadOnly(150, 250);
    }

    /**
     * Unchecks all the element of the left drawer menu.
     */
    private void uncheckNavigationViewItems() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(false);
        }
    }

    /**
     * Calls the RegIdService to register the regId.
     */
    private void registerRegId() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegIdService.getInstance().register(section);
            }
        });
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }

    /**
     * Redirects to details page of the notifications.
     */
    private void manageNotificationRedirection() {
        Intent intent = getIntent();

        if (intent != null) {
            Notification notification = (Notification) intent.getSerializableExtra(ApplicationConstants.GCM_NOTIFICATION);
            if (notification != null) {
                loadNotificationFragment(notification);
            }
        }
    }

    public void loadNotificationFragment(Notification notification) {
        RSSItem rssItem = notification.getRssItem();
        fragmentOrganizer.clearBackStack();
        if (rssItem != null) {
            uncheckNavigationViewItems();
            this.loadDetailsFragment(rssItem, false);
            return;
        }
        loadFragment(ApplicationConstants.MENU_NOTIFICATIONS, this.currentFragmentId, false);
    }


//TODO
//    /**
//     * Updates the section..
//     */
//    private void updateSection() {
//        AboutService.getInstance().getFromSite(new NetworkCallback<Section>() {
//            @Override
//            public void onSuccess(Section result) {
//                // If the user is on the about tab it updates the section.
//                if (currentFragmentId == R.id.drawer_item_about) {
//                    ((AboutFragment) fragmentHashMap.get(ApplicationConstants.MENU_ABOUT)).setSection(result);
//                    section = result;
//                }
//            }
//
//            @Override
//            public void onNoAvailableData() {
//            }
//
//            @Override
//            public void onFailure(String error) {
//            }
//        });
//    }

    /**
     * Executes the action of the menuItem id
     *
     * @param menuItemId  Id of the item selected.
     */
    private void executeDrawerMenuAction(int menuItemId) {
        fragmentOrganizer.clearBackStack();
        CacheService.saveObjectToCache(ApplicationConstants.CACHE_DEFAULT_MENU, menuItemId);

        switch (menuItemId) {
            case R.id.drawer_item_news:
                FeedListFragment newsListFragment = (FeedListFragment) loadFragment(ApplicationConstants.MENU_NEWS, menuItemId, false);
                newsListFragment.setRssFeedService(NewsService.getInstance());
                break;
            case R.id.drawer_item_events:
                FeedListFragment eventListFragment = (FeedListFragment) loadFragment(ApplicationConstants.MENU_EVENTS, menuItemId, false);
                eventListFragment.setRssFeedService(EventsService.getInstance());
                break;
            case R.id.drawer_item_partners:
                FeedListFragment partnersListFragment = (FeedListFragment) loadFragment(ApplicationConstants.MENU_PARTNERS, menuItemId, false);
                partnersListFragment.setRssFeedService(PartnersService.getInstance());
                break;
            case R.id.drawer_item_guide:
                loadFragment(ApplicationConstants.MENU_GUIDE, menuItemId, false);
                break;
            case R.id.drawer_item_about:
                loadFragment(ApplicationConstants.MENU_ABOUT, menuItemId, false);
                break;
            case R.id.drawer_item_reset:
                PreferencesService.resetSection();
                Intent intent = new Intent(this, FirstLaunchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    /**
     * Loads the fragment into the content frame.
     * @param fragment Fragment to load.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
    public Fragment loadFragment(String fragment, boolean addToBackStack) {
        return this.fragmentOrganizer.loadFragment(fragment, addToBackStack);
    }

    /**
     * Loads the fragment into the content frame and checked the item in the menu.
     * @param fragment Fragment to load.
     * @param currentFragmentId Id of the current fragment displayed.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
    public Fragment loadFragment(String fragment, int currentFragmentId, boolean addToBackStack) {
        this.currentFragmentId = currentFragmentId;
        navigationView.setCheckedItem(currentFragmentId);
        return this.fragmentOrganizer.loadFragment(fragment, addToBackStack);
    }

    /**
     * Loads the details fragment into the content frame.
     * @param rssItem RssItem to display into the details fragment.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
    public void loadDetailsFragment(RSSItem rssItem, boolean addToBackStack){
        DetailsFragment detailsFragment = (DetailsFragment) loadFragment(ApplicationConstants.FRAGMENT_FEED_DETAILS, addToBackStack);
        detailsFragment.setFeed(rssItem);
    }

    /**
     * Loads the guide fragment into the content frame.
     * @param guide the whole guide.
     * @param currentNode the current displayed node of the guide.
     */
    public void loadGuideFragment(Guide guide, Node currentNode){
        GuideFragment guideFragment = (GuideFragment) loadFragment(ApplicationConstants.FRAGMENT_GUIDE, true);
        guideFragment.setCurrentNode(guide, currentNode);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}