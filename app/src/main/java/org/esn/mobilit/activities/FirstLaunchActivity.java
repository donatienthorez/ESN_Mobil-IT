package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.adapters.SpinnerAdapter;
import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.renderers.HomepageRenderer;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.CountriesService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class FirstLaunchActivity extends Activity {

    @Bind(R.id.startButton)
    public Button startButton;
    @Bind(R.id.chooseCountry)
    public TextView chooseCountryTextView;
    @Bind(R.id.progressBar)
    public ProgressBar progressBar;
    @Bind(R.id.spinnerCountries)
    public Spinner spinnerCountries;
    @Bind(R.id.spinnerSections)
    public Spinner spinnerSections;

    private List<Country> countryList;
    private Country currentCountry;
    private ArrayAdapter countriesAdapter, sectionsAdapter;
    private ArrayList<String> countries, sections;
    private int sectionPosition;
    private boolean countryLoaded;

    @OnItemSelected(R.id.spinnerCountries)
    public void onCountriesItemSelected(Spinner spinner, int position) {
        if (position != 0) {
            currentCountry = countryList.get(position - 1);
            initSectionsSpinner();
        }
    }

    @OnItemSelected(R.id.spinnerSections)
    public void onSectionsItemSelected(Spinner spinner, int position) {
        if (position != 0) {
            sectionPosition = position - 1;
            startButton.setEnabled(true);
            startButton.setVisibility(View.VISIBLE);
        }
    }

    private BroadcastReceiver networkStateReceiver;

    /**
     * Saves country and section in cache and launchs the HomeActivity.
     *
     * @param view The view.
     */
    @OnClick(R.id.startButton)
    public void launchHomeActivity(View view) {
        Section currentSection = currentCountry.getSections().get(sectionPosition);

        CacheService.saveObjectToCache(ApplicationConstants.CACHE_COUNTRY, currentCountry);
        CacheService.saveObjectToCache(ApplicationConstants.CACHE_SECTION, currentSection);

        Intent intent = new Intent(FirstLaunchActivity.this, HomeActivity.class);
        intent.putExtra("section", currentSection);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        initContent();
        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!countryLoaded) {
                    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                    getCountries();
                }
            }
        };
    }

    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    /**
     * Initializes the content of the view.
     */
    private void initContent() {
        ButterKnife.bind(this);

        startButton.setEnabled(false);

        HomepageRenderer homepageRenderer = new HomepageRenderer();
        SpannableStringBuilder text = homepageRenderer.renderHomepageText();
        chooseCountryTextView.setText(text, TextView.BufferType.SPANNABLE);

        countries = new ArrayList<>();
        countriesAdapter = new SpinnerAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        spinnerCountries.setAdapter(countriesAdapter);

        sections = new ArrayList<>();
        sectionsAdapter = new SpinnerAdapter<String>(this, android.R.layout.simple_list_item_1, sections);
        spinnerSections.setAdapter(sectionsAdapter);
    }


    /**
     * Gets the countries and initializes country spinner on success.
     */
    private void getCountries() {
        CountriesService.getCountries(new NetworkCallback<List<Country>>() {
            @Override
            public void onSuccess(List<Country> result) {
                countryLoaded = true;
                countries.add(getResources().getString(R.string.selectyourcountry));
                for (Country country : result) {
                    countries.add(country.getName());
                }
                countryList = result;

                initCountriesSpinner();
            }

            @Override
            public void onNoAvailableData() {
                Toast.makeText(
                        MobilITApplication.getContext(),
                        getResources().getString(R.string.error_message_no_data_countries),
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(
                        MobilITApplication.getContext(),
                        getResources().getString(R.string.error_message_network),
                        Toast.LENGTH_LONG
                ).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    /**
     * Initializes the countries spinner depending on the currentCountry selected.
     */
    private void initCountriesSpinner() {
        countriesAdapter.notifyDataSetChanged();

        spinnerCountries.setSelection(0);
        spinnerCountries.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);
    }

    /**
     * Initializes the sections spinner depending on the currentCountry selected.
     */
    private void initSectionsSpinner() {
        sections.clear();
        sections.add(0, getResources().getString(R.string.selectyoursection));
        sections.addAll(currentCountry.getSectionsNamesArray());

        sectionsAdapter.notifyDataSetChanged();

        spinnerSections.setSelection(0);
        spinnerSections.setVisibility(View.VISIBLE);
    }
}
