package esnlille.esn_mobil_it;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by Spider on 21/11/14.
 */
public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // execute your xml news feed loader
        new AsyncLoadXMLFeed().execute();

    }

    private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){
            // show your progress dialog

        }

        @Override
        protected Void doInBackground(Void... voids){
            // load your xml feed asynchronously
        }

        @Override
        protected void onPostExecute(Void params){
            // dismiss your dialog
            // launch your News activity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // close this activity
            finish();
        }

    }
}