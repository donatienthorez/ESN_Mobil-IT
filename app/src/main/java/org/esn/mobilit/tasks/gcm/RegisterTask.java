package org.esn.mobilit.tasks.gcm;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.activities.Callback;
import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.services.GCMService;
import org.esn.mobilit.utils.ApplicationConstants;

import java.io.IOException;

public class RegisterTask extends AsyncTask<Void, Void, String> {

    Callback<Object> callback;

    public RegisterTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = "";
        try {
            if (GCMService.getInstance().getGcmObj() == null) {
                GoogleCloudMessaging gcmObj = GoogleCloudMessaging.getInstance(MobilITApplication.getContext());
                GCMService.getInstance().setGcmObj(gcmObj);
            }
            String regId = GCMService.getInstance().getGcmObj().register(ApplicationConstants.GOOGLE_PROJ_ID);
            GCMService.getInstance().setRegId(regId);

        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        if (!TextUtils.isEmpty(GCMService.getInstance().getRegId())) {
            new PostRegID(callback).execute();
        }
        else
        {
            callback.onSuccess(null);
        }
    }
}