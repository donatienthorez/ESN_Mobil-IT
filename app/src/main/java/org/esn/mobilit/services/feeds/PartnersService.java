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

public class PartnersService {

    private static PartnersService instance;
    private static RSS partners;

    private PartnersService() {
        instance = new PartnersService();
    }

    public static PartnersService getInstance() {
        if (instance == null){
            instance = new PartnersService();
        }
        return instance;
    }

    private interface PartnersServiceInterface{
        @GET(ApplicationConstants.PARTNERS_PATH + ApplicationConstants.FEED_PATH)
        void getNews(Callback<RSS> callback);
    }

    public static RSS getPartners(final NetworkCallback<RSS> callback) {
        try{
            initPartners(callback);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return partners;
    }

    public static void initPartners(final NetworkCallback<RSS> callback) throws ParseException{
        PartnersServiceInterface partnersService = new RestAdapter
                .Builder()
                .setEndpoint(PreferencesService.getDefaults("SECTION_WEBSITE"))
                .setConverter(new SimpleXMLConverter())
                .build()
                .create(PartnersServiceInterface.class);

        partnersService.getNews(new Callback<RSS>() {
            @Override
            public void success(RSS partners, Response response) {
                partners.getRSSChannel().moveImage();
                FeedService.getInstance().setFeedPartners(new RSSFeedParser(partners.getRSSChannel().getList()));
                CacheService.saveObjectToCache(
                        "feedPartners",
                        new RSSFeedParser(partners.getRSSChannel().getList())
                );
                callback.onSuccess(partners);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}
