package org.esn.mobilit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.esn.mobilit.pojo.Country;
import org.esn.mobilit.pojo.Section;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Spider on 02/12/14.
 */
public class CountrySpinnerActivity extends Activity {

    private static final String TAG = CountrySpinnerActivity.class.getSimpleName();
    ArrayList<String> spinnerCountries;
    ArrayList<Country> countries;
    ArrayList<String> spinnerSections;
    ArrayList<Section> sections;
    JSONObject jsonobject;
    JSONArray jsonarray;
    Intent intent;
    Context context = this;
    private Country currentCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_spinner);

        new DownloadJSONCountries().execute();
    }

    // Download JSON file AsyncTask for Countries
    private class DownloadJSONCountries extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            countries = new ArrayList<Country>();
            // Create an array to populate the spinner
            spinnerCountries = new ArrayList<String>();
            // JSON file URL address
            jsonobject = org.esn.mobilit.JSONfunctions
                    .getJSONfromURL("http://www.esnlille.fr/WebServices/Sections/getCountries.php");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("countries");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Country c = new Country(jsonobject.optString("code"),jsonobject.optString("name"),jsonobject.optString("url"));
                    countries.add(c);

                    // Populate spinner with country names
                    spinnerCountries.add(c.getName());
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Spinner mySpinner = (Spinner) findViewById(R.id.countries);
            mySpinner.setSelected(false);
            // Spinner adapter
            mySpinner
                    .setAdapter(new ArrayAdapter<String>(CountrySpinnerActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            spinnerCountries));

            // Spinner on item click listener
            mySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        int count=0;

                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            if(count >= 1) {
                                // TODO Auto-generated method stub
                                // Locate the textviews in activity_main.xml
                                TextView txtName = (TextView) findViewById(R.id.name);
                                TextView txtCode = (TextView) findViewById(R.id.code);
                                TextView txtUrl = (TextView) findViewById(R.id.url);

                                // Set the text followed by the position
                                txtName.setText("Name : "
                                        + countries.get(position).getName());
                                txtCode.setText("Code : "
                                        + countries.get(position).getCode_country());
                                txtUrl.setText("URL : "
                                        + countries.get(position).getUrl());

                                //Set currentCountry
                                currentCountry = countries.get(position);

                                //Change preferences
                                SharedPreferences.Editor spOptionEditor;
                                SharedPreferences spOptions;

                                    //Init
                                    spOptions = getSharedPreferences("section", 0);

                                    //Change
                                    spOptionEditor = spOptions.edit();
                                    spOptionEditor.putString("CODE_COUNTRY", currentCountry.getCode_country());
                                    spOptionEditor.commit();

                                finish();
                                /*
                                //Load new Activity
                                intent = new Intent(context, SectionSpinnerActivity.class);
                                intent.putExtra("code_country", countries.get(position).getCode_country());

                                // Start new Activity
                                startActivity(intent);
                                */
                            }
                            count++;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }
}
