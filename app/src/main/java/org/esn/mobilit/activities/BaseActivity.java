package org.esn.mobilit.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.fragments.NotificationFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.services.navigation.NavigationUri;
import org.esn.mobilit.services.navigation.NavigationUriType;
import org.esn.mobilit.services.navigation.NavigationUriTypeHelper;
import org.esn.mobilit.services.navigation.UnknownNavigationUriException;
import org.esn.mobilit.utils.inject.ForApplication;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {

    @ForApplication
    @Inject
    Context context;
    @Inject
    CacheService cacheService;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Manages the navigation to the URI by opening fragments or activity.
     */
    public void navigateToUri(NavigationUri navigationUri, boolean addToBackStack) {
        Fragment fragment;

        switch (navigationUri.getNavigationUriType()) {
            case FEED_LIST:
                fragment = new FeedListFragment();
                break;
            case FEED_DETAILS:
                fragment = new DetailsFragment();
                break;
            case NOTIFICATIONS:
                fragment = new NotificationFragment();
                break;
            case GUIDE:
                fragment = new GuideFragment();
                break;
            case ABOUT:
                fragment = new AboutFragment();
                break;
            default:
                //FIXME think about what to do in that case
                return;
        }
        fragment.setArguments(navigationUri.getBundle());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
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

    public void clearFragmentBackStack() {
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Base Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
