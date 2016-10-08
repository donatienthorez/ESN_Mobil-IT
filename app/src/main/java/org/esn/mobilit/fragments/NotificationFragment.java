package org.esn.mobilit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.esn.mobilit.R;
import org.esn.mobilit.adapters.NotificationAdapter;
import org.esn.mobilit.models.Notification;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {

    @Bind(R.id.recyclerViewNotificationsList) protected RecyclerView recyclerView;

    private List<Notification> notifications;
    private NotificationAdapter adapter;

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new NotificationAdapter(getNotificationsList());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * To be deleted, just to give data for now
     *
     * @return List<Notification>
     */
    public List<Notification> getNotificationsList(){

        notifications = new ArrayList<>();
        notifications.add(new Notification("t1", "d1"));
        notifications.add(new Notification("t2", "d2"));
        notifications.add(new Notification("t3", "d3"));

        return notifications;
    }
}
