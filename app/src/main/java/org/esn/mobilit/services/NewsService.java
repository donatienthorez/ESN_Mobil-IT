package org.esn.mobilit.services;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.NetworkCallback;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.parser.RSSFeed;

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
        return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(Utils.getDefaults(MobilITApplication.getContext(), "SECTION_WEBSITE"))
            .setConverter(new SimpleXMLConverter())
            .build();

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
        NewsServiceInterface newService = restAdapter.create(NewsServiceInterface.class);
        newService.getNews(new Callback<RSS>() {
            @Override
            public void success(RSS news, Response response) {
                news.channel.moveImage();
                FeedService.getInstance().setFeedNews(new RSSFeed(news.getChannel().getList()));
                Utils.saveObjectToCache(
                        MobilITApplication.getContext(),
                        "feedNews",
                        new RSSFeed(news.getChannel().getList())
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
