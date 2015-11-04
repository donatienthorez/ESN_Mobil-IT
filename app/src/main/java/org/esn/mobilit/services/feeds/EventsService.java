package org.esn.mobilit.services.feeds;

import org.esn.mobilit.MobilITApplication;
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

public class EventsService {

    private static EventsService instance;
    private static RSS events;
    private static final String TAG = "EventsService";

    private EventsService() {
        instance = new EventsService();
    }

    public static EventsService getInstance() {
        return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(Utils.getDefaults(MobilITApplication.getContext(), "SECTION_WEBSITE"))
            .setConverter(new SimpleXMLConverter())
            .build();

    private interface EventsServiceInterface{
        @GET(ApplicationConstants.EVENTS_PATH + ApplicationConstants.FEED_PATH)
        void getEvents(Callback<RSS> callback);
    }

    public static RSS getEvents(final NetworkCallback<RSS> callback) {
        try{
            initEvents(callback);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return events;
    }

    public static void initEvents(final NetworkCallback<RSS> callback) throws ParseException{
        EventsServiceInterface eventsService = restAdapter.create(EventsServiceInterface.class);
        eventsService.getEvents(new Callback<RSS>() {
            @Override
            public void success(RSS events, Response response) {
                events.getRSSChannel().moveImage();
                FeedService.getInstance().setFeedEvents(new RSSFeedParser(events.getRSSChannel().getList()));
                Utils.saveObjectToCache(
                        MobilITApplication.getContext(),
                        "feedEvents",
                        new RSSFeedParser(events.getRSSChannel().getList())
                );
                callback.onSuccess(events);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}
