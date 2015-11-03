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

public class PartnersService {

    private static PartnersService instance;
    private static RSS partners;
    private static final String TAG = "NewsService";

    private PartnersService() {
        instance = new PartnersService();
    }

    public static PartnersService getInstance() {
        return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(Utils.getDefaults(MobilITApplication.getContext(), "SECTION_WEBSITE"))
            .setConverter(new SimpleXMLConverter())
            .build();

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
        PartnersServiceInterface partnersService = restAdapter.create(PartnersServiceInterface.class);
        partnersService.getNews(new Callback<RSS>() {
            @Override
            public void success(RSS partners, Response response) {
                partners.getRSSChannel().moveImage();
                FeedService.getInstance().setFeedPartners(new RSSFeed(partners.getRSSChannel().getList()));
                Utils.saveObjectToCache(
                        MobilITApplication.getContext(),
                        "feedPartners",
                        new RSSFeed(partners.getRSSChannel().getList())
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
