package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Countries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountriesSpinnerActivity extends Activity {

    private ArrayList<String> spinnerCountries;
    private ArrayList<Countries> countries_list;
    private JSONObject jsonobject;
    private JSONArray jsonarray;
    private Countries currentCountry;
    private int count=0;
    private Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(CountriesSpinnerActivity.class.getSimpleName(),"onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mySpinner = (Spinner) findViewById(R.id.countries);
        new DownloadJSONCountries().execute();
    }

    // Download JSON file AsyncTask for Countries
    private class DownloadJSONCountries extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(CountriesSpinnerActivity.class.getSimpleName(),"doInBackground:");
            countries_list = new ArrayList<Countries>();
            // Create an array to populate the spinner
            spinnerCountries = new ArrayList<String>();
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
                    spinnerCountries.add(countries_object.getName());
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            Log.d(CountriesSpinnerActivity.class.getSimpleName(),"onPostExecute:");
            // Locate the spinner in activity_main.xml
            setContentView(R.layout.activity_countries_spinner);
            mySpinner = (Spinner) findViewById(R.id.countries);
            mySpinner.setSelected(false);

            // Spinner adapter
            mySpinner.setAdapter(new SpinnerAdapter(CountriesSpinnerActivity.this,spinnerCountries));

            // Spinner on item click listener
            mySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            Log.d(CountriesSpinnerActivity.class.getSimpleName(),"COUNT:"+count);

                            if(count >= 1) {

                                //Set currentCountry
                                currentCountry = countries_list.get(position);

                                //Change preferences
                                SharedPreferences.Editor spOptionEditor;
                                SharedPreferences spOptions;

                                    //Init
                                    spOptions = getSharedPreferences("section", 0);

                                    //Change
                                    spOptionEditor = spOptions.edit();
                                    spOptionEditor.putString("CODE_COUNTRY", currentCountry.getCode_country());
                                    spOptionEditor.commit();

                                Log.d(CountriesSpinnerActivity.class.getSimpleName(),"FINISH");
                                finish();
                            }
                            count++;
                        }

                        public void onNothingSelected(AdapterView<?> arg0) {
                            Log.d(CountriesSpinnerActivity.class.getSimpleName(),"onNothingSelected:");
                        }
                    });
        }
    }
}
