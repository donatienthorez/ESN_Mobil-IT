package org.esn.mobilit.fragments.Guide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.GuideListAdapter;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideFragment extends Fragment {

    @Bind(R.id.swipe_refresh) protected SwipeRefreshLayout swipeRefreshLayoutListView;
    @Bind(R.id.recyclerViewFeedList) protected RecyclerView recyclerView;
    @Bind(R.id.empty) protected TextView emptyListMessage;

    private Guide guide;
    private Node currentNode;
    private List<Node> listNodes;
    private GuideListAdapter adapter;

    @Inject
    GuideService guideService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this, view);
        InjectUtil.component().inject(this);
        adapter = new GuideListAdapter();
        adapter.setNodes(listNodes, currentNode);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayoutListView.setEnabled(false);
        refreshContent();

        adapter.setOnItemClickListener(new GuideListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentNode != null) { position--;}
                if (position >= 0 && listNodes.get(position) != null) {
                    ((HomeActivity) getActivity()).loadGuideFragment(guide, listNodes.get(position), true);
                }
            }
        });

        return view;
    }

    /**
     * Refreshes content of the listView by calling the Retrofit Feed provider.
     */
    private void refreshContent(){
        swipeRefreshLayoutListView.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayoutListView.setRefreshing(true);
        Thread thread = (new Thread() {
            @Override
            public void run() {
                guideService.getFromSite(new NetworkCallback<Guide>() {
                    @Override
                    public void onNoConnection() {
                    }

                    @Override
                    public void onSuccess(Guide result) {
                        setCurrentNode(result, currentNode);
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(guide != null ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onNoAvailableData() {
                        if (guide == null) {
                            swipeRefreshLayoutListView.setRefreshing(false);
                            emptyListMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(guide == null ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        swipeRefreshLayoutListView.post(thread);
    }

    public GuideFragment setCurrentNode(Guide guide, Node node) {
        if (guide != null && guide.isActivated() && guide.isCreated()) {
            this.guide = guide;
            this.listNodes = node != null ? node.getNodes() : guide.getNodes();
            this.currentNode = node;
            if (adapter != null && currentNode == null) {
                adapter.setNodes(listNodes, currentNode);
            }
        }
        return this;
    }
}
