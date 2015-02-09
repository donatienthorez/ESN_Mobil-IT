package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import org.esn.mobilit.models.Category;
import org.esn.mobilit.models.Countries;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.models.Sections;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FirstLaunchActivity extends Activity {
    private static final String TAG = FirstLaunchActivity.class.getSimpleName();

    //Activity
    private Activity currentActivity;

    //JSON
    private JSONObject jsonobject;
    private JSONArray jsonarray;

    //Layout
    private LinearLayout spinners_layout;
    private Button startButton;
    private TextView textView;

    // Attributes for spinnerCountries
    private Spinner spinnerCountries;
    private ArrayList<String> spinnerCountries_data;
    private ArrayList<Countries> countries_list;
    private Countries currentCountry;

    // Attributes for spinnerSections
    private Spinner spinnerSections;
    private ArrayList<String> spinnerSections_data;
    private ArrayList<Sections> sections_list;
    private Sections currentSection;
    private ProgressBar progressBar;

    // Attributes for SurvivalGuide
    private ArrayList<Category> categories_list;

    // Detail Section
    private Section sectionChoosed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init activity
        currentActivity = this;

        //Change layout
        setContentView(R.layout.activity_firstlaunch);

        //Init Content
        startButton = (Button) findViewById(R.id.start_button);
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

        addSpinnerCountries();
    }

    public void launchAwesomeFeatures(View view){
        setDefaults("CODE_COUNTRY", currentCountry.getCode_country());
        setDefaults("CODE_SECTION", currentSection.getCode_section());
        setDefaults("SECTION_WEBSITE", sectionChoosed.getWebsite());

        Log.d(TAG, " Country : " + getDefaults("CODE_COUNTRY"));
        Log.d(TAG, " Section : " + getDefaults("CODE_SECTION"));
        Log.d(TAG, " Section Website : " + getDefaults("SECTION_WEBSITE"));
        Log.d(TAG, "FINISH FIRSTLAUNCHACTIVITY");

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    protected void addSpinnerCountries(){
        spinners_layout = (LinearLayout) findViewById(R.id.spinners_layout);

        //Get Layout to put spinners in it
        if (spinnerCountries == null) {
            // Init Spiner and load data
            spinnerCountries = new Spinner(this);

            //new DownloadJSONCountries().execute();
            initFrenchCountry();
        }
    }

    protected void addSpinnerSections(){
        //Delete previous spinner and add progressbar
        if (spinnerSections != null) {
            spinners_layout.removeView(spinnerSections);
            progressBar = new ProgressBar(this);
            spinners_layout.addView(progressBar);
        }

        // Init Spiner and load data
        spinnerSections = new Spinner(this);
        new DownloadJSONSections().execute();
    }

    protected void onResume(){
        super.onResume();
        //chooseActivity();
        startButton = (Button) findViewById(R.id.start_button);
        addSpinnerCountries();
    }

    // PREFERENCES
    public void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(key, null);
    }

    //Download JSON
    private void initFrenchCountry(){
        startButton.setEnabled(false);
        countries_list = new ArrayList<Countries>();
        spinnerCountries_data = new ArrayList<String>();
        currentCountry = new Countries("ESN France", "https:\\/\\/galaxy.esn.org\\/section\\/FR", "FR");
        countries_list.add(currentCountry);
        spinnerCountries_data.add("ESN France");
        addSpinnerSections();
        spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,spinnerCountries_data));
        spinners_layout.addView(spinnerCountries);
    }

    private class DownloadJSONCountries extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Unable button
            startButton.setEnabled(false);

            countries_list = new ArrayList<Countries>();
            // Create an array to populate the spinner
            spinnerCountries_data = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(ApplicationConstants.SECTIONS_WEBSERVICE_URL + "getCountries.php");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("countries");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Countries countries_object = new Countries(jsonobject.optString("name"),jsonobject.optString("url"),jsonobject.optString("code_country"));
                    countries_list.add(countries_object);

                    // Populate spinner with country names
                    spinnerCountries_data.add(countries_object.getName());
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            spinnerCountries.setSelection(10, true);

            return null;
        }

        protected void onPostExecute(Void args) {
            spinnerCountries.setSelected(false);

            // Spinner adapter
            spinnerCountries.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,spinnerCountries_data));

            // Spinner on item click listener
            spinnerCountries.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {

                        startButton.setEnabled(false);

                        //Set currentCountry
                        currentCountry = countries_list.get(position);

                        //Load new spinner
                        addSpinnerSections();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {}
                }
            );

            //Remove progressbar
            if (progressBar != null)
                spinners_layout.removeView(progressBar);

            // Add spinner to the linear layout
            spinners_layout.addView(spinnerCountries);
        }
    }

    private class DownloadJSONSections extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Create sections_list array
            sections_list = new ArrayList<Sections>();

            // Create an array to populate the spinner
            spinnerSections_data = new ArrayList<String>();

            if (Utils.isConnected(currentActivity)){
                String url = ApplicationConstants.SECTIONS_WEBSERVICE_URL + "getSections.php?code_country="+currentCountry.getCode_country();
                jsonobject = JSONfunctions.getJSONfromURL(url);
            }
            else{
                try {
                    jsonobject = new JSONObject(Utils.loadSectionsFromFile(currentActivity, currentCountry.getCode_country()));
                }catch (Exception e){
                    Log.d(TAG, "ERROR GETTING SECTIONS FROM FILE");
                }
            }

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("sections");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    Sections sections_object = new Sections(  jsonobject.optString("name"),
                            jsonobject.optString("url"),
                            jsonobject.optString("code_country"),
                            jsonobject.optString("code_section"));
                    sections_list.add(sections_object);

                    // Populate spinner with sections names
                    spinnerSections_data.add(jsonobject.optString("name"));
                }
            } catch (Exception e) {
                Log.d(TAG,"Error" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            // Spinner adapter
            spinnerSections.setAdapter(new SpinnerAdapter(FirstLaunchActivity.this,spinnerSections_data));

            // Spinner on item click listener
            spinnerSections
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // Unable button
                            startButton.setEnabled(false);

                            //Set current Section
                            currentSection = sections_list.get(position);

                            //Get section Details
                            new DownloadJSONSection().execute();
                        }
                        public void onNothingSelected(AdapterView<?> arg0) {}
                    });

            //Remove progressbar
            if (progressBar != null)
                spinners_layout.removeView(progressBar);

            // Add spinner to the linear layout
            spinners_layout.addView(spinnerSections);


        }
    }

    private class DownloadJSONSection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonobject = null;
            JSONArray jsonarray = null;

            // JSON file URL address or cache
            if (Utils.isConnected(currentActivity)){
                String url = ApplicationConstants.SECTIONS_WEBSERVICE_URL +  "getSection.php?code_country="+currentCountry.getCode_country()+"&code_section="+currentSection.getCode_section();
                jsonobject = JSONfunctions.getJSONfromURL(url);
            }
            else{
                try {
                    jsonobject = new JSONObject(Utils.loadSectionFromFile(currentActivity, currentSection.getCode_section()));
                }catch (Exception e){
                    Log.d(TAG, "ERROR GETTING SECTIONS FROM FILE");
                }
            }


            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("section");

                jsonobject = jsonarray.getJSONObject(0);
                sectionChoosed = new Section(
                        jsonobject.optString("name"),
                        jsonobject.optString("code_section"),
                        jsonobject.optString("code_country"),
                        jsonobject.optString("Address"),
                        jsonobject.optString("Telephone"),
                        jsonobject.optString("website"),
                        jsonobject.optString("E-Mail"),
                        jsonobject.optString("University")
                );


            } catch (Exception e) {
                Log.d(TAG, "Error" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            if (sectionChoosed != null)
                startButton.setEnabled(true);
        }
    }
}
