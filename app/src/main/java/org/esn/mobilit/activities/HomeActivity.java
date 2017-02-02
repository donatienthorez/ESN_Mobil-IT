package org.esn.mobilit.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.fragments.NotificationFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.models.Notification;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.AboutService;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.services.gcm.RegIdService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)   protected DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)     protected RelativeLayout drawerRelativeLayout;
    @Bind(R.id.toolbar)         protected Toolbar toolbar;
    @Bind(R.id.navigation_view) protected NavigationView navigationView;

    private HashMap<String, Fragment> fragmentHashMap;

    private int currentFragmentId;

    @Inject
    CacheService cacheService;

    @Inject
    PreferencesService preferencesService;
    @Inject
    RegIdService regIdService;
    @Inject
    AboutService aboutService;
    @Inject
    GuideService guideService;

    @ForApplication @Inject
    Context context;

    @Inject
    AppState appState;

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
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_NOTIFICATIONS), this.currentFragmentId, true);
                break;
        }
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InjectUtil.component().inject(this);

        if (!appState.hasValidSection()) {
            Intent intent = new Intent(this, FirstLaunchActivity.class);
            startActivity(intent);
        } else {
            ButterKnife.bind(this);

            // register the user with GCM.
            regIdService.register();
            buildMenu();
            manageNotificationRedirection();
            updateSection();
        }
    }

    /**
     * Build the left drawer menu.
     */
    private void buildMenu() {
        setFragmentHashMap();
        Object defaultMenu = cacheService.get(ApplicationConstants.CACHE_DEFAULT_MENU);
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

        Glide.with(context)
                .load(appState.getSection().getLogo_url())
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
        if (rssItem != null) {
            uncheckNavigationViewItems();
            this.loadDetailsFragment(rssItem, false);
            return;
        }
        this.loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_NOTIFICATIONS), this.currentFragmentId, true);
    }

    /**
     * Updates the section..
     */
    private void updateSection(){
        aboutService.getFromSite(new NetworkCallback<Section>() {
            @Override
            public void onNoConnection(Section section) {
            }

            @Override
            public void onSuccess(Section result) {
                // If the user is on the about tab it updates the section.
                if (currentFragmentId == R.id.drawer_item_about) {
                    ((AboutFragment) fragmentHashMap.get(ApplicationConstants.MENU_ABOUT)).setSection(result);
                    appState.setSection(result, true);
                }
            }

            @Override
            public void onNoAvailableData() {
                //TODO manage onNoAvailableData error.
            }

            @Override
            public void onFailure(String error) {
                //TODO manage onFailure error.
            }
        });
    }

    /**
     * Executes the action of the menuItem id
     *
     * @param menuItemId  Id of the item selected.
     */
    private void executeDrawerMenuAction(int menuItemId) {
        clearFragmentBackStack();
        cacheService.save(ApplicationConstants.CACHE_DEFAULT_MENU, menuItemId);

        switch (menuItemId) {
            case R.id.drawer_item_news:
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_NEWS), menuItemId, false);
                break;
            case R.id.drawer_item_events:
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_EVENTS), menuItemId, false);
                break;
            case R.id.drawer_item_partners:
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_PARTNERS), menuItemId, false);
                break;
            case R.id.drawer_item_guide:
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_GUIDE), menuItemId, false);
                break;
            case R.id.drawer_item_about:
                loadFragment(fragmentHashMap.get(ApplicationConstants.MENU_ABOUT), menuItemId, false);
                break;
            case R.id.drawer_item_reset:
                preferencesService.resetSection();
                Intent intent = new Intent(this, FirstLaunchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    /**
     * Loads the fragment into the content frame.
     * @param fragment Fragment to load.
     * @param currentFragmentId Id of the current fragment displayed.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
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

    /**
     * Loads the details fragment into the content frame.
     * @param rssItem RssItem to display into the details fragment.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
    public void loadDetailsFragment(RSSItem rssItem, boolean addToBackStack){
        Fragment fragment = (new DetailsFragment()).setFeed(rssItem);
        loadFragment(fragment, this.currentFragmentId, addToBackStack);
    }

    /**
     * Loads the details fragment into the content frame.
     * @param node Node to display into the details fragment.
     * @param addToBackStack If the current fragment should be added to the back stack.
     */
    public void loadGuideFragment(Guide guide, Node node, boolean addToBackStack){
        GuideFragment fragment = (new GuideFragment()).setCurrentNode(guide, node);
        loadFragment(fragment, this.currentFragmentId, addToBackStack);
    }

    /**
     * Sets the Fragment HashMap menu.
     */
    public void setFragmentHashMap()
    {
        fragmentHashMap = new HashMap<>();
        fragmentHashMap.put(
                ApplicationConstants.MENU_NEWS,
                (new FeedListFragment()).setType(FeedType.NEWS)
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_EVENTS,
                (new FeedListFragment()).setType(FeedType.EVENTS)
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_PARTNERS,
                (new FeedListFragment()).setType(FeedType.PARTNERS)
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_GUIDE,
                (new GuideFragment()).setCurrentNode(guideService.getFromCache(), null)
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_ABOUT,
                new AboutFragment()
        );
        fragmentHashMap.put(
                ApplicationConstants.MENU_NOTIFICATIONS,
                new NotificationFragment()
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

    public void clearFragmentBackStack(){
        FragmentManager fm = getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}