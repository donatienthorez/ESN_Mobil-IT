package org.esn.mobilit.tasks.gcm;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.utils.ApplicationConstants;

import java.io.IOException;

public class Register extends AsyncTask<Void, Void, String> {

    SplashActivity activity;
    String regId;
    GoogleCloudMessaging gcmObj;


    public Register(SplashActivity activity) {
        this.activity = activity;
        this.regId = "";
        this.gcmObj = activity.getGcmObj();
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = "";
        try {
            if (gcmObj == null) {
                gcmObj = GoogleCloudMessaging.getInstance(activity.getContext());
                activity.setGcmObj(gcmObj);
            }
            regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
            activity.setRegId(regId);
            msg = "Registration ID :" + regId;

        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        if (!TextUtils.isEmpty(regId)) {
            new PostRegID(activity).execute();
        }
    }
}