package org.esn.mobilit.fragments.Guide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.FeedListAdapter;
import org.esn.mobilit.adapters.GuideListAdapter;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.services.GuideService;
import org.esn.mobilit.services.feeds.PartnersService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideFragment extends Fragment {

    @Bind(R.id.swipe_refresh) protected SwipeRefreshLayout swipeRefreshLayoutListView;
    @Bind(R.id.recyclerViewFeedList) protected RecyclerView recyclerView;
    @Bind(R.id.empty) protected TextView emptyListMessage;
    private GuideListAdapter adapter;
    private Guide guide;
    private Node currentNode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.guide = GuideService.getInstance().getFromCache();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        ButterKnife.bind(this, view);
        this.adapter = new GuideListAdapter(GuideService.getInstance().getFromCache());

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView != null && recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0)) == 0) {
                    swipeRefreshLayoutListView.setEnabled(recyclerView.getChildAt(0).getTop() >= 0);
                }
            }
        });

        swipeRefreshLayoutListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isConnected()) {
                    refreshContent();
                } else {
                    Toast.makeText(
                            MobilITApplication.getContext(),
                            getResources().getString(Utils.isConnected() ?
                                    R.string.error_message_network :
                                    R.string.info_message_no_network),
                            Toast.LENGTH_SHORT
                    ).show();
                    swipeRefreshLayoutListView.setRefreshing(false);
                }
            }
        });
        refreshContent();

        adapter.setOnItemClickListener(new GuideListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (guide.getNode(position).getNodes() != null && guide.getNode(position).getNodes().size() > position) {
                    // get good fragment
//                    ((HomeActivity) getActivity()).loadDetailsGuideFragment(guide.getNode(position).getNodes().get(position), true);
                }
            }
        });

        return view;
    }

    public List<Node> getNodes() {
        return guide.getNodes();
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
                GuideService.getInstance().getFromSite(new NetworkCallback<Guide>() {
                    @Override
                    public void onSuccess(Guide result) {
                        setGuide(result);
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNoAvailableData() {
                        if (guide == null) {
                            adapter.setEmptyGuide();
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

    public void setGuide(Guide guide) {
        if (guide != null && guide.isActivated() && guide.isCreated()) {
            this.guide = guide;
            adapter.setGuide(guide);
        }


//            emptyListMessage.setVisibility(View.GONE);
//            GuideRenderer sgr = new GuideRenderer();
//            String survivalContent = sgr.renderSurvivalGuide(guide);
//
//            guideContentWebView.loadData(survivalContent, "text/html; charset=UTF-8", null);
//            guideContentWebView.setScrollContainer(false);
//
//            // disable scroll on touch
//            guideContentWebView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return (event.getAction() == MotionEvent.ACTION_MOVE);
//                }
//            });
//
//            WebSettings webSettings = guideContentWebView.getSettings();
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            webSettings.setDefaultFontSize(10);
//            webSettings.setJavaScriptEnabled(true);
    }
}
