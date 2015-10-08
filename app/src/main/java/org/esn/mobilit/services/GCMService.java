package org.esn.mobilit.services;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.tasks.gcm.RegisterTask;
import org.esn.mobilit.utils.Utils;

public class GCMService {

    Context context;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID = "regId";
    GoogleCloudMessaging gcmObj;
    String regId = "";
    SplashActivity activity;

    public GCMService(Context context, SplashActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void pushForGcm()
    {
        if (Utils.getDefaults(context, REG_ID) == null) {
            // Registering RegID
            if (checkPlayServices(activity)) {
                new RegisterTask(activity);
            }
        }

        activity.incrementCount();
    }
    public void storeRegIdinSharedPref() {
        Utils.setDefaults(context, REG_ID, regId);
    }

    private boolean checkPlayServices(SplashActivity activity) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Toast.makeText(
                //        context,
                //        "This device doesn't support Play services, App will not work normally",
                //        Toast.LENGTH_LONG).show();
                //finish();
            }
            return false;
        }
        return true;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public GoogleCloudMessaging getGcmObj() {
        return gcmObj;
    }

    public void setGcmObj(GoogleCloudMessaging gcmObj) {
        this.gcmObj = gcmObj;
    }
}
