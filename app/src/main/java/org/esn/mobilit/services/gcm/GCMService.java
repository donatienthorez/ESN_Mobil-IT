package org.esn.mobilit.services.gcm;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.tasks.gcm.RegisterTask;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

public class GCMService implements Cachable, Launchable{

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID = "regId";
    GoogleCloudMessaging gcmObj;
    String regId = "";
    private String pushMsg = "";
    public static final String GCM = "gcm";

    private static GCMService instance;

    private GCMService(){}

    public static GCMService getInstance(){
        if (instance == null){
            instance = new GCMService();
        }
        return instance;
    }

    public String getString(){
        return GCM;
    }

    @Override
    public void doAction(final NetworkCallback callback) {
        if (PreferencesService.getDefaults(REG_ID) == null) {

            // Registering RegID
            if (checkPlayServices()) {
                new RegisterTask(new Callback<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        PreferencesService.setDefaults(REG_ID, regId);
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
        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }
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
