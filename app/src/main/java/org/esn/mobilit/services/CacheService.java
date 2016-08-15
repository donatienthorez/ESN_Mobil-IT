package org.esn.mobilit.services;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.utils.storage.InternalStorage;

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
        try {
            if (InternalStorage.objectExists(key)) {
                return InternalStorage.readObject(key);
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
    public static void saveObjectToCache(String key, Object object){

        try {
            InternalStorage.writeObject(key, object);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }

    public static void deleteObjectFromCache(String key){
        try {
            InternalStorage.deleteObject(key);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }
}
