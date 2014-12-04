package org.esn.mobilit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.esn.mobilit.pojo.Sections;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SectionsSpinnerActivity extends Activity {

    private static final String TAG = SectionsSpinnerActivity.class.getSimpleName();
    String code_country;
    ArrayList<String> spinnerSections;
    ArrayList<Sections> sections_list;
    JSONObject jsonobject;
    JSONArray jsonarray;
    TextView txtName;
    Sections currentSection;
    private SharedPreferences spOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_spinner);

        //Init components
        txtName = (TextView) findViewById(R.id.section_name);
        spOptions = getSharedPreferences("section", 0);

        //Check if from CountrySpinnerActivity
        Bundle datas = getIntent().getExtras();
        if (datas == null){
            code_country = spOptions.getString("CODE_COUNTRY", null);
        }else{
            code_country = datas.getString("code_country");
        }

        if (code_country != null) {
            Log.d(TAG, "Code_country:"+code_country);
            new DownloadJSONSections().execute();
        }else{
            Log.d(TAG, "Code_country:null");
        }



    }

    public void chooseSection(View view) {
        Log.d(TAG,"Entering ChooseSection");

        //Creat SharedPreferences
        SharedPreferences.Editor spOptionEditor;
        SharedPreferences spOptions;

        //Init
        spOptions = getSharedPreferences("section", 0);

        //Change
        spOptionEditor = spOptions.edit();
        spOptionEditor.putString("CODE_SECTION", currentSection.getCode_section());
        spOptionEditor.putString("CODE_COUNTRY", currentSection.getCode_country());
        spOptionEditor.commit();


        finish();
    }


    // Download JSON file AsyncTask for Sections
    private class DownloadJSONSections extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Create sections_list array
            sections_list = new ArrayList<Sections>();

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
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    Sections sections_object = new Sections(  jsonobject.optString("name"),
                                                jsonobject.optString("url"),
                                                jsonobject.optString("code_country"),
                                                jsonobject.optString("code_section"));
                    sections_list.add(sections_object);

                    // Populate spinner with sections names
                    spinnerSections.add(jsonobject.optString("name"));
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
                    .setAdapter(new ArrayAdapter<String>(SectionsSpinnerActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            spinnerSections));

            // Spinner on item click listener

            spinnerSection
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                                currentSection = sections_list.get(position);
                                txtName.setText("Name : "
                                        + currentSection.getName());
                        }
                        public void onNothingSelected(AdapterView<?> arg0) {}
                    });
        }
    }
}