package org.esn.mobilit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import org.esn.mobilit.R;
import org.esn.mobilit.services.AboutService;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.services.gcm.RegIdService;
import org.esn.mobilit.services.navigation.NavigationUri;
import org.esn.mobilit.services.navigation.NavigationUriType;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.InjectUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)
    protected RelativeLayout drawerRelativeLayout;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;

    @Inject
    PreferencesService preferencesService;
    @Inject
    RegIdService regIdService;
    @Inject
    AboutService aboutService;
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
                navigateToUri(new NavigationUri(NavigationUriType.NOTIFICATIONS), true);
                break;
        }
        return true;
    }

    @Override
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
        }
    }

    /**
     * Build the left drawer menu.
     */
    private void buildMenu() {
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
     * Executes the action of the menuItem id
     *
     * @param menuItemId  Id of the item selected.
     */
    private void executeDrawerMenuAction(int menuItemId) {
        clearFragmentBackStack();
        cacheService.save(ApplicationConstants.CACHE_DEFAULT_MENU, menuItemId);
        Bundle bundle = new Bundle();

        switch (menuItemId) {
            case R.id.drawer_item_news:
                bundle.putSerializable("feedType", FeedType.NEWS);
                navigateToUri(new NavigationUri(NavigationUriType.FEED_LIST, bundle), false);
                break;
            case R.id.drawer_item_events:
                bundle.putSerializable("feedType", FeedType.EVENTS);
                navigateToUri(new NavigationUri(NavigationUriType.FEED_LIST, bundle), false);
                break;
            case R.id.drawer_item_partners:
                bundle.putSerializable("feedType", FeedType.PARTNERS);
                navigateToUri(new NavigationUri(NavigationUriType.FEED_LIST, bundle), false);
                break;
            case R.id.drawer_item_guide:
                navigateToUri(new NavigationUri(NavigationUriType.GUIDE), false);
                break;
            case R.id.drawer_item_about:
                navigateToUri(createNavigationUri("about"), false);
//                navigateToUri(new NavigationUri(NavigationUriType.ABOUT), false);
                break;
            case R.id.drawer_item_reset:
                preferencesService.resetSection();
                Intent intent = new Intent(this, FirstLaunchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
    //FIXME check correct menu item
}