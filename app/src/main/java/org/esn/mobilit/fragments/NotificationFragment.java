package org.esn.mobilit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.BaseActivity;
import org.esn.mobilit.adapters.NotificationAdapter;
import org.esn.mobilit.models.Notification;
import org.esn.mobilit.services.NotificationService;
import org.esn.mobilit.services.navigation.NavigationUri;
import org.esn.mobilit.services.navigation.NavigationUriType;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {

    @Inject
    NotificationService notificationService;

    @Bind(R.id.recyclerViewNotificationsList)
    protected RecyclerView recyclerView;

    //fixme put that list inside appState
    private List<Notification> notificationList;

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        InjectUtil.component().inject(this);

        notificationList = notificationService.getFromCache();
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NavigationUri navigationUri = new NavigationUri(NavigationUriType.NOTIFICATIONS);
                //FIXME
//                navigationUri.setParameter("rssItem", notificationList.get(position).getRssItem());
                ((BaseActivity) getActivity()).navigateToUri(navigationUri, false);
            }
        });

        return view;
    }
}