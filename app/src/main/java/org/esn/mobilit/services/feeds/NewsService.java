package org.esn.mobilit.services.feeds;

import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import java.text.ParseException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;

public class NewsService {

    private static NewsService instance;
    private static RSS news;
    private static final String TAG = "NewsService";

    private NewsService() {
        instance = new NewsService();
    }

    public static NewsService getInstance() {
        if (instance == null){
            instance = new NewsService();
        }
        return instance;
    }

    private interface NewsServiceInterface{
        @GET(ApplicationConstants.NEWS_PATH + ApplicationConstants.FEED_PATH)
        void getNews(Callback<RSS> callback);
    }

    public static RSS getNews(final NetworkCallback<RSS> callback) {
        try{
            initNews(callback);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return news;
    }

    public static void initNews(final NetworkCallback<RSS> callback) throws ParseException{
        NewsServiceInterface newService = new RestAdapter
                .Builder()
                .setEndpoint(PreferencesService.getDefaults("SECTION_WEBSITE"))
                .setConverter(new SimpleXMLConverter())
                .build()
                .create(NewsServiceInterface.class);

        newService.getNews(new Callback<RSS>() {
            @Override
            public void success(RSS news, Response response) {
                news.getRSSChannel().moveImage();
                FeedService.getInstance().setFeedNews(new RSSFeedParser(news.getRSSChannel().getList()));
                CacheService.saveObjectToCache(
                        "feedNews",
                        new RSSFeedParser(news.getRSSChannel().getList())
                );
                callback.onSuccess(news);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}
