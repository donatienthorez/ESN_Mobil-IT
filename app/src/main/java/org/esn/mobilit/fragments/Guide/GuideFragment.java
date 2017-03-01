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
import org.esn.mobilit.activities.BaseActivity;
import org.esn.mobilit.adapters.GuideListAdapter;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.services.AppState;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.navigation.NavigationUri;
import org.esn.mobilit.services.navigation.NavigationUriType;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.InjectUtil;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideFragment extends Fragment {

    @Inject
    GuideService guideService;
    @Inject
    AppState appState;

    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayoutListView;

    @Bind(R.id.recyclerViewFeedList)
    protected RecyclerView recyclerView;

    @Bind(R.id.empty)
    protected TextView emptyListMessage;

    private Node currentNode;
    private List<Node> listNodes;
    private GuideListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this, view);
        InjectUtil.component().inject(this);
        adapter = new GuideListAdapter();
        if (getArguments() != null && getArguments().getSerializable("node") != null) {
            setCurrentNode((Node) getArguments().getSerializable("node"));
        } else {
            refreshContent();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayoutListView.setEnabled(false);
        refreshContent();

        adapter.setOnItemClickListener(new GuideListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentNode != null) { position--;}
                if (position >= 0 && listNodes.get(position) != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("node", listNodes.get(position) );
                    NavigationUri navigationUri = new NavigationUri(NavigationUriType.GUIDE, bundle);
                    ((BaseActivity) getActivity()).navigateToUri(navigationUri, true);
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
                    public void onNoConnection(Guide guide) {
                    }

                    @Override
                    public void onSuccess(Guide result) {
                        setCurrentNode(currentNode);
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(appState.getGuide() != null ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onNoAvailableData() {
                        if (appState.getGuide() == null) {
                            swipeRefreshLayoutListView.setRefreshing(false);
                            emptyListMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(appState.getGuide() == null ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        swipeRefreshLayoutListView.post(thread);
    }

    private void setCurrentNode(Node node) {
        Guide guide = appState.getGuide();

        if (guide != null && guide.isActivated() && guide.isCreated()) {
            this.listNodes = node == null ? guide.getNodes() : node.getNodes();
            this.currentNode = node;
            if (adapter != null) {
                adapter.setNodes(listNodes, currentNode);
            }
        }
    }
}
