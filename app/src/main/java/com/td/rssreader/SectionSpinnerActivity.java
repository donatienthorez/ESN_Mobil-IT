package com.td.rssreader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.td.rssreader.pojo.Sections;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Spider on 02/12/14.
 */
public class SectionSpinnerActivity extends Activity {

    private static final String TAG = SectionSpinnerActivity.class.getSimpleName();
    String code_country;
    ArrayList<String> spinnerSections;
    ArrayList<Sections> sections;
    JSONObject jsonobject;
    JSONArray jsonarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_spinner);

        Bundle datas = getIntent().getExtras();
        code_country = datas.getString("code_country");
        if (code_country!= null) {
            Log.d(TAG, "Code_country:"+code_country);
            new DownloadJSONSections().execute();
        }else{
            Log.d(TAG, "Code_country:null");
        }



    }

    // Download JSON file AsyncTask for Sections
    private class DownloadJSONSections extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            sections = new ArrayList<Sections>();
            // Create an array to populate the spinner
            spinnerSections = new ArrayList<String>();

            String url = "http://www.esnlille.fr/WebServices/Sections/getSections.php?code_country="+code_country;
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(url);
            Log.d(TAG,"URL:"+url);
            try {

                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("sections");
                Log.d(TAG,"TAILLE:"+jsonarray.length());
                for (int i = 0; i < jsonarray.length(); i++) {
                    Log.d(TAG,jsonobject.optString("name"));
                    Sections s = new Sections(  jsonobject.optString("name"),
                                                jsonobject.optString("url"),
                                                jsonobject.optString("code_country"),
                                                jsonobject.optString("code_section"));
                    sections.add(s);

                    // Populate spinner with sections names
                    spinnerSections.add(s.getName());
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Spinner spinnerSection = (Spinner) findViewById(R.id.sections_spinner);

            // Spinner adapter
            spinnerSection
                    .setAdapter(new ArrayAdapter<String>(SectionSpinnerActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            spinnerSections));

            // Spinner on item click listener
            spinnerSection
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            TextView txtName = (TextView) findViewById(R.id.section_name);
                            txtName.setText("Name : "
                                    + sections.get(position).getName());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }
}
