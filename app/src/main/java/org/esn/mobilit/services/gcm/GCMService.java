package org.esn.mobilit.services.gcm;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.tasks.gcm.RegisterTask;
import org.esn.mobilit.utils.Utils;

public class GCMService {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID = "regId";
    GoogleCloudMessaging gcmObj;
    String regId = "";
    private String pushMsg = "";

    private static GCMService instance;
    public static GCMService getInstance(){
        if (instance == null){
            instance = new GCMService();
        }
        return instance;
    }

    public void pushForGcm(Activity activity, Callback<Object> callback)
    {
        if (Utils.getDefaults(MobilITApplication.getContext(), REG_ID) == null) {

            // Registering RegID
            if (checkPlayServices(activity)) {
                new RegisterTask(callback).execute();
            }
        }
        else {
            callback.onSuccess(null);
        }
    }
    public void storeRegIdinSharedPref() {
        Utils.setDefaults(MobilITApplication.getContext(), REG_ID, regId);
    }

    private boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
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

    public String getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(String pushMsg) {
        this.pushMsg = pushMsg;
    }
}
