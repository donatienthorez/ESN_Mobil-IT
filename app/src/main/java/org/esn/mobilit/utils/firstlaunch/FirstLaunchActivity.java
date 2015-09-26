package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.esn.mobilit.R;
import org.esn.mobilit.models.Countries;
import org.esn.mobilit.models.Country;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FirstLaunchActivity extends Activity {
    private static final String TAG = FirstLaunchActivity.class.getSimpleName();

    //Activity
    private Activity context;

    //Layout
    private LinearLayout spinners_layout;
    private Button startButton;
    private TextView textView;
    private ProgressBar progressBar;

    // Attributes for spinnerCountries
    private Countries json_countries, cache_countries, current_countries;
    private Country currentCountry;
    private Section currentSection;

    // Spinners
    private Spinner spinnerCountries;
    private Spinner spinnerSections;

    // Other Attributes
    private String revision;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_firstlaunch);
        initContent();

        //Check last revision and download new json if not up to date
        if (Utils.isConnected(context)){
            new checkLastRevision().execute();
        }

        cache_countries = (Countries) Utils.getObjectFromCache(context, "countries");
        if (cache_countries != null){
            initCountriesSpinner();
        }else{
            if (!Utils.isConnected(context)){
                try{
                    JSONObject jsonobject = new JSONObject(Utils.loadCountriesFromAsset(context));
                    getCountriesFromJSON(jsonobject);
                    savingNewCountries();
                }catch (Exception e){
                    Log.d(TAG, "Error importing data from asset");
                }
            }
        }
    }

    private void initContent(){
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        progressBar = ((ProgressBar)findViewById (R.id.progressBar));
        progressBar.setVisibility(View.VISIBLE);

        textView    = (TextView) findViewById(R.id.chooseyourcountry);

        //Set text color
        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(getResources().getString(R.string.chooseyour) + " "); // Choose your

        SpannableString countryspan = new SpannableString(getResources().getString(R.string.country));
        countryspan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, countryspan.length(), 0);
        text.append(countryspan); // Choose your country
        text.append(", ");// Choose your country ,

        SpannableString cityspan = new SpannableString(getResources().getString(R.string.city));
        cityspan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, cityspan.length(), 0);
        text.append(cityspan + " "); // Choose your country, city
        text.append(getResources().getString(R.string.and) + " ");// Choose your country ,city and

        SpannableString esnsectionspan = new SpannableString(getResources().getString(R.string.esnsection));
        esnsectionspan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, esnsectionspan.length(), 0);
        text.append(esnsectionspan); // Choose your country, city and ESN Section
        text.append('.');// Choose your country ,city and ESN Section.

        textView.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void initCountriesSpinner(){
        spinners_layout = (LinearLayout) findViewById(R.id.spinners_layout);
        if (spinnerCountries != null) {
            spinners_layout.removeView(spinnerSections);
        }

        spinnerCountries = new Spinner(this);

        ArrayList<String> datas = new ArrayList<String>();
        current_countries = (json_countries != null) ? json_countries : cache_countries;

        //Add dummy string
        datas.add(getResources().getString(R.string.selectyourcountry));
        for(Country country : current_countries.getCountries()){
            datas.add(country.getName());
        }

        spinnerCountries.setSelection(0);
        spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,datas));
        spinnerCountries.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (position != 0) {
                            currentCountry = current_countries.getCountries().get(position-1);
                            Log.d(TAG, "currentCountry selected is " + currentCountry.getName());
                            initSectionsSpinner();
                        }
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
        );
        progressBar.setVisibility(View.INVISIBLE);
        spinners_layout.addView(spinnerCountries);
    }

    private void initSectionsSpinner(){
        progressBar.setVisibility(View.VISIBLE);
        if (spinnerSections != null) {
            spinners_layout.removeView(spinnerSections);
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
                            Log.d(TAG, "currentSection selected is " + currentCountry.getSections().get(position-1).getName());
                            startButton.setEnabled(true);
                            startButton.setVisibility(View.VISIBLE);
                        }
                    }
                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
        );
        progressBar.setVisibility(View.INVISIBLE);
        spinners_layout.addView(spinnerSections);
    }

    public void launchHomeActivity(View view){
        //Load new parameters
        Utils.setDefaults(context, "CODE_COUNTRY", current_countries.getCountryFromSection(currentSection).getCode_country());
        Utils.setDefaults(context, "CODE_SECTION", currentSection.getCode_section());
        Utils.setDefaults(context, "SECTION_WEBSITE", currentSection.getWebsite());
        Utils.saveObjectToCache(context, "country", currentCountry);
        Utils.saveObjectToCache(context, "section", currentSection);

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }


    /**
     * INTERN CLASS FOR ASYNCTASK ACTIONS
     */

    private class checkLastRevision extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonobject = null;
            JSONArray jsonarray = null;

            jsonobject = JSONfunctions.getJSONfromURL(ApplicationConstants.APP_WEBSERVICE_URL + "getRevision.php");
            try {
                jsonarray = jsonobject.getJSONArray("revision");
                jsonobject = jsonarray.getJSONObject(0);
                revision = jsonobject.optString("date");

            } catch (Exception e) {
                Log.d(TAG, "error getting revision : " + e.getMessage());
            }

            return null;
        }

        protected void onPostExecute(Void args) {
            if (revision != null && revision.length() == 14) {
                String pref_revision = Utils.getDefaults(context, "revision");
                if (pref_revision != null && pref_revision.length() == 14) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

                    try {
                        Date dl_date = format.parse(revision);
                        Date pref_date = format.parse(pref_revision);

                        if (pref_date.compareTo(dl_date) < 0){
                            Log.d(TAG, "dl_date (" + dl_date.toString() + ") > pref_date (" + pref_date.toString() + ")");
                            new DownloadJSON().execute();
                        }else{
                            if (pref_date.compareTo(dl_date) == 0){

                                // Fix onResume error
                                if (cache_countries == null)
                                    new DownloadJSON().execute();
                            }
                        }
                    }
                    catch(Exception e){
                        Log.d(TAG, e.toString());
                    }
                }else{
                    new DownloadJSON().execute();
                }
            }
        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonobject = null;
            JSONArray jsonarray = null;

            if (revision != null){
                jsonobject = JSONfunctions.getJSONfromURL(ApplicationConstants.APP_WEBSERVICE_URL + "json/" + revision + ".json");
                getCountriesFromJSON(jsonobject);
            }

            return null;
        }

        protected void onPostExecute(Void args) {
            savingNewCountries();
        }
    }

    private void getCountriesFromJSON(JSONObject jsonobject){
        JSONArray jsonarray = null;

        try {
            json_countries = new Countries(revision);
            JSONArray jsonarray_countries = jsonobject.getJSONArray("countries");
            for (int i = 0; i < jsonarray_countries.length(); i++) {
                JSONObject json_country = jsonarray_countries.getJSONObject(i);
                Country country = new Country(
                        json_country.optInt("id"),
                        json_country.optString("name"),
                        json_country.optString("url"),
                        json_country.optString("code_country"),
                        json_country.optString("website"),
                        json_country.optString("email")
                );
                json_countries.addCountry(country);

                JSONArray json_sections = json_country.getJSONArray("sections");
                for (int j = 0; j < json_sections.length(); j++) {
                    JSONObject json_section = json_sections.getJSONObject(j);
                    Section section = new Section(
                            json_section.optInt("sid"),
                            json_section.optInt("sid_country"),
                            json_section.optString("sname"),
                            json_section.optString("surl"),
                            json_section.optString("scode_section"),
                            json_section.optString("saddress"),
                            json_section.optString("swebsite"),
                            json_section.optString("sphone"),
                            json_section.optString("semail"),
                            json_section.optString("suniversity")
                    );
                    country.addSection(section);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savingNewCountries(){
        if (json_countries != null && json_countries.getCountries().size() > 0){
            try {
                Utils.saveObjectToCache(context, "countries", json_countries);
                Utils.setDefaults(context, "revision", json_countries.getRevision());

                Countries new_cache_countries = (Countries) Utils.getObjectFromCache(context, "countries");

                initCountriesSpinner();
            }catch (Exception e){
                Log.d(TAG, "Exception saving object to cache :" + e);
            }
        }
    }
}
