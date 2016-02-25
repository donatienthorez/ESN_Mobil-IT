package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.FeedServiceInterface;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.utils.callbacks.LoadingCallback;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.R;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.services.LauncherService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    ArrayList<FeedServiceInterface> arrayList;

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

        final Section section = (Section) CacheService.getObjectFromCache("section");

        gcmService.pushForGcm(this, new Callback<Object>() {
            @Override
            public void onSuccess(Object result) {
                launcherService.incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
                launcherService.incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }
        });

        Glide.with(MobilITApplication.getContext())
                .load(section.getLogo_url())
                .downloadOnly(150, 250);
        // Connected - Start parsing
        textView.setText("Image loaded");

        arrayList = new ArrayList<FeedServiceInterface>();
        arrayList.add(EventsService.getInstance());
        arrayList.add(NewsService.getInstance());
        arrayList.add(PartnersService.getInstance());

        // Connected - Start parsing
        textView.setText(R.string.loading);

        FeedService.getInstance().loadFeeds(arrayList, section, new LoadingCallback<RSSFeedParser>() {
            @Override
            public void onSuccess(RSSFeedParser result) {
                launchHomeActivity();
            }

            @Override
            public void onNoAvailableData() {
                showLoadingError(R.string.emptycache);
            }

            @Override
            public void onFailure(RetrofitError error) {
                showLoadingError(R.string.emptycache);
                textView.setText("Oops une erreur s'est produite");
            }

            public void onProgress(int id, RSSFeedParser result) {
                if (result == null) {
                    textView.setText("No feed found");
                } else {
                    textView.setText(getResources().getString(id, result.getItemCount()));
                }
            }
        });

//            GuideService.getGuide(section, new NetworkCallback<Guide>() {
//                @Override
//                public void onSuccess(Guide result) {
//                    textView.setText(getResources().getString(R.string.load_survival_end));
//                    launcherService.incrementCount();
//                    if (launcherService.launchHomeActivity()) {
//                        launchHomeActivity();
//                    }
//                }
//
//                @Override
//                public void onFailure(RetrofitError error) {
//                    launcherService.incrementCount();
//                    if (launcherService.launchHomeActivity()) {
//                        launchHomeActivity();
//                    }
//                }
//            });
//        }
    }

    public void showLoadingError(int id){
        textView.setText(id);
        progressBar.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);
        buttonSection.setVisibility(View.VISIBLE);
    }

    public void launchHomeActivity(){
        textView.setText(R.string.start_homeactivity);
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void onResume(){
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            gcmService.setPushMsg(getIntent().getExtras().getString("title"));
        }
    }
}