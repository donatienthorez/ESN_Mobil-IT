package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Countries;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.models.Sections;
import org.esn.mobilit.network.JSONfunctions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FirstLaunchActivity extends Activity {
    private static final String TAG = FirstLaunchActivity.class.getSimpleName();

    //JSON
    private JSONObject jsonobject;
    private JSONArray jsonarray;

    //Layout
    private LinearLayout spinners_layout;
    private Button startButton;

    // Preferences
    private SharedPreferences spOptions;
    private SharedPreferences.Editor spOptionEditor;

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

    // Detail Section
    private Section sectionChoosed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change layout
        setContentView(R.layout.activity_firstlaunch);

        //Init vars
        spOptions = getSharedPreferences("section", 0);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setEnabled(false);

        addSpinnerCountries();
    }

    public void launchAwesomeFeatures(View view){
        Log.d(TAG, " Country : " + getDefaults("CODE_COUNTRY"));
        Log.d(TAG, " Section : " + getDefaults("CODE_SECTION"));
        Log.d(TAG, " Section Website : " + getDefaults("SECTION_WEBSITE"));

        Log.d(TAG, "FINISH FIRSTLAUNCHACTIVITY");

        finish();
    }

    protected void addSpinnerCountries(){
        spinners_layout = (LinearLayout) findViewById(R.id.spinners_layout);

        //Get Layout to put spinners in it
        if (spinnerCountries == null) {
            // Init Spiner and load data
            spinnerCountries = new Spinner(this);
            new DownloadJSONCountries().execute();

            progressBar = new ProgressBar(this);
            spinners_layout.addView(progressBar);
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
                    .getJSONfromURL("http://www.esnlille.fr/WebServices/Sections/getCountries.php");

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

                        //Change preferences
                        setDefaults("CODE_COUNTRY", currentCountry.getCode_country());

                        //Load new spinner
                        addSpinnerSections();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        Log.d(FirstLaunchActivity.class.getSimpleName(), "onNothingSelected:");
                    }
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

            String url = "http://www.esnlille.fr/WebServices/Sections/getSections.php?code_country="+currentCountry.getCode_country();

            // JSON file URL address
            jsonobject = JSONfunctions.getJSONfromURL(url);
            Log.d(TAG,"URL:"+url);

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
                Log.e("Error", e.getMessage());
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

                            //Change preferences
                            setDefaults("CODE_SECTION", currentSection.getCode_section());

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
            String url = "http://www.esnlille.fr/WebServices/Sections/getSection.php?code_country="+currentCountry.getCode_country()+"&code_section="+currentSection.getCode_section();
            Log.d(TAG, "DownloadJSONSection > doInBackground > url : " + url );

            JSONObject jsonobject;
            JSONArray jsonarray;

            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(url);

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
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            Log.d(TAG,"onPostExecute Detail Section");

            if (sectionChoosed != null) {
                setDefaults("SECTION_WEBSITE", sectionChoosed.getWebsite());

                Log.d(TAG, "SECTION:" + sectionChoosed.toString());
                startButton.setEnabled(true);
            }else{
                Log.d(TAG, "SECTION:null");
            }
        }
    }
}
