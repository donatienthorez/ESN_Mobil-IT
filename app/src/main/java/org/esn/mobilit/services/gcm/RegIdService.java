package org.esn.mobilit.services.gcm;

import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import retrofit.client.Response;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.providers.PostRegProvider;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.Callback;

public class RegIdService implements CachableInterface {

    String regId = "";

    private static RegIdService instance;

    private RegIdService(){}

    public static RegIdService getInstance(){
        if (instance == null){
            instance = new RegIdService();
        }
        return instance;
    }

    public String getString(){
        return ApplicationConstants.PREFERENCES_REG_ID;
    }

    public void register() {
        if (!checkPlayServices()) {
            return;
        }

        try {
            setRegId(
                    GoogleCloudMessaging
                            .getInstance(MobilITApplication.getContext())
                            .register(ApplicationConstants.GOOGLE_PROJ_ID)
            );
        } catch (IOException e) {
        }

        if (TextUtils.isEmpty(getRegId())) {
            return;
        }

        Section section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        PostRegProvider.makeRequest(
                section.getCode_section(),
                getRegId(),
                new Callback<Response>() {
                    @Override
                    public void onSuccess(Response result) {
                        PreferencesService.setDefaults(getString(), regId);
                    }

                    @Override
                    public void onFailure(String error) {
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
}
