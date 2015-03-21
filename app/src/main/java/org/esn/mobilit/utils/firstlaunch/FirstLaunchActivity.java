package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Category;
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
    //private ArrayList<Sections> sections_list;
    //private Sections currentSection;
    private ProgressBar progressBar;

    // Attributes for SurvivalGuide
    private ArrayList<Category> categories_list;

    // Other Attributes
    private String revision;

    // Detail Section
    private Section sectionChoosed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init activity
        currentActivity = this;

        //Change layout
        getActionBar().setIcon(R.drawable.ic_launcher);
        setContentView(R.layout.activity_firstlaunch);
        textView    = (TextView) findViewById(R.id.chooseyourcountry);
        textView.setText("Checking revision ...");

        if (Utils.getDefaults(this, "revision") != null){
            textView.setText("Current revision is " + Utils.getDefaults(this, "revision"));
        }else{
            textView.setText("No revision stored, check last revision online");
        }

        new checkLastRevision().execute();

        //Init Content
        /*startButton = (Button) findViewById(R.id.start_button);
        startButton.setEnabled(false);
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

        addSpinnerCountries();*/
    }

    public void launchAwesomeFeatures(View view){
        //Utils.setDefaults(this, "CODE_COUNTRY", currentCountry.getCode_country());
        //Utils.setDefaults(this, "CODE_SECTION", currentSection.getCode_section());
        //Utils.setDefaults(this, "SECTION_WEBSITE", sectionChoosed.getWebsite());

        Log.d(TAG, " Country : " + Utils.getDefaults(this, "CODE_COUNTRY"));
        Log.d(TAG, " Section : " + Utils.getDefaults(this, "CODE_SECTION"));
        Log.d(TAG, " Section Website : " + Utils.getDefaults(this, "SECTION_WEBSITE"));
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
        //new DownloadJSONSections().execute();
    }

    protected void onResume(){
        super.onResume();
        //chooseActivity();
        //startButton = (Button) findViewById(R.id.start_button);
        //addSpinnerCountries();
    }

    private class checkLastRevision extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonobject = null;
            JSONArray jsonarray = null;

            // GET Json FROM Webservice to check the last revision
            jsonobject = JSONfunctions.getJSONfromURL(ApplicationConstants.APP_WEBSERVICE_URL + "getRevision.php");
            try {
                jsonarray = jsonobject.getJSONArray("revision");
                jsonobject = jsonarray.getJSONObject(0);
                revision = jsonobject.optString("date");

                if (revision.length() == 14)
                    Log.d(TAG, "revision online = " + revision);
                else
                    Log.d(TAG, "error getting revision");
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                Log.d(TAG, "error getting revision");
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void args) {
            if (revision != null && revision.length() == 14) {
                String pref_revision = Utils.getDefaults(currentActivity, "revision");
                if (pref_revision != null && pref_revision.length() == 14) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHiiss");

                    try {
                        Date dl_date = format.parse(revision);
                        Date pref_date = format.parse(pref_revision);

                        if (dl_date.compareTo(pref_date) < 0){
                            Log.d(TAG, "dl_date is Greater than my pref_date");
                            Utils.setDefaults(currentActivity, "revision", revision);
                            new DownloadJSON().execute();
                        }else{
                            Log.d(TAG, "pref_date is Greater than my dl_date");
                        }
                    }
                    catch(Exception e){
                        Log.d(TAG, e.toString());
                    }
                }else{
                    Log.d(TAG, "No previous revision stored, update revision");
                    Utils.setDefaults(currentActivity, "revision", revision);
                }
            }
            else{
                Log.d(TAG, "Error getting revision onPostExecute");
            }

        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "getting json from online");

            //Check if file revision is set
            String pref_revision = Utils.getDefaults(currentActivity, "revision");
            if (pref_revision != null){
                jsonobject = JSONfunctions.getJSONfromURL(ApplicationConstants.APP_WEBSERVICE_URL + "json/" + pref_revision + ".json");

                try {
                    Countries countries = new Countries(pref_revision);
                    JSONArray json_countries = jsonobject.getJSONArray("countries");
                    for (int i = 0; i < json_countries.length(); i++) {
                        JSONObject json_country = json_countries.getJSONObject(i);
                        Country country = new Country(
                                json_country.optInt("id"),
                                json_country.optString("name"),
                                json_country.optString("url"),
                                json_country.optString("code_country"),
                                json_country.optString("website"),
                                json_country.optString("email")
                        );
                        countries.addCountry(country);

                        JSONArray json_sections = json_country.getJSONArray("sections");
                        for (int j = 0; j < json_sections.length(); j++) {
                            JSONObject json_section = json_sections.getJSONObject(j);
                            Section section = new Section(
                                    json_section.optInt("id"),
                                    json_section.optInt("id_country"),
                                    json_section.optString("name"),
                                    json_section.optString("url"),
                                    json_section.optString("code_section"),
                                    json_section.optString("address"),
                                    json_section.optString("website"),
                                    json_section.optString("phone"),
                                    json_section.optString("email"),
                                    json_section.optString("university")
                            );
                            country.addSection(section);
                        }
                    }

                    if (countries.getCountries().size() > 0){
                        Log.d(TAG, "countries downloaded = " + countries.getCountries().size());
                        Log.d(TAG, "saving new json in cache");
                    }
                    else
                        Log.d(TAG,"error getting countries");
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    Log.d(TAG,"error getting countries");
                    e.printStackTrace();
                }

            }else{
                Log.d(TAG,"revision from preferences is null");
            }

            return null;
        }

        protected void onPostExecute(Void args) {

        }
    }
}
