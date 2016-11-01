package org.esn.mobilit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.NotificationAdapter;
import org.esn.mobilit.models.Notification;
import org.esn.mobilit.services.NotificationService;
import org.esn.mobilit.widgets.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {

    @Bind(R.id.recyclerViewNotificationsList) protected RecyclerView recyclerView;
    private List<Notification> notificationList;

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        notificationList = NotificationService.getInstance().getFromCache();
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), null);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((HomeActivity) getActivity()).loadNotificationFragment(notificationList.get(position));
            }
        });

        return view;
    }
}
