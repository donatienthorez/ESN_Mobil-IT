package org.esn.mobilit.services.gcm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.esn.mobilit.network.providers.PostRegProvider;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.interfaces.CachableInterface;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.callbacks.Callback;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.client.Response;

@Singleton
public class RegIdService implements CachableInterface {

    String regId = "";
    private static final String TAG = "RegIdService";

    @ForApplication
    @Inject
    Context context;

    @Inject
    AppState appState;

    @Inject
    PreferencesService preferencesService;

    @Inject
    public RegIdService() {
        InjectUtil.component().inject(this);
    }

    public String getString(){
        return ApplicationConstants.PREFERENCES_REG_ID;
    }


    public void register() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkPlayServices()) {
                    Crashlytics.log(Log.ERROR, TAG, "CheckPlay services is not available");
                    return;
                }

                try {
                    setRegId(
                            GoogleCloudMessaging
                                    .getInstance(context)
                                    .register(ApplicationConstants.GOOGLE_PROJECT_ID)
                    );
                } catch (IOException exception) {
                    Crashlytics.logException(exception);
                }

                if (TextUtils.isEmpty(getRegId())) {
                    return;
                }

                PostRegProvider.makeRequest(
                        appState.getSection().getCode_section(),
                        getRegId(),
                        new Callback<Response>() {
                            @Override
                            public void onSuccess(Response result) {
                                preferencesService.setDefaults(getString(), regId);
                            }

                            @Override
                            public void onFailure(String error) {
                            }
                        }
                );
            }
        });
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}
