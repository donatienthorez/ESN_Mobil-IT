package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.NetworkCallback;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Countries;
import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CountriesService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class FirstLaunchActivity extends Activity {
    private static final String TAG = FirstLaunchActivity.class.getSimpleName();

    //Layout
    private LinearLayout spinnersLayout;
    private Button startButton;
    private TextView textView;
    private ProgressBar progressBar;

    // Attributes for spinnerCountries
    private Countries countries;
    private Country currentCountry;
    private Section currentSection;

    // Spinners
    private Spinner spinnerCountries;
    private Spinner spinnerSections;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstlaunch);
        initContent();

        if (Utils.isConnected(this)){
            CountriesService.getCountries(new NetworkCallback<Countries>() {
                @Override
                public void onSuccess(Countries result) {
                    countries = result;
                    initCountriesSpinner();
                }

                @Override
                public void onFailure(RetrofitError error) {

                }
            });
        }
    }

    private void initContent(){
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        progressBar = ((ProgressBar)findViewById (R.id.progressBar));
        progressBar.setVisibility(View.VISIBLE);

        spinnersLayout = (LinearLayout) findViewById(R.id.spinners_layout);
        spinnerCountries = new Spinner(this);

        textView    = (TextView) findViewById(R.id.chooseyourcountry);

        //Set text color
        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(getResources().getString(R.string.chooseyour) + " "); // Choose your

        SpannableString countrySpan = new SpannableString(getResources().getString(R.string.country));
        countrySpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, countrySpan.length(), 0);
        text.append(countrySpan); // Choose your country
        text.append(", ");// Choose your country ,

        SpannableString cityspan = new SpannableString(getResources().getString(R.string.city));
        cityspan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, cityspan.length(), 0);
        text.append(cityspan + " "); // Choose your country, city
        text.append(getResources().getString(R.string.and) + " ");// Choose your country ,city and

        SpannableString esnSectionSpan = new SpannableString(getResources().getString(R.string.esnsection));
        esnSectionSpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, esnSectionSpan.length(), 0);
        text.append(esnSectionSpan); // Choose your country, city and ESN Section
        text.append('.');// Choose your country ,city and ESN Section.

        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void initCountriesSpinner(){
        ArrayList<String> datas = new ArrayList<String>();

        //Add dummy string
        datas.add(getResources().getString(R.string.selectyourcountry));
        for(Country country : countries.getCountries()){
            datas.add(country.getName());
        }

        spinnerCountries.setSelection(0);
        spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,datas));
        spinnerCountries.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentCountry = countries.getCountries().get(position-1);
                            Log.d(TAG, "currentCountry selected is " + currentCountry.getName());
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
            Log.d("section ",section.getName());
        }

        spinnerSections.setSelection(0);
        spinnerSections.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,datas));
        spinnerSections.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentSection = currentCountry.getSections().get(position-1);
                            Log.d(TAG, "currentSection selected is " + currentCountry.getSections().get(position-1).getName());
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
        Utils.setDefaults(this, "CODE_COUNTRY", countries.getCountryFromSection(currentSection).getCode_country());
        Utils.setDefaults(this, "CODE_SECTION", currentSection.getCode_section());
        Utils.setDefaults(this, "SECTION_WEBSITE", currentSection.getWebsite());
        Utils.saveObjectToCache(this, "country", currentCountry);
        Utils.saveObjectToCache(this, "section", currentSection);

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
