package org.esn.mobilit.services.gcm;

import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.PostRegProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.launcher.interfaces.Cachable;
import org.esn.mobilit.services.launcher.interfaces.Launchable;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import java.io.IOException;

import retrofit.client.Response;

public class GCMService implements Cachable, Launchable<String>{

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
    public void doAction(final NetworkCallback<String> callback) {

        if (PreferencesService.getDefaults(getString()) != null) {
            setRegId(PreferencesService.getDefaults(getString()));
            callback.onSuccess(getRegId());
            return;
        }

        if (!checkPlayServices()) {
            callback.onFailure("Google play service is not available");
            return;
        }

        try {
            setRegId(
                    GoogleCloudMessaging
                            .getInstance(MobilITApplication.getContext())
                            .register(ApplicationConstants.GOOGLE_PROJ_ID)
            );
        } catch (IOException e) {
            callback.onFailure(e.getMessage());
        }

        if (TextUtils.isEmpty(getRegId())) {
            callback.onFailure("RegId is empty");
            return;
        }

        Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        PostRegProvider.makeRequest(
                section.getCode_section(),
                getRegId(),
                new Callback<Response>() {
                    @Override
                    public void onSuccess(Response result) {
                        callback.onSuccess(regId);
                        PreferencesService.setDefaults(getString(), regId);
                    }

                    @Override
                    public void onFailure(String error) {
                        callback.onFailure(error);
                    }
                }
        );
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

    public String getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(String pushMsg) {
        this.pushMsg = pushMsg;
    }
}
