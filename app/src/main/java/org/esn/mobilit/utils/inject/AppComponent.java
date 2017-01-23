package org.esn.mobilit.utils.inject;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.activities.FirstLaunchActivity;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.FeedListAdapter;
import org.esn.mobilit.adapters.GuideListAdapter;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.fragments.NotificationFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;
import org.esn.mobilit.network.providers.FeedProvider;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.services.PreferencesService;
import org.esn.mobilit.services.feeds.EventsService;
import org.esn.mobilit.services.feeds.NewsService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.utils.parser.RSSFeedParser;
import org.esn.mobilit.utils.storage.InternalStorage;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AndroidModules.class
        }
)
public interface AppComponent{
    void inject(MobilITApplication app);

    void inject(FirstLaunchActivity firstLaunchActivity);
    void inject(HomeActivity homeActivity);

    void inject(AboutFragment aboutFragment);
    void inject(NotificationFragment notificationFragment);
    void inject(DetailsFragment detailsFragment);
    void inject(GuideFragment guideFragment);

    void inject(GuideListAdapter guideListAdapter);

    void inject(FeedListAdapter feedListAdapter);

    void inject(RSSFeedParser rssFeedParser);

    void inject(PreferencesService preferencesService);

    void inject(InternalStorage internalStorage);

    void inject(CacheService cacheService);

    void inject(AppState appState);

    void inject(FeedProvider feedProvider);

    void inject(EventsService eventsService);
    void inject(NewsService newsService);
    void inject(PartnersService partnersService);


    void inject(FeedListFragment feedListFragment);
}