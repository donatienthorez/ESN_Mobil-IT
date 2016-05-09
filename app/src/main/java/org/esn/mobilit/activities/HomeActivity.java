package org.esn.mobilit.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.HashMap;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.gcm.RegIdService;
import org.esn.mobilit.utils.ApplicationConstants;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)   protected DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)     protected RelativeLayout drawerRelativeLayout;
    @Bind(R.id.toolbar)         protected Toolbar toolbar;
    @Bind(R.id.navigation_view) protected NavigationView navigationView;
    private HashMap<String, Fragment> fragmentHashMap;
    private int currentFragmentId;
    private Section section;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        section = (Section) CacheService.
                getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        if (section == null || TextUtils.isEmpty(section.getWebsite())) {
            Intent intent = new Intent(this, FirstLaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        ButterKnife.bind(this);

        registerRegId();
        buildMenu();

        Intent intent = getIntent();

        if (intent != null) {
            RSSItem rssItem = (RSSItem) intent.getSerializableExtra(ApplicationConstants.GCM_RSS_ITEM);

            if (rssItem != null) {
                uncheckNavigationViewItems();
                this.replaceByDetailsFragment(rssItem, false);
            }
        }
    }

    /**
     * Build the left drawer menu.
     */
    private void buildMenu() {
        setFragmentHashMap();
        Object defaultMenu = CacheService.getObjectFromCache(ApplicationConstants.CACHE_DEFAULT_MENU);
        executeDrawerMenuAction(defaultMenu != null ? (int) defaultMenu : R.id.drawer_item_news);
        setSupportActionBar(toolbar);

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
     * Call the RegIdService to register the regId.
     */
    private void registerRegId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RegIdService.getInstance().register();
            }
        });
    }

    private void executeDrawerMenuAction(int menuItemId) {
        switch (menuItemId) {
            case R.id.drawer_item_news:
                CacheService.saveObjectToCache(ApplicationConstants.CACHE_DEFAULT_MENU, R.id.drawer_item_news);
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_NEWS), menuItemId, false);
                break;
            case R.id.drawer_item_events:
                CacheService.saveObjectToCache(ApplicationConstants.CACHE_DEFAULT_MENU, R.id.drawer_item_events);
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_EVENTS), menuItemId, false);
                break;
            case R.id.drawer_item_partners:
                CacheService.saveObjectToCache(ApplicationConstants.CACHE_DEFAULT_MENU, R.id.drawer_item_partners);
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_PARTNERS), menuItemId, false);
                break;
            case R.id.drawer_item_guide:
                CacheService.saveObjectToCache(ApplicationConstants.CACHE_DEFAULT_MENU, R.id.drawer_item_guide);
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_GUIDE), menuItemId, false);
                break;
            case R.id.drawer_item_about:
                CacheService.saveObjectToCache(ApplicationConstants.CACHE_DEFAULT_MENU, R.id.drawer_item_about);
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_ABOUT), menuItemId, false);
                break;
            case R.id.drawer_item_reset:
                PreferencesService.resetSection();
                Intent intent = new Intent(this, FirstLaunchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    public void loadFragment(Fragment fragment, int currentFragmentId, boolean addToBackStack) {
        this.currentFragmentId = currentFragmentId;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        navigationView.setCheckedItem(currentFragmentId);
    }

    public void replaceByDetailsFragment(RSSItem rssItem, boolean addToBackStack){
        Fragment fragment = (new DetailsFragment()).setFeed(rssItem);
        loadFragment(fragment, this.currentFragmentId, addToBackStack);
    }

    public void setFragmentHashMap()
    {
        fragmentHashMap = new HashMap<>();
        fragmentHashMap.put(
                ApplicationConstants.MENU_NEWS,
                (new FeedListFragment()).setService(NewsService.getInstance())
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_EVENTS,
                (new FeedListFragment()).setService(EventsService.getInstance())
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_PARTNERS,
                (new FeedListFragment()).setService(PartnersService.getInstance())
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_GUIDE,
                new GuideFragment()
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_ABOUT,
                new AboutFragment()
        );
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