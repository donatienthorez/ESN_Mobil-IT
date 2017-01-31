package org.esn.mobilit.services;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.utils.inject.InjectUtil;
import org.esn.mobilit.utils.parser.RSSFeedParser;
import org.esn.mobilit.utils.storage.InternalStorage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CacheService {

    @Inject
    InternalStorage internalStorage;

    @Inject
    public CacheService() {
        InjectUtil.component().inject(this);
    }

    /*
     * Get serializable object in cache
     *
     * @param String key
     *
     * @return Object o
     */
    public Object get(String key){
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
    public void save(String key, Object object){

        try {
            internalStorage.writeObject(key, object);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }

    public void delete(String key){
        try {
            internalStorage.deleteObject(key);
        } catch (Exception exception){
            Crashlytics.logException(exception);
        }
    }

    public RSSFeedParser getFeed(String feedType) {
        return new RSSFeedParser((List<RSSItem>) this.get(feedType));
    }

    public void setFeed(String feedType, List<RSSItem> rssItemList) {
        this.save(feedType, rssItemList);
    }
}
