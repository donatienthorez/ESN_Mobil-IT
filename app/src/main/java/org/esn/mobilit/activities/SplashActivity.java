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

import org.esn.mobilit.R;
import org.esn.mobilit.services.GCMService;
import org.esn.mobilit.services.LauncherService;
import org.esn.mobilit.tasks.feed.XMLFeedEventsTask;
import org.esn.mobilit.tasks.feed.XMLFeedNewsTask;
import org.esn.mobilit.tasks.feed.XMLFeedPartnersTask;
import org.esn.mobilit.tasks.feed.XMLSurvivalGuideTask;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends Activity {

    @Bind(R.id.textView) protected TextView textView;
    @Bind(R.id.version)  protected TextView version;
    @Bind(R.id.changesection) protected Button buttonSection;
    @Bind(R.id.retry) protected Button buttonRetry;
    @Bind(R.id.progressBar) protected ProgressBar progressBar;

    FeedService feedService;
    GCMService gcmService;
    LauncherService launcherService;

    @OnClick({R.id.changesection, R.id.retry})
    public void onClick(View v) {
        Intent returnIntent = new Intent();
        setResult(ApplicationConstants.RESULT_FIRST_LAUNCH, returnIntent);
        finish();
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

        if (!Utils.isConnected(this)){
            // No connectivity - Load cache
            textView.setText(getResources().getString(R.string.tryingcache));
            feedService.getFeedsFromCache();

            if(feedService.emptyFeeds()){
                textView.setText(getResources().getString(R.string.emptycache));
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                launchHomeActivity();
            }
            buttonRetry.setVisibility(View.VISIBLE);
            buttonSection.setVisibility(View.VISIBLE);

        }else{

            // Connected - Start parsing
            textView.setText(R.string.loading);
            gcmService.pushForGcm(this, callbackGCMConstructor());
            new XMLFeedNewsTask(callbackFeedConstructor(R.string.load_news_end)).execute();
            new XMLFeedEventsTask(callbackFeedConstructor(R.string.load_events_end)).execute();
            new XMLFeedPartnersTask(callbackFeedConstructor(R.string.load_partners_end)).execute();
            new XMLSurvivalGuideTask(callbackSurvivalGuideConstructor(R.string.load_survival_end)).execute();
        }
    }

    public void retry(){
        textView.setText(R.string.noitems);
        progressBar.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);
    }

    public Callback callbackFeedConstructor(final int stringId){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                textView.setText(getResources().getString(stringId, ((RSSFeed) result).getItemCount()));
                launcherService.incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }
            @Override
            public void onFailure(Exception ex) {
                //TODO
            }
        };
    }

    public Callback callbackSurvivalGuideConstructor(final int stringId){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                textView.setText(getResources().getString(stringId));
                launcherService.incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
                //TODO
            }
        };
    }

    public Callback callbackGCMConstructor(){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                launcherService.incrementCount();
                if(launcherService.launchHomeActivity()) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
                //TODO
            }
        };
    }

    public void launchHomeActivity(){
        if (feedService.getTotalItems() > 0) {
            textView.setText(R.string.start_homeactivity);
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivityForResult(i, ApplicationConstants.RESULT_SPLASH_ACTIVITY);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ApplicationConstants.RESULT_SPLASH_ACTIVITY) {
            Intent returnIntent = new Intent();
            if (resultCode == RESULT_CANCELED){
                setResult(ApplicationConstants.RESULT_CLOSE_ALL, returnIntent);
                finish();
            } else if (resultCode == ApplicationConstants.RESULT_FIRST_LAUNCH){
                setResult(ApplicationConstants.RESULT_FIRST_LAUNCH, returnIntent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}