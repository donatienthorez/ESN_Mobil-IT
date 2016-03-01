package org.esn.mobilit.tasks.gcm;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.services.gcm.PostRegService;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.io.IOException;
import java.text.ParseException;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterTask extends AsyncTask<Void, Void, String> {

    Callback<Object> callback;

    public RegisterTask(Callback<Object> callback) {
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
            try {
                PostRegService.registerId(
                    new NetworkCallback<Response>() {
                        @Override
                        public void onSuccess(Response result) {
                            callback.onSuccess(result);
                        }

                        @Override
                        public void onNoAvailableData() {

                        }

                        @Override
                        public void onFailure(String error) {
                            callback.onFailure(error);
                        }
                    }
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            callback.onSuccess(null);
        }
    }
}
