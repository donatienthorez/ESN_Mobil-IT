package org.esn.mobilit.utils.inject;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.activities.FirstLaunchActivity;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.GuideListAdapter;
import org.esn.mobilit.fragments.AboutFragment;
import org.esn.mobilit.fragments.Guide.GuideFragment;
import org.esn.mobilit.fragments.NotificationFragment;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;
import org.esn.mobilit.fragments.Satellite.FeedListFragment;

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
    void inject(FeedListFragment feedListFragment);
    void inject(DetailsFragment detailsFragment);
    void inject(GuideFragment guideFragment);

    void inject(GuideListAdapter guideListAdapter);

}