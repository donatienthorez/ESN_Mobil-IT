package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.esn.mobilit.activities.SplashActivity;


public class FirstLaunchActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private SharedPreferences spOptions;
    private SharedPreferences.Editor spOptionEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init vars
        spOptions = getSharedPreferences("section", 0);

        chooseActivity();
    }

    protected void onResume(){
        super.onResume();
        chooseActivity();
    }

    protected void chooseActivity(){
        String code_section = spOptions.getString("CODE_SECTION", null);
        String code_country = spOptions.getString("CODE_COUNTRY", null);
        String section_website = spOptions.getString("SECTION_WEBSITE", null);
        Intent intent = null;

        //Log.d(TAG, "CODE_SECTION :"     + code_section);
        //Log.d(TAG, "CODE_COUNTRY :"     + code_country);
        //Log.d(TAG, "SECTION_WEBSITE :"  + section_website);

        if ((code_country == null || code_country.equalsIgnoreCase(""))
                || (code_section == null    || code_section.equalsIgnoreCase(""))){
            if (code_country == null || code_country.equalsIgnoreCase("")
                || (code_section == null    || code_section.equalsIgnoreCase("")) ) {
                if (code_country == null || code_country.equalsIgnoreCase("")) {
                    intent = new Intent(this, CountriesSpinnerActivity.class);
                } else {
                    if (code_section == null || code_section.equalsIgnoreCase("")) {
                        intent = new Intent(this, SectionsSpinnerActivity.class);
                    }
                }
            }else{
                intent = new Intent(this, GetSectionDetail.class);
            }

            startActivity(intent);
        }
        else {
            if (section_website == null || section_website.equalsIgnoreCase("")){
                intent = new Intent(this, GetSectionDetail.class);
                startActivity(intent);
            }
            else{
                finish();
            }


        }
    }
}
