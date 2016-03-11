package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import io.fabric.sdk.android.Fabric;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.adapters.SpinnerAdapter;
import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.R;
import org.esn.mobilit.renderers.HomepageRenderer;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.CountriesService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class FirstLaunchActivity extends Activity {

    @Bind(R.id.startButton)      public Button startButton;
    @Bind(R.id.chooseCountry)    public TextView chooseCountryTextView;
    @Bind(R.id.progressBar)      public ProgressBar progressBar;
    @Bind(R.id.spinnerCountries) public Spinner spinnerCountries;
    @Bind(R.id.spinnerSections)  public Spinner spinnerSections;

    private Country currentCountry;
    private Section currentSection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_first_launch);
        Section section = (Section) CacheService.
                getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        if (section != null && !TextUtils.isEmpty(section.getWebsite())) {
            Intent intent = new Intent(this, LoadingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        initContent();

        CountriesService.getCountries(new NetworkCallback<List<Country>>() {
            @Override
            public void onSuccess(List<Country> result) {
                initCountriesSpinner(result);
            }

            @Override
            public void onNoAvailableData() {
                Toast.makeText(
                        MobilITApplication.getContext(),
                        getResources().getString(R.string.error_message_network),
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(
                        MobilITApplication.getContext(),
                        getResources().getString(R.string.error_message_no_data_countries),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void initContent(){
        // Load Butterknife
        ButterKnife.bind(this);

        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        HomepageRenderer homepageRenderer = new HomepageRenderer();
        SpannableStringBuilder text = homepageRenderer.renderHomepageText();
        chooseCountryTextView.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void initCountriesSpinner(final List<Country> countries){
        ArrayList<String> datas = new ArrayList<String>();

        datas.add(getResources().getString(R.string.selectyourcountry));
        for(Country country : countries){
            datas.add(country.getName());
        }

        spinnerCountries.setSelection(0);
        spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this, datas));
        spinnerCountries.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentCountry = countries.get(position - 1);
                            initSectionsSpinner();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                }
        );
        spinnerCountries.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initSectionsSpinner(){
        progressBar.setVisibility(View.VISIBLE);

        ArrayList<String> sections = currentCountry.getSectionsNamesArray();
        sections.add(0, getResources().getString(R.string.selectyoursection));

        spinnerSections.setSelection(0);
        spinnerSections.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this, sections));
        spinnerSections.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentSection = currentCountry.getSections().get(position - 1);
                            startButton.setEnabled(true);
                            startButton.setVisibility(View.VISIBLE);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                }
        );
        spinnerSections.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void launchSplashActivity(View view){
        PreferencesService.setDefaults(ApplicationConstants.PREFERENCES_CODE_COUNTRY, currentCountry.getCodeCountry());
        PreferencesService.setDefaults(ApplicationConstants.PREFERENCES_CODE_SECTION, currentSection.getCode_section());

        CacheService.saveObjectToCache(ApplicationConstants.CACHE_COUNTRY, currentCountry);
        CacheService.saveObjectToCache(ApplicationConstants.CACHE_SECTION, currentSection);

        Intent intent = new Intent(this, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
