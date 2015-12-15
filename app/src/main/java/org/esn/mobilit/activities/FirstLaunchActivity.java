package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Countries;
import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CountriesService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.adapters.SpinnerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

public class FirstLaunchActivity extends Activity {

    private static final String TAG = FirstLaunchActivity.class.getSimpleName();

    //Layout
    @Bind(R.id.spinnersLayout) public LinearLayout spinnersLayout;
    @Bind(R.id.startButton)    public Button startButton;
    @Bind(R.id.chooseCountry)  public TextView textView;
    @Bind(R.id.progressBar)    public ProgressBar progressBar;

    // Attributes for spinnerCountries
    private Country currentCountry;
    private Section currentSection;

    // Spinners
    private Spinner spinnerCountries;
    private Spinner spinnerSections;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstlaunch);

        initContent();

        if (Utils.isConnected()){
            CountriesService.getCountries(new NetworkCallback<Countries>() {
                @Override
                public void onSuccess(Countries result) {
                    initCountriesSpinner();
                }

                @Override
                public void onFailure(RetrofitError error) {

                }
            });
        }
    }

    private void initContent(){
        // Load Butterknife
        ButterKnife.bind(this);

        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        spinnerCountries = new Spinner(this);

        //Set text color
        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(getResources().getString(R.string.chooseyour) + " "); // Choose your

        ForegroundColorSpan blue = new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB);

        SpannableString countrySpan = new SpannableString(getResources().getString(R.string.country));
        countrySpan.setSpan(blue, 0, countrySpan.length(), 0);
        text.append(countrySpan); // Choose your country
        text.append(", ");// Choose your country ,

        SpannableString cityspan = new SpannableString(getResources().getString(R.string.city));
        cityspan.setSpan(blue, 0, cityspan.length(), 0);
        text.append(cityspan + " "); // Choose your country, city
        text.append(getResources().getString(R.string.and) + " ");// Choose your country ,city and

        SpannableString esnSectionSpan = new SpannableString(getResources().getString(R.string.esnsection));
        esnSectionSpan.setSpan(blue, 0, esnSectionSpan.length(), 0);
        text.append(esnSectionSpan); // Choose your country, city and ESN Section
        text.append('.');// Choose your country ,city and ESN Section.

        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void initCountriesSpinner(){
        ArrayList<String> datas = new ArrayList<String>();

        //Add dummy string
        datas.add(getResources().getString(R.string.selectyourcountry));
        for(Country country : CountriesService.getCountries().getCountries()){
            datas.add(country.getName());
        }

        spinnerCountries.setSelection(0);
        spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this, datas));
        spinnerCountries.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentCountry = CountriesService.getCountries().getCountry(position - 1);
                            initSectionsSpinner();
                        }
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
        );
        progressBar.setVisibility(View.INVISIBLE);
        spinnersLayout.addView(spinnerCountries);
    }

    private void initSectionsSpinner(){
        progressBar.setVisibility(View.VISIBLE);
        if (spinnerSections != null) {
            spinnersLayout.removeView(spinnerSections);
        }

        spinnerSections = new Spinner(this);

        ArrayList<String> datas = new ArrayList<String>();

        //Add dummy string
        datas.add(getResources().getString(R.string.selectyoursection));
        for(Section section : currentCountry.getSections()){
            datas.add(section.getName());
        }

        spinnerSections.setSelection(0);
        spinnerSections.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,datas));
        spinnerSections.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentSection = currentCountry.getSections().get(position-1);
                            startButton.setEnabled(true);
                            startButton.setVisibility(View.VISIBLE);
                        }
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
        );
        progressBar.setVisibility(View.INVISIBLE);
        spinnersLayout.addView(spinnerSections);
    }

    public void launchHomeActivity(View view){
        //Load new parameters
        PreferencesService.setDefaults(
                "CODE_COUNTRY",
                CountriesService.getCountries()
                        .getCountryFromSection(currentSection)
                        .getCodeCountry()
        );
        PreferencesService.setDefaults("CODE_SECTION", currentSection.getCode_section());
        PreferencesService.setDefaults("SECTION_WEBSITE", currentSection.getWebsite());

        CacheService.saveObjectToCache("country", currentCountry);
        CacheService.saveObjectToCache("section", currentSection);

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
