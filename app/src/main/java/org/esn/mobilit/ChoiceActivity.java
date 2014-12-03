package org.esn.mobilit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class ChoiceActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private SharedPreferences spOptions;
    private SharedPreferences.Editor spOptionEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        spOptions = getSharedPreferences("section", 0);
        spOptionEditor = spOptions.edit();
        spOptionEditor.putString("SECTION_NAME", "");
        spOptionEditor.commit();

        String section_name = spOptions.getString("SECTION_NAME", null);
        Intent intent;

        if (section_name.equalsIgnoreCase("")) {
            Log.d(TAG, "SECTION_NAME null");
            intent = new Intent(this, CountrySpinnerActivity.class);
        }
        else {
            Log.d(TAG, "SECTION_NAME :" + section_name);
            intent = new Intent(this, org.esn.mobilit.SplashActivity.class);
        }

        startActivity(intent);
    }
}
