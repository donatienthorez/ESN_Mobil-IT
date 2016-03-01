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
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.LoadingCallback;
import org.esn.mobilit.R;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.services.launcher.LauncherService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadingActivity extends Activity {

    @Bind(R.id.textView) protected TextView textView;
    @Bind(R.id.version)  protected TextView version;
    @Bind(R.id.changesection) protected Button buttonSection;
    @Bind(R.id.retry) protected Button buttonRetry;
    @Bind(R.id.progressBar) protected ProgressBar progressBar;

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
        setContentView(R.layout.splash);

        ButterKnife.bind(this);

        // Add the version of the application
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("v " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            version.setText("v 1.0.0");
        }

        // Connected - Start parsing
        textView.setText(R.string.loading);
    }
    @Override
    public void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            GCMService.getInstance().setPushMsg(getIntent().getExtras().getString("title"));
        }

        final Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        Glide.with(MobilITApplication.getContext())
                .load(section.getLogo_url())
                .downloadOnly(150, 250);

        ArrayList<Launchable> arrayList;
        arrayList = new ArrayList<Launchable>();
        arrayList.add(EventsService.getInstance());
        arrayList.add(NewsService.getInstance());
        arrayList.add(PartnersService.getInstance());
        arrayList.add(GuideService.getInstance());
        arrayList.add(GCMService.getInstance());

        LauncherService.getInstance().launchServices(arrayList, new LoadingCallback() {
            @Override
            public void onSuccess() {
                launchHomeActivity();
            }

            @Override
            public void onNoAvailableData() {
                showLoadingError(R.string.emptycache);
            }

            @Override
            public void onFailure() {
                showLoadingError(R.string.emptycache);
                textView.setText("Oops une erreur s'est produite");
            }

            public void onProgress(int id) {
                textView.setText(id);
            }
        });
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
}
