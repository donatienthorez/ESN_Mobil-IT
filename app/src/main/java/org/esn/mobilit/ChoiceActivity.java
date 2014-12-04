package org.esn.mobilit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class ChoiceActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private SharedPreferences spOptions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reinitPreferences();
        chooseActivity();
    }

    protected void onResume(){
        super.onResume();
        chooseActivity();
    }

    protected void reinitPreferences(){
        //Init
        SharedPreferences.Editor spOptionEditor;
        spOptions = getSharedPreferences("section", 0);

        //Change
        spOptionEditor = spOptions.edit();
        spOptionEditor.putString("CODE_SECTION", null);
        spOptionEditor.putString("CODE_COUNTRY", null);
        spOptionEditor.commit();
    }


    protected void chooseActivity(){
        spOptions = getSharedPreferences("section", 0);
        String code_section = spOptions.getString("CODE_SECTION", null);
        String code_country = spOptions.getString("CODE_COUNTRY", null);
        String section_website = spOptions.getString("SECTION_WEBSITE", null);
        Intent intent = null;

        Log.d(TAG, "CODE_SECTION :"     + code_section);
        Log.d(TAG, "CODE_COUNTRY :"     + code_country);
        Log.d(TAG, "SECTION_WEBSITE :"  + section_website);

        if ((code_country == null || code_country.equalsIgnoreCase(""))
                || (code_section == null    || code_section.equalsIgnoreCase(""))
                || (section_website == null || section_website.equalsIgnoreCase(""))){

            if (code_country == null || code_country.equalsIgnoreCase("")
                || (code_section == null    || code_section.equalsIgnoreCase("")) ) {

                if (code_country == null || code_country.equalsIgnoreCase("")) {
                    Log.d(TAG, "CODE_COUNTRY && CODE_SECTION :null");
                    intent = new Intent(this, CountriesSpinnerActivity.class);
                } else {
                    if (code_section == null || code_section.equalsIgnoreCase("")) {
                        Log.d(TAG, "CODE_SECTION :" + code_section);
                        Log.d(TAG, "CODE_COUNTRY :" + code_country);
                        intent = new Intent(this, SectionsSpinnerActivity.class);
                    } else {
                        intent = new Intent(this, SectionSpinnerActivity.class);
                    }
                }
            }else{
                intent = new Intent(this, SectionSpinnerActivity.class);
            }
        }
        else {
            Log.d(TAG, "CODE_SECTION :" + code_section);
            Log.d(TAG, "CODE_COUNTRY :" + code_country);
            Log.d(TAG, "SECTION_WEBSITE :" + section_website);
            intent = new Intent(this, org.esn.mobilit.SplashActivity.class);
        }

        startActivity(intent);
    }
}
