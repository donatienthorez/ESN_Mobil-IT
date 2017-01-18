package org.esn.mobilit.services;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.storage.InternalStorage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CacheService {

    private static final String TAG = CacheService.class.getSimpleName();

    Context context;

    @Inject
    public CacheService(@ForApplication Context context) {
        this.context = context;
    }

    @Inject
    InternalStorage internalStorage;


    /*
     * Get serializable object in cache
     *
     * @param String key
     *
     * @return Object o
     */
    public Object getObjectFromCache(String key){
        try {
            if (internalStorage.objectExists(key)) {
                return internalStorage.readObject(key);
            }
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
        return null;
    }

    /*
     * Save serializable object in cache
     *
     * @param String key
     * @param Object o
     */
    public void saveObjectToCache(String key, Object object){

        try {
            internalStorage.writeObject(key, object);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }

    public void deleteObjectFromCache(String key){
        try {
            internalStorage.deleteObject(key);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }
}
