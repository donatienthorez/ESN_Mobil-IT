package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.Abouts;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.AboutService;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.R;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.services.LauncherService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.tasks.feed.XMLSurvivalGuideTask;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import retrofit.RetrofitError;

public class SplashActivity extends Activity {

    @Bind(R.id.textView) protected TextView textView;
    @Bind(R.id.version)  protected TextView version;
    @Bind(R.id.changesection) protected Button buttonSection;
    @Bind(R.id.retry) protected Button buttonRetry;
    @Bind(R.id.progressBar) protected ProgressBar progressBar;

    FeedService feedService;
    GCMService gcmService;
    LauncherService launcherService;

    @OnClick(R.id.changesection)
    public void changeSection(View v) {
        PreferencesService.resetSection();
        Intent intent = new Intent(this, FirstLaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.retry)
    public void retry(View v) {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change layout
        setContentView(R.layout.splash);

        // Load Butterknife
        ButterKnife.bind(this);

        // Load the services
        feedService = FeedService.getInstance();
        gcmService = GCMService.getInstance();
        launcherService = LauncherService.getInstance();

        // Add the version of the application
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("v " + pInfo.versionName);
        } catch(PackageManager.NameNotFoundException e){
            version.setText("v 1.0.0");
        }

        if (!Utils.isConnected()){
            FeedService.getInstance().getFeedsFromCache();
            if(feedService.emptyFeeds()){
                textView.setText(getResources().getString(R.string.emptycache));
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                // No connectivity - Load cache
                textView.setText(getResources().getString(R.string.tryingcache));
                launchHomeActivity();
            }
            buttonRetry.setVisibility(View.VISIBLE);
            buttonSection.setVisibility(View.VISIBLE);

        }else{

            // Connected - Start parsing
            textView.setText(R.string.loading);

            launcherService.resetCount();
            gcmService.pushForGcm(this, callbackGCMConstructor());

            final Section section = (Section) CacheService.getObjectFromCache("section");
            final String url = ApplicationConstants.LOGOINSERTER_URL + "assets/img/logos/" + section.getLogo_url();

            if (section.getLogo_url() == null || section.getLogo_url().equalsIgnoreCase("")) {
                AboutService.getAbout(new NetworkCallback<Abouts>() {
                    @Override
                    public void onSuccess(Abouts result) {
                        section.setLogo_url(result.getAbout().getLogoPath());
                        CacheService.saveObjectToCache("section", section);
                        Glide.with(MobilITApplication.getContext())
                                .load(url)
                                .downloadOnly(150, 250);
                    }

                    @Override
                    public void onFailure(RetrofitError error) {
                    }
                });
            } else {
                Glide.with(MobilITApplication.getContext())
                        .load(url)
                        .downloadOnly(150, 250);
            }

            NewsService.getNews(new NetworkCallback<RSS>() {
                @Override
                public void onSuccess(RSS result) {
                    textView.setText(getResources().getString(R.string.load_news_end, result.getListSize()));
                    launcherService.incrementCount();
                    if (launcherService.launchHomeActivity()) {
                        launchHomeActivity();
                    }
                }

                @Override
                public void onFailure(RetrofitError error) {
                    launcherService.incrementCount();
                    if (launcherService.launchHomeActivity()) {
                        launchHomeActivity();
                    }
                }
            });

            EventsService.getEvents(new NetworkCallback<RSS>() {
                @Override
                public void onSuccess(RSS result) {
                    textView.setText(getResources().getString(R.string.load_events_end, result.getListSize()));
                    launcherService.incrementCount();
                    if (launcherService.launchHomeActivity()) {
                        launchHomeActivity();
                    }
                }

                @Override
                public void onFailure(RetrofitError error) {
                    launcherService.incrementCount();
                    if (launcherService.getInstance().launchHomeActivity()) {
                        launchHomeActivity();
                    }
                }
            });

            PartnersService.getPartners(new NetworkCallback<RSS>() {
                @Override
                public void onSuccess(RSS result) {
                    textView.setText(getResources().getString(R.string.load_partners_end, result.getListSize()));
                    launcherService.incrementCount();
                    if (launcherService.launchHomeActivity()) {
                        launchHomeActivity();
                    }
                }

                @Override
                public void onFailure(RetrofitError error) {
                    launcherService.incrementCount();
                    if (launcherService.launchHomeActivity()) {
                        launchHomeActivity();
                    }
                }
            });

            new XMLSurvivalGuideTask(callbackSurvivalGuideConstructor(R.string.load_survival_end)).execute();
        }
    }

    public void retry(){
        textView.setText(R.string.noitems);
        progressBar.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);
        buttonSection.setVisibility(View.VISIBLE);
    }

    public Callback callbackSurvivalGuideConstructor(final int stringId){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                textView.setText(getResources().getString(stringId));
                launcherService.getInstance().incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
                launcherService.getInstance().incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }
        };
    }

    public Callback callbackGCMConstructor(){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                launcherService.getInstance().incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
                launcherService.getInstance().incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }
        };
    }

    public void launchHomeActivity(){
        feedService.getFeedsFromCache();
        if (feedService.getTotalItems() > 0) {
            textView.setText(R.string.start_homeactivity);
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        } else {
            this.retry();
        }
    }

    public void onResume(){
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            gcmService.setPushMsg(getIntent().getExtras().getString("title"));
        }
    }
}