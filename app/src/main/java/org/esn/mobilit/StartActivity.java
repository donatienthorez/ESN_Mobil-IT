package org.esn.mobilit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.esn.mobilit.fragments.HomeActivity;
import org.esn.mobilit.utils.firstlaunch.FirstLaunchActivity;


public class StartActivity extends Activity {
    private static final String TAG = StartActivity.class.getSimpleName();
    private SharedPreferences spOptions;
    private SharedPreferences.Editor spOptionEditor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //Init vars
        spOptions = getSharedPreferences("section", 0);
        reinitPreferences();
        chooseActivity();
    }
    protected void reinitPreferences(){
        //Init
        spOptions = getSharedPreferences("section", 0);

        //Change
        spOptionEditor = spOptions.edit();
        spOptionEditor.putString("CODE_SECTION", null);
        spOptionEditor.putString("CODE_COUNTRY", null);
        spOptionEditor.putString("SECTION_WEBSITE", null);
        spOptionEditor.commit();
    }


    protected void onResume(){
        super.onResume();
        chooseActivity();
    }

    protected void chooseActivity(){
        String section_website = spOptions.getString("SECTION_WEBSITE", null);
        Intent intent = null;

        String code_section = spOptions.getString("CODE_SECTION", null);
        String code_country = spOptions.getString("CODE_COUNTRY", null);

        Log.d(TAG, "CODE_SECTION :"     + code_section);
        Log.d(TAG, "CODE_COUNTRY :"     + code_country);
        Log.d(TAG, "SECTION_WEBSITE :"  + section_website);

        if ((section_website == null || section_website.equalsIgnoreCase(""))){
            intent = new Intent(this, FirstLaunchActivity.class);
            Log.d(TAG,"FirstLaunchActivity");
            startActivity(intent);
        }
        else {
            intent = new Intent(this, HomeActivity.class);
            Log.d(TAG,"HomeActivity");
            startActivity(intent);
        }
    }
}
