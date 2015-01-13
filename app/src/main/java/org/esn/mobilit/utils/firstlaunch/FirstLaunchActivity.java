package org.esn.mobilit.utils.firstlaunch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.SplashActivity;


public class FirstLaunchActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private SharedPreferences spOptions;
    private SharedPreferences.Editor spOptionEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change layout
        setContentView(R.layout.activity_firstlaunch);

        //Init vars
        spOptions = getSharedPreferences("section", 0);

        initContent();
        chooseActivity();
    }

    protected void initContent(){
        Spinner spinnerCountries = (Spinner) findViewById(R.id.spinner_countries);
        spinnerCountries.getSelectedView();
        spinnerCountries.setClickable(false);
        spinnerCountries.setEnabled(false);
    }

    protected void chooseActivity(){
        String code_section = spOptions.getString("CODE_SECTION", null);
        String code_country = spOptions.getString("CODE_COUNTRY", null);
        String section_website = spOptions.getString("SECTION_WEBSITE", null);
        Intent intent = null;

        Log.d(TAG, "CODE_SECTION :"     + code_section);
        Log.d(TAG, "CODE_COUNTRY :"     + code_country);
        Log.d(TAG, "SECTION_WEBSITE :"  + section_website);

        //Si le pays et la section ne sont pas choisit
        if ((code_country == null || code_country.equalsIgnoreCase(""))
                || (code_section == null    || code_section.equalsIgnoreCase(""))){

            //Si le pays est null
            if (code_country == null || code_country.equalsIgnoreCase("")) {
                //On lance le l'intent pour le countriesspinner
                intent = new Intent(this, CountriesSpinnerActivity.class);
            } else if (code_section == null || code_section.equalsIgnoreCase("")) {
                intent = new Intent(this, SectionsSpinnerActivity.class);
            }

            //On lance l'intent définit avant
            startActivity(intent);
        }
        //Si le pays et la section ne sont pas null
        else {
            //Si les détails de la section sont null
            if (section_website == null || section_website.equalsIgnoreCase("")){
                //On lance l'intent pour rechercher les détails
                intent = new Intent(this, GetSectionDetail.class);
                startActivity(intent);
            }
            //Si les détails de la section ne sont pas null
            else{
                //On termine l'activité et on revient à la startactivity
                finish();
            }
        }
    }

    protected void onResume(){
        super.onResume();
        //chooseActivity();
        initContent();
    }
}
