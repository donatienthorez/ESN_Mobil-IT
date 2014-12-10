package org.esn.mobilit.firstlaunch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.esn.mobilit.JSONfunctions;
import org.esn.mobilit.pojo.Section;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetSectionDetail extends Activity {

    private static final String TAG = SectionsSpinnerActivity.class.getSimpleName();
    private Section section;
    private SharedPreferences spOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spOptions = getSharedPreferences("section", 0);
        new DownloadJSONSection().execute();
    }

    // Download JSON file AsyncTask for Sections
    private class DownloadJSONSection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences.Editor spOptionEditor;
            SharedPreferences spOptions = getSharedPreferences("section", 0);
            String code_section = spOptions.getString("CODE_SECTION", null);
            String code_country = spOptions.getString("CODE_COUNTRY", null);

            String url = "http://www.esnlille.fr/WebServices/Sections/getSection.php?code_country="+code_country+"&code_section="+code_section;
            Log.d(TAG, url);
            JSONObject jsonobject;
            JSONArray jsonarray;

            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(url);

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("section");
                //for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(0);
                    section = new Section(
                            jsonobject.optString("name"),
                            jsonobject.optString("code_section"),
                            jsonobject.optString("code_country"),
                            jsonobject.optString("Address"),
                            jsonobject.optString("Telephone"),
                            jsonobject.optString("website"),
                            jsonobject.optString("E-Mail"),
                            jsonobject.optString("University")
                    );
                //}


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            Log.d(TAG,"onPostExecute");

            if (section != null) {
                //Creat SharedPreferences
                SharedPreferences.Editor spOptionEditor;
                SharedPreferences spOptions;

                //Init
                spOptions = getSharedPreferences("section", 0);

                //Change
                spOptionEditor = spOptions.edit();
                spOptionEditor.putString("SECTION_WEBSITE", section.getWebsite());
                spOptionEditor.commit();

                Log.d(TAG, "SECTION:" + section.toString());
                finish();
            }else{
                Log.d(TAG, "SECTION:null");
            }
        }
    }
}