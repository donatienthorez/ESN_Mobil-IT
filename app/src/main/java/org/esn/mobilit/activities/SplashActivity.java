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

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.services.GCMService;
import org.esn.mobilit.tasks.feed.XMLFeedEventsTask;
import org.esn.mobilit.tasks.feed.XMLFeedNewsTask;
import org.esn.mobilit.tasks.feed.XMLFeedPartnersTask;
import org.esn.mobilit.tasks.feed.XMLSurvivalGuideTask;
import org.esn.mobilit.services.FeedService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

public class SplashActivity extends Activity {

    public int count, count_limit;
    private Intent intent;
    private String pushMsg;

    public void incrementCount() {
        this.count++;
    }

    private TextView textView;
    private TextView version;
    private Button buttonSection;
    private Button buttonRetry;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change layout
        setContentView(R.layout.splash);

        initView();

        intent = new Intent(MobilITApplication.getContext(), HomeActivity.class);
        count_limit = 5;
        count = 0;

        if (!Utils.isConnected(this)){
            // No connectivity - Load cache
            textView.setText(getResources().getString(R.string.tryingcache));
            FeedService.getInstance().getRssFeedFromCache();

            if(FeedService.getInstance().emptyFeeds()){
                textView.setText(getResources().getString(R.string.emptycache));
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                // Load from cache
                Bundle bundle = new Bundle();
                bundle.putSerializable("feedEvents", FeedService.getInstance().getFeedEvents());
                bundle.putSerializable("feedNews", FeedService.getInstance().getFeedNews());
                bundle.putSerializable("feedPartners", FeedService.getInstance().getFeedPartners());
                bundle.putSerializable("survivalGuide", FeedService.getInstance().getSurvivalguide());
                intent.putExtras(bundle);
                launchHomeActivity();
            }
            buttonRetry.setVisibility(View.VISIBLE);
            buttonSection.setVisibility(View.VISIBLE);

        }else{

            GCMService.getInstance().pushForGcm(this, callbackGCMConstructor());
            // on success :
            this.incrementCount();

            // Connected - Start parsing
            textView.setText(R.string.loading);
            new XMLFeedNewsTask(callbackFeedConstructor(R.string.load_news_end)).execute();
            new XMLFeedEventsTask(callbackFeedConstructor(R.string.load_events_end)).execute();
            new XMLFeedPartnersTask(callbackFeedConstructor(R.string.load_partners_end)).execute();
            new XMLSurvivalGuideTask(callbackSurvivalGuideConstructor(R.string.load_survival_end)).execute();
        }
    }

    public void initView(){
        //Init Elements
        version = (TextView) findViewById(R.id.version);
        textView = ((TextView) findViewById (R.id.textView));
        progressBar = ((ProgressBar) findViewById (R.id.progressBar));

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("v " + pInfo.versionName);
        } catch(PackageManager.NameNotFoundException e){
            version.setText("v 1.0.0");
        }

        buttonRetry = (Button) findViewById(R.id.retry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(ApplicationConstants.RESULT_FIRST_LAUNCH, returnIntent);
                finish();
            }
        });
        buttonRetry.setVisibility(View.INVISIBLE);

        buttonSection = (Button) findViewById(R.id.changesection);
        buttonSection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(ApplicationConstants.RESULT_FIRST_LAUNCH, returnIntent);
                finish();
            }
        });
        buttonSection.setVisibility(View.INVISIBLE);
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
                incrementCount();
                if (count == count_limit) {
                    launchHomeActivity();
                }
            }
            @Override
            public void onFailure(Exception ex) {
            }
        };
    }

    public Callback callbackSurvivalGuideConstructor(final int stringId){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                textView.setText(getResources().getString(stringId));
                incrementCount();
                if (count == count_limit) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
            }
        };
    }

    public Callback callbackGCMConstructor(){
        return new Callback() {
            @Override
            public void onSuccess(Object result) {
                incrementCount();
                if (count == count_limit) {
                    launchHomeActivity();
                }
            }

            @Override
            public void onFailure(Exception ex) {
            }
        };
    }

    public void launchHomeActivity(){
        int total = 0;
        total += FeedService.getInstance().getFeedEvents().getItemCount();
        total += FeedService.getInstance().getFeedNews().getItemCount();
        total += FeedService.getInstance().getFeedPartners().getItemCount();
        total += FeedService.getInstance().getSurvivalguide().getCategories().size();

        if (total > 0) {
            textView.setText(R.string.start_homeactivity);

            Intent i = new Intent(getApplicationContext(), HomeActivity.class);

            if (pushMsg != null) {
                i.putExtra("pushReceived", true);
                i.putExtra("pushMsg", pushMsg);
            }else{
                i.putExtra("pushReceived", false);
            }
            startActivityForResult(i, ApplicationConstants.RESULT_SPLASH_ACTIVITY);
        }
        else {
            this.retry();
        }
    }

    public void onResume(){
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            pushMsg = getIntent().getExtras().getString("title");
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