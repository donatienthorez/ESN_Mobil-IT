package org.esn.mobilit.services;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.storage.InternalStorage;

import java.io.IOException;

public class CacheService {

    private static final String TAG = CacheService.class.getSimpleName();
    private static CacheService instance;

    private CacheService(){
    }

    public static CacheService getInstance(){
        if (instance == null){
            instance = new CacheService();
        }
        return instance;
    }

    /*
     * Get serializable object in cache
     *
     * @param String key
     *
     * @return Object o
     */
    public static Object getObjectFromCache(String key){
        Object object = null;
        if (!key.equalsIgnoreCase("countries")) {
            key = PreferencesService.getDefaults(ApplicationConstants.PREFERENCES_CODE_SECTION) + "_" + key;
        }

        try {
            if (InternalStorage.objectExists(key)) {
                object = InternalStorage.readObject(key);
            }
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
        return object;
    }

    /*
     * Save serializable object in cache
     *
     * @param String key
     * @param Object o
     */
    public static void saveObjectToCache(String key, Object object){
        if (!key.equalsIgnoreCase("countries")) {
            key = PreferencesService.getDefaults(ApplicationConstants.PREFERENCES_CODE_SECTION) + "_" + key;
        }

        try {
            InternalStorage.writeObject(key, object);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }
}
