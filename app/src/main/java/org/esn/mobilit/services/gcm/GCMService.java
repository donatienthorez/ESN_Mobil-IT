package org.esn.mobilit.services.gcm;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.tasks.gcm.RegisterTask;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GCMService implements Cachable, Launchable{

    GoogleCloudMessaging gcmObj;
    String regId = "";
    private String pushMsg = "";

    private static GCMService instance;

    private GCMService(){}

    public static GCMService getInstance(){
        if (instance == null){
            instance = new GCMService();
        }
        return instance;
    }

    public String getString(){
        return ApplicationConstants.PREFERENCES_REG_ID;
    }

    @Override
    public void doAction(final NetworkCallback callback) {
        if (PreferencesService.getDefaults(getString()) == null) {

            if (checkPlayServices()) {
                new RegisterTask(new Callback<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        PreferencesService.setDefaults(getString(), regId);
                        callback.onSuccess(result);

                    }

                    @Override
                    public void onFailure(String ex) {
                        callback.onFailure(ex);

                    }
                }).execute();
            }
        }
        else {
            callback.onSuccess(null);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(MobilITApplication.getContext());
        return resultCode == ConnectionResult.SUCCESS;
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
